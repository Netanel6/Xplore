package com.netanel.xplore

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.localDatabase.user.viewModel.UserViewModel
import com.netanel.xplore.navigation.NavigationStack
import com.netanel.xplore.ui.MainTopAppBar
import com.netanel.xplore.ui.theme.XploreTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configuration = Configuration(resources.configuration)
        configuration.setLayoutDirection(Locale(getString(R.string.he_lang)))
        resources.updateConfiguration(configuration, resources.displayMetrics)

        enableEdgeToEdge()
        setContent {
            XploreTheme {
                MainScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(userViewModel: UserViewModel = hiltViewModel()) {
    Scaffold(
        topBar = { MainTopAppBar() },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavigationStack(userViewModel = userViewModel)
        }
    }
}

