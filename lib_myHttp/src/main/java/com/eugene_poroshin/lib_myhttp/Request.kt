package com.eugene_poroshin.lib_myhttp

import com.sun.net.httpserver.Headers
import java.net.URL

class Request(
    val url: URL? = null,
    val method: String = "GET",
    val headers: Headers,
    val body: RequestBody? = null
) {

}