package com.example.openshock

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.openshock.data.ControlData
import com.example.openshock.data.ControlType
import com.example.openshock.ui.theme.OpenShockTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenShockTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValue ->
                    val coroutineScope = rememberCoroutineScope()
                    var intensity by remember {
                        mutableIntStateOf(0)
                    }
                    var duration by remember {
                        mutableIntStateOf(300)
                    }

                    Column(modifier = Modifier.padding(paddingValue)) {
                        Selector(intensity) { newIntensity ->
                            intensity = newIntensity
                        }

                        Selector(duration, 100) { newDuration ->
                            duration = newDuration
                        }

                        Button(onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                // QAkxYqNzgez51m9l4TVPAjGza7w7wkHImWAlrn9NecFJyTzo6q7yWzKfpkWxUzi7
                                val client = HttpClient(CIO) {
                                    install(ContentNegotiation) {
                                        json()
                                    }
                                }
                                val response = client.post("https://api.openshock.app/1/shockers/control") {
                                    contentType(ContentType.Application.Json)
                                    headers {
                                        append("OpenShockToken", "QAkxYqNzgez51m9l4TVPAjGza7w7wkHImWAlrn9NecFJyTzo6q7yWzKfpkWxUzi7")
                                    }
                                    setBody(arrayOf(ControlData("d735a04a-7b49-4e99-88ad-186a63f880cd", duration, intensity, ControlType.Shock)))
                                }

                                Log.i("PISS", arrayOf(ControlData("d735a04a-7b49-4e99-88ad-186a63f880cd", duration, intensity, ControlType.Shock)).toString())

                                print(response.request.content.toString())
                                Log.i("MOTHERFUCKER", response.request.content.toString())

                                client.close()
                            }
                        }) {
                            Text(text = "fuck")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Selector(number: Int, jump: Int = 1, changeNumber: (Int) -> Unit) {
    OpenShockTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            Button(onClick = {
                if (number == 0) changeNumber(99)
                else changeNumber(number - jump)
            }) {
                Text(text = "-")
            }
            Text(
                text = number.toString(),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Button(onClick = {
                if (number == 99) changeNumber(0)
                else changeNumber(number + jump)
            }) {
                Text(text = "+")
            }
        }
    }
}