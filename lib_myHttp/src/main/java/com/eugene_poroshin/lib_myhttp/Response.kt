package com.eugene_poroshin.lib_myhttp

import com.sun.net.httpserver.Headers

class Response(
    val body: ResponseBody,
    val code: Int,
    val status: String,
    val headers: Headers) {
}