package com.appsdeveloperblog.estore.handler

import com.appsdeveloperblog.payments.ws.events.DepositRequestedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
@KafkaListener(
    topics = ["deposit-money-topic"],
    containerFactory = "kafkaListenerContainerFactory"
)
class DepositRequestedEventHandler {

    private val LOGGER: Logger = LoggerFactory.getLogger(this.javaClass)

    @KafkaHandler
    fun handle(@Payload depositRequestedEvent: DepositRequestedEvent) {
        LOGGER.info("Received a new deposit event: {} ", depositRequestedEvent.amount)
    }
}