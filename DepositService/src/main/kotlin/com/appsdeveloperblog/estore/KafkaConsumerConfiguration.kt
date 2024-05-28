package com.appsdeveloperblog.estore

import com.appsdeveloperblog.payments.ws.error.NotRetryableException
import com.appsdeveloperblog.payments.ws.error.RetryableException
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.util.backoff.FixedBackOff
import java.util.*

@Configuration
class KafkaConsumerConfiguration {
    @Autowired
    var environment: Environment? = null

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Any> {
        val config: MutableMap<String, Any?> = HashMap()

        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] =
            environment!!.getProperty("spring.kafka.consumer.bootstrap-servers")
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] =
            ErrorHandlingDeserializer::class.java
        config[ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS] = JsonDeserializer::class.java
        config[ConsumerConfig.GROUP_ID_CONFIG] = environment!!.getProperty("spring.kafka.consumer.group-id")
        config[JsonDeserializer.TRUSTED_PACKAGES] =
            environment!!.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages")

        config[ConsumerConfig.ISOLATION_LEVEL_CONFIG] =
            environment!!.getProperty("spring.kafka.consumer.isolation-level", "READ_COMMITTED")
                .lowercase()

        return DefaultKafkaConsumerFactory(config)
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String?, Any?>, kafkaTemplate: KafkaTemplate<String?, Any?>?
    ): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = consumerFactory

        val errorHandler = DefaultErrorHandler(
            DeadLetterPublishingRecoverer(
                kafkaTemplate!!
            ),
            FixedBackOff(5000, 3)
        )
        errorHandler.addNotRetryableExceptions(NotRetryableException::class.java)
        errorHandler.addRetryableExceptions(RetryableException::class.java)
        factory.setCommonErrorHandler(errorHandler)

        return factory
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, Any>?): KafkaTemplate<String, Any> {
        return KafkaTemplate(producerFactory!!)
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        val config: MutableMap<String, Any?> = HashMap()
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] =
            environment!!.getProperty("spring.kafka.consumer.bootstrap-servers")
        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        return DefaultKafkaProducerFactory(config)
    }
}