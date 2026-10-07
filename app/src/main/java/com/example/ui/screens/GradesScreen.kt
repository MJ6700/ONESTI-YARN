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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubjectGrade
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiGoldDark
import com.example.ui.theme.StiGoldLight
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy
import com.example.ui.theme.StiSuccess

@Composable
fun GradesScreen(
  grades: List<SubjectGrade>,
  selectedTerm: String,
  allTerms: List<String>,
  onSelectTerm: (String) -> Unit,
  onUpdateGrade: (SubjectGrade, Double, Double, Double, Double) -> Unit,
  modifier: Modifier = Modifier
) {
  var showGradingScaleModal by remember { mutableStateOf(false) }
  var gradeToSimulate by remember { mutableStateOf<SubjectGrade?>(null) }

  // Compute GWA
  val calculatedGwa = if (grades.isNotEmpty()) {
    var totalUnits = 0.0
    var weightedSum = 0.0
    for (g in grades) {
      val gVal = if (g.finalGrade > 0) g.finalGrade else (g.prelim + g.midterm + g.prefinal) / 3.0
      weightedSum += gVal * g.units
      totalUnits += g.units
    }
    if (totalUnits > 0) Math.round((weightedSum / totalUnits) * 100.0) / 100.0 else 0.0
  } else 0.0

  val standingText = when {
    calculatedGwa in 1.00..1.25 -> "President's Lister"
    calculatedGwa in 1.26..1.50 -> "Dean's Lister"
    calculatedGwa in 1.51..3.00 -> "Regular Academic Standing"
    else -> "Subject for Evaluation"
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF4F7FB))
  ) {
    // Term Selector Row
    Surface(
      color = Color.White,
      shadowElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(allTerms) { term ->
          val isSelected = term == selectedTerm
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isSelected) StiPrimaryNavy else Color(0xFFF1F5F9),
            modifier = Modifier
              .clickable { onSelectTerm(term) }
              .testTag("term_filter_$term")
          ) {
            Text(
              text = term,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) StiAccentGold else Color(0xFF475569),
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            )
          }
        }
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // GWA Summary Hero
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = StiPrimaryNavy),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                brush = Brush.horizontalGradient(
                  colors = listOf(
                    StiPrimaryNavy,
                    StiPrimaryBlue
                  )
                )
              )
              .padding(18.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "GRADE WEIGHTED AVERAGE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = StiAccentGold,
                    letterSpacing = 1.sp
                  )
                  Text(
                    text = "%.2f".format(calculatedGwa),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                  )
                }

                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = StiAccentGold
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.EmojiEvents,
                      contentDescription = null,
                      tint = StiGoldDark,
                      modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = standingText.uppercase(),
                      color = StiGoldDark,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Black
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "${grades.size} Subjects • ${grades.sumOf { it.units }} Total Units",
                  fontSize = 12.sp,
                  color = Color(0xFFBACFE8)
                )

                TextButton(
                  onClick = { showGradingScaleModal = true },
                  modifier = Modifier.testTag("grades_view_scale_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = null,
                    tint = StiAccentGold,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "STI Grading Scale",
                    color = StiAccentGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }

      // Subject Grades List
      items(grades) { subject ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { gradeToSimulate = subject }
            .testTag("subject_grade_${subject.courseCode}"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFFE8EEF8)
                ) {
                  Text(
                    text = subject.courseCode,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    color = StiPrimaryNavy,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "${subject.units} Units",
                  fontSize = 11.sp,
                  color = Color(0xFF64748B),
                  fontWeight = FontWeight.Medium
                )
              }

              Surface(
                shape = RoundedCornerShape(6.dp),
                color = when (subject.remarks) {
                  "EXCELLENT" -> Color(0xFFD1E7DD)
                  "VERY GOOD" -> Color(0xFFCFE2FF)
                  else -> Color(0xFFFFF3CD)
                }
              ) {
                Text(
                  text = subject.remarks,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = when (subject.remarks) {
                    "EXCELLENT" -> StiSuccess
                    "VERY GOOD" -> StiPrimaryNavy
                    else -> StiGoldDark
                  },
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = subject.courseTitle,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Period Grade Badges: Prelim, Midterm, Pre-Final, Final
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              PeriodGradeBox(label = "PRELIM", grade = subject.prelim)
              PeriodGradeBox(label = "MIDTERM", grade = subject.midterm)
              PeriodGradeBox(label = "PRE-FINAL", grade = subject.prefinal)
              PeriodGradeBox(label = "FINAL", grade = subject.finalGrade, isHighlight = true)
            }
          }
        }
      }
    }
  }

  // STI Grading Scale Reference Sheet
  if (showGradingScaleModal) {
    AlertDialog(
      onDismissRequest = { showGradingScaleModal = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.School,
            contentDescription = null,
            tint = StiPrimaryNavy,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Official STI Grading Scale", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      },
      text = {
        Column {
          val scale = listOf(
            Triple("1.00", "97.50 - 100.00%", "Excellent"),
            Triple("1.25", "94.50 - 97.49%", "Very Good"),
            Triple("1.50", "91.50 - 94.49%", "Very Good"),
            Triple("1.75", "88.50 - 91.49%", "Good"),
            Triple("2.00", "85.50 - 88.49%", "Good"),
            Triple("2.25", "82.50 - 85.49%", "Satisfactory"),
            Triple("2.50", "79.50 - 82.49%", "Fair"),
            Triple("2.75", "76.50 - 79.49%", "Fair"),
            Triple("3.00", "75.00 - 76.49%", "Passed"),
            Triple("5.00", "Below 75.00%", "Failed")
          )

          scale.forEach { (point, percent, desc) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = point,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (point == "1.00" || point == "1.25") StiPrimaryNavy else Color(0xFF334155),
                modifier = Modifier.width(42.dp)
              )
              Text(text = percent, fontSize = 11.sp, color = Color(0xFF64748B), modifier = Modifier.weight(1f))
              Text(
                text = desc,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = if (point == "5.00") Color(0xFFDC3545) else Color(0xFF1E293B)
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { showGradingScaleModal = false },
          colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryNavy)
        ) {
          Text("Got It")
        }
      }
    )
  }

  // What-If Grade Simulator Modal
  gradeToSimulate?.let { targetGrade ->
    var prelimScore by remember { mutableDoubleStateOf(targetGrade.prelim) }
    var midtermScore by remember { mutableDoubleStateOf(targetGrade.midterm) }
    var prefinalScore by remember { mutableDoubleStateOf(targetGrade.prefinal) }
    var finalScore by remember { mutableDoubleStateOf(targetGrade.finalGrade) }

    val simulatedFinal = Math.round(((prelimScore * 0.20) + (midtermScore * 0.20) + (prefinalScore * 0.20) + (finalScore * 0.40)) * 100.0) / 100.0

    AlertDialog(
      onDismissRequest = { gradeToSimulate = null },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Calculate,
            contentDescription = null,
            tint = StiPrimaryNavy,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "What-If Grade Simulator", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      },
      text = {
        Column {
          Text(
            text = "${targetGrade.courseCode}: ${targetGrade.courseTitle}",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = StiPrimaryNavy
          )
          Text(
            text = "Adjust periodic grades to test your target semestral rating:",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )

          Spacer(modifier = Modifier.height(12.dp))

          GradeSliderRow(
            label = "Prelim Grade",
            value = prelimScore,
            onValueChange = { prelimScore = it }
          )
          GradeSliderRow(
            label = "Midterm Grade",
            value = midtermScore,
            onValueChange = { midtermScore = it }
          )
          GradeSliderRow(
            label = "Pre-Final Grade",
            value = prefinalScore,
            onValueChange = { prefinalScore = it }
          )
          GradeSliderRow(
            label = "Final Exam Rating",
            value = finalScore,
            onValueChange = { finalScore = it }
          )

          Spacer(modifier = Modifier.height(10.dp))

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = StiGoldLight,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Simulated Final:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = StiGoldDark
              )
              Text(
                text = "%.2f".format(simulatedFinal),
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = StiPrimaryNavy
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onUpdateGrade(targetGrade, prelimScore, midtermScore, prefinalScore, simulatedFinal)
            gradeToSimulate = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryNavy)
        ) {
          Text("Apply to Record")
        }
      },
      dismissButton = {
        TextButton(onClick = { gradeToSimulate = null }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
fun PeriodGradeBox(label: String, grade: Double, isHighlight: Boolean = false) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = if (isHighlight) StiGoldLight else Color(0xFFF1F5F9),
    modifier = Modifier.width(72.dp)
  ) {
    Column(
      modifier = Modifier.padding(vertical = 6.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = label,
        fontSize = 8.sp,
        fontWeight = FontWeight.Bold,
        color = if (isHighlight) StiGoldDark else Color(0xFF64748B)
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "%.2f".format(grade),
        fontSize = 13.sp,
        fontWeight = FontWeight.Black,
        color = if (isHighlight) StiPrimaryNavy else Color(0xFF1E293B)
      )
    }
  }
}

@Composable
fun GradeSliderRow(label: String, value: Double, onValueChange: (Double) -> Unit) {
  Column(modifier = Modifier.padding(vertical = 2.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF334155))
      Text(text = "%.2f".format(value), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StiPrimaryNavy)
    }
    Slider(
      value = value.toFloat(),
      onValueChange = { onValueChange(Math.round(it * 100.0) / 100.0) },
      valueRange = 1.0f..3.0f,
      steps = 7,
      colors = SliderDefaults.colors(
        thumbColor = StiPrimaryNavy,
        activeTrackColor = StiPrimaryBlue
      )
    )
  }
}
