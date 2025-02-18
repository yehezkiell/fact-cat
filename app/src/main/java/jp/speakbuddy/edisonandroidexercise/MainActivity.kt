package jp.speakbuddy.edisonandroidexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import jp.speakbuddy.edisonandroidexercise.design_system.components.HeaderView
import jp.speakbuddy.edisonandroidexercise.design_system.components.ScaffoldView
import jp.speakbuddy.edisonandroidexercise.design_system.components.rememberSnackbarHostState
import jp.speakbuddy.edisonandroidexercise.design_system.theme.EdisonAndroidExerciseTheme
import jp.speakbuddy.edisonandroidexercise.fact.ui.FactScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()
            EdisonAndroidExerciseTheme {
                val snackbarHostState = rememberSnackbarHostState()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScaffoldView(
                        topBar = {
                            HeaderView(title = "Fact Cat", showNavigation = false)
                        },
                        snackbarHostState = snackbarHostState,
                        content = {
                            FactScreen(
                                modifier = Modifier.padding(it),
                                snackbarHostState = snackbarHostState
                            )
                        }
                    )
                }
            }
        }
    }
}