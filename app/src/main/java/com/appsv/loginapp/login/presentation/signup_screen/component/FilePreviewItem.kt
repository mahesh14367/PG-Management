package com.appsv.loginapp.login.presentation.signup_screen.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.appsv.loginapp.R

@Composable
fun FilePreviewItem(uri: Uri) {
    val context = LocalContext.current
    val fileName = remember {
        try {
            getFileNameFromUri(context, uri)
        } catch (e: Exception) {
            "Unknown File"
        }
    }
    val mimeType = remember {
        try {
            context.contentResolver.getType(uri)
        } catch (e: Exception) {
            null
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                mimeType?.startsWith("image") == true -> { // If it's an image
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Uploaded Image",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                mimeType?.startsWith("video") == true -> { // If it's a video
                    Icon(
                        painter = painterResource(R.drawable.video_file_icon),
                        contentDescription = "Video File",
                        tint = Color.Red,
                        modifier = Modifier.size(50.dp)
                    )
                }
                mimeType == "application/pdf" -> { // If it's a PDF
                    Icon(
                        painter = painterResource(R.drawable.picture_as_pdf_icon),
                        contentDescription = "PDF File",
                        tint = Color.Blue,
                        modifier = Modifier.size(50.dp)
                    )
                }
                mimeType?.startsWith("audio") == true -> { // If it's an audio file
                    Icon(
                        painter = painterResource(R.drawable.audio_file_icon),
                        contentDescription = "Audio File",
                        tint = Color.Green,
                        modifier = Modifier.size(50.dp)
                    )
                }
                else -> { // Default icon for other file types
                    Icon(
                        painter = painterResource(R.drawable.default_file_icon),
                        contentDescription = "Document File",
                        tint =  colorResource(id=R.color.gray),
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = fileName, fontSize = 12.sp, color = colorResource(id=R.color.gray))
            }
        }
    }
}

