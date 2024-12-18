package com.bignerdranch.android.realtime

import HiddenFeed
import UserFeed
import android.content.pm.PackageManager
import android.Manifest;
import android.annotation.SuppressLint
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
import com.bignerdranch.android.realtime.LoginScreen as LoginScreen
import android.os.Build
import android.app.AlarmManager
import android.app.NotificationChannel
import android.content.Context
import android.content.Intent
import java.util.Calendar
import android.app.NotificationManager
import android.app.PendingIntent
import android.provider.Settings
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlin.random.Random


object PassUser{
    var username = ""
}

class MainActivity : ComponentActivity() {
    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // setCameraPreview()
            } else {
                // Camera permission denied
            }

        }

    @SuppressLint("ScheduleExactAlarm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestNotificationPermission()
        checkAndRequestExactAlarmPermission()
        createNotificationChannel()

        FeedBoolean.resetBooleanIfNewDay(this) // boolean value that should be used for feed
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, Random.nextInt(7, 19)) // random time from 7 am to 7pm
            set(Calendar.MINUTE, Random.nextInt(0, 59))  // Generates a random integer between 1 and 99
            set(Calendar.SECOND, 0)
        }

        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

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

        lifecycleScope.launch {
            //postRepository.deleteAll()
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel Name"
            val descriptionText = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("channel_id", name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(
                    this, "Please grant exact alarm permission in Settings", Toast.LENGTH_LONG
                ).show()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit){
    var username by remember {mutableStateOf("")}
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

        Button(onClick = {
            if(authenticate(username, context)){
                onLoginSuccess()
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Incorrect username and/or password", Toast.LENGTH_SHORT).show()
            }
        }, contentPadding = PaddingValues(start = 60.dp, end = 60.dp, top = 8.dp, bottom = 8.dp),
            modifier = Modifier.padding(top = 18.dp)
        ) {
            Text(text = "Login", fontSize = 22.sp)
        }
    }
}

private fun authenticate(username: String, context: Context): Boolean {
    val appDatabase = AppDatabase.getDatabase(context)
    val usersDao = appDatabase.usersDao()
    if(usersDao.getUserDataDetails(username) == null){
        val newUser = Users(
            username = username,
            password = ""
        )
        usersDao.insertUser(newUser)
        Log.d("user created", usersDao.getUserDataDetails(username).toString())
        PassUser.username = username
        Log.d("current user", PassUser.username)
        return true
    }else{
        Log.d("user found", usersDao.getUserDataDetails(username).toString())
        PassUser.username = username
        return true
    }
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

        composable("feed"){
            UserFeed(navController)
        }

        composable("hiddenFeed"){
            HiddenFeed(navController)
        }

        composable("camera") {
            CameraPreviewScreen(navController)
        }
    }
}







