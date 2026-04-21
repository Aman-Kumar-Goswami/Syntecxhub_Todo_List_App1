package com.example.todolistapp.data

import androidx.compose.ui.graphics.Color
import com.example.todolistapp.ui.theme.PriorityHigh
import com.example.todolistapp.ui.theme.PriorityLow
import com.example.todolistapp.ui.theme.PriorityMedium

enum class Priority(val color: Color) {
    LOW(PriorityLow),
    MEDIUM(PriorityMedium),
    HIGH(PriorityHigh)
}
