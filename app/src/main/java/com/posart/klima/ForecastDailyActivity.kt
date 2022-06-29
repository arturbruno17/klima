package com.posart.klima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.posart.klima.ui.theme.KlimaTheme

class ForecastDailyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KlimaTheme {
                ForecastDailyScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ForecastDailyScreen() {
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
                items(7) {
                    ForecastDaily()
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
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_to_previous_screen)
                    )
                }
            }
        )
    }

    @Composable
    fun ForecastDaily() {
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
                    .width(48.dp),
                painter = painterResource(id = R.drawable.day_rain),
                contentDescription = ""
            )
            Text(
                text = "28ºC",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Terça-feira",
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
            ForecastDaily()
        }
    }

    @Preview
    @Composable
    fun ForecastDailyScreenPreview() {
        KlimaTheme(dynamicColor = false) {
            ForecastDailyScreen()
        }
    }
}