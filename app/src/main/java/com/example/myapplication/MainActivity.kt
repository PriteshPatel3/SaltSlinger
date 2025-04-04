package com.example.myapplication

import android.app.Activity
import android.app.Application
import android.graphics.drawable.ColorDrawable
import com.example.myapplication.getItemsByTriggerType
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.reflect.KMutableProperty0
import com.example.myapplication.models.GameResources
import com.example.myapplication.models.Item
import com.example.myapplication.cast
import com.example.myapplication.castGeneral
import com.example.myapplication.view.GameViewModel
import dagger.hilt.android.HiltAndroidApp
import java.io.InputStream

//import com.example.myapplication.

@HiltAndroidApp
class MyApp : Application()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val window = (context as? Activity)?.window

            // Prevent screen from sleeping
            DisposableEffect(Unit) {
                window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                onDispose {
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                // Your main UI
                GameResourceTracker()

                // Logo at the bottom-right corner
                Image(
                    painter = painterResource(id = R.drawable.logo), // Ensure logo is in res/drawable
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(150.dp) // Adjust size as needed
                        .align(Alignment.BottomEnd) // Align to bottom-right
                        .padding(35.dp) // Add some margin
                )
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameResourceTracker() {
//    var gameResources by remember { mutableStateOf(GameResources()) }
    val viewModel: GameViewModel = viewModel()
    val gameResources = viewModel.gameResources.value
//    val gameResources = remember { GameResources() }
//    var isPanelExpanded by remember { mutableStateOf(false) }
    // Animate the panel width
    val panelWidth by animateDpAsState(
        targetValue = if (gameResources.isPanelExpanded.value) 200.dp else 60.dp, // Expanded: 200dp, Shrunk: 60dp
    )

    // This inits random items with item class structure
    val items = listOf(
        Item(1, "Kykar", "kykar", "spirit", "hasteSpirit", 1, "noncreature", "creaturegenerator", R.drawable.spirit_token,{ states ->
            var extra = 0
            if (states["hProdigy"] == true) extra += 1 // Harmonic doubles it
            if (states["veyran"] == true) extra += 1  // Veyran adds another
            extra
        }),
        Item(2, "Harmonic Prodigy", "hProdigy", "none", "none", 0, "none", "buff",), // No direct resource, just synergy
        Item(3, "Veyran", "veyran", "none", "none",0, "none", "buff",), // No direct resource, just synergy
        Item(4, "Monastery Mentor", "mMentor", "monk", "hasteMonk", 1, "noncreature", "creaturegenerator", R.drawable.monk_token, { states ->
            var extra = 0
            if (states["veyran"] == true) extra += 1  // Veyran adds another
            if (states["hProdigy"] == true) extra += 1 // Harmonic doubles, Veyran has no effect
            extra
        }),
        Item(5, "Storm Kiln Artist", "skArtist", "treasure", "none",1, "magecraft", "generator",R.drawable.treasure_token,{ states ->
            var extra = 0
            if (states["hProdigy"] == true) extra += 1
            if (states["veyran"] == true) extra += 1  // Veyran adds another
            extra
        }),
        Item(7, "Kykar Zehphyr Awakaner", "kykar-z-awak", "spirit", "hasteSpirit", 1, "noncreature", "creaturegenerator",R.drawable.spirit_token,{ states ->
            var extra = 0
            if (states["hProdigy"] == true) extra += 1 // Harmonic doubles it
            if (states["veyran"] == true) extra += 1  // Veyran adds another
            extra
        }),
//        Item(7, "Archmage Emritus", "aEmritus", "draw", 1, "magecraft", "buff"),
//        Item(7, "Archmage of runes", "aRunes", "draw", 1, "instsor", "buff"),
//        Item(7, "Ashling flame dancer", "afDancer", "discarddraw", 1, "magecraft", "buff"),
        Item(7, "Balmor battlemage captain", "bbCaptain", "battlemage buff", "none",1, "instsor", "buff",R.drawable.bbcap_token,{ states ->
            var extra = 0
            if (states["hProdigy"] == true) extra += 1 // Harmonic doubles it
            if (states["veyran"] == true) extra += 1  // Veyran adds another
            extra
        }),
        Item(7, "Jeskai Ascendency", "jAscendency", "Jeskai Asc buff", "none",1, "noncreature", "buff",R.drawable.jasc_token,{ states ->
            var extra = 0
            if (states["veyran"] == true) extra += 1  // Veyran adds another
            extra
        }),
        Item(7, "whirldwind of thought", "woThought", "draw", "none",1, "noncreature", "buff",-1, { states ->
            var extra = 0
            if (states["veyran"] == true) extra += 1  // Veyran adds another
            extra
        }),
        Item(7, "Birgi", "birgi", "Birgi Red", "none",1, "any", "generator",R.drawable.redmana_token, { states ->
            var extra = 0
            if (states["veyran"] == true) extra += 1  // Veyran adds another
            extra
        })
    )

    // States of SidePanelItem are tracked here
    val checkedStates = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },  // Convert state map to a list of pairs
            restore = { list ->
                mutableStateMapOf<String, Boolean>().apply {
                    list.forEach { (key, value) -> put(key, value) }
                }
            }
        )
    ) {
        mutableStateMapOf<String, Boolean>().apply {
            items.forEach { put(it.code, false) } // Initialize all as unchecked
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFCBC9F9))) {
        // Main Content (background)
        // Scrollable Resource Trackers
        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp)
                .verticalScroll(rememberScrollState())
                .padding(top = 25.dp)

        ) {
            MainTracker(gameResources, checkedStates, items)
            Spacer(modifier = Modifier.height(150.dp)) // Add spacing for bottom overlay
        }

        // Overlay button
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .zIndex(1f)
                .background(Color.White.copy(alpha = 0.3f)) // 30% transparent white
                .padding(bottom = 5.dp)
                .fillMaxWidth()
        ) {
            CastButtonSection(gameResources, checkedStates, items)
        }
        // Floating Side Panel
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart) // Position it on the left side
                .zIndex(1f) // Float above main content
                .width(panelWidth)
                .fillMaxHeight()
