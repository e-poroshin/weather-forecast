package com.eugene_poroshin.myhttp

import com.sun.net.httpserver.Headers

class Response() {

    var body: String? = null
    var code: Int = 0
    lateinit var status: Status
    val headers: Headers? = null
}