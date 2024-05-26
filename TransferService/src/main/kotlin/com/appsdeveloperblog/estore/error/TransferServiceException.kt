package com.appsdeveloperblog.estore.error

class TransferServiceException: RuntimeException {
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String?) : super(message)
}