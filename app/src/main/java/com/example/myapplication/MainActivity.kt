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
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Item(
    val id: Int,
    val name: String,
    val code: String,
    val resourceType: String, // e.g., "spirit", "monk", "treasure"
    val baseIncrement: Int = 1, // Default increment
    val synergyRules: (Map<String, Boolean>) -> Int = { 0 } // Additional increment based on other items
)

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
    var storm by remember { mutableStateOf(0) }
    var monk by remember { mutableStateOf(0) }
    var extraResource by remember { mutableStateOf(0) }
    var isPanelExpanded by remember { mutableStateOf(false) } // State for panel

    // Animate the panel width
    val panelWidth by animateDpAsState(
        targetValue = if (isPanelExpanded) 200.dp else 60.dp, // Expanded: 200dp, Shrunk: 60dp
    )

    // This inits random items with item class structure
    val items = listOf(
        Item(1, "Kykar", "kykar", "spirit", 1, { states ->
            var extra = 0
            if (states["hProdigy"] == true) extra += 1 // Harmonic doubles it
            if (states["veyran"] == true) extra += 1  // Veyran adds another
            extra
        }),
        Item(2, "Harmonic Prodigy", "hProdigy", "none", 0), // No direct resource, just synergy
        Item(3, "Veyran", "veyran", "none", 0), // No direct resource, just synergy
        Item(4, "Monastery Mentor", "mMentor", "monk", 1, { states ->
            if (states["hProdigy"] == true) 1 else 0 // Harmonic doubles, Veyran has no effect
        })
    )

    // States of SidePanelItem are tracked here
    val checkedStates = remember {
        mutableStateMapOf<String, Boolean>().apply {
            items.forEach { put(it.code, false) } // Initialize all as unchecked
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // Optional: distinguish the layout
    ) {
        // Side Panel
        Box(
            modifier = Modifier
                .width(panelWidth)
                .fillMaxHeight()
                .background(Color.DarkGray)
                .padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Toggle Button
                Button(
                    onClick = { isPanelExpanded = !isPanelExpanded },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(if (isPanelExpanded) 50.dp else 40.dp), // Increase size for visibility
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    contentPadding = PaddingValues(0.dp) // Remove default padding
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu, // No need for the if-else
                        contentDescription = "Toggle Panel",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp) // Reduce size to fit inside button
                    )
                }

                // Panel Content (visible when expanded)
                if (isPanelExpanded) {


                    Column (
                        modifier = Modifier
                            .weight(1f) // Take remaining space
                            .verticalScroll(rememberScrollState())
                            .fillMaxHeight(),
                    ){
                        items.forEach { item ->
                            SidePanelItem(
                                item = item,
                                isChecked = checkedStates[item.code] ?: false, // Get state for this item
                                onCheckedChange = { newValue ->
                                    checkedStates[item.code] = newValue // Update state for this item
                                    println("${item.name} is now ${if (newValue) "checked" else "unchecked"}")
                                }
                            )
                        }
                    }

                }
            }
        }


        // Main Content Column
        Column(
            modifier = Modifier
                .weight(1f) // Take remaining space
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween // Space out children
        ) {

        // Main Trackers (Resource Trackers)
        FlowRow(
            modifier = Modifier
//                .weight(1f) // Take remaining space
                .padding(top = 25.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CounterColumn("Prowess", prowess, { prowess++ }, { prowess-- }, Modifier.weight(1f))
            CounterColumn("Storm", storm, { storm++ }, { storm-- }, Modifier.weight(1f))

            if (checkedStates["kykar"] == true) {
                val spiritIncrement = calculateIncrement("spirit", checkedStates, items)
                CounterColumn("Spirit", spirit, { spirit += spiritIncrement }, { spirit-- }, Modifier.weight(1f))
            }
            if (checkedStates["skArtist"] == true){
                CounterColumn("Treasure", treasure, { treasure++ }, { treasure-- }, Modifier.weight(1f))
            }
            if (checkedStates["mMentor"] == true){
                CounterColumn("Monk", monk, { monk++ }, { monk-- }, Modifier.weight(1f))
            }
            CounterColumn("Extra", extraResource, { extraResource++ }, { extraResource-- }, Modifier.weight(1f))
        }

        FlowRow(
            modifier = Modifier
//                .weight(1f) // Take remaining space
                .padding(25.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.Bottom // Align content to bottom
        ) {
            CastColumn("Creature", {})
            CastColumn("Ins/Soc", {})
            CastColumn("Non Creature", {})
        }
        }
    }
}
fun calculateIncrement(resourceType: String, checkedStates: Map<String, Boolean>, items: List<Item>): Int {
    var totalIncrement = 0
    items.filter { checkedStates[it.code] == true }.forEach { item ->
        if (item.resourceType == resourceType) {
            totalIncrement += item.baseIncrement + item.synergyRules(checkedStates)
        }
    }
    return totalIncrement
}

@Composable
fun SidePanelItem(
    item: Item,
    isChecked: Boolean,           // State passed from parent
    onCheckedChange: (Boolean) -> Unit  // Callback to update parent state
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = item.name,
            color = Color.White,
            fontSize = 15.sp,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start)
        )

        Checkbox(
            checked = isChecked,      // Use the passed state
            onCheckedChange = onCheckedChange  // Forward changes to parent
        )
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

@Composable
fun CastColumn(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Blue, // Default background color
    contentColor: Color = Color.White,  // Default text/icon color
    cornerRadius: Dp = 8.dp,            // Default corner radius
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp), // Default padding
    textStyle: TextStyle = TextStyle(fontSize = 16.sp) // Default text style
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(12.dp),
        shape = RoundedCornerShape(cornerRadius), // Rounded corners
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        contentPadding = padding
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}