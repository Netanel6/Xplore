package com.netanel.xplore

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.localDatabase.user.viewModel.UserViewModel
import com.netanel.xplore.navigation.NavigationStack
import com.netanel.xplore.ui.theme.GradientEnd
import com.netanel.xplore.ui.theme.GradientMid
import com.netanel.xplore.ui.theme.GradientStart
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
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GradientStart, GradientMid, GradientEnd)
                    )
                )
        ) {
            NavigationStack(userViewModel = userViewModel)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.question_mark),
                tint = Color.White,
                contentDescription = "Menu Icon"
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

