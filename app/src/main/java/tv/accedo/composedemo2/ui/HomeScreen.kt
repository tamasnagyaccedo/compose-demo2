package tv.accedo.composedemo2.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object HomeScreen : Screen {

    fun register(
        ip: String,
        navGraphBuilder: NavGraphBuilder,
        showDialog: MutableState<Boolean>,
        modifier: Modifier = Modifier,
        onLoginClick: () -> Unit
    ) {
        navGraphBuilder.composable<HomeScreen> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = ip,
                    fontSize = 24.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {

                        showDialog.value = true
                    }
                ) {

                    Text("Update IP address")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {

                        onLoginClick()
                    }
                ) {
                    Text("Login")
                }
            }
        }
    }
}