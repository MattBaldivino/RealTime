package com.bignerdranch.android.realtime

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.bignerdranch.android.realtime.ui.theme.Purple40
import com.bignerdranch.android.realtime.FeedBoolean


@Composable
fun HomeScreen(navController: NavHostController){
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize().background(Color.White)){
        Column(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
            {
            Text(text = "Home")

                Button(
                    onClick = {
                    navController.navigate("camera") // Navigate to the Camera
                }) {
                    Text(text = "Open Camera")
                    Modifier.background(color = Purple40)
                }
                Button(onClick = {
                    if(FeedBoolean.getBoolean(context)){
                        navController.navigate("feed") // Navigate to the Feed
                    }
                    else{
                        navController.navigate("hiddenFeed") // Navigate to the Hidden Feed
                    }
                }) {
                    Text(text = "Open Feed")
                    Modifier.background(color = Purple40)
                }
        }
    }
}