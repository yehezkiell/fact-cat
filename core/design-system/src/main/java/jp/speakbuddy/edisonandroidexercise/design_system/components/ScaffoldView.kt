package jp.speakbuddy.edisonandroidexercise.design_system.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.speakbuddy.edisonandroidexercise.design_system.theme.EdisonAndroidExerciseTheme

@Composable
fun ScaffoldView(
    content: @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = rememberSnackbarHostState(),
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        content = content
    )
}

@Composable
fun rememberSnackbarHostState() = remember {
    SnackbarHostState()
}

@Preview
@Composable
private fun ScaffoldViewPreview() {
    EdisonAndroidExerciseTheme {
        ScaffoldView(
            topBar = {
                HeaderView("Title")
            },
            content = {
                Text("Scaffold View", modifier = Modifier.padding(it))
            }
        )
    }
}