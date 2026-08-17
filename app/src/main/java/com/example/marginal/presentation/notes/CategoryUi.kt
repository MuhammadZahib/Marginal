package com.example.marginal.presentation.notes

import androidx.compose.ui.graphics.Color
import com.example.marginal.domain.model.NoteCategory
import com.example.marginal.ui.theme.Amber
import com.example.marginal.ui.theme.Brick
import com.example.marginal.ui.theme.Plum
import com.example.marginal.ui.theme.Sage

fun NoteCategory.color(): Color = when (this) {
    NoteCategory.PERSONAL -> Sage
    NoteCategory.WORK -> Amber
    NoteCategory.IDEAS -> Plum
    NoteCategory.URGENT -> Brick
}

fun NoteCategory.label(): String = when (this) {
    NoteCategory.PERSONAL -> "Personal"
    NoteCategory.WORK -> "Work"
    NoteCategory.IDEAS -> "Ideas"
    NoteCategory.URGENT -> "Urgent"
}
