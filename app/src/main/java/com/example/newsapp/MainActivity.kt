package com.example.newsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.model.Article
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.uimodels.NewsViewModel
import com.example.newsapp.uimodels.NewsViewModelProviderFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val newsRepository = NewsRepository(ArticleDatabase(this))
            val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
            viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)
            NewsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val news = remember {
                        mutableStateOf("")
                    }
                    var newsList = remember {
                        mutableListOf<Article>()
                    }
                    lifecycleScope.launch {
                        news.value = RetrofitInstance.api.getBreakingNews().body()!!.articles[1].content
                        Log.e("NNN",""+news.value)
                    }
                    Column() {
                        Greeting(news.value)
                        //Greeting(news.value)
                    }

                    /*BreakingNewsScreen(news[1].content){
                        Log.e("NNN","Breaking"+news.size)
                    }*/

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "$name!")
}

@Composable
fun BreakingNewsScreen(list : String,selectedItem: (String) -> Unit) {
    LazyColumn {
        items(20) {
            Text(
                text = "list $list",
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        selectedItem("list item $it")
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NewsAppTheme {
        Greeting("Android")
    }
}