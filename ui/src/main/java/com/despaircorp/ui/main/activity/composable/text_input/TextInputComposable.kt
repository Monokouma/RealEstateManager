package com.despaircorp.ui.main.activity.composable.text_input

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue


@Composable
fun TextFieldWithIcons(
    onValueAgentNameTextChange: (String) -> Unit,
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    return OutlinedTextField(
        value = text,
        leadingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = "edit icon") },
        onValueChange = {
            text = it
            onValueAgentNameTextChange.invoke(it.text)
        },
        label = { Text(text = "Name", color = MaterialTheme.colorScheme.outline) },
        placeholder = { Text(text = "Enter your name", color = MaterialTheme.colorScheme.outline) },
        
        )
}