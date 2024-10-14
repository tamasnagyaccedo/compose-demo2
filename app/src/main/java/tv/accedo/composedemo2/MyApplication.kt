package tv.accedo.composedemo2

import android.app.Application
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import tv.accedo.composedemo2.service.IBackendService
import tv.accedo.composedemo2.service.ISam3Service
import tv.accedo.composedemo2.service.ITokenService
import tv.accedo.composedemo2.service.impl.BackendService
import tv.accedo.composedemo2.service.impl.Sam3Service
import tv.accedo.composedemo2.service.impl.TokenService

class MyApplication : Application() {

    private val module = module {
        factory { MainViewModel() }

        single {
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                        }
                    )
                }
            }
        }

        single<IBackendService> { BackendService(get(), this@MyApplication) }
        single<ITokenService> { TokenService() }
        single<ISam3Service> { Sam3Service() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(module)
        }
    }
}