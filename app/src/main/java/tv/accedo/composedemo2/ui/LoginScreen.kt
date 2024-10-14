package tv.accedo.composedemo2.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tv.accedo.composedemo2.MainViewModel
import tv.accedo.composedemo2.ui.theme.ComposeDemo2Theme

@Serializable
data object LoginScreen : Screen, KoinComponent {

    private val model: MainViewModel by inject()

    fun register(
        navGraphBuilder: NavGraphBuilder,
        modifier: Modifier = Modifier,
        onLogin: () -> Unit,
        onBackClick: () -> Unit
    ) {
        navGraphBuilder.composable<LoginScreen> {
            val username = remember { model.usernameState }
            val password = remember { model.passwordState }

            Extracted(modifier, username, password, onLogin, onBackClick)
        }
    }
}



@Composable
@Preview(showBackground = true)
fun Extracted(
    modifier: Modifier = Modifier,
    username: MutableState<String> = mutableStateOf(""),
    password: MutableState<String> = mutableStateOf(""),
    onLogin: () -> Unit = {},
    onBackClick: () -> Unit = {}
) = ComposeDemo2Theme(darkTheme = true) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Login")

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = username.value,
            onValueChange = {

                username.value = it
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password.value,
            onValueChange = {

                password.value = it
            },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onLogin()
            }
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onBackClick()
            }
        ) {
            Text("Cancel")
        }
    }
}