package com.example.ballgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameScreen()
        }
    }
}

@Composable
fun GameScreen() {
    val ballX = remember { mutableStateOf(200f) }
    val ballY = remember { mutableStateOf(100f) }
    val paddleX = remember { mutableStateOf(100f) }
    val score = remember { mutableStateOf(0) }
    val velocityY = remember { mutableStateOf(20f) }
    val ballRadius = 20f
    val paddleWidth = 150f
    val paddleHeight = 20f

    val canvasHeightState = remember { mutableStateOf(0f) } 

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    paddleX.value = (paddleX.value + dragAmount.x).coerceIn(0f, size.width - paddleWidth)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            canvasHeightState.value = size.height

            drawCircle(
                color = Color.Red,
                radius = ballRadius,
                center = Offset(ballX.value, ballY.value)
            )
            drawRect(
                color = Color.Blue,
                topLeft = Offset(paddleX.value, size.height - paddleHeight - 20f),
                size = Size(paddleWidth, paddleHeight)
            )
        }

        Text(
            text = "Score: ${score.value}",
            modifier = Modifier.align(Alignment.TopCenter).padding(12.dp,30.dp),
            fontSize = 32.sp,
            color = Color.Yellow
        )

        LaunchedEffect(canvasHeightState.value) {
            while (true) {
                delay(16)

                ballY.value += velocityY.value

                val paddleTop = canvasHeightState.value - paddleHeight - 20f

                val isBallHitPaddle = ballY.value + ballRadius >= paddleTop &&
                        ballX.value in paddleX.value..(paddleX.value + paddleWidth)

                if (isBallHitPaddle) {
                    velocityY.value = -velocityY.value
                    score.value++
                }

                if (ballY.value <= 0f) {
                    velocityY.value = -velocityY.value
                }

                if (ballY.value > canvasHeightState.value) {
                    ballY.value = 100f
                    score.value = 0
                    velocityY.value = 20f
                }
            }
        }
    }
}
