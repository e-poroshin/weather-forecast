package com.eugene_poroshin.myhttp

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.util.*

class MyHttpClient() {

    lateinit var connection: HttpURLConnection
    private val timer = Timer("Timer")

    fun newCall(request: Request): Response? {
        val response = Response()
        val url = request.url
        HttpURLConnection.setFollowRedirects(false)
        connection = url?.openConnection() as HttpURLConnection
        connection.requestMethod = request.method
        connection.connectTimeout = 1000
        connection.readTimeout = 3000

        val builder: StringBuilder
        var reader: BufferedReader? = null
        try {
            connection.connect()
            runTimer()
            when (connection.responseCode) {
                in 200..299 -> {
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
                    response.code = connection.responseCode
                    response.status = Status.SUCCESS
                }
                HttpURLConnection.HTTP_UNAVAILABLE -> throw MyException.NoConnectionException("No internet connection")
            }
            timer.cancel()
            return response
        } catch (exception: MyException) {
            exception.message
            response.status = Status.ERROR
        } finally {
            connection.disconnect()
            reader?.close()
        }
        return response
    }

    private fun runTimer() {
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                throw MyException.TimeoutException("Timeout")
            }
        }
        val delay = 10000L
        timer.schedule(task, delay)
    }
}
