package tv.accedo.composedemo2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dialog(
    modifier: Modifier = Modifier,
    shouldShow: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
): Flow<Boolean?> {
    val flow = MutableSharedFlow<Boolean>()
    if (shouldShow.value) {
        BasicAlertDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier
                .fillMaxWidth(),
            properties = properties,
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = AlertDialogDefaults.shape
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    content()
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        Button(
                            onClick = {
                                CoroutineScope(EmptyCoroutineContext).launch {

                                    flow.emit(true)
                                }
                            }
                        ) {
                            Text("Confirm")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                CoroutineScope(EmptyCoroutineContext).launch {

                                    flow.emit(false)
                                }
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }

    return flow
}