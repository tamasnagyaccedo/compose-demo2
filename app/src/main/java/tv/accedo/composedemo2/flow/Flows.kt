package tv.accedo.composedemo2.flow

import androidx.lifecycle.AtomicReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext

interface Flows {
    companion object
}

inline fun <reified T1, reified T2, reified R> Flows.Companion.zip(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    crossinline transform: suspend (T1, T2) -> R,
): Flow<R> = channelFlow {

    val state1 = AtomicReference<T1?>(null)
    val state2 = AtomicReference<T2?>(null)

    val flow = MutableSharedFlow<Unit>()

    flow1
        .onEach {
            state1.set(it)
            flow.emit(Unit)
        }
        .launchIn(CoroutineScope(EmptyCoroutineContext))

    flow2
        .onEach {
            state2.set(it)
            flow.emit(Unit)
        }
        .launchIn(CoroutineScope(EmptyCoroutineContext))

    flow.onEach {
        state1.get()?.let { t1 ->
            state2.get()?.let { t2 ->
                send(transform(t1, t2))
            }
        }
    }
        .launchIn(CoroutineScope(EmptyCoroutineContext))

    awaitClose { channel.close() }
}

inline fun <reified T1, reified T2, reified T3, reified R> Flows.Companion.zip(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    crossinline transform: suspend (T1, T2, T3) -> R,
): Flow<R> = channelFlow {

    val state1 = AtomicReference<T1?>(null)
    val state2 = AtomicReference<T2?>(null)
    val state3 = AtomicReference<T3?>(null)

    val flow = MutableSharedFlow<Unit>()

    flow1
        .onEach {

            state1.set(it)
            flow.emit(Unit)
        }
        .launchIn(CoroutineScope(EmptyCoroutineContext))

    flow2
        .onEach {

            state2.set(it)
            flow.emit(Unit)
        }
        .launchIn(CoroutineScope(EmptyCoroutineContext))

    flow3
        .onEach {

            state3.set(it)
            flow.emit(Unit)
        }
        .launchIn(CoroutineScope(EmptyCoroutineContext))

    flow.onEach {

        state1.get()?.let { t1 ->
            state2.get()?.let { t2 ->
                state3.get()?.let { t3 ->

                    state1.set(null)
                    state2.set(null)
                    state3.set(null)

                    send(transform(t1, t2, t3))
                }
            }
        }
    }
        .launchIn(CoroutineScope(EmptyCoroutineContext))

    awaitClose { channel.close() }
}