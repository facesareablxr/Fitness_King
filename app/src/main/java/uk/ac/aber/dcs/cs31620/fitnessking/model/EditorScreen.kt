package uk.ac.aber.dcs.cs31620.fitnessking.model

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.R
import java.io.File

class EditorScreen(application: Application): AndroidViewModel(application) {
    private var textFile: File = File(application.filesDir, "exercise.txt")
    var editorText by mutableStateOf(
        TextFieldValue(
            text = "",
            selection = TextRange(0)
        )
    )

    fun onStart(){
        viewModelScope.launch(Dispatchers.IO){
            var result = ""
            if(textFile.exists() && editorText.text.isEmpty()){
                result = textFile.readText()
            }
            if (editorText.text.isEmpty() && result.isNotEmpty()){
                editorText = TextFieldValue(
                    text = result,
                    selection = TextRange(result.length)
                )
            }
        }
    }

    fun onPause(){
        viewModelScope.launch(Dispatchers.IO) {
            textFile.writeText(editorText.toString())
        }
    }


}