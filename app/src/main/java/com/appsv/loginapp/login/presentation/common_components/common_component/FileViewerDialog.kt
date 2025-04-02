package com.appsv.loginapp.login.presentation.common_components.common_component

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.appsv.loginapp.R

@Composable
fun FileViewerDialog(
    fileUrl: String?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    fileUrl == null -> {
                        Text("No file available", color = MaterialTheme.colorScheme.error)
                    }

                    fileUrl.isImage() -> {
                        ImagePreview(fileUrl)
                    }

                    fileUrl.isPdf() -> {
                        PdfViewerPlaceholder(fileUrl, context)
                    }

                    else -> {
                        GenericFileViewer(fileUrl, context)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}

// File Type Checkers
fun String?.isImage(): Boolean {
    return this?.let {
        it.endsWith(".jpg", ignoreCase = true) ||
                it.endsWith(".jpeg", ignoreCase = true) ||
                it.endsWith(".png", ignoreCase = true) ||
                it.endsWith(".webp", ignoreCase = true)
    } ?: false
}

fun String?.isPdf(): Boolean {
    return this?.endsWith(".pdf", ignoreCase = true) ?: false
}

// Preview Components
@Composable
fun ImagePreview(imageUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = "Uploaded image",
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun PdfViewerPlaceholder(pdfUrl: String, context: Context) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                // Open PDF with external viewer
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(pdfUrl)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }
    ) {
        Icon(
            painter = painterResource(id= R.drawable.picture_as_pdf_icon),
            contentDescription = "PDF file",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        Text("PDF File", style = MaterialTheme.typography.titleMedium)
        Text("Tap to open", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun GenericFileViewer(fileUrl: String, context: Context) {
    val fileName = fileUrl.substringAfterLast('/')

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                // Open file with compatible app
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(fileUrl)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }
    ) {
        Icon(
            painter = painterResource(id= R.drawable.default_file_icon),
            contentDescription = "Document file",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Text(fileName, style = MaterialTheme.typography.titleMedium)
        Text("Tap to open", style = MaterialTheme.typography.bodySmall)
    }
}