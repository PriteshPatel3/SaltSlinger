package com.example.myapplication

import android.graphics.Rect
import android.os.Bundle
import android.view.TouchDelegate
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide status bar (NEW)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        setContent {
            GameResourceTracker()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameResourceTracker() {
    var spirit by remember { mutableStateOf(0) }
    var prowess by remember { mutableStateOf(0) }
    var treasure by remember { mutableStateOf(0) }
    var extraResource by remember { mutableStateOf(0) }

    // Row to arrange everything horizontally
    FlowRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        CounterColumn("Spirit", spirit, { spirit++ }, { spirit-- }, Modifier.weight(1f))
        CounterColumn("Prowess", prowess, { prowess++ }, { prowess-- } , Modifier.weight(1f))
        CounterColumn("Treasure", treasure, { treasure++ }, { treasure-- } , Modifier.weight(1f))
        CounterColumn("Extra", extraResource, { extraResource++ }, { extraResource-- } , Modifier.weight(1f)) // Fourth column
    }
}

@Composable
fun CounterColumn(
    label: String,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        // Decrement Button with expanded hitbox
        Box(
            modifier = Modifier
                // Increase the clickable area (e.g., 48.dp is Material Design minimum)
                .size(60.dp) // Larger touch area
                .clickable(onClick = onDecrement) // Handle click
        ) {
            Button(
                onClick = onDecrement, // Empty onClick since parent Box handles it
                modifier = Modifier
                    .size(30.dp) // Visible size remains 30.dp
                    .align(Alignment.Center), // Center within the larger hitbox
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                contentPadding = PaddingValues(0.dp) // Adjust padding
            ) {
                Text(
                    text = "-",
                    fontSize = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 20.sp, color = Color.Black)
            Text(text = count.toString(), fontSize = 24.sp, color = Color.Black)
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Increment Button with expanded hitbox
        CircularButton(
            icon = Icons.Default.Add,
            onClick = onIncrement
        )
    }
}

@Composable
fun CircularButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp) // Larger touch area
            .clickable(onClick = onClick) // Handle click
    ) {
        Button(
            onClick = onClick, // Empty onClick since parent Box handles it
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier
                .size(30.dp) // Visible size remains 30.dp
                .align(Alignment.Center), // Center within the larger hitbox
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Button",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}