package uk.ac.aber.dcs.cs31620.fitnessking.ui.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

/**
 * A composable version of OutlinedTextField with added functionality to dismiss the keyboard on outside click.
 *
 * @param value is the current text value of the text field.
 * @param onValueChange is the callback for when the value changes.
 * @param modifier is the modifier for styling and layout.
 * @param label is the composable content for the text field label.
 * @param keyboardOptions is the options for configuring the keyboard.
 * @param keyboardActions is the actions for configuring keyboard behavior.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OutlinedTextFieldWithKeyboardDismiss(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
            .clickable { keyboardController?.hide() }, // Close keyboard on outside click
    )
}