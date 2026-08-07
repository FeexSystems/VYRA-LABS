package com.example.vyra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.vyra.theme.CyberBg
import com.example.vyra.theme.CyberBorder
import com.example.vyra.theme.CyberSurface
import com.example.vyra.theme.ElectricMagenta
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.NeonGreen
import com.example.vyra.theme.QuantumViolet
import com.example.vyra.theme.TextMuted
import com.example.vyra.theme.TextSecondary
import com.example.vyra.ui.components.CyberpunkCard
import com.example.vyra.ui.components.GoVyraHeader
import com.example.vyra.ui.viewmodels.ChatContact
import com.example.vyra.ui.viewmodels.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val contacts by viewModel.contacts.collectAsState()
    val selectedContactId by viewModel.selectedContactId.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var inputText by remember { mutableStateOf("") }

    val activeContact = contacts.find { it.id == selectedContactId } ?: contacts.firstOrNull()

    val filteredContacts = remember(contacts, searchQuery) {
        if (searchQuery.isBlank()) contacts
        else contacts.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.handle.contains(searchQuery, ignoreCase = true)
        }
    }

    val activeMessages = remember(messages, selectedContactId) {
        messages.filter { it.senderId == selectedContactId || it.recipientId == selectedContactId }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg)
    ) {
        // Top Go VYRA Header
        GoVyraHeader(
            title = "MESSAGING & INBOX",
            subtitle = "Direct Fan Messages & AI Agent Channels"
        )

        // Contact Selector Row
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search creators, fans & AI agents...", fontSize = 12.sp, color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = NeonCyan) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CyberBorder
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal Contact Avatars
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filteredContacts) { contact ->
                    val isSelected = contact.id == selectedContactId
                    ContactChipItem(
                        contact = contact,
                        isSelected = isSelected,
                        onClick = { viewModel.selectContact(contact.id) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Active Chat Box Area
        if (activeContact != null) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Active Contact Bar
                CyberpunkCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (activeContact.isAgent) QuantumViolet else NeonCyan
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (activeContact.isAgent) QuantumViolet.copy(alpha = 0.3f) else NeonCyan.copy(alpha = 0.3f))
                                    .border(1.dp, if (activeContact.isAgent) QuantumViolet else NeonCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (activeContact.isAgent) {
                                    Icon(Icons.Default.Psychology, contentDescription = "Agent", tint = QuantumViolet, modifier = Modifier.size(20.dp))
                                } else {
                                    Text(activeContact.name.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(activeContact.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${activeContact.handle} • ${activeContact.role}", color = TextMuted, fontSize = 11.sp)
                            }
                        }

                        // Online Status
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (activeContact.isOnline) NeonGreen else TextMuted)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                if (activeContact.isOnline) "ONLINE" else "OFFLINE",
                                color = if (activeContact.isOnline) NeonGreen else TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Messages List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(activeMessages) { msg ->
                        MessageBubbleItem(msg = msg)
                    }
                }

                // AI Agent Suggestions
                if (activeContact.isAgent) {
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        val prompts = when (activeContact.id) {
                            "bushfeexer" -> listOf("Generate TikTok hashtags", "Analyze optimal hook time")
                            "holokai" -> listOf("Create pidgin voice greeting", "Simulate fan response")
                            else -> listOf("Forecast MRR for next month", "Optimize payment conversion")
                        }
                        items(prompts) { p ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(QuantumViolet.copy(alpha = 0.2f))
                                    .border(1.dp, QuantumViolet, RoundedCornerShape(12.dp))
                                    .clickable {
                                        viewModel.sendMessage(p)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(p, color = QuantumViolet, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Message Input Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Type message or prompt...", fontSize = 12.sp, color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendMessage(inputText)
                                inputText = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(NeonCyan)
                            .testTag("chat_send_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ContactChipItem(
    contact: ChatContact,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) NeonCyan.copy(alpha = 0.25f) else CyberSurface)
            .border(1.dp, if (isSelected) NeonCyan else CyberBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (contact.isOnline) NeonGreen else TextMuted)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = contact.name,
                color = if (isSelected) NeonCyan else Color.White,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun MessageBubbleItem(msg: com.example.vyra.data.models.ChatMessage) {
    val isUser = msg.isFromUser
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .background(if (isUser) NeonCyan.copy(alpha = 0.2f) else CyberSurface)
                .border(
                    1.dp,
                    if (isUser) NeonCyan else CyberBorder,
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isUser) "You" else msg.senderName,
                        color = if (isUser) NeonCyan else ElectricMagenta,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Text(
                        text = msg.timestamp,
                        color = TextMuted,
                        fontSize = 9.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = msg.content,
                    color = Color.White,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
