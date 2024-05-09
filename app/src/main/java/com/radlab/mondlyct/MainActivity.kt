package com.radlab.mondlyct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.radlab.mondlyct.models.Item
import com.radlab.mondlyct.repo.CodeTaskRepository
import com.radlab.mondlyct.room.AppDatabase
import com.radlab.mondlyct.ui.theme.MondlyCTTheme
import com.radlab.mondlyct.viemodels.CodeTaskIntent
import com.radlab.mondlyct.viemodels.CodeTaskState
import com.radlab.mondlyct.viemodels.CodeTaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel =
            CodeTaskViewModel(codeTaskRepository = CodeTaskRepository(AppDatabase.getInstance(this)))
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
            ItemView(item = item)
        }
    }
}

@Composable
fun ItemView(item: Item) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = item.attributes.imageInfo.imageUrl,
            contentDescription = item.attributes.description,
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            error = painterResource(id = R.drawable.ic_launcher_foreground),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = item.attributes.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = item.attributes.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}



