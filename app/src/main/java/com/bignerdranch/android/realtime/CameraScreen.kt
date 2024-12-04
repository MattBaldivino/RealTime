package com.bignerdranch.android.realtime


import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bignerdranch.android.realtime.composables.CameraPreviewScreen
import com.bignerdranch.android.realtime.ui.theme.RealTimeTheme
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.bignerdranch.android.realtime.composables.CameraPreviewScreen


@Composable
fun CameraScreen(navController: NavHostController) {

    Box(modifier = Modifier.fillMaxSize().background(Color.White)){
        Column(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(text = "Camera")

            Button(onClick = {
                navController.navigate("home") // Navigate to the Camera Screen
            }) {
                Text(text = "Home")
            }
        }
    }
}







