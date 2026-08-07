package com.example.vyra.ui.screens

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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.vyra.data.models.AfricanCurrencies
import com.example.vyra.data.models.PaymentGateway
import com.example.vyra.theme.CyberBg
import com.example.vyra.theme.CyberBorder
import com.example.vyra.theme.CyberSurface
import com.example.vyra.theme.ElectricMagenta
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.NeonGreen
import com.example.vyra.theme.QuantumViolet
import com.example.vyra.theme.TextMuted
import com.example.vyra.theme.TextSecondary
import com.example.vyra.theme.parseHexColor
import com.example.vyra.ui.components.AfricanPaymentGatewaySheet
import com.example.vyra.ui.components.CyberpunkCard
import com.example.vyra.ui.viewmodels.MonetizationViewModel
import com.example.vyra.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MonetizationScreen(
    viewModel: MonetizationViewModel,
    profileViewModel: ProfileViewModel
) {
    val projectedRevenue by viewModel.projectedRevenue.collectAsState()
    val vipPrice by viewModel.vipPrice.collectAsState()
    val premiumPrice by viewModel.premiumPrice.collectAsState()
    val tiers by viewModel.tiers.collectAsState()

    val userProfile by profileViewModel.userProfile.collectAsState()
    val selectedCurrency by profileViewModel.selectedCurrency.collectAsState()

    var showConfigureGatewayDialog by remember { mutableStateOf<PaymentGateway?>(null) }
    var gatewayAccountInput by remember { mutableStateOf("") }
    var testTxStatusMessage by remember { mutableStateOf<String?>(null) }
    var showCheckoutSheet by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "MONETIZATION & PAYMENT GATEWAYS",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "African multi-currency pricing, Flutterwave/Paystack/OPay gateway payouts & subscription tiers",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        // 1. PREFERRED LOCAL CURRENCY SELECTOR CARD
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CurrencyExchange,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SELECT PREFERRED LOCAL CURRENCY",
                                color = NeonCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonCyan.copy(alpha = 0.2f))
                                .border(1.dp, NeonCyan, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${selectedCurrency.flag} ${selectedCurrency.code} (${selectedCurrency.symbol})",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Choose your billing and payout currency across major African markets:",
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AfricanCurrencies.list.forEach { currency ->
                            val isSelected = currency.code == selectedCurrency.code
                            Box(
                                modifier = Modifier
                                    .testTag("currency_chip_${currency.code}")
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) NeonCyan.copy(alpha = 0.25f)
                                        else CyberSurface
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) NeonCyan else CyberBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { profileViewModel.setCurrency(currency.code) }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = currency.flag,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = currency.code,
                                        color = if (isSelected) Color.White else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. PROJECTED REVENUE OVERVIEW CARD
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonGreen,
                accentGlow = QuantumViolet
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "PROJECTED MONTHLY REVENUE (${selectedCurrency.code})",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedCurrency.formatAmount(projectedRevenue),
                                color = NeonGreen,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "≈ $${String.format("%.2f", projectedRevenue)} USD @ 1 USD = ${selectedCurrency.rateToUsd} ${selectedCurrency.code}",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(NeonGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }

        // LAUNCH CHECKOUT SHEET BUTTON
        item {
            Button(
                onClick = { showCheckoutSheet = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricMagenta,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_launch_checkout_sheet"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Payment,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LAUNCH AFRICAN CHECKOUT SDK (PAYSTACK / FLUTTERWAVE / OPAY)",
                    color = Color.Black,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        // 3. AFRICAN PAYMENT GATEWAYS LIST
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = null,
                            tint = ElectricMagenta,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AFRICAN PAYMENT GATEWAYS",
                            color = ElectricMagenta,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Text(
                        text = "Primary Payout: ${userProfile.creatorDetails.primaryPayoutGatewayId.uppercase()}",
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Integrated Flutterwave, Paystack & OPay gateways for African local currency transactions",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        items(userProfile.gateways) { gateway ->
            val isPrimary = userProfile.creatorDetails.primaryPayoutGatewayId == gateway.id
            val gatewayColor = parseHexColor(gateway.badgeColorHex)

            CyberpunkCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("payment_gateway_${gateway.id}"),
                borderColor = if (isPrimary) gatewayColor else CyberBorder,
                accentGlow = if (isPrimary) gatewayColor.copy(alpha = 0.3f) else Color.Transparent
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(gatewayColor.copy(alpha = 0.2f))
                                    .border(1.dp, gatewayColor, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = gatewayColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = gateway.name,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    if (gateway.isConnected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Connected",
                                            tint = NeonGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = gateway.brandTagline,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = { profileViewModel.setPrimaryPayoutGateway(gateway.id) },
                            modifier = Modifier.testTag("btn_select_primary_${gateway.id}")
                        ) {
                            Icon(
                                imageVector = if (isPrimary) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                contentDescription = "Set Primary Payout Gateway",
                                tint = if (isPrimary) gatewayColor else TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Account: ${gateway.accountIdentifier}",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Fee: ${gateway.feePercentage}%",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Supported: ${gateway.supportedRegions}",
                        color = TextMuted,
                        fontSize = 10.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                showConfigureGatewayDialog = gateway
                                gatewayAccountInput = gateway.accountIdentifier
                            },
                            modifier = Modifier.testTag("btn_configure_gateway_${gateway.id}"),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("CONFIGURE ACCOUNT", color = TextSecondary, fontSize = 10.sp)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                profileViewModel.executeSimulatedTransaction(
                                    type = "PAYOUT",
                                    title = "Test Payout via ${gateway.name}",
                                    amountUsd = 100.0,
                                    gatewayName = gateway.name
                                )
                                testTxStatusMessage = "Payout of ${selectedCurrency.formatAmount(100.0)} sent via ${gateway.name}!"
                            },
                            modifier = Modifier.testTag("btn_test_payout_${gateway.id}"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = gatewayColor,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("TEST PAYOUT", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        testTxStatusMessage?.let { status ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NeonGreen.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = status,
                            color = NeonGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 4. PRICE MODELING SLIDERS
        item {
            CyberpunkCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "ADAPTIVE SUBSCRIPTION TIER PRICING",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "VIP Tier Price: ${selectedCurrency.formatAmount(vipPrice.toDouble())}/mo ($${String.format("%.2f", vipPrice)} USD)",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = vipPrice,
                        onValueChange = { viewModel.updateVipPrice(it) },
                        valueRange = 9.99f..49.99f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricMagenta,
                            activeTrackColor = ElectricMagenta,
                            inactiveTrackColor = CyberBorder
                        ),
                        modifier = Modifier.testTag("slider_vip_price")
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Premium Tier Price: ${selectedCurrency.formatAmount(premiumPrice.toDouble())}/mo ($${String.format("%.2f", premiumPrice)} USD)",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = premiumPrice,
                        onValueChange = { viewModel.updatePremiumPrice(it) },
                        valueRange = 4.99f..24.99f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = CyberBorder
                        ),
                        modifier = Modifier.testTag("slider_premium_price")
                    )
                }
            }
        }

        // 5. ACTIVE SUBSCRIPTION TIERS LIST
        item {
            Text(
                text = "ACTIVE SUBSCRIPTION TIERS",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        items(tiers) { tier ->
            val tierColor = Color(tier.badgeColorHex)
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = tierColor
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = tier.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "${tier.subscribersCount} Active Subscribers",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = "${selectedCurrency.formatAmount(tier.priceMonthly)}/mo",
                            color = tierColor,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    tier.benefits.forEach { b ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = tierColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = b, color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    showConfigureGatewayDialog?.let { gw ->
        androidx.compose.ui.window.Dialog(onDismissRequest = { showConfigureGatewayDialog = null }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "CONFIGURE ${gw.name.uppercase()}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Enter account identifier / merchant key / phone number for local payouts:",
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = gatewayAccountInput,
                        onValueChange = { gatewayAccountInput = it },
                        label = { Text("Account Identifier / Merchant ID") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedLabelColor = NeonCyan
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_gateway_account")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { showConfigureGatewayDialog = null },
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
                        ) {
                            Text("CANCEL", color = TextMuted)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                profileViewModel.updateGatewayDetails(
                                    gatewayId = gw.id,
                                    accountIdentifier = gatewayAccountInput,
                                    accountName = userProfile.creatorDetails.name
                                )
                                showConfigureGatewayDialog = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black)
                        ) {
                            Text("SAVE GATEWAY", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showCheckoutSheet) {
        AfricanPaymentGatewaySheet(
            title = "Vyra AI Creator Subscription / Tip",
            amountUsd = 25.0,
            selectedCurrency = selectedCurrency,
            onCurrencyChanged = { curr -> profileViewModel.setCurrency(curr.code) },
            gateways = userProfile.gateways,
            onDismiss = { showCheckoutSheet = false },
            onCheckoutSuccess = { tx ->
                profileViewModel.executeSimulatedTransaction(
                    type = tx.type,
                    title = tx.title,
                    amountUsd = tx.amountUsd,
                    gatewayName = tx.gatewayName
                )
                testTxStatusMessage = "Success! Received ${selectedCurrency.formatAmount(tx.amountUsd)} via ${tx.gatewayName} (${tx.referenceCode})"
            }
        )
    }
}
