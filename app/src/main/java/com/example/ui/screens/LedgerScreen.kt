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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LedgerItem
import com.example.data.model.StudentProfile
import com.example.ui.theme.StiAccentGold
import com.example.ui.theme.StiGoldDark
import com.example.ui.theme.StiGoldLight
import com.example.ui.theme.StiPrimaryBlue
import com.example.ui.theme.StiPrimaryNavy
import com.example.ui.theme.StiSuccess
import com.example.ui.theme.StiWarning

@Composable
fun LedgerScreen(
  profile: StudentProfile?,
  ledgerItems: List<LedgerItem>,
  selectedPaymentItem: LedgerItem?,
  onOpenPaymentModal: (LedgerItem) -> Unit,
  onClosePaymentModal: () -> Unit,
  onSubmitPayment: (Long, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedPaymentMethod by remember { mutableStateOf("GCash") }
  var showItemizedBreakdown by remember { mutableStateOf(false) }

  val totalAssessment = ledgerItems.sumOf { it.amount }
  val totalPaid = ledgerItems.filter { it.status == "PAID" }.sumOf { it.amount }
  val remainingBalance = (totalAssessment - totalPaid).coerceAtLeast(0.0)

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF4F7FB)),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Statement of Account Summary Hero Card
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
            Text(
              text = "STATEMENT OF ACCOUNT",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = StiAccentGold,
              letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "₱%,.2f".format(remainingBalance),
              fontSize = 32.sp,
              fontWeight = FontWeight.Black,
              color = Color.White
            )
            Text(
              text = "Remaining Outstanding Balance for 1st Term",
              fontSize = 11.sp,
              color = Color(0xFFBACFE8)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(text = "Total Assessment", fontSize = 10.sp, color = Color(0xFFBACFE8))
                Text(
                  text = "₱%,.2f".format(totalAssessment),
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }

              Column {
                Text(text = "Total Payments Credited", fontSize = 10.sp, color = Color(0xFFBACFE8))
                Text(
                  text = "₱%,.2f".format(totalPaid),
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = StiAccentGold
                )
              }

              TextButton(
                onClick = { showItemizedBreakdown = true },
                modifier = Modifier.testTag("view_breakdown_button")
              ) {
                Text(
                  text = "Itemized Fees",
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

    // Installment Schedule Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Installment Payment Schedule",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = StiPrimaryNavy
        )
      }
    }

    // Installment Items
    items(ledgerItems) { item ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("ledger_item_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = item.title,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color(0xFF1E293B)
            )

            Surface(
              shape = RoundedCornerShape(6.dp),
              color = when (item.status) {
                "PAID" -> Color(0xFFD1E7DD)
                "OUTSTANDING" -> Color(0xFFFFE5D0)
                else -> Color(0xFFE2EAF4)
              }
            ) {
              Text(
                text = item.status,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = when (item.status) {
                  "PAID" -> StiSuccess
                  "OUTSTANDING" -> StiWarning
                  else -> StiPrimaryNavy
                },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = item.description,
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "₱%,.2f".format(item.amount),
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = StiPrimaryNavy
              )
              if (item.status == "PAID") {
                Text(
                  text = "Paid on ${item.paidDate ?: "Aug 2026"} • ${item.referenceNo ?: "OR-STI"}",
                  fontSize = 10.sp,
                  color = StiSuccess,
                  fontFamily = FontFamily.Monospace
                )
              } else {
                Text(
                  text = "Due: ${item.dueDate}",
                  fontSize = 11.sp,
                  color = if (item.status == "OUTSTANDING") StiWarning else Color(0xFF64748B),
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            if (item.status == "OUTSTANDING") {
              Button(
                onClick = { onOpenPaymentModal(item) },
                colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryNavy),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("pay_installment_${item.id}")
              ) {
                Icon(
                  imageVector = Icons.Default.Payment,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                  tint = StiAccentGold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Pay Now",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = Color.White
                )
              }
            }
          }
        }
      }
    }
  }

  // Itemized Assessment Breakdown Modal
  if (showItemizedBreakdown) {
    AlertDialog(
      onDismissRequest = { showItemizedBreakdown = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Receipt,
            contentDescription = null,
            tint = StiPrimaryNavy,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Assessment Breakdown", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      },
      text = {
        Column {
          val breakdown = listOf(
            "Tuition Fee (20 Units)" to "₱22,000.00",
            "IT / Computer Lab Fee" to "₱6,500.00",
            "Energy & Airconditioning" to "₱3,500.00",
            "Matriculation & Registration" to "₱2,500.00",
            "Library & Learning Commons" to "₱1,500.00",
            "Medical & Dental Clinic" to "₱1,000.00",
            "Student Publication & Council" to "₱1,500.00"
          )
          breakdown.forEach { (fee, cost) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = fee, fontSize = 12.sp, color = Color(0xFF334155))
              Text(text = cost, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StiPrimaryNavy)
            }
          }
          Divider(modifier = Modifier.padding(vertical = 8.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Total Assessment:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = StiPrimaryNavy)
            Text(text = "₱38,500.00", fontWeight = FontWeight.Black, fontSize = 14.sp, color = StiPrimaryNavy)
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { showItemizedBreakdown = false },
          colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryNavy)
        ) {
          Text("Close")
        }
      }
    )
  }

  // Payment Confirmation & Method Selection Modal
  selectedPaymentItem?.let { targetItem ->
    AlertDialog(
      onDismissRequest = onClosePaymentModal,
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AccountBalanceWallet,
            contentDescription = null,
            tint = StiPrimaryNavy,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Settle Tuition Fee", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      },
      text = {
        Column {
          Text(
            text = "Settling: ${targetItem.title}",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = StiPrimaryNavy
          )
          Text(
            text = "Amount Due: ₱%,.2f".format(targetItem.amount),
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = StiPrimaryBlue
          )

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Choose Payment Gateway / Channel:",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155)
          )

          val methods = listOf("GCash", "Maya (PayMaya)", "Landbank e-Payment", "STI Campus Cashier")
          methods.forEach { method ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedPaymentMethod = method }
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = selectedPaymentMethod == method,
                onClick = { selectedPaymentMethod = method },
                colors = RadioButtonDefaults.colors(selectedColor = StiPrimaryNavy)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = method, fontSize = 13.sp, color = Color(0xFF1E293B))
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Instant credit verification. Official Receipt will be automatically registered on your ONE STI student ledger.",
            fontSize = 10.sp,
            color = Color(0xFF64748B)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onSubmitPayment(targetItem.id, selectedPaymentMethod)
          },
          colors = ButtonDefaults.buttonColors(containerColor = StiPrimaryNavy),
          modifier = Modifier.testTag("confirm_submit_payment_button")
        ) {
          Text("Confirm Payment")
        }
      },
      dismissButton = {
        TextButton(onClick = onClosePaymentModal) {
          Text("Cancel")
        }
      }
    )
  }
}
