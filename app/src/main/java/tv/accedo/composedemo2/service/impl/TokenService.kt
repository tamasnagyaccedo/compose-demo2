package tv.accedo.composedemo2.service.impl

import androidx.compose.runtime.mutableStateOf
import io.ktor.util.encodeBase64
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tv.accedo.composedemo2.service.ITokenService
import tv.accedo.composedemo2.service.TokenScope
import java.security.MessageDigest

class TokenService : ITokenService {
    private val token = mutableStateOf("")

    override suspend fun getToken(
        username: String,
        password: String,
        timestamp: Long,
    ): Flow<Token> =
        flow {

            if (username == "TestUser1" && password == "") {

                MessageDigest.getInstance("SHA-256").apply {

                    update(username.encodeToByteArray())
                    update(password.encodeToByteArray())
                    update(timestamp.toString().encodeToByteArray())

                    val token = digest().encodeBase64()
                    this@TokenService.token.value = token
                    emit(OpenIdToken(token))
                }
            } else {

                throw Throwable("Invalid credentials")
            }
        }

    override suspend fun getToken(
        openIdToken: String,
        tokenScope: TokenScope,
        timestamp: Long
    ): Flow<Token> = flow {
        if (openIdToken == token.value) {
            MessageDigest.getInstance("SHA-256").apply {
                update(openIdToken.encodeToByteArray())
                update(tokenScope.toString().encodeToByteArray())
                update(timestamp.toString().encodeToByteArray())

                val token = digest().encodeBase64()
                when (tokenScope) {

                    TokenScope.Jwt -> emit(JwtToken(token))
                    TokenScope.OpenID -> Unit
                    TokenScope.Sam3 -> emit(Sam3Token(token))
                }
            }
        } else {
            throw Throwable("Invalid token")
        }
    }
}