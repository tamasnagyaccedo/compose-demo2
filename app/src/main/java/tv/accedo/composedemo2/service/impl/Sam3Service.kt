package tv.accedo.composedemo2.service.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tv.accedo.composedemo2.service.ISam3Service

class Sam3Service : ISam3Service {
    override fun doSomething(sam3Token: String): Flow<String> = flow {
        emit("Hello World!")

    }
}