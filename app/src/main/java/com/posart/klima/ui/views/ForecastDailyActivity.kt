package com.posart.klima.ui.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.posart.klima.R
import com.posart.klima.ui.entities.DailyForecast
import com.posart.klima.ui.entities.Temperatures
import com.posart.klima.ui.entities.WeatherImage
import com.posart.klima.ui.theme.KlimaTheme

class ForecastDailyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val temperatureUnit = intent.getStringExtra("temperatureUnit") ?: "ºC"
        val dailyForecast = intent.getParcelableArrayExtra("dailyForecast")?.asList()?.map {
            it as DailyForecast
        }
        dailyForecast?.let {
            setContent {
                KlimaTheme {
                    ForecastDailyScreen(it, temperatureUnit)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ForecastDailyScreen(
        dailyForecast: List<DailyForecast>,
        temperatureUnit: String
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = { TopAppBar() }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding),
                contentPadding = PaddingValues(
                    start = dimensionResource(id = R.dimen.normal),
                    top = dimensionResource(id = R.dimen.normal),
                    end = dimensionResource(id = R.dimen.normal)
                ),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.normal))
            ) {
                items(dailyForecast) {
                    ForecastDaily(
                        image = it.weatherImage[0].icon,
                        minTemperatureValue = it.temperature.minTemperature.toString() + temperatureUnit,
                        maxTemperatureValue = it.temperature.maxTemperature.toString() + temperatureUnit,
                        date = it.dateTime
                    )
                }
            }
        }
    }

    @Composable
    private fun TopAppBar() {
        SmallTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.calendar)
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

    @Composable
    fun ForecastDaily(
        @DrawableRes image: Int,
        minTemperatureValue: String,
        maxTemperatureValue: String,
        date: String
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(
                        dimensionResource(R.dimen.normal)
                    )
                )
                .padding(
                    horizontal = dimensionResource(id = R.dimen.normal),
                    vertical = dimensionResource(id = R.dimen.small)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(end = dimensionResource(id = R.dimen.small))
                    .size(48.dp),
                painter = painterResource(image),
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .padding(end = dimensionResource(R.dimen.small)),
                text = maxTemperatureValue,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = minTemperatureValue,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.61f),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = date,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End
            )
        }
    }

    @Preview
    @Composable
    fun ForecastDailyPreview() {
        KlimaTheme(dynamicColor = false) {
            ForecastDaily(
                image = R.drawable.day_rain,
                minTemperatureValue = "21ºC",
                maxTemperatureValue = "28ºC",
                date = "21/06"
            )
        }
    }

    @Preview
    @Composable
    fun ForecastDailyScreenPreview() {
        KlimaTheme(dynamicColor = false) {
            val list = listOf(
                DailyForecast(
                    dateTime = "21/06",
                    temperature = Temperatures(
                        minTemperature = 21,
                        maxTemperature = 28
                    ),
                    weatherImage = listOf(
                        WeatherImage(
                            id = 200,
                            icon = R.drawable.day_rain,
                            title = R.string.rain
                        )
                    )
                )
            )

            ForecastDailyScreen(list, "ºC")
        }
    }
}