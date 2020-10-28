package com.noteslist.app.notes.gateway

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.noteslist.app.notes.models.FirebaseDbKeys.COLLECTION_NOTES
import com.noteslist.app.notes.models.FirebaseDbKeys.FIELD_TEXT
import com.noteslist.app.notes.models.Note
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class NotesFirebaseGateway(
    private val db: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : NotesRemoteGateway {
    override fun getNotes(): Flowable<List<Note>> =
        Flowable.generate { emitter ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val notesRef = db.document(userId).collection(COLLECTION_NOTES)
                notesRef.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        emitter.onError(e)
                    }
                    if (snapshot != null) {
                        val documents = snapshot.documents
                        val notes = documents.map {
                            Note(
                                id = it.id,
                                text = it[FIELD_TEXT] as String
                            )
                        }
                        emitter.onNext(notes)
                    } else {
                        emitter.onNext(emptyList())
                    }
                }
            } else {
                emitter.onError(Exception("User data is absent"))
            }
        }

    override fun addNote(text: String): Single<Note> =
        Single.create { emitter ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val notesRef = db.document(userId).collection(COLLECTION_NOTES)
                notesRef.add(
                    mapOf(FIELD_TEXT to text)
                ).addOnCompleteListener {
                    it.exception?.let { error ->
                        emitter.onError(error)
                    }
                    if (it.isSuccessful && it.result != null) {
                        emitter.onSuccess(
                            Note(
                                id = it.result!!.id,
                                text = text
                            )
                        )
                    } else if (it.isCanceled) {
                        emitter.onError(Exception("Operation canceled"))
                    }
                }
            } else {
                emitter.onError(Exception("User data is absent"))
            }
        }


    override fun saveNote(note: Note): Single<Note> =
        Single.create { emitter ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val noteRef = db.document(userId).collection(COLLECTION_NOTES).document(note.id)
                noteRef.set(mapOf(FIELD_TEXT to note.text))
                    .addOnCompleteListener {
                        it.exception?.let { error ->
                            emitter.onError(error)
                        }
                        if (it.isSuccessful) {
                            emitter.onSuccess(note)
                        } else if (it.isCanceled) {
                            emitter.onError(Exception("Operation canceled"))
                        }
                    }
            } else {
                emitter.onError(Exception("User data is absent"))
            }
        }

    override fun deleteNote(id: String): Completable =
        Completable.create { emitter ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val noteRef = db.document(userId).collection(COLLECTION_NOTES).document(id)
                noteRef.delete().addOnCompleteListener {
                    it.exception?.let { error ->
                        emitter.onError(error)
                    }
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else if (it.isCanceled) {
                        emitter.onError(Exception("Operation canceled"))
                    }
                }
            } else {
                emitter.onError(Exception("User data is absent"))
            }
        }


}