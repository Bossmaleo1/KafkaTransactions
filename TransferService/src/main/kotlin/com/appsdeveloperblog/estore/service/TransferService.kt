package com.appsdeveloperblog.estore.service

import com.appsdeveloperblog.estore.model.TransferRestModel
import kotlin.jvm.Throws

interface TransferService {
    @Throws(Exception::class)
    fun transfer(transferRestModel: TransferRestModel?): Boolean
}