package tv.accedo.composedemo2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.future.future
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import tv.accedo.composedemo2.flow.Flows
import tv.accedo.composedemo2.flow.zip
import tv.accedo.composedemo2.ui.HomeScreen
import tv.accedo.composedemo2.ui.LoginScreen
import tv.accedo.composedemo2.ui.UserScreen
import tv.accedo.composedemo2.ui.dialog
import tv.accedo.composedemo2.ui.theme.ComposeDemo2Theme

class MainActivity : ComponentActivity() {
    private val model by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            ComposeDemo2Theme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val showDialog = remember { model.showDialog }
                    val ipState = model.ipState
                    val scope = rememberCoroutineScope()
                    val navController = rememberNavController()
                    val usernameState = remember { model.usernameState }
                    val passwordState = remember { model.passwordState }

                    val dialog = key(showDialog.value) {
                        dialog(
                            shouldShow = showDialog,
                            onDismissRequest = {
                                showDialog.value = false
                            }
                        ) {
                            Text("Update IP address?")
                        }
                    }

                    DisposableEffect(Unit) {

                        Flows
                            .zip(
                                model.ipFlow,
                                dialog,
                            ) { ip, isConfirmed ->

                                ipState.value = if (isConfirmed == true) ip else "unknown"

                                showDialog.value = false
                            }
                            .launchIn(scope)

                        onDispose {
                            scope.cancel()
                            model.stop()
                        }
                    }

                    val future = runBlocking {
                        future {
                            delay(100)
                            "192.168.1.1"
                        }
                    }

                    future.join()

                    key(ipState.value) {
                        NavHost(
                            navController = navController,
                            startDestination = HomeScreen,
                            modifier = Modifier.padding(32.dp),
                        ) {

                            HomeScreen.register(
                                ip = "IP: ${ipState.value}",
                                navGraphBuilder = this,
                                showDialog = showDialog,
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                            ) {

                                navController.navigate(LoginScreen)
                            }

                            LoginScreen.register(
                                navGraphBuilder = this,
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize(),
                                onLogin = {

                                navController.navigate(UserScreen(usernameState.value, passwordState.value))
                            },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )

                            UserScreen.register(this)
                        }
                    }
                }
            }
        }
    }
}

