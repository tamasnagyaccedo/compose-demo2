package tv.accedo.composedemo2.service.impl

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tv.accedo.composedemo2.service.IBackendService

class BackendService(
    private val ktorClient: HttpClient,
    private val context: Context
) : IBackendService {
    override fun getIP(): Flow<String> = flow {
        ktorClient.get("https://api.ipify.org")
            .body<String>()
            .also {
                emit(it)
            }
    }
}