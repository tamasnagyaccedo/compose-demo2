package tv.accedo.composedemo2.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tv.accedo.composedemo2.MainViewModel
import tv.accedo.composedemo2.flow.Flows
import tv.accedo.composedemo2.flow.zip
import tv.accedo.composedemo2.service.ISam3Service
import tv.accedo.composedemo2.service.TokenScope
import tv.accedo.composedemo2.service.impl.JwtToken
import tv.accedo.composedemo2.service.impl.OpenIdToken
import tv.accedo.composedemo2.service.impl.Sam3Token
import java.time.Clock
import kotlin.coroutines.EmptyCoroutineContext

@Serializable
data class UserScreen(
    val username: String,
    val password: String,
) : Screen, KoinComponent {

    companion object : KoinComponent {
        private val model by inject<MainViewModel>()
        private val sam3Service by inject<ISam3Service>()

        @OptIn(ExperimentalCoroutinesApi::class)
        fun register(
            navGraphBuilder: NavGraphBuilder,
            modifier: Modifier = Modifier
        ) {
            navGraphBuilder.composable<UserScreen> { backStackEntry ->
                val (username, password) = backStackEntry.toRoute<UserScreen>()
                val sam3 = remember { mutableStateOf(Sam3Token("")) }
                val jwt = remember { mutableStateOf(JwtToken("")) }
                val openId = remember { mutableStateOf(OpenIdToken("")) }
                val scope = rememberCoroutineScope()

                val timestamp = remember { Clock.systemUTC().millis() }
                DisposableEffect(Unit) {

                    model.login(
                        username,
                        password,
                        timestamp
                    )
                        .flatMapMerge { openIdToken ->

                            openId.value = openIdToken as OpenIdToken

                            fun Flow<Sam3Token>.doSomething() = flatMapMerge { sam3Token ->

                                Flows.zip(
                                    sam3Service.doSomething(sam3Token.token),
                                    sam3Service.doSomething(sam3Token.token)
                                ) { first, second ->
                                    first to second
                                }
                                    .catch {
                                        emit("error" to "error")
                                    }
                            }

                            Flows.zip(
                                model.getToken(openIdToken.token, TokenScope.Jwt, timestamp),
                                model.getToken(openIdToken.token, TokenScope.Sam3, timestamp),
                            ) { jwtToken, sam3Token ->

                                jwt.value = jwtToken as JwtToken
                                sam3.value = sam3Token as Sam3Token
                                sam3Token as Sam3Token
                            }
                                .doSomething()
                        }
                        .flowOn(EmptyCoroutineContext)
                        .onEach {

                        }
                        .flowOn(Dispatchers.Default)
                        .onEach {

                        }
                        .flowOn(Dispatchers.IO)
                        .onStart {

                        }
                        .onCompletion {

                        }
                        .catch {

                        }
                        .take(1)
                        .launchIn(scope)

                    onDispose {
                        scope.cancel()
                        model.stop()
                    }
                }


                Box(
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Welcome $username!",
                            fontSize = 32.sp,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "sam3: ${sam3.value}",
                            fontSize = 24.sp,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "jwt: ${jwt.value}",
                            fontSize = 24.sp,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "openid: ${openId.value}",
                            fontSize = 24.sp,
                        )
                    }
                }

            }
        }
    }
}
