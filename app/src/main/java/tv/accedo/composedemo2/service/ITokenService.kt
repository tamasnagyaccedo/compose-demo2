package tv.accedo.composedemo2.service

import kotlinx.coroutines.flow.Flow
import tv.accedo.composedemo2.service.impl.Token

interface ITokenService {
    suspend fun getToken(
        username: String,
        password: String,
        timestamp: Long,
    ): Flow<Token>

    suspend fun getToken(
        openIdToken: String,
        tokenScope: TokenScope,
        timestamp: Long,
    ): Flow<Token>

}