package com.noteslist.app.notes.models.view

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note(
    val id: String,
    val text: String,
    val createdAt: Date
) : Parcelable