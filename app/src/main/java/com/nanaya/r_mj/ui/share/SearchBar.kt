package com.nanaya.r_mj.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nanaya.r_mj.ui.theme.White94

@Composable
fun SearchBar(
    modifier: Modifier=Modifier,
    label: @Composable (() -> Unit)? = null,
    searchQuery: TextFieldValue,
    onSearchQueryChanged: (TextFieldValue) -> Unit,
    onSearch:(String)->Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = searchQuery,
        label = label,
        onValueChange = onSearchQueryChanged,
        modifier = modifier
            .fillMaxWidth()
            ,
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (searchQuery.text.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChanged(TextFieldValue("")) }) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            keyboardController?.hide()
            onSearch(searchQuery.text)
        })
    )
}


@Preview
@Composable
private fun SearchBarPreview() {
    SearchBar(searchQuery = TextFieldValue(), onSearchQueryChanged ={} ) {

    }
}