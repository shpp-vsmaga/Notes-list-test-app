package com.noteslist.app.helpers

import com.noteslist.app.notes.models.db.NoteEntity
import com.noteslist.app.notes.models.view.Note
import java.util.*

object TestData {
    private const val FIELD_ID = "id"
    private const val FIELD_TEXT = "text"
    private const val FIELD_CREATED_AT = "createdAt"

    //valid mocked test data representing Note model
    val notesValidData = mapOf(
        "1" to mapOf(
            FIELD_ID to "1",
            FIELD_TEXT to "text1",
            FIELD_CREATED_AT to Date(0)
        ),
        "2" to mapOf(
            FIELD_ID to "2",
            FIELD_TEXT to "text2",
            FIELD_CREATED_AT to Date(0)
        ),
        "3" to mapOf(
            FIELD_ID to "3",
            FIELD_TEXT to "text3",
            FIELD_CREATED_AT to Date(0)
        )
    )

    /**
     * @return mocked list of DB models base on the test data
     */
    fun getNotesDbModels(notesData: Map<String, Map<String, Any>>): List<NoteEntity> =
        notesData.values.map {
            NoteEntity(
                id = it[FIELD_ID] as String,
                text = it[FIELD_TEXT] as String,
                createdAt = it[FIELD_CREATED_AT] as Date
            )
        }

    /**
     * @return mocked list of UI models base on the test data
     */
    fun getNotesUiModels(notesData: Map<String, Map<String, Any>>): List<Note> =
        notesData.values.map {
            Note(
                id = (it[FIELD_ID] as String),
                text = (it[FIELD_TEXT] as String),
                createdAt = (it[FIELD_CREATED_AT] as Date)
            )
        }

}