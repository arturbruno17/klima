package com.posart.klima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.posart.klima.ui.theme.KlimaTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KlimaTheme {
                SettingsScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SettingsScreen() {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopAppBar()
            }
        ) { padding ->
            Row(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = dimensionResource(R.dimen.normal))
                    .padding(top = dimensionResource(R.dimen.normal))
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    text = stringResource(R.string.unit_system),
                    style = MaterialTheme.typography.titleMedium
                )

                val options =
                    listOf(stringResource(R.string.imperial), stringResource(R.string.metric))
                var expanded by remember { mutableStateOf(false) }
                var selectedOptionText by remember { mutableStateOf(options[1]) }
                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(48.dp),
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                ) {
                    TextField(
                        readOnly = true,
                        value = selectedOptionText,
                        onValueChange = {},
                        trailingIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    painter = if (expanded) painterResource(R.drawable.ic_keyboard_arrow_up) else painterResource(
                                        R.drawable.ic_keyboard_arrow_down
                                    ),
                                    contentDescription = ""
                                )
                            }
                        },
                        shape = RoundedCornerShape(
                            topStart = dimensionResource(R.dimen.normal),
                            topEnd = dimensionResource(R.dimen.normal)
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            textColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    ExposedDropdownMenu(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                            ),
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = selectionOption,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                },
                                onClick = {
                                    selectedOptionText = selectionOption
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun TopAppBar() {
        SmallTopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            title = {
                Text(
                    text = stringResource(R.string.settings)
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    finish()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_to_previous_screen)
                    )
                }
            }
        )
    }

    @Preview
    @Composable
    fun TopAppBarPreview() {
        KlimaTheme(dynamicColor = false) {
            TopAppBar()
        }
    }

    @Preview
    @Composable
    fun SettingsScreenPreview() {
        KlimaTheme(dynamicColor = false) {
            SettingsScreen()
        }
    }
}