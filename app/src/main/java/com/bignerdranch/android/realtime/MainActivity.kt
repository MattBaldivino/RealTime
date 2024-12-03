package com.bignerdranch.android.realtime

import android.content.pm.PackageManager
import android.Manifest;
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bignerdranch.android.realtime.composables.CameraPreviewScreen
import com.bignerdranch.android.realtime.ui.theme.RealTimeTheme
import database.AppDatabase
import kotlinx.coroutines.launch
import java.util.Date
import com.bignerdranch.android.realtime.LoginScreen as LoginScreen

class MainActivity : ComponentActivity() {

    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // setCameraPreview()
            } else {
                // Camera permission denied
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) -> {
                // setCameraPreview()
            }
            else -> {
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }

        setContent {
            RealTimeTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    val navController = rememberNavController()
                    NavGraph(navController)
                }
            }
        }

        //INSERTING DUMMY DATA TO ALLOW FOR FUTURE TESTING/IMPLEMENTATION
        //PLEASE DELETE WHEN FINISHED
        //PROPER IMPLEMENTATION WILL LIKELY HAVE TO BE DONE USING A VIEW MODEL AND/OR COROUTINES
        val database by lazy { AppDatabase.getDatabase(this) }
        val userRepository by lazy { UsersRepository(database.usersDao()) }
        val postRepository by lazy { PostsRepository(database.postsDao())}

        val user = Users(
            username = "JaneDoe",
            password = "1234"
        )

        val dummyDate = Date()
        val post = Posts(
            //dummyphoto doesn't exist, it's just a string placeholder
            photoFileName = "dummyphoto.png",
            date = dummyDate,
            owner = "JaneDoe"
        )

        lifecycleScope.launch {
            userRepository.insertUser(user)
            val returnedUser: Users = userRepository.getUser("JaneDoe")
            Log.d("user", returnedUser.toString())

            postRepository.insertPost(post)
            val returnedPosts = postRepository.getPosts(dummyDate)
            Log.d("post", returnedPosts.joinToString())

        }

    }

//    private fun setCameraPreview() {
//        setContent {
//            CameraPreviewScreen()
//        }
//    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit){
    var username by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    val context = LocalContext.current.applicationContext

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 26.dp, vertical = 140.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(text = "Welcome to RealTime")
        OutlinedTextField(value = username, onValueChange = {username = it},
            label = {Text(text = "Username")},
            shape = RoundedCornerShape(20.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Username")
            },
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(value = password, onValueChange = {password = it},
            label = {Text(text = "Password")},
            shape = RoundedCornerShape(20.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Password")
            },
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        Button(onClick = {
            if(authenticate(username, password)){
                onLoginSuccess()
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT)
            }else{
                Toast.makeText(context, "Incorrect username and/or password", Toast.LENGTH_SHORT)
            }
        }, contentPadding = PaddingValues(start = 60.dp, end = 60.dp, top = 8.dp, bottom = 8.dp),
            modifier = Modifier.padding(top = 18.dp)
        ) {
            Text(text = "Login", fontSize = 22.sp)
        }
    }
}

private fun authenticate(username: String, password: String): Boolean {
    val validUsername = "TestUser"
    val validPassword = ":3"
    return (username == validUsername) && (password == validPassword)
}


@Composable
fun NavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = "login"){
        composable("login"){
            LoginScreen(onLoginSuccess = {
                navController.navigate("home"){
                    popUpTo(0)
                }
            })
        }

        composable("home"){
            HomeScreen(navController)
        }

        composable("camera") {
            CameraPreviewScreen(navController)
        }
    }

}




