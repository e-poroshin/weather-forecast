package com.eugene_poroshin.myhttp

open class MyException(message: String?) : Exception(message) {

    class NoConnectionException(message: String?) : MyException(message) {

        override val message: String?
            get() = super.message
    }

    class TimeoutException(message: String?) : MyException(message) {

        override val message: String?
            get() = super.message
    }
}
