package com.noteslist.app.notes.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.noteslist.app.notes.models.db.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notes: List<NoteEntity>)

    @Query(QUERY_REMOVE_SINGLE)
    suspend fun delete(id: String)

    @Query(QUERY_REMOVE_ALL)
    suspend fun deleteAll()

    @Query(QUERY_ALL_NOTES)
    fun getAllFlow(): Flow<List<NoteEntity>>

    companion object {
        private const val QUERY_ALL_NOTES = """
           SELECT *
           FROM ${NoteEntity.TABLE_NAME}
           ORDER BY createdAt DESC
        """

        private const val QUERY_REMOVE_SINGLE = """
           DELETE
           FROM ${NoteEntity.TABLE_NAME}
           WHERE id = :id
        """

        private const val QUERY_REMOVE_ALL = """
           DELETE
           FROM ${NoteEntity.TABLE_NAME}
        """
    }
}