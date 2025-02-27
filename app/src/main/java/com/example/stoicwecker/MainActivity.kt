package com.example.stoicwecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stoicwecker.data.QuoteApiImpl
import com.example.stoicwecker.ui.MainScreen
import com.example.stoicwecker.ui.theme.StoicWeckerTheme
import com.example.stoicwecker.viewmodel.AlarmViewModel
import com.example.stoicwecker.viewmodel.AlarmViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val quoteApi = QuoteApiImpl()
        val factory = AlarmViewModelFactory(quoteApi)
        
        setContent {
            StoicWeckerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: AlarmViewModel = viewModel(factory = factory)
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StoicWeckerTheme {
        Greeting("Android")
    }
}