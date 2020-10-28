package com.noteslist.app.notes.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: NoteEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notes: List<NoteEntity>): Completable

    @Query(QUERY_REMOVE_SINGLE)
    fun delete(id: String): Completable

    @Query(QUERY_ALL_NOTES)
    fun getAllFlowable(): Flowable<List<NoteEntity>>

    companion object {
        private const val QUERY_ALL_NOTES = """
           SELECT *
           FROM ${NoteEntity.TABLE_NAME}
           ORDER BY id DESC
        """

        private const val QUERY_REMOVE_SINGLE = """
           DELETE
           FROM ${NoteEntity.TABLE_NAME}
           WHERE id = :id
        """
    }
}