import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.sp
import com.bignerdranch.android.realtime.Posts


@Composable
fun UserFeed (navController: NavHostController) {
    LazyCol(navController)
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

@Composable
fun UserPost(owner: String){
    Text(text = owner, fontSize = 10.sp)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyCol(navController: NavHostController){
    val context = LocalContext.current
    val postsLists = getUserInfo(context)

    LazyColumn(content = {
        stickyHeader {
            Button(
                onClick = {navController.navigate("home") },
                modifier = Modifier.padding(40.dp)
            ) {
                Text(text = "Home")
            }
        }
        itemsIndexed(postsLists, itemContent = { index, item ->
            item.owner?.let { UserPost(it) }
            val currentImage = item.image?.let { convertImageByteArrayToBitmap(it) }
            currentImage?.let { BitmapImage(it) }
        })
    })
}