//                .background(Color.White)
                .padding(0.dp)
//                .background(Color.White.copy(alpha = 0.5f))

        ) {
            // Your existing side panel content here
            SidePanel(gameResources, checkedStates, items)
        }
    }
}

@Composable
fun SidePanel(gameResources: GameResources, checkedStates: MutableMap<String, Boolean>, items: List<Item>) {
    val targetBackgroundColor = if (gameResources.isPanelExpanded.value) {
        Color.White.copy(alpha = 0.7f) // 30% transparent
    } else {
        Color.White.copy(alpha = 0f) // Fully transparent
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(targetBackgroundColor)
            .padding(10.dp)

    ) {
        // Toggle Button
        Button(
            onClick = {
                gameResources.isPanelExpanded.value = !gameResources.isPanelExpanded.value
                      },
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(if (gameResources.isPanelExpanded.value) 50.dp else 40.dp), // Increase size for visibility
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(0.dp), // Remove default padding
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0f))
        ) {
            Icon(
                imageVector = Icons.Default.Menu, // No need for the if-else
                contentDescription = "Toggle Panel",
                tint = Color(0xFF6B68B0),
                modifier = Modifier.size(30.dp) // Reduce size to fit inside button
            )
        }

        // Panel Content (visible when expanded)
        if (gameResources.isPanelExpanded.value) {
            Column (
                modifier = Modifier
                    .weight(1f) // Take remaining space
                    .verticalScroll(rememberScrollState())
//                    .background(Color.White)
                    .fillMaxHeight(),
            ){

                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
//                                .verticalScroll(rememberScrollState())
                ) {
                    // Toggle Button
                    Button(
                        onClick = { gameResources.sectionGenerator.value = !gameResources.sectionGenerator.value },
                        modifier = Modifier
                            .padding(8.dp)
                            .width(150.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(
                            0xFF8480D9
                        )
                        ),
                        shape = RoundedCornerShape(12.dp), // Adds rounded corners
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Generator",
                            color = Color.White, // Adjust text color as needed
                            fontSize = 14.sp,    // Adjust font size as needed
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.CenterVertically) // Center vertically
                        )
                    }
                    if (gameResources.sectionGenerator.value) {

                        Column(
                            modifier = Modifier
                                .background(Color(0x30FFFFFF)) // Set background color here
                                .fillMaxHeight()
                                .wrapContentSize()
                        ) {
                            items.filter { it.type == "generator" ||  it.type == "creaturegenerator"}.forEach { item ->
                                SidePanelItem(
                                    item = item,
                                    isChecked = checkedStates[item.code] ?: false,
                                    onCheckedChange = { newValue ->
                                        checkedStates[item.code] = newValue
                                        println("${item.name} is now ${if (newValue) "checked" else "unchecked"}")
                                    }
                                )
                            }
                        }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
//                                .verticalScroll(rememberScrollState())
                ) {
                    // Toggle Button
                    Button(
                        onClick = { gameResources.sectionBuff.value = !gameResources.sectionBuff.value },
                        modifier = Modifier
                            .padding(8.dp)
                            .width(150.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(
                            0xFF8480D9
                        )
                        ),
                        shape = RoundedCornerShape(12.dp), // Adds rounded corners
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Buffs",
                            color = Color.White, // Adjust text color as needed
                            fontSize = 14.sp,    // Adjust font size as needed
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.CenterVertically) // Center vertically
                        )
                    }
                    if (gameResources.sectionBuff.value) {

                        Column(
                            modifier = Modifier
                                .background(Color(0x30FFFFFF)) // Set background color here
                                .fillMaxHeight()
                                .wrapContentSize()
                        ) {
                            items.filter { it.type == "buff" }.forEach { item ->
                                SidePanelItem(
                                    item = item,
                                    isChecked = checkedStates[item.code] ?: false,
                                    onCheckedChange = { newValue ->
                                        checkedStates[item.code] = newValue
                                        println("${item.name} is now ${if (newValue) "checked" else "unchecked"}")
                                    }
                                )
                            }
                        }
                    }
                }

            }

        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainTracker(gameResources: GameResources, checkedStates: MutableMap<String, Boolean>, items: List<Item> ){
        // Scrollable Resource Trackers
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(), // Make sure FlowRow expands
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier
//                    .background(Color(0xFFF8F5E7)) // Set your desired background color
                    .padding(8.dp) // Optional: add padding
            ) {
                Row {
                    CounterColumn(mapOf("image" to R.drawable.prowess_token, "text" to "Prowess"), gameResources.prowess.value, { gameResources.prowess.value++ }, { gameResources.prowess.value-- }, Modifier.weight(1f))
                    CounterColumn(mapOf("image" to R.drawable.storm_token, "text" to "Storm"), gameResources.storm.value, { gameResources.storm.value++ }, { gameResources.storm.value-- }, Modifier.weight(1f))
                    CounterColumn(mapOf("image" to R.drawable.treasure_token, "text" to "Treasure"), gameResources.treasure.value, { gameResources.treasure.value++ }, { gameResources.treasure.value-- }, Modifier.weight(1f))
                }
            }
            for ((key, value) in gameResources.resourceMap) {
                if (key in listOf("prowess", "storm", "treasure")) {
                    continue
                }
                val anyChecked = checkedStates.filterKeys { it in key }.values.any { it }
                val filteredItems = items.filter { it.resourceType == key }
                for (item in filteredItems) {
                    if (checkedStates.filterKeys { it in item.code }.values.any { it }) {
                        if (item.type == "creaturegenerator") {
//                            Column(
//                                modifier = Modifier
//                                    .background(Color(0xFFF8F5E7)) // Set your desired background color
//                                    .padding(8.dp) // Optional: add padding
//                            ) {
//                                Row(
//                                    horizontalArrangement = Arrangement.spacedBy(4.dp),  // Adjust spacing between elements
//                                    verticalAlignment = Alignment.CenterVertically,
//                                    modifier = Modifier.fillMaxWidth()
//                                ) {
                                    // CounterColumn for haste variant
//                                    Image(
//                                        painter = painterResource(id = item.imageId),
//                                        contentDescription = "Image in CounterColumn",
//                                        modifier = Modifier.size(42.dp)  // Adjust size as needed
//                                    )
                                    CounterColumn(mapOf("image" to item.imageId, "text" to "Haste"), gameResources.resourceMap[item.hasteResourceType]?.value ?: 0, { gameResources.resourceMap[item.hasteResourceType]?.let { it.value++ } }, { gameResources.resourceMap[item.hasteResourceType]?.let { it.value-- } }, Modifier.weight(1f))
                                    CounterColumn(mapOf("image" to item.imageId, "text" to "Non-Haste"), value.value, { value.value++ }, { value.value-- }, Modifier.weight(1f))

//                                    CounterColumn("Haste $key", gameResources.resourceMap[item.hasteResourceType]?.value ?: 0, { gameResources.resourceMap[item.hasteResourceType]?.let { it.value++ } }, { gameResources.resourceMap[item.hasteResourceType]?.let { it.value-- } }, Modifier.weight(1f))
//                                    CounterColumn("Non-Haste $key", value.value, { value.value++ }, { value.value-- }, Modifier.weight(1f))

//                                }
//                            }


                        } else {
                                CounterColumn(mapOf("image" to item.imageId, "text" to "$key"), value.value, { value.value++ }, { value.value-- }, Modifier.weight(1f))
                            }
                        }
                        break
                    }

                }
            }

            ResourceCounterList(gameResources, checkedStates)
        }


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CastButtonSection(gameResources: GameResources, checkedStates: MutableMap<String, Boolean>, items: List<Item>){

    // Bottom Section
    FlowRow(
        modifier = Modifier
            .padding(horizontal = 100.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.Bottom // Align content to bottom
    ) {
        CastColumn("Creature", {
            cast("any", gameResources, items, checkedStates)
            castGeneral("storm", gameResources)
        })

        CastColumn("Ins/Soc", {
            cast("noncreature", gameResources, items, checkedStates)
            cast("magecraft", gameResources, items, checkedStates)
            cast("instsor", gameResources, items, checkedStates)
            cast("any", gameResources, items, checkedStates)
            castGeneral("storm", gameResources)
            castGeneral("prowess", gameResources)
        })

        CastColumn("Copy Ins/Soc", {
            cast("magecraft", gameResources, items, checkedStates)
        })

        CastColumn("Non Creature", {
            cast("noncreature", gameResources, items, checkedStates)
            castGeneral("storm", gameResources)
            castGeneral("prowess", gameResources)
            cast("any", gameResources, items, checkedStates)
        })

        CastColumn("End Step", {
            endStep("storm", gameResources)
            endStep("prowess", gameResources)
            endStep("Birgi Red", gameResources)
            nonHaste2Haste(items, gameResources )
        })
    }
}
@Composable
fun ResourceCounterList(gameResources: GameResources, checkedStates: MutableMap<String, Boolean>) {
    var refreshTrigger by remember { mutableStateOf(false) }

    LaunchedEffect(checkedStates) {
        refreshTrigger = !refreshTrigger // Triggers recomposition
    }

    Column {
        gameResources.resourceMap.forEach { (key, value) ->
            val anyChecked = checkedStates.filterKeys { it in key }.values.any { it }
            if (anyChecked) {
                CounterColumn(
                    key, value.value,
                    { value.value++ }, { value.value-- },
                    Modifier.weight(1f)
                )
            }
        }
    }
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
            color = Color(0xFF8480D9),
            fontSize = 15.sp,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start)
        )

        Checkbox(
            checked = isChecked,      // Use the passed state
            onCheckedChange = onCheckedChange,  // Forward changes to parent
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF8480D9), // Color when checked
                uncheckedColor = Color(0xFF8480D9).copy(alpha = 0.5f), // Optional: Color when unchecked
                checkmarkColor = Color.White // Optional: Color of the checkmark
            )
        )
    }
}

@Composable
fun CounterColumn(
    label: Any,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .then(
                if (label is String) Modifier.padding(top = 15.dp) else Modifier
            ) // Add top padding if label is String
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFfdcdff)),
                contentPadding = PaddingValues(0.dp) // Adjust padding
            ) {
                Text(
                    text = "-",
                    fontSize = 22.sp,
                    color = Color(0xFF8480D9)
                )
            }
        }


        // Conditionally Render Label or Image
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            when (label) {
                is String -> {
                    // If label is a string, display it as Text
                    Text(text = label, fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))
                }
                is Map<*, *> -> {
                    // If label is a map (dict), expect keys "image" and "text"
                    val imageRes = label["image"] as? Int
                    val text = label["text"] as? String

                    imageRes?.let {
                        Image(
                            painter = painterResource(id = it),
                            contentDescription = "Image in CounterColumn",
                            modifier = Modifier.size(42.dp)  // Adjust size as needed
                        )
                    }

                    text?.let {
                        // Display text below image
                        Text(text = it, fontSize = 16.sp, color = Color.Black)
                    }
                }}
            Text(text = count.toString(), fontSize = 16.sp, color = Color.Black)
        }

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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC9F7FF)),
            modifier = Modifier
                .size(30.dp) // Visible size remains 30.dp
                .align(Alignment.Center), // Center within the larger hitbox
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Button",
                tint = Color(0xFF8480D9),
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
fun CastColumn(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF8480D9), // Default background color
    contentColor: Color = Color.White,  // Default text/icon color
    cornerRadius: Dp = 3.dp,            // Default corner radius
    padding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp), // Default padding
    textStyle: TextStyle = TextStyle(fontSize = 13.sp), // Default text style

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