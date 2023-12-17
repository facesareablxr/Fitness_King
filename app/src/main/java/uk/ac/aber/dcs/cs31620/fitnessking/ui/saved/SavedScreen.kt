package uk.ac.aber.dcs.cs31620.fitnessking.ui.saved

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.job
import uk.ac.aber.dcs.cs31620.fitnessking.model.EditorLifecycleHandler
import uk.ac.aber.dcs.cs31620.fitnessking.model.EditorViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold

@Composable
fun SavedScreen(
    editorViewModel: EditorViewModel = viewModel(),
            navController: NavHostController
){
    TopLevelScaffold(
        navController = navController
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            EditorLifecycleHandler{
                if(it == Lifecycle.Event.ON_START){
                    editorViewModel.onStart()
                } else if(it == Lifecycle.Event.ON_PAUSE){
                    editorViewModel.onPause()
                }
            }
            TextEditor(text = editorViewModel.editorText,onValueChange = {
                editorViewModel.editorText = it
            }
            )
        }
    }
}

@Composable
fun TextEditor(text: TextFieldValue, onValueChange: (TextFieldValue) -> Unit = {}) {
    val focusRequester = remember {
        FocusRequester()
    }
    TextField(value = text,onValueChange = onValueChange,
        modifier = Modifier.fillMaxSize().focusRequester(focusRequester)
    )
    LaunchedEffect(Unit) {
        this.coroutineContext.job.invokeOnCompletion {
            focusRequester.requestFocus()
        }
    }
}


