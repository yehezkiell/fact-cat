package jp.speakbuddy.edisonandroidexercise.fact.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.speakbuddy.edisonandroidexercise.design_system.components.ErrorView
import jp.speakbuddy.edisonandroidexercise.design_system.components.ImageView
import jp.speakbuddy.edisonandroidexercise.design_system.components.LoadingView
import jp.speakbuddy.edisonandroidexercise.design_system.components.rememberSnackbarHostState
import jp.speakbuddy.edisonandroidexercise.design_system.theme.EdisonAndroidExerciseTheme
import jp.speakbuddy.edisonandroidexercise.fact.R
import jp.speakbuddy.edisonandroidexercise.fact.model.FactCatDetail
import jp.speakbuddy.edisonandroidexercise.fact.model.ToastState
import kotlinx.coroutines.launch

@Composable
fun FactScreen(
    viewModel: FactViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = rememberSnackbarHostState()
) {
    val data = viewModel.uiState.collectAsStateWithLifecycle()
    val detail = data.value.detail
    val isLoading = data.value.isLoading
    val toasterState = data.value.toasterState

    SnackBarError(
        toasterState,
        snackbarHostState
    )

    //for configuration change
    val scrollState = rememberSaveable(saver = ScrollState.Saver) { ScrollState(0) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        when (detail) {
            is FactCatDetail.Success -> {
                val uiModel = detail.uiModel

                FactScreenContent(
                    fact = uiModel.fact,
                    length = uiModel.length,
                    containsCatsString = uiModel.containsCats,
                    isLoading = isLoading,
                    onUpdateFactClick = { viewModel.updateFact() }
                )
            }

            is FactCatDetail.Fail -> {
                ErrorView(
                    message = detail.errorMessage,
                    onRetry = { viewModel.updateFact() },
                    isLoading = isLoading,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is FactCatDetail.InitialLoading -> {
                LoadingView()
            }
        }
    }
}

@Composable
private fun SnackBarError(
    toastState: ToastState?,
    snackbarHostState: SnackbarHostState
) {
    if (toastState == null) return
    val scope = rememberCoroutineScope()
    //time key will launch effect to ignore same error message
    LaunchedEffect(toastState.toasterTimeMilis) {
        val errorMessage = toastState.toasterMessage
        if (errorMessage.isNotEmpty()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}

@Composable
private fun FactScreenContent(
    fact: String,
    length: Int,
    containsCatsString: Boolean,
    isLoading: Boolean,
    onUpdateFactClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MultiCatsText(containsCatsString)
            CatCircleImage()
            Text(
                text = fact,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onSurface
            )

            FactLength(length)

            UpdateButton(isLoading, onUpdateFactClick)
        }
    }
}

@Composable
private fun FactLength(length: Int) {
    if (length > 100) {
        Text(
            text = "Fact length: $length",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MultiCatsText(containsCatsString: Boolean) {
    if (containsCatsString) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(R.string.multi_cat_description),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun CatCircleImage() {
    ImageView(
        imageUrl = "https://images.unsplash.com/photo-1596854407944-bf87f6fdd49e?q=80&w=1760&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        contentDescription = "Cat image",
        modifier = Modifier
            .size(180.dp)
            .clip(CircleShape)
            .border(
                width = 4.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            )
    )
}

@Composable
private fun UpdateButton(isLoading: Boolean, onUpdateFactClick: () -> Unit) {
    Button(
        onClick = onUpdateFactClick,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "Update fact",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FactScreenPreview() {
    EdisonAndroidExerciseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val fact =
                "Cats have an extra organ that allows them to taste scents on the air, which is why they appear to be sniffing and staring at the same time."
            FactScreenContent(
                fact = fact,
                length = fact.length,
                isLoading = false,
                containsCatsString = true,
                onUpdateFactClick = {}
            )
        }
    }
}
