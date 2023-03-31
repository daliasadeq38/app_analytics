package com.example.appanalytics.listener

import com.example.appanalytics.model.Note

interface OnClickNote {
    fun onClick(note: Note)
}