package tv.accedo.composedemo2

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tv.accedo.composedemo2.service.IBackendService
import tv.accedo.composedemo2.service.ITokenService
import tv.accedo.composedemo2.service.TokenScope
import tv.accedo.composedemo2.service.impl.OpenIdToken
import kotlin.coroutines.EmptyCoroutineContext

class MainViewModel : ViewModel(), KoinComponent {
    private val backendService: IBackendService by inject()
    private val tokenService: ITokenService by inject()

    private val scope = CoroutineScope(EmptyCoroutineContext)

    val usernameState = mutableStateOf("TestUser1")
    val passwordState = mutableStateOf("")
    val tokenState = mutableStateOf<OpenIdToken?>(null)

    val ipState = mutableStateOf("Loading...").apply {

        scope.launch {

            ipFlow.collect {

                value = it
            }
        }
    }

    val ipFlow: Flow<String>
        get() = backendService.getIP()

    val showDialog = mutableStateOf(false)

    fun login(username: String, password: String, timestamp: Long) = runBlocking {

        tokenService.getToken(username, password, timestamp)
            .onEach {

                usernameState.value = username
                passwordState.value = password
                tokenState.value = it as? OpenIdToken
            }
            .filterNotNull()
            .take(1)
    }

    fun getToken(openIdToken: String, tokenScope: TokenScope, timestamp: Long) = runBlocking {

        tokenService.getToken(openIdToken, tokenScope, timestamp)
            .filterNotNull()
    }

    fun stop() {

        scope.cancel()
    }
}