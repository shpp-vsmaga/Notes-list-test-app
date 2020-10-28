package com.noteslist.app.notes.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    val id: String,
    val text: String
) : Parcelable