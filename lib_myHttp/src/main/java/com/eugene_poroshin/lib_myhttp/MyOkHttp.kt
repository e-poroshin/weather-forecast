package com.eugene_poroshin.lib_myhttp

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection

class MyOkHttp() : Call.Factory {

    lateinit var connection: HttpURLConnection
    lateinit var thread: Thread
    lateinit var exception: Exception

    override fun newCall(request: Request): Call {
        val url = request.url
        connection = url?.openConnection() as HttpURLConnection

        var builder: StringBuilder? = null
        var reader: BufferedReader? = null
        try {
            connection.requestMethod = request.method
            connection.connect()
            val input = InputStreamReader(connection.inputStream)
            reader = BufferedReader(input)
            builder = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                builder.append(line + "\n")
                line = reader.readLine()
            }
            return builder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
            reader?.close()
        }
        return builder.toString()
    }
}
