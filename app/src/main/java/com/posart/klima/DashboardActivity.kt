package com.posart.klima

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.posart.klima.ui.theme.KlimaTheme

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KlimaTheme {
                App()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun App() {

        var fieldVisible by remember { mutableStateOf(false) }
        var fieldValue by remember { mutableStateOf("") }

        Scaffold(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface),
            topBar = {
                TopAppBar(
                    onNavigationItemClick = {
                        fieldVisible = !fieldVisible
                    },
                    title = fieldValue,
                    onShowTextField = {
                        if (fieldVisible) Icons.Filled.Close else Icons.Filled.Search
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
            ) {
                AnimatedVisibility(
                    visible = fieldVisible
                ) {
                    TextField(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.normal))
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(
                            topStart = dimensionResource(R.dimen.normal),
                            topEnd = dimensionResource(R.dimen.normal)
                        ),
                        value = fieldValue,
                        onValueChange = { text ->
                            fieldValue = text
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.place),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                    alpha = 0.61f
                                )
                            )
                        },
                        textStyle = MaterialTheme.typography.titleLarge,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,

                        ),
                        trailingIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = stringResource(R.string.place_description),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        },
                        singleLine = true,
                    )
                }
                ForecastToday(
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.large))
                        .padding(horizontal = dimensionResource(R.dimen.normal))
                        .fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.normal))
                        .padding(top = dimensionResource(id = R.dimen.normal))
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(3) {
                        ForecastDetails()
                    }
                }
                LazyRow(
                    contentPadding = PaddingValues(dimensionResource(id = R.dimen.normal)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.normal))
                ) {
                    items(10) {
                        ForecastHourly()
                    }
                }
                repeat(3) {
                    ForecastAdditionalDetails(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.normal))
                            .padding(bottom = dimensionResource(R.dimen.normal))
                    )
                }
            }
        }
    }

    @Composable
    private fun TopAppBar(
        onNavigationItemClick: () -> Unit,
        title: String,
        onShowTextField: () -> ImageVector
    ) {
        SmallTopAppBar(
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigationItemClick
                ) {
                    Icon(
                        imageVector = onShowTextField(),
                        contentDescription = stringResource(R.string.place_description),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    startActivity(
                        Intent(this@DashboardActivity, SettingsActivity::class.java)
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.settings_description),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = stringResource(R.string.calendar_description),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )
    }

    @Composable
    private fun ForecastToday(modifier: Modifier =  Modifier) {
        Row(
            modifier = modifier
                .clip(shape = RoundedCornerShape(size = dimensionResource(R.dimen.normal)))
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = dimensionResource(R.dimen.large)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .padding(end = dimensionResource(R.dimen.large))
                    .size(128.dp),
                painter = painterResource(id = R.drawable.day_rain),
                contentDescription = stringResource(R.string.day_rain),
            )
            Column {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "23",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "ºC",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text(
                    text = "Rain",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }

    @Composable
    fun ForecastDetails() {
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    RoundedCornerShape(dimensionResource(R.dimen.normal))
                )
                .padding(
                    dimensionResource(id = R.dimen.large),
                    dimensionResource(id = R.dimen.normal)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(48.dp),
                painter = painterResource(id = R.drawable.wind_speed),
                contentDescription = stringResource(R.string.wind_speed),
            )
            Text(
                text = "23m/s",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = "Wind",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.81f)
            )
        }
    }

    @Composable
    fun ForecastHourly() {
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.tertiaryContainer,
                    RoundedCornerShape(dimensionResource(R.dimen.normal))
                )
                .padding(
                    dimensionResource(id = R.dimen.normal),
                    dimensionResource(id = R.dimen.normal)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "14h",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.81f)
            )
            Text(
                text = "25ºC",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }

    @Composable
    fun ForecastAdditionalDetails(modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.normal))
                )
                .padding(
                    dimensionResource(id = R.dimen.normal), dimensionResource(R.dimen.normal)
                )
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "320º",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "Wind direction",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.81f)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "4.57",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "UV",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.81f)
                )
            }
        }
    }


    @Preview
    @Composable
    fun PreviewApp() {
        KlimaTheme(dynamicColor = false) {
            App()
        }
    }

    @Preview
    @Composable
    fun ForecastTodayPreview() {
        KlimaTheme(dynamicColor = false) {
            ForecastToday(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

    @Preview
    @Composable
    fun ForecastDetailsPreview() {
        KlimaTheme(dynamicColor = false) {
            ForecastDetails()
        }
    }

    @Preview
    @Composable
    fun ForecastHourlyPreview() {
        KlimaTheme(dynamicColor = false) {
            ForecastHourly()
        }
    }

    @Preview
    @Composable
    fun ForecastAdditionalDetailsPreview() {
        KlimaTheme(dynamicColor = false) {
            ForecastAdditionalDetails()
        }
    }
}

