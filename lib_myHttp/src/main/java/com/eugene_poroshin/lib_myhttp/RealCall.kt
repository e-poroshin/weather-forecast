package com.eugene_poroshin.lib_myhttp

class RealCall(
    val client: MyOkHttp,
    val originalRequest: Request
) : Call {

    override fun request(): Request = originalRequest

    override fun execute(): Response {


    }
}