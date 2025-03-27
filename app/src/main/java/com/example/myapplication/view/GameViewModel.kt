package com.example.myapplication.view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.models.GameResources
import com.example.myapplication.models.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {
    var gameResources = mutableStateOf(GameResources())
}