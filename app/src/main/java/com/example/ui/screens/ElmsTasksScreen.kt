package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ElmsTask
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiGoldDark
import com.example.ui.theme.StiGoldLight
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy
import com.example.ui.theme.StiSuccess
import com.example.ui.theme.StiWarning

@Composable
fun ElmsTasksScreen(
  tasks: List<ElmsTask>,
  currentFilter: String,
  onFilterChange: (String) -> Unit,
  onToggleTask: (ElmsTask) -> Unit,
  onAddNewTask: (String, String, String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showAddTaskDialog by remember { mutableStateOf(false) }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAddTaskDialog = true },
        containerColor = StiPrimaryNavy,
        contentColor = StiAccentGold,
        modifier = Modifier.testTag("add_task_fab")
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFF4F7FB))
    ) {
      // Filter Tabs Row
      Surface(
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          val filters = listOf("ALL", "PENDING", "COMPLETED")
          filters.forEach { filter ->
            val isSelected = currentFilter == filter
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) StiPrimaryNavy else Color(0xFFF1F5F9),
              modifier = Modifier
                .clickable { onFilterChange(filter) }
                .testTag("filter_task_$filter")
            ) {
              Text(
                text = filter,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) StiAccentGold else Color(0xFF475569),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
              )
            }
          }
        }
      }

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "STI ELMS Coursework (${tasks.size})",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = StiPrimaryNavy
            )
          }
        }

        items(tasks) { task ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("task_item_${task.id}"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Clickable checkbox
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .clickable { onToggleTask(task) }
                  .testTag("toggle_task_${task.id}"),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                  contentDescription = "Toggle Complete",
                  tint = if (task.isCompleted) StiSuccess else Color(0xFF94A3B8),
                  modifier = Modifier.size(26.dp)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFE8EEF8)
                  ) {
                    Text(
                      text = task.courseCode,
                      fontWeight = FontWeight.Black,
                      fontSize = 11.sp,
                      color = StiPrimaryNavy,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }

                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (task.taskType) {
                      "QUIZ" -> Color(0xFFFFF3CD)
                      "LAB EXERCISE" -> Color(0xFFCFE2FF)
                      else -> Color(0xFFE2D9F3)
                    }
                  ) {
                    Text(
                      text = task.taskType,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Bold,
                      color = when (task.taskType) {
                        "QUIZ" -> StiGoldDark
                        "LAB EXERCISE" -> StiPrimaryNavy
                        else -> Color(0xFF4A148C)
                      },
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = task.title,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = if (task.isCompleted) Color(0xFF64748B) else Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Event,
                      contentDescription = null,
                      tint = if (task.isCompleted) Color(0xFF94A3B8) else StiWarning,
                      modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "Due: ${task.deadline}",
                      fontSize = 11.sp,
                      color = if (task.isCompleted) Color(0xFF94A3B8) else Color(0xFF475569)
                    )
                  }

                  if (task.score != null) {
                    Text(
                      text = "Score: ${task.score}",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = StiSuccess
                    )
                  } else if (task.isCompleted) {
                    Text(
                      text = "Submitted",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = StiPrimaryBlue
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  // Add Task Dialog
  if (showAddTaskDialog) {
    var newCode by remember { mutableStateOf("ITE312") }
    var newTitle by remember { mutableStateOf("") }
    var newType by remember { mutableStateOf("LAB EXERCISE") }
    var newDeadline by remember { mutableStateOf("Friday, 11:59 PM") }

    AlertDialog(
      onDismissRequest = { showAddTaskDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Assignment,
            contentDescription = null,
            tint = StiPrimaryNavy,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Add Coursework / Task", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = newCode,
            onValueChange = { newCode = it },
            label = { Text("Course Code (e.g. ITE312)") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = newTitle,
            onValueChange = { newTitle = it },
            label = { Text("Task Description") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = newDeadline,
            onValueChange = { newDeadline = it },
            label = { Text("Deadline") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newTitle.isNotBlank()) {
              onAddNewTask(newCode, newTitle, newType, newDeadline)
              showAddTaskDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryNavy),
          modifier = Modifier.testTag("save_new_task_button")
        ) {
          Text("Save Task")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddTaskDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
