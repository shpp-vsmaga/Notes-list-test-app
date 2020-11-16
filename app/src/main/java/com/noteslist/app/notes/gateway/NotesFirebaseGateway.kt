package com.noteslist.app.notes.gateway

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.noteslist.app.common.utils.ExecuteOnCaller
import com.noteslist.app.notes.models.FirebaseDbKeys.COLLECTION_NOTES
import com.noteslist.app.notes.models.FirebaseDbKeys.COLLECTION_USER_NOTES
import com.noteslist.app.notes.models.FirebaseDbKeys.FIELD_CREATED_AT
import com.noteslist.app.notes.models.FirebaseDbKeys.FIELD_TEXT
import com.noteslist.app.notes.models.view.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Implementation of NotesRemoteGateway based on the Firebase Firestore
 */
class NotesFirebaseGateway(
    private val db: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : NotesRemoteGateway {

    @ExperimentalCoroutinesApi
    override fun getNotes(): Flow<List<Note>> =
        callbackFlow {
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val notesRef = db.collection(COLLECTION_NOTES).document(userId)
                    .collection(COLLECTION_USER_NOTES)
                val registration = notesRef.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                    }
                    if (snapshot != null) {
                        val documents = snapshot.documents
                        val notes = documents.map {
                            Note(
                                id = it.id,
                                text = (it[FIELD_TEXT] as? String).orEmpty(),
                                createdAt = (it[FIELD_CREATED_AT] as? Timestamp)?.toDate()
                                    ?: Date()
                            )
                        }
                        offer(notes)
                    } else {
                        offer(emptyList<Note>())
                    }
                }
                awaitClose { registration.remove() }
            } else {
                close(Exception("User data is absent"))
            }
        }

    override suspend fun addNote(text: String): Note =
        suspendCancellableCoroutine { continuation ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val notesRef = db.collection(COLLECTION_NOTES).document(userId)
                    .collection(COLLECTION_USER_NOTES)
                notesRef.add(
                    mapOf(FIELD_TEXT to text, FIELD_CREATED_AT to Timestamp.now())
                ).addOnCompleteListener {
                    it.exception?.let { error ->
                        continuation.resumeWithException(error)
                    }
                    if (it.isSuccessful) {
                        it.result?.get()?.addOnCompleteListener(ExecuteOnCaller()) { snapshot ->
                            snapshot.result?.let { docRef ->
                                continuation.resume(
                                    Note(
                                        id = docRef.id,
                                        text = (docRef[FIELD_TEXT] as? String).orEmpty(),
                                        createdAt = (docRef[FIELD_CREATED_AT] as? Timestamp)?.toDate()
                                            ?: Date()
                                    )
                                )
                            }
                        }
                    } else if (it.isCanceled) {
                        continuation.resumeWithException(Exception("Operation canceled"))
                    }
                }
            } else {
                continuation.resumeWithException(Exception("User data is absent"))
            }

        }

    override suspend fun saveNote(note: Note): Note =
        suspendCancellableCoroutine { continuation ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val noteRef = db.collection(COLLECTION_NOTES).document(userId)
                    .collection(COLLECTION_USER_NOTES).document(note.id)
                noteRef.set(mapOf(FIELD_TEXT to note.text))
                    .addOnCompleteListener {
                        it.exception?.let { error ->
                            continuation.resumeWithException(error)
                        }
                        if (it.isSuccessful) {
                            continuation.resume(note)
                        } else if (it.isCanceled) {
                            continuation.resumeWithException(Exception("Operation canceled"))
                        }
                    }
            } else {
                continuation.resumeWithException(Exception("User data is absent"))
            }
        }

    override suspend fun deleteNote(id: String) {
        suspendCancellableCoroutine<Unit> { continuation ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val noteRef = db.collection(COLLECTION_NOTES).document(userId)
                    .collection(COLLECTION_USER_NOTES).document(id)
                noteRef.delete().addOnCompleteListener {
                    it.exception?.let { error ->
                        continuation.resumeWithException(error)
                    }
                    if (it.isSuccessful) {
                        continuation.resume(Unit)
                    } else if (it.isCanceled) {
                        continuation.resumeWithException(Exception("Operation canceled"))
                    }
                }
            } else {
                continuation.resumeWithException(Exception("User data is absent"))
            }
        }
    }

}