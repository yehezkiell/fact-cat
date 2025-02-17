package jp.speakbuddy.edisonandroidexercise.fact.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.speakbuddy.edisonandroidexercise.design_system.components.ErrorView
import jp.speakbuddy.edisonandroidexercise.design_system.components.LoadingView
import jp.speakbuddy.edisonandroidexercise.design_system.theme.EdisonAndroidExerciseTheme
import jp.speakbuddy.edisonandroidexercise.fact.model.FactCatDetail

@Composable
fun FactScreen(
    viewModel: FactViewModel = hiltViewModel()
) {
    val data = viewModel.uiState.collectAsStateWithLifecycle()
    val detail = data.value.detail
    val isLoading = data.value.isLoading

    when (detail) {
        is FactCatDetail.Success -> {
            val uiModel = detail.uiModel

            FactScreenContent(
                fact = uiModel.fact,
                length = uiModel.length,
                containsCatsString = uiModel.containsCats,
                isLoading = isLoading,
                onUpdateFactClick = { viewModel.update() }
            )
        }

        is FactCatDetail.Fail -> {
            ErrorView(
                message = detail.errorMessage,
                onRetry = { viewModel.update() },
                isLoading = isLoading,
                modifier = Modifier.fillMaxSize()
            )
        }

        is FactCatDetail.InitialLoading -> {
            LoadingView()
        }
    }
}

@Composable
private fun FactScreenContent(
    fact: String,
    length: Int,
    containsCatsString: Boolean,
    isLoading: Boolean,
    onUpdateFactClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        Text(
            text = "Fact",
            style = MaterialTheme.typography.titleLarge
        )

        if (containsCatsString) {
            Text(
                text = "Multiple cats found!",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = fact,
            style = MaterialTheme.typography.bodyLarge
        )

        if (length > 100) {
            Text(
                text = length.toString(),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = onUpdateFactClick,
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(text = "Update fact")
            }
        }
    }
}

@Preview
@Composable
private fun FactScreenPreview() {
    EdisonAndroidExerciseTheme {
        val fact = "Preview fact text"
        FactScreenContent(
            fact = "Preview fact text",
            length = fact.length,
            isLoading = false,
            containsCatsString = true,
            onUpdateFactClick = {}
        )
    }
}
