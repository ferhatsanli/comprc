package com.ferhat.comprc.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
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
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
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
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    uiState = uiState,
                    onPowerButtonClick = { viewModel.onPowerButtonClick() },
                    onRefresh = { viewModel.onRefreshServerStatus() },
                    onTargetIPChange = { newIP -> viewModel.onTargetIPChanged(newIP) }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    onPowerButtonClick: () -> Unit,
    onRefresh: () -> Unit,
    onTargetIPChange: (String) -> Unit,
    onServerTimeChanged: (String) -> Unit
) {
    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(modifier = modifier.fillMaxSize()) {
            TargetTextBox(
                modifier = Modifier.align(Alignment.TopCenter),
                targetIP = uiState.serverIP,
                onValueChange = onTargetIPChange
            )
            ServerTime(
                modifier = Modifier.align(Alignment.BottomCenter),
                time =
            )
        }
        RemotePowerButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            onClick = onPowerButtonClick
        )
        ServerStatusBar(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            status = uiState.serverStatus,
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ServerStatusBar(
    modifier: Modifier = Modifier,
    status: ServerStatus = ServerStatus.NA,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {}
) {
    val ledColor = when {
        isRefreshing -> Color(0xFFFFC107) // Sari
        status == ServerStatus.OK -> Color(0xFF4CAF50) // Yeşil
        status == ServerStatus.BAD -> Color(0xFFF44336) // Kırmızı
        else -> Color(0xFFBDBDBD) // Gri
    }
    val statusText = when (status) {
        ServerStatus.OK -> "OK"
        ServerStatus.BAD -> "Bad"
        ServerStatus.NA -> "N/A"
    }
    val rotation = rememberRotation(isRefreshing)
    Row(
        modifier = modifier
//            .background(Color(0x66000000), shape = CircleShape)
            .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .clickable {
                Log.i("ferhat", "ServerStatusBar: isRefreshing=$isRefreshing, status=$status")
                onRefresh()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Refresh butonu
        Box(
            modifier = Modifier
                .size(28.dp)
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Yenile",
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .graphicsLayer { rotationZ = rotation }
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
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun rememberRotation(isRefreshing: Boolean): Float {
    return if (isRefreshing) {
        val infiniteTransition = rememberInfiniteTransition(label = "refresh")
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing)
            ),
            label = "rotation"
        ).value
    } else 0f
}

@Composable
fun TargetTextBox(
    modifier: Modifier = Modifier,
    targetIP: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier.padding(horizontal = 10.dp, vertical = 36.dp),
        value = targetIP,
        onValueChange = onValueChange
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RemotePowerButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val TAG = "ferhat"
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
                            Log.i(TAG, "RemotePowerButton: pressed down")
                            true
                        }

                        android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                            pressed = false
                            if (it.action == android.view.MotionEvent.ACTION_UP) {
                                Log.i(TAG, "RemotePowerButton: released")
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

@Composable
fun ServerTime(modifier: Modifier = Modifier, time: String = "13:35.33"){
    Box(modifier.background(MaterialTheme.colorScheme.secondary, shape = CircleShape)){
        Text(text = time, fontSize = 18.sp)
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
            onRefresh = {},
            onTargetIPChange = {}
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun colorCheck(){
//    Box(Modifier.size(30.dp).background(MaterialTheme.colorScheme.secondary))
//}