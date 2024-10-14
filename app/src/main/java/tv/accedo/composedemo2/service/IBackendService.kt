package tv.accedo.composedemo2.service

import kotlinx.coroutines.flow.Flow

interface IBackendService {
    fun getIP(): Flow<String>

}