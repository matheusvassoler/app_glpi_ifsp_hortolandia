package com.glpi.ifsp.hortolandia.data.resource

import java.lang.Exception

sealed class Resource<T> (
    val data: T? = null,
    val exception: Exception? = null
) {
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(exception: Exception, data: T? = null) : Resource<T>(data, exception)
}
