package com.eugene_poroshin.lib_myhttp

import java.io.IOException

interface Call {

    fun request(): Request

    @Throws(IOException::class)
    fun execute(): Response

    interface Factory {
        fun newCall(request: Request): Call
    }
}