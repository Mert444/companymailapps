package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel

data class ToneDetail(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TranslatorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val inputText by viewModel.inputText.collectAsState()
    val selectedTone by viewModel.selectedTone.collectAsState()
    val translatedEmail by viewModel.translatedEmail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var copiedToClipboard by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val toneDetails = remember {
        listOf(
            ToneDetail(
                title = "Grenzen aufzeigen",
                subtitle = "Verweist höflich & unanfechtbar auf den bestehenden Rahmen.",
                icon = Icons.Default.Shield
            ),
            ToneDetail(
                title = "Diplomatisch",
                subtitle = "Fokus auf Lösungen, Kompromisse und gemeinsamen Erfolg.",
                icon = Icons.Default.Psychology
            ),
            ToneDetail(
                title = "Kurz & Prägnant",
                subtitle = "Executive Summary: Direkte Stichpunkte ohne Beiwerk.",
                icon = Icons.Default.Speed
            ),
            ToneDetail(
                title = "Deeskalierend",
                subtitle = "Nimmt Druck heraus, beruhigt die Situation bei klarem Ziel.",
                icon = Icons.Default.VolunteerActivism
            )
        )
    }

    // Glowing animation for active state
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Executive Hero Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            )
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Executive Emblem",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "EXECUTIVE SUITE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.5.sp
                            )
                        }
                        Text(
                            text = "C-Level E-Mail Dolmetscher",
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Übersetzt Frust in souveräne, unanfechtbare Business-Mails.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Section 1: Input Box
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "1. RAW DRAFT / EMOTIONALE EINGABE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )

                if (inputText.isNotBlank()) {
                    Text(
                        text = "${inputText.length} Zeichen",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            OutlinedTextField(
                value = inputText,
                onValueChange = { viewModel.onInputTextChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_text_field"),
                placeholder = {
                    Text(
                        "Schreib frei von der Seele was dich nervt... e.g. \"Ich habe dir das schon 100-mal gesagt, liest du deine Mails gar nicht?!\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                minLines = 4,
                maxLines = 8,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                trailingIcon = {
                    if (inputText.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.onInputTextChange("") },
                            modifier = Modifier.testTag("clear_input_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Text löschen",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )

            // Test Prompts Chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                viewModel.sampleInputs.forEachIndexed { index, sample ->
                    SuggestionChip(
                        onClick = { viewModel.onInputTextChange(sample) },
                        label = {
                            Text(
                                text = "Test-Szenario ${index + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = "Beispiel",
                                modifier = Modifier.size(12.dp)
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Section 2: Tone Nuance Selector Cards
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "2. DIPLOMATISCHE TONE-NUANCE WÄHLEN",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                toneDetails.forEach { detail ->
                    val isSelected = detail.title == selectedTone
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { viewModel.onToneSelect(detail.title) }
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(14.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = detail.icon,
                                    contentDescription = detail.title,
                                    tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = detail.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = detail.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Action Generate Button
        Button(
            onClick = {
                copiedToClipboard = false
                viewModel.translateEmail()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("translate_button"),
            enabled = inputText.isNotBlank() && !isLoading,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 2.dp
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Transformiere in C-Level E-Mail...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Generieren"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "IN C-LEVEL E-MAIL TRANSFORMEREN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Section 3: Result Output Card
        AnimatedVisibility(
            visible = translatedEmail.isNotBlank(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "3. FERTIGE C-LEVEL BUSINESS E-MAIL",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = selectedTone,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .testTag("result_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        // Email Subject Header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Mail Header",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Betreff: Geschäftliche Abstimmung",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )

                        Text(
                            text = translatedEmail,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 24.sp,
                            fontFamily = FontFamily.SansSerif
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 14.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )

                        // Action Buttons: Copy, Share
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("C-Level E-Mail", translatedEmail)
                                    clipboard.setPrimaryClip(clip)
                                    copiedToClipboard = true
                                    viewModel.showToast("E-Mail erfolgreich in die Zwischenablage kopiert!")
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("copy_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (copiedToClipboard) Color(0xFF10B981) else MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    imageVector = if (copiedToClipboard) Icons.Default.Check else Icons.Default.ContentCopy,
                                    contentDescription = "Kopieren"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (copiedToClipboard) "In Zwischenablage kopiert!" else "E-Mail Kopieren",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "Geschäftliche Abstimmung")
                                        putExtra(Intent.EXTRA_TEXT, translatedEmail)
                                    }
                                    context.startActivity(Intent.createChooser(intent, "C-Level E-Mail teilen"))
                                },
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("share_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Teilen"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
