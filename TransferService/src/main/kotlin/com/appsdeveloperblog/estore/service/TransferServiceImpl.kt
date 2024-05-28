package com.appsdeveloperblog.estore.service

import com.appsdeveloperblog.estore.error.TransferServiceException
import com.appsdeveloperblog.estore.model.TransferRestModel
import com.appsdeveloperblog.payments.ws.events.DepositRequestedEvent
import com.appsdeveloperblog.payments.ws.events.WithdrawalRequestedEvent
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.net.ConnectException

@Service
class TransferServiceImpl(
    private var kafkaTemplate: KafkaTemplate<String?, Any?>? = null,
    private var environment: Environment? = null,
    private var restTemplate: RestTemplate? = null
) : TransferService {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(TransferServiceImpl::class.java)
    }

    @Throws(Exception::class)
    private fun callRemoteServce(): ResponseEntity<String> {
        val requestUrl = "http://localhost:8082/response/200"
        val response = restTemplate!!.exchange(
            requestUrl, HttpMethod.GET, null,
            String::class.java
        )

        if (response.statusCode.value() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
            throw Exception("Destination Microservice not availble")
        }

        if (response.statusCode.value() == HttpStatus.OK.value()) {
            LOGGER.info(String.format("Received response from mock service: %s", response.body))
        }
        return response
    }

    @Transactional
    override fun transfer(transferRestModel: TransferRestModel?): Boolean {
        val withdrawalEvent = WithdrawalRequestedEvent(
            transferRestModel!!.senderId,
            transferRestModel.recepientId,
            transferRestModel.amount
        )
        val depositEvent = DepositRequestedEvent(
            transferRestModel.senderId,
            transferRestModel.recepientId,
            transferRestModel.amount
        )

        try {
            kafkaTemplate!!.send(environment!!.getProperty("withdraw-money-topic", "withdraw-money-topic"), withdrawalEvent)
            LOGGER.info(String.format("Sent event to withdrawal topic."))

            // Business logic that causes and error
            callRemoteServce()

            kafkaTemplate!!.send(environment!!.getProperty("deposit-money-topic", "deposit-money-topic"), depositEvent)
            LOGGER.info(String.format("Sent event to deposit topic"))
        } catch (ex: Exception) {
            LOGGER.error(ex.message, ex)
            throw TransferServiceException(ex)
        }

        return true
    }
}