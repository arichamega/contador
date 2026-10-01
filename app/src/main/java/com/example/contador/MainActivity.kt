package com.example.contador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contador.ui.theme.ContadorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContadorTheme {
                ContadorScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContadorScreen(
    viewModel: ContadorViewModel = viewModel()
) {
    val count by viewModel.count.collectAsState()

    ContadorContent(
        count = count,
        onIncrement = { viewModel.increment() },
        onDecrement = { viewModel.decrement() },
        onReset = { viewModel.reset() },
        onStepChange = { step ->
            if (step > 0) viewModel.increment(step) else viewModel.decrement(-step)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContadorContent(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    onStepChange: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Contador",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Status Chip
            val statusText = when {
                count > 0 -> "Positivo"
                count < 0 -> "Negativo"
                else -> "Cero"
            }
            val statusColor = when {
                count > 0 -> MaterialTheme.colorScheme.primary
                count < 0 -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.outline
            }

            AssistChip(
                onClick = { },
                label = { Text(statusText, color = statusColor, fontWeight = FontWeight.SemiBold) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main Counter Card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Animated Counter Number
                    AnimatedContent(
                        targetState = count,
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInVertically { height -> height } + fadeIn()) togetherWith
                                        (slideOutVertically { height -> -height } + fadeOut())
                            } else {
                                (slideInVertically { height -> -height } + fadeIn()) togetherWith
                                        (slideOutVertically { height -> height } + fadeOut())
                            }.using(SizeTransform(clip = false))
                        },
                        label = "counterAnimation"
                    ) { targetCount ->
                        Text(
                            text = "$targetCount",
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                targetCount > 0 -> MaterialTheme.colorScheme.primary
                                targetCount < 0 -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Action Buttons ( - , Reset , + )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Decrement Button
                        FilledTonalIconButton(
                            onClick = onDecrement,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Restar",
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        // Reset Button
                        OutlinedIconButton(
                            onClick = onReset,
                            modifier = Modifier.size(52.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reiniciar",
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Increment Button
                        FilledIconButton(
                            onClick = onIncrement,
                            modifier = Modifier.size(64.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Sumar",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Quick Step Buttons (-10, -5, +5, +10)
            Text(
                text = "Acciones rápidas",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuickActionButton(text = "-10") { onStepChange(-10) }
                QuickActionButton(text = "-5") { onStepChange(-5) }
                QuickActionButton(text = "+5") { onStepChange(+5) }
                QuickActionButton(text = "+10") { onStepChange(+10) }
            }
        }
    }
}

@Composable
fun QuickActionButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContadorPreview() {
    ContadorTheme {
        ContadorContent(
            count = 5,
            onIncrement = {},
            onDecrement = {},
            onReset = {},
            onStepChange = {}
        )
    }
}
