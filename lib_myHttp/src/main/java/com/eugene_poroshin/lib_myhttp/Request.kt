package com.eugene_poroshin.lib_myhttp

import java.net.URL

class Request(
    var url: URL? = null,
    val method: String = "GET",
    val headers: String = "",
    val body: String? = null
) {

}