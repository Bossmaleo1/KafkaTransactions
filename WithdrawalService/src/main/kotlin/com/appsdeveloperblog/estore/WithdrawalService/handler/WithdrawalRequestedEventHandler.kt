package com.appsdeveloperblog.estore.WithdrawalService.handler

import com.appsdeveloperblog.payments.ws.events.WithdrawalRequestedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
@KafkaListener(topics = ["withdraw-money-topic"], containerFactory = "kafkaListenerContainerFactory")
class WithdrawalRequestedEventHandler {
    //private val LOGGER: Logger = LoggerFactory.getLogger(this.javaClass)

    @KafkaHandler
    fun handle(@Payload withdrawalRequestedEvent: WithdrawalRequestedEvent) {
       // LOGGER.info("Received a new withdrawal event: {} ", withdrawalRequestedEvent.amount)
    }
}