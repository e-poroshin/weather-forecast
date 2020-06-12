package com.eugene_poroshin.myhttp

import sun.rmi.runtime.Log


open class MyException(message: String?) : Exception(message)  {

    class NoConnectionException(message: String?) : MyException(message) {

        override val message: String?
            get() = "No internet connection: " + super.message
    }

    class TimeoutException(message: String?) : MyException(message) {

        override val message: String?
            get() = "Timeout: " + super.message
    }
}
