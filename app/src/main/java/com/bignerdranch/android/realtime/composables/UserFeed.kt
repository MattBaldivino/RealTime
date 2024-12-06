import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavHostController
import database.AppDatabase
import java.util.Date
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bignerdranch.android.realtime.Posts


@Composable
fun UserFeed (navController: NavHostController, ) {
    val context = LocalContext.current
    val postsLists = getUserInfo(context)
    for (i in postsLists.indices) {
        println(postsLists[i])
    }
    println(postsLists.joinToString(", "))

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            for (i in 0 until postsLists.size) {
                postsLists[i].owner?.let { Text(text = it) }
                val currentImage = postsLists[i].image?.let { convertImageByteArrayToBitmap(it) }
                currentImage?.let { BitmapImage(it) }
            }
            Button(
                onClick = {
                    navController.navigate("home") // Navigate to the Camera Screen
                },
                modifier = Modifier.padding(bottom = 40.dp) // Add padding to position below the first button
            ) {
                Text(text = "Home")
            }
        }
    }
}

fun getUserInfo(context: Context): List<Posts> {
    val appDatabase = AppDatabase.getDatabase(context)
    val currentDate = Date().date
    val postsDao = appDatabase.postsDao()
    val postsList = postsDao.getPosts(currentDate)
    return postsList
}

@Composable
fun BitmapImage(bitmap: Bitmap) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Post Image",
    )
}

fun convertImageByteArrayToBitmap(imageData: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
}