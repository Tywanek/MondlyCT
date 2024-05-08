package com.radlab.mondlyct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.radlab.mondlyct.models.Item
import com.radlab.mondlyct.repo.CodeTaskRepository
import com.radlab.mondlyct.ui.theme.MondlyCTTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel = CodeTaskViewModel(codeTaskRepository = CodeTaskRepository())
        super.onCreate(savedInstanceState)

        setContent {
            MondlyCTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val codeTaskState = viewModel.state.collectAsState()
                    viewModel.processIntent(CodeTaskIntent.LoadData)
                    CodeTaskView(codeTaskState.value)
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
    MondlyCTTheme {
        Greeting("Android")
    }
}

@Composable
fun CodeTaskView(codeTaskState: CodeTaskState) {
    when (codeTaskState) {
        is CodeTaskState.Loading -> {
            Text(text = codeTaskState.message)
        }
        is CodeTaskState.Success -> {
            ItemList(items = codeTaskState.data)
        }
        is CodeTaskState.Error -> {
            Text(text = codeTaskState.errorMessage)
        }
    }
}

@Composable
fun ItemList(items: List<Item>) {
    LazyColumn {
        items(items = items) { item ->
            Item(item = item)
        }
    }
}

@Composable
fun Item(item: Item) {
    Text(text = item.toString())
}