package jp.speakbuddy.edisonandroidexercise.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import coil.compose.AsyncImagePainter.State.Error
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageView(
    imageUrl: String,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier,
    loadingView: @Composable () -> Unit = {}
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val imageLoader = rememberAsyncImagePainter(
        model = imageUrl,
        onState = { state ->
            isLoading = state is Loading
            isError = state is Error
        },
    )
    val isLocalInspection = LocalInspectionMode.current
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading && !isLocalInspection) {
            // Display a progress bar while loading
            loadingView.invoke()
        }
        Image(
            painter = imageLoader,
            contentDescription = contentDescription,
            contentScale = contentScale
        )
    }
}