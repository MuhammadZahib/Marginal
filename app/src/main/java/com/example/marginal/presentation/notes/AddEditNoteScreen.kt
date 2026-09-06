package com.example.marginal.presentation.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.marginal.domain.model.NoteCategory
import com.example.marginal.presentation.common.CameraIcon
import com.example.marginal.presentation.common.CheckIcon
import com.example.marginal.presentation.common.MarginalBackButton
import com.example.marginal.presentation.common.MarginalIconButton
import com.example.marginal.presentation.common.TrashIcon
import com.example.marginal.ui.theme.Brick
import com.example.marginal.ui.theme.Ink
import com.example.marginal.ui.theme.Paper

@Composable
fun AddEditNoteScreen(
    onBackClick: () -> Unit,
    onScanClick: () -> Unit,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MarginalBackButton(onClick = onBackClick, tint = Ink)

            Spacer(modifier = Modifier.weight(1f))

            MarginalIconButton(onClick = onScanClick) {
                CameraIcon(modifier = Modifier.size(19.dp), tint = Ink)
            }
            Spacer(modifier = Modifier.width(4.dp))

            if (uiState.isEditMode) {
                MarginalIconButton(onClick = { showDeleteConfirm = true }) {
                    TrashIcon(modifier = Modifier.size(18.dp), tint = Brick)
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            MarginalIconButton(
                onClick = { viewModel.save(onDone = onBackClick) },
                backgroundColor = if (uiState.isSaving) Ink.copy(alpha = 0.5f) else Ink,
            ) {
                CheckIcon(modifier = Modifier.size(16.dp), tint = Paper)
            }
        }

        TextField(
            value = uiState.title,
            onValueChange = viewModel::onTitleChange,
            placeholder = { Text("Note title") },
            textStyle = MaterialTheme.typography.headlineSmall,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Paper,
                focusedContainerColor = Paper,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        TextField(
            value = uiState.body,
            onValueChange = viewModel::onBodyChange,
            placeholder = { Text("Start writing…") },
            textStyle = TextStyle(fontSize = 16.sp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Paper,
                focusedContainerColor = Paper,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier.fillMaxWidth().weight(1f),
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NoteCategory.entries.forEach { category ->
                FilterChip(
                    selected = uiState.category == category,
                    onClick = { viewModel.onCategoryChange(category) },
                    label = { Text(category.label()) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Ink,
                        selectedLabelColor = Paper,
                    ),
                )
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete this note?") },
            text = { Text("\"${uiState.title.ifBlank { "Untitled" }}\" will be permanently removed. This can't be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirm = false
                    viewModel.delete(onDone = onBackClick)
                }) {
                    Text("Delete", color = Brick)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            },
        )
    }
}
