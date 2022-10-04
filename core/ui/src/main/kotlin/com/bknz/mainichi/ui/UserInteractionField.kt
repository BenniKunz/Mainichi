package com.bknz.mainichi.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun UserInteractionField(
    value: String,
    onNewValue: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyBoardOptions : KeyboardOptions = KeyboardOptions()
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        label = { Text(text = placeholder) },
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = placeholder) },
        leadingIcon = { },
        visualTransformation = visualTransformation,
        keyboardOptions = keyBoardOptions
    )
}