package com.appsdeveloperblog.payments.ws.error

class NotRetryableException : RuntimeException {
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String?) : super(message)
}