package com.posart.klima.ui.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.posart.klima.R
import com.posart.klima.UNIT_SYSTEM
import com.posart.klima.UnitSystem
import com.posart.klima.dataStore
import com.posart.klima.ui.theme.KlimaTheme
import com.posart.klima.ui.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var unitSystem: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                dataStore.data.map { preferences ->
                    preferences[UNIT_SYSTEM]
                }.collect {
                    it?.let {
                        unitSystem = it
                        return@collect
                    }
                    dataStore.edit { settings ->
                        settings[UNIT_SYSTEM] = UnitSystem.METRIC.value
                    }
                }
            }
        }

        setContent {
            KlimaTheme {
                App()
            }
        }

        viewModel.latLng.observe(this) {
            viewModel.getWeatherForecast(it.lat, it.lon, "minutely,alerts", unitSystem)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun App() {

        val weatherForecast by viewModel.response.observeAsState()

        var fieldVisible by remember { mutableStateOf(false) }
        var fieldValue by rememberSaveable { mutableStateOf("") }

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
                            IconButton(onClick = {
                                viewModel.getLatLngFromLocation(fieldValue)
                            }) {
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
                weatherForecast?.let {
                    if (it is WeatherViewModel.WeatherForecastResponse.Success) {

                        ForecastToday(
                            modifier = Modifier
                                .padding(top = dimensionResource(R.dimen.large))
                                .padding(horizontal = dimensionResource(R.dimen.normal))
                                .fillMaxWidth(),
                            it.weatherForecast.currentForecast.temperature.toString(),
                            it.weatherForecast.temperatureUnit,
                            it.weatherForecast.currentForecast.weatherImage[0].title,
                            it.weatherForecast.currentForecast.weatherImage[0].icon
                        )

                        Row(
                            modifier = Modifier
                                .padding(horizontal = dimensionResource(R.dimen.normal))
                                .padding(top = dimensionResource(id = R.dimen.normal))
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            ForecastDetails(
                                R.drawable.wind_speed,
                                R.string.wind_speed,
                                it.weatherForecast.currentForecast.windSpeed.toString() + it.weatherForecast.speedUnit,
                            )

                            ForecastDetails(
                                R.drawable.humidity,
                                R.string.humidity,
                                it.weatherForecast.currentForecast.humidity
                            )

                            ForecastDetails(
                                it.weatherForecast.currentForecast.weatherImage[0].icon,
                                R.string.feels_like,
                                it.weatherForecast.currentForecast.feelsLike.toString() + it.weatherForecast.temperatureUnit,
                            )

                        }

                        LazyRow(
                            contentPadding = PaddingValues(dimensionResource(id = R.dimen.normal)),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.normal))
                        ) {
                            items(it.weatherForecast.hourlyForecast) { item ->
                                ForecastHourly(
                                    item.temperature.toString() + it.weatherForecast.temperatureUnit,
                                    item.dateTime
                                )
                            }
                        }

                        ForecastAdditionalDetails(
                            modifier = Modifier
                                .padding(horizontal = dimensionResource(R.dimen.normal))
                                .padding(bottom = dimensionResource(R.dimen.normal)),
                            it.weatherForecast.currentForecast.windDeg,
                            R.string.wind_direction,
                            it.weatherForecast.currentForecast.uvi.toString(),
                            R.string.uvi
                        )

                        ForecastAdditionalDetails(
                            modifier = Modifier
                                .padding(horizontal = dimensionResource(R.dimen.normal))
                                .padding(bottom = dimensionResource(R.dimen.normal)),
                            it.weatherForecast.currentForecast.sunrise,
                            R.string.sunrise,
                            it.weatherForecast.currentForecast.sunset,
                            R.string.sunset
                        )

                        ForecastAdditionalDetails(
                            modifier = Modifier
                                .padding(horizontal = dimensionResource(R.dimen.normal))
                                .padding(bottom = dimensionResource(R.dimen.normal)),
                            it.weatherForecast.currentForecast.pressure,
                            R.string.pressure,
                            it.weatherForecast.currentForecast.clouds,
                            R.string.clouds
                        )

                    }

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

                val dailyForecast by viewModel.response.observeAsState()
                val dailyForecastIconVisible =
                    dailyForecast is WeatherViewModel.WeatherForecastResponse.Success
                AnimatedVisibility(visible = dailyForecastIconVisible) {
                    IconButton(onClick = {
                        val intent = Intent(
                            this@DashboardActivity,
                            ForecastDailyActivity::class.java
                        ).apply {
                            putExtra(
                                "dailyForecast",
                                (dailyForecast as WeatherViewModel.WeatherForecastResponse.Success)
                                    .weatherForecast.dailyForecast.toTypedArray()
                            )
                            putExtra(
                                "temperatureUnit",
                                (dailyForecast as WeatherViewModel.WeatherForecastResponse.Success)
                                    .weatherForecast.temperatureUnit
                            )
                        }
                        startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = stringResource(R.string.calendar_description),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

            }
        )
    }

    @Composable
    private fun ForecastToday(
        modifier: Modifier = Modifier,
        value: String,
        temperatureUnit: String,
        @StringRes description: Int,
        @DrawableRes icon: Int
    ) {
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
                painter = painterResource(icon),
                contentDescription = stringResource(R.string.rain),
            )
            Column {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = temperatureUnit,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text(
                    text = stringResource(description),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }

    @Composable
    fun ForecastDetails(
        @DrawableRes imageId: Int,
        @StringRes description: Int,
        value: String,
    ) {
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
                painter = painterResource(id = imageId),
                contentDescription = stringResource(description),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = stringResource(id = description),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.81f)
            )
        }
    }

    @Composable
    fun ForecastHourly(
        temperature: String,
        time: String
    ) {
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
                text = temperature,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.81f)
            )
            Text(
                text = time,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }

    @Composable
    fun ForecastAdditionalDetails(
        modifier: Modifier = Modifier,
        firstValue: String,
        @StringRes firstDescription: Int,
        secondValue: String,
        @StringRes secondDescription: Int
    ) {
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
                    text = firstValue,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = stringResource(firstDescription),
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
                    text = secondValue,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = stringResource(secondDescription),
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
                    .fillMaxWidth(),
                "23",
                "ºC",
                R.string.rain,
                R.drawable.day_rain
            )
        }
    }

    @Preview
    @Composable
    fun ForecastDetailsPreview() {
        KlimaTheme(dynamicColor = false) {
            ForecastDetails(
                R.drawable.wind_speed,
                R.string.wind_speed,
                "23m/s"
            )
        }
    }

    @Preview
    @Composable
    fun ForecastHourlyPreview() {
        KlimaTheme(dynamicColor = false) {
            ForecastHourly(
                "25ºC",
                "15h"
            )
        }
    }

    @Preview
    @Composable
    fun ForecastAdditionalDetailsPreview() {
        KlimaTheme(dynamicColor = false) {
            ForecastAdditionalDetails(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.normal))
                    .padding(bottom = dimensionResource(R.dimen.normal)),
                "5:32",
                R.string.sunrise,
                "17:53",
                R.string.sunset
            )
        }
    }
}

