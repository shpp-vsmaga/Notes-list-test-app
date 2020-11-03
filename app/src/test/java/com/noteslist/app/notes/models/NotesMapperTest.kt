package com.noteslist.app.notes.models

import com.noteslist.app.helpers.TestData
import junit.framework.TestCase
import org.joda.time.DateTimeZone
import org.joda.time.tz.UTCProvider
import org.junit.Test

class NotesMapperTest : TestCase() {

    public override fun setUp() {
        super.setUp()
        //required for the correct instantiation of DateTime class
        DateTimeZone.setProvider(UTCProvider())
    }

    @Test
    fun testFromDatabase() {
        val entities = TestData.getNotesDbModels(TestData.notesValidData)
        val models = NotesMapper.fromDatabase(entities)
        entities.forEach { note ->
            val convertedModel = models.first { it.id == note.id }

            assertEquals(note.id, convertedModel.id)
            assertEquals(note.text, convertedModel.text)
            assertEquals(note.createdAt, convertedModel.createdAt)
        }
    }

    @Test
    fun testToDatabase() {
        val models = TestData.getNotesUiModels(TestData.notesValidData)
        val entities = NotesMapper.toDatabase(models)
        models.forEach { note ->
            val convertedEntity = entities.first { it.id == note.id }

            assertEquals(note.id, convertedEntity.id)
            assertEquals(note.text, convertedEntity.text)
            assertEquals(note.createdAt, convertedEntity.createdAt)
        }
    }

    @Test
    fun testToDatabaseSingleModel() {
        val model = TestData.getNotesUiModels(TestData.notesValidData).first()
        val convertedEntity = NotesMapper.toDatabase(model)

        assertEquals(model.id, convertedEntity.id)
        assertEquals(model.text, convertedEntity.text)
        assertEquals(model.createdAt, convertedEntity.createdAt)
    }

}