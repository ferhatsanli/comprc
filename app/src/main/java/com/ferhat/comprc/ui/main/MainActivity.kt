package com.ferhat.comprc.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
// ...existing code...
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ferhat.comprc.presentation.MainViewModel
import com.ferhat.comprc.domain.model.MainUiState
import com.ferhat.comprc.domain.model.ServerStatus
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ferhat.comprc.ui.theme.CompRCTheme
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.graphicsLayer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompRCTheme {
                val viewModel: MainViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()
                MainScreen(
                    uiState = uiState,
                    onPowerButtonClick = { viewModel.onPowerButtonClick() },
                    onRefresh = { viewModel.onRefreshServerStatus() }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    uiState: MainUiState,
    onPowerButtonClick: () -> Unit,
    onRefresh: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        RemotePowerButton(
            modifier = Modifier.fillMaxSize(),
            onClick = onPowerButtonClick
        )
        ServerStatusBar(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            status = uiState.serverStatus,
            onRefresh = onRefresh
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ServerStatusBar(
    modifier: Modifier = Modifier,
    status: ServerStatus = ServerStatus.NA,
    onRefresh: () -> Unit = {}
) {
    val ledColor = when (status) {
        ServerStatus.OK -> Color(0xFF4CAF50) // Yeşil
        ServerStatus.BAD -> Color(0xFFF44336) // Kırmızı
        ServerStatus.NA -> Color(0xFFBDBDBD) // Gri
    }
    val statusText = when (status) {
        ServerStatus.OK -> "OK"
        ServerStatus.BAD -> "Bad"
        ServerStatus.NA -> "N/A"
    }
    Row(
        modifier = modifier
            .background(Color(0x66000000), shape = CircleShape)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Refresh butonu
        Box(
            modifier = Modifier
                .size(28.dp)
                .zIndex(1f)
                .pointerInteropFilter {
                    when (it.action) {
                        android.view.MotionEvent.ACTION_UP -> {
                            onRefresh()
                            true
                        }
                        else -> false
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Yenile",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        // Küçük LED
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(ledColor, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        // Durum metni
        Text(
            text = "Server status: $statusText",
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RemotePowerButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = 0.5f)
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .background(
                    color = Color.Red,
                    shape = CircleShape
                )
                .pointerInteropFilter {
                    when (it.action) {
                        android.view.MotionEvent.ACTION_DOWN -> {
                            pressed = true
                            true
                        }
                        android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                            pressed = false
                            if (it.action == android.view.MotionEvent.ACTION_UP) {
                                onClick()
                            }
                            true
                        }
                        else -> false
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = "Aç/Kapa",
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

// onPowerButtonClick kaldırıldı, event ViewModel'e deleg ediliyor

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CompRCTheme {
        MainScreen(
            uiState = MainUiState(serverStatus = ServerStatus.OK),
            onPowerButtonClick = {},
            onRefresh = {}
        )
    }
}
