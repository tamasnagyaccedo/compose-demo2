package tv.accedo.composedemo2.service

import kotlinx.coroutines.flow.Flow

interface ISam3Service {
    fun doSomething(sam3Token: String): Flow<String>
}