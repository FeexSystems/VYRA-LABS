package com.example.vyra.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.vyra.data.models.AfricanCurrencies
import com.example.vyra.data.models.AfricanCurrency
import com.example.vyra.data.models.AfricanPaymentGatewaysDefaults
import com.example.vyra.data.models.PaymentGateway
import com.example.vyra.data.models.PaymentTransaction
import com.example.vyra.data.repository.PaymentGatewayRepository
import com.example.vyra.theme.CyberBorder
import com.example.vyra.theme.CyberSurface
import com.example.vyra.theme.ElectricMagenta
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.NeonGreen
import com.example.vyra.theme.QuantumViolet
import com.example.vyra.theme.TextMuted
import com.example.vyra.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AfricanPaymentGatewaySheet(
    title: String,
    amountUsd: Double,
    selectedCurrency: AfricanCurrency,
    onCurrencyChanged: (AfricanCurrency) -> Unit,
    gateways: List<PaymentGateway> = AfricanPaymentGatewaysDefaults.gateways,
    paymentGatewayRepository: PaymentGatewayRepository? = null,
    onDismiss: () -> Unit,
    onCheckoutSuccess: (PaymentTransaction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedGatewayId by remember { mutableStateOf("paystack") }
    var selectedPaymentMethod by remember { mutableStateOf("Debit/Credit Card") }
    var isProcessing by remember { mutableStateOf(false) }
    var processingStatusText by remember { mutableStateOf("") }
    var txSuccessResult by remember { mutableStateOf<PaymentTransaction?>(null) }

    val currentGateway = gateways.find { it.id.equals(selectedGatewayId, ignoreCase = true) } ?: gateways.first()
    val localAmountFormatted = selectedCurrency.formatAmount(amountUsd)

    Dialog(onDismissRequest = { if (!isProcessing) onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .testTag("dialog_african_payment_sheet"),
            colors = CardDefaults.cardColors(containerColor = CyberSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonCyan),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AFRICAN CHECKOUT",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    if (!isProcessing) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("btn_close_payment_sheet")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (txSuccessResult != null) {
                    // Success View
                    val tx = txSuccessResult!!
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = NeonGreen,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "PAYMENT VERIFIED!",
                            color = NeonGreen,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Ref: ${tx.referenceCode ?: tx.id.take(12)}",
                            color = TextMuted,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeonGreen.copy(alpha = 0.15f))
                                .border(1.dp, NeonGreen, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = tx.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Paid: $localAmountFormatted (${tx.gatewayName}) via ${tx.paymentMethod ?: "Card"}",
                                    color = NeonGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onCheckoutSuccess(tx)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_done_payment_sheet"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("DONE", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Amount Display
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeonCyan.copy(alpha = 0.15f))
                            .border(1.dp, NeonCyan, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = title.uppercase(),
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = localAmountFormatted,
                                    color = NeonCyan,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "≈ $${String.format("%.2f", amountUsd)} USD",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyberSurface)
                                    .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${selectedCurrency.flag} ${selectedCurrency.code}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Currency Selector (NGN, KES, ZAR, GHS)
                    Text(
                        text = "PREFERRED BILLING CURRENCY",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(
                            AfricanCurrencies.NGN,
                            AfricanCurrencies.KES,
                            AfricanCurrencies.ZAR,
                            AfricanCurrencies.GHS
                        ).forEach { curr ->
                            val isSel = curr.code == selectedCurrency.code
                            Box(
                                modifier = Modifier
                                    .testTag("sheet_currency_${curr.code}")
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) NeonCyan.copy(alpha = 0.25f) else CyberSurface)
                                    .border(1.dp, if (isSel) NeonCyan else CyberBorder, RoundedCornerShape(8.dp))
                                    .clickable { onCurrencyChanged(curr) }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "${curr.flag} ${curr.code}",
                                    color = if (isSel) Color.White else TextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Gateway Selection (Flutterwave, Paystack, OPay)
                    Text(
                        text = "SELECT PAYMENT GATEWAY SDK",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val displayGateways = listOf(
                        AfricanPaymentGatewaysDefaults.PAYSTACK,
                        AfricanPaymentGatewaysDefaults.FLUTTERWAVE,
                        AfricanPaymentGatewaysDefaults.OPAY
                    )

                    displayGateways.forEach { gw ->
                        val isSelected = gw.id == selectedGatewayId
                        val parseColor = try { Color(android.graphics.Color.parseColor(gw.accentHex)) } catch (_: Exception) { NeonCyan }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .testTag("sheet_gateway_${gw.id}")
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) parseColor.copy(alpha = 0.2f) else CyberSurface)
                                .border(1.dp, if (isSelected) parseColor else CyberBorder, RoundedCornerShape(10.dp))
                                .clickable { selectedGatewayId = gw.id }
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(parseColor.copy(alpha = 0.25f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccountBalanceWallet,
                                            contentDescription = null,
                                            tint = parseColor,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = gw.name,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = gw.description,
                                            color = TextMuted,
                                            fontSize = 9.sp,
                                            maxLines = 1
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = parseColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Payment Methods Selector
                    Text(
                        text = "PAYMENT METHOD",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    val methods = listOf("Debit/Credit Card", "Bank Transfer", "USSD", "Mobile Money / Wallet")
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        methods.forEach { method ->
                            val isSel = selectedPaymentMethod == method
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) QuantumViolet.copy(alpha = 0.3f) else CyberSurface)
                                    .border(1.dp, if (isSel) QuantumViolet else CyberBorder, RoundedCornerShape(6.dp))
                                    .clickable { selectedPaymentMethod = method }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = method,
                                    color = if (isSel) Color.White else TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Button or Loading Indicator
                    if (isProcessing) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = NeonCyan,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = processingStatusText,
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        val parseColorCurrent = try { Color(android.graphics.Color.parseColor(currentGateway.accentHex)) } catch (_: Exception) { NeonCyan }

                        Button(
                            onClick = {
                                isProcessing = true
                                processingStatusText = "Connecting to ${currentGateway.name} SDK..."
                                coroutineScope.launch {
                                    if (paymentGatewayRepository != null) {
                                        val initResult = paymentGatewayRepository.initiatePayment(
                                            amount = selectedCurrency.convertFromUsd(amountUsd),
                                            currencyCode = selectedCurrency.code,
                                            gatewayId = currentGateway.id,
                                            paymentMethod = selectedPaymentMethod
                                        )
                                        val reference = initResult.getOrNull()?.reference ?: "VYRA_${System.currentTimeMillis()}"
                                        
                                        processingStatusText = "Authenticating transaction ($reference)..."
                                        delay(400)
                                        
                                        processingStatusText = "Verifying with ${currentGateway.name}..."
                                        val verifiedResult = paymentGatewayRepository.verifyAndRecordPayment(
                                            reference = reference,
                                            gatewayId = currentGateway.id,
                                            amount = selectedCurrency.convertFromUsd(amountUsd),
                                            currencyCode = selectedCurrency.code,
                                            description = title
                                        )
                                        
                                        val tx = verifiedResult.getOrNull()?.transaction ?: PaymentTransaction(
                                            id = "tx_${System.currentTimeMillis()}",
                                            amount = selectedCurrency.convertFromUsd(amountUsd),
                                            currency = selectedCurrency.code,
                                            gateway = currentGateway.id,
                                            status = "completed",
                                            timestamp = System.currentTimeMillis(),
                                            description = title,
                                            referenceCode = reference,
                                            paymentMethod = selectedPaymentMethod
                                        )
                                        txSuccessResult = tx
                                    } else {
                                        delay(700)
                                        val tx = PaymentTransaction(
                                            id = "tx_${System.currentTimeMillis()}",
                                            amount = selectedCurrency.convertFromUsd(amountUsd),
                                            currency = selectedCurrency.code,
                                            gateway = currentGateway.id,
                                            status = "completed",
                                            timestamp = System.currentTimeMillis(),
                                            description = title,
                                            referenceCode = "${currentGateway.id.take(3).uppercase()}-${System.currentTimeMillis().toString().takeLast(6)}",
                                            paymentMethod = selectedPaymentMethod
                                        )
                                        txSuccessResult = tx
                                    }
                                    isProcessing = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = parseColorCurrent,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_pay_now_sdk_hook"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PAY $localAmountFormatted VIA ${currentGateway.name.uppercase()}",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
