package com.eugene_poroshin.lib_myhttp

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection

class MyHttpClient() {

    lateinit var connection: HttpURLConnection
    lateinit var thread: Thread
    lateinit var exception: Exception

    fun newCall(request: Request): Response? {
        val response = Response()
        val url = request.url
        connection = url?.openConnection() as HttpURLConnection

        val builder: StringBuilder
        var reader: BufferedReader? = null
        try {
            connection.requestMethod = request.method
            connection.readTimeout = 10000
            connection.connect()
            val input = InputStreamReader(connection.inputStream)
            reader = BufferedReader(input)
            builder = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                builder.append(line + "\n")
                line = reader.readLine()
            }
            val body: String = builder.toString()
            response.body = body
//            val codeStart: String? = body.substringAfterLast("cod\":")
//            val codeResult: Int? = codeStart?.subSequence(0, 3) as Int
//            response.code = codeResult
            response.status = Status.SUCCESS
            return response
        } catch (exception: IOException) {
            exception.printStackTrace()
            response.status = Status.ERROR
        } finally {
            connection.disconnect()
            reader?.close()
        }
        return response
    }
}
