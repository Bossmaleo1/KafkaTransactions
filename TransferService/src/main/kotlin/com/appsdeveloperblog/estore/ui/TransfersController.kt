package com.appsdeveloperblog.estore.ui

import com.appsdeveloperblog.estore.model.TransferRestModel
import com.appsdeveloperblog.estore.service.TransferService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transfers")
class TransfersController(val transferService: TransferService) {
    private val LOGGER: Logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping
    fun transfer(@RequestBody transferRestModel: TransferRestModel?): Boolean {
        return transferService.transfer(transferRestModel)
    }
}