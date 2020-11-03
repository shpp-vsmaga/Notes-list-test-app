package com.noteslist.app.notes.gateway

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.noteslist.app.common.utils.ExecuteOnCaller
import com.noteslist.app.common.utils.toJodaDateTime
import com.noteslist.app.notes.models.FirebaseDbKeys.COLLECTION_NOTES
import com.noteslist.app.notes.models.FirebaseDbKeys.COLLECTION_USER_NOTES
import com.noteslist.app.notes.models.FirebaseDbKeys.FIELD_CREATED_AT
import com.noteslist.app.notes.models.FirebaseDbKeys.FIELD_TEXT
import com.noteslist.app.notes.models.view.Note
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.DateTime

/**
 * Implementation of NotesRemoteGateway based on the Firebase Firestore
 */
class NotesFirebaseGateway(
    private val db: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : NotesRemoteGateway {

    override fun getNotes(): Single<List<Note>> =
        Single.create { emitter ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val notesRef = db.collection(COLLECTION_NOTES).document(userId)
                    .collection(COLLECTION_USER_NOTES)
                notesRef.addSnapshotListener(ExecuteOnCaller()) { snapshot, e ->
                    if (e != null) {
                        emitter.onError(e)
                    }
                    if (snapshot != null) {
                        val documents = snapshot.documents
                        val notes = documents.map {
                            Note(
                                id = it.id,
                                text = (it[FIELD_TEXT] as? String).orEmpty(),
                                createdAt = (it[FIELD_CREATED_AT] as? Timestamp)?.toJodaDateTime()
                                    ?: DateTime.now()
                            )
                        }
                        emitter.onSuccess(notes)
                    } else {
                        emitter.onSuccess(emptyList())
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
                val notesRef = db.collection(COLLECTION_NOTES).document(userId)
                    .collection(COLLECTION_USER_NOTES)
                notesRef.add(
                    mapOf(FIELD_TEXT to text, FIELD_CREATED_AT to Timestamp.now())
                ).addOnCompleteListener(ExecuteOnCaller()) {
                    it.exception?.let { error ->
                        emitter.onError(error)
                    }
                    if (it.isSuccessful) {
                        it.result?.get()?.addOnCompleteListener(ExecuteOnCaller()) { snapshot ->
                            snapshot.result?.let { docRef ->
                                emitter.onSuccess(
                                    Note(
                                        id = docRef.id,
                                        text = (docRef[FIELD_TEXT] as? String).orEmpty(),
                                        createdAt = (docRef[FIELD_CREATED_AT] as? Timestamp)?.toJodaDateTime()
                                            ?: DateTime.now()
                                    )
                                )
                            }
                        }
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
                val noteRef = db.collection(COLLECTION_NOTES).document(userId)
                    .collection(COLLECTION_USER_NOTES).document(note.id)
                noteRef.set(mapOf(FIELD_TEXT to note.text))
                    .addOnCompleteListener(ExecuteOnCaller()) {
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
                val noteRef = db.collection(COLLECTION_NOTES).document(userId)
                    .collection(COLLECTION_USER_NOTES).document(id)
                noteRef.delete().addOnCompleteListener(ExecuteOnCaller()) {
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