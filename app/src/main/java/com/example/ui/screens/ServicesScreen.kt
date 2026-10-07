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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.ClearanceItem
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiGoldDark
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy
import com.example.ui.theme.StiSuccess
import com.example.ui.theme.StiWarning

data class DirectoryEntry(
  val department: String,
  val office: String,
  val phone: String,
  val email: String
)

@Composable
fun ServicesScreen(
  clearanceItems: List<ClearanceItem>,
  modifier: Modifier = Modifier
) {
  var showDocRequestDialog by remember { mutableStateOf(false) }
  var requestedDocType by remember { mutableStateOf("") }
  var requestConfirmed by remember { mutableStateOf(false) }

  val directoryList = listOf(
    DirectoryEntry("Office of the Registrar", "2nd Floor, Admin Wing", "(02) 8888-7841", "registrar@globalcity.sti.edu.ph"),
    DirectoryEntry("Accounting & Cashier", "Ground Floor, South Lobby", "(02) 8888-7842", "accounting@globalcity.sti.edu.ph"),
    DirectoryEntry("Guidance & Counseling Center", "3rd Floor, Student Hub", "(02) 8888-7843", "guidance@globalcity.sti.edu.ph"),
    DirectoryEntry("University Clinic", "Ground Floor, Health Unit", "(02) 8888-7844", "clinic@globalcity.sti.edu.ph"),
    DirectoryEntry("IT Support & ELMS Helpdesk", "4th Floor, CompLab Wing", "(02) 8888-7845", "ithelpdesk@globalcity.sti.edu.ph")
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF4F7FB)),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Semestral Clearance Section
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.FactCheck,
            contentDescription = null,
            tint = StiPrimaryNavy,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Semestral Clearance Tracker",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = StiPrimaryNavy
          )
        }

        val clearedCount = clearanceItems.count { it.status == "CLEARED" }
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFFE2EAF4)
        ) {
          Text(
            text = "$clearedCount of ${clearanceItems.size} Cleared",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = StiPrimaryNavy,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }

    items(clearanceItems) { item ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("clearance_item_${item.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(
                if (item.status == "CLEARED") Color(0xFFD1E7DD) else Color(0xFFFFE5D0)
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (item.status == "CLEARED") Icons.Default.CheckCircle else Icons.Default.HourglassBottom,
              contentDescription = item.status,
              tint = if (item.status == "CLEARED") StiSuccess else StiWarning,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = item.officeName,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF1E293B)
              )

              Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (item.status == "CLEARED") Color(0xFFD1E7DD) else Color(0xFFFFE5D0)
              ) {
                Text(
                  text = item.status,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (item.status == "CLEARED") StiSuccess else StiWarning,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = item.officerInCharge,
              fontSize = 11.sp,
              color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = item.remarks,
              fontSize = 10.sp,
              color = Color(0xFF475569)
            )
          }
        }
      }
    }

    // Document Request Section
    item {
      Text(
        text = "Official Academic Credentials Request",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = StiPrimaryNavy,
        modifier = Modifier.padding(top = 8.dp)
      )
    }

    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Request Registrar Documents Online",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color(0xFF1E293B)
          )
          Text(
            text = "Processed within 3-5 working days. Digital copies are uploaded to your ONE STI portal.",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )

          Spacer(modifier = Modifier.height(12.dp))

          val docs = listOf(
            "Certificate of Registration (COR / COE)",
            "Official Transcript of Records (TOR)",
            "Certificate of Good Moral Character",
            "Certified True Copy of Grades"
          )

          docs.forEach { doc ->
            OutlinedButton(
              onClick = {
                requestedDocType = doc
                showDocRequestDialog = true
              },
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
                .testTag("request_doc_${doc.take(10).replace(" ", "_")}"),
              shape = RoundedCornerShape(10.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = doc,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  color = StiPrimaryNavy
                )
                Icon(
                  imageVector = Icons.Default.Send,
                  contentDescription = null,
                  tint = StiPrimaryNavy,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }
        }
      }
    }

    // Campus Directory Section
    item {
      Text(
        text = "STI Campus Offices & Directory",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = StiPrimaryNavy,
        modifier = Modifier.padding(top = 8.dp)
      )
    }

    items(directoryList) { entry ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = entry.department,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = StiPrimaryNavy
          )
          Text(
            text = entry.office,
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = StiPrimaryBlue, modifier = Modifier.size(13.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = entry.phone, fontSize = 11.sp, color = Color(0xFF334155))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = StiPrimaryBlue, modifier = Modifier.size(13.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = entry.email, fontSize = 10.sp, color = StiPrimaryBlue)
            }
          }
        }
      }
    }
  }

  // Document Request Dialog
  if (showDocRequestDialog) {
    AlertDialog(
      onDismissRequest = {
        showDocRequestDialog = false
        requestConfirmed = false
      },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = StiPrimaryNavy, modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Document Request", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      },
      text = {
        if (!requestConfirmed) {
          Column {
            Text(
              text = "You are requesting: $requestedDocType",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = StiPrimaryNavy
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Student Name: Alexander M. Santos\nStudent No: 02000284729\nProgram: BS Information Technology\nCampus: STI College Global City",
              fontSize = 11.sp,
              lineHeight = 16.sp,
              color = Color(0xFF475569)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "Standard processing fee of ₱150.00 will be added to your ledger upon registrar approval.",
              fontSize = 10.sp,
              color = Color(0xFF64748B)
            )
          }
        } else {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = StiSuccess, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Request Lodged Successfully!",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = StiPrimaryNavy
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Reference Ticket #REQ-STI-7491. An email notification will be sent when ready for pick-up or digital download.",
              fontSize = 11.sp,
              color = Color(0xFF64748B),
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      },
      confirmButton = {
        if (!requestConfirmed) {
          Button(
            onClick = { requestConfirmed = true },
            colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryNavy),
            modifier = Modifier.testTag("submit_doc_request_button")
          ) {
            Text("Confirm Request")
          }
        } else {
          Button(
            onClick = {
              showDocRequestDialog = false
              requestConfirmed = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryNavy)
          ) {
            Text("Done")
          }
        }
      },
      dismissButton = {
        if (!requestConfirmed) {
          TextButton(onClick = { showDocRequestDialog = false }) {
            Text("Cancel")
          }
        }
      }
    )
  }
}
