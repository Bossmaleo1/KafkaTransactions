package com.appsdeveloperblog.estore

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaConfig {

    @Value("withdraw-money-topic")
    private val withdrawTopicName: String? = null

    @Value("deposit-money-topic")
    private val depositTopicName: String? = null

    @Value("\${spring.kafka.producer.bootstrap-servers}")
    private val bootstrapServers: String? = null

    @Value("\${spring.kafka.producer.key-serializer}")
    private val keySerializer: String? = null

    @Value("\${spring.kafka.producer.value-serializer}")
    private val valueSerializer: String? = null

    @Value("\${spring.kafka.producer.acks}")
    private val acks: String? = null

    @Value("\${spring.kafka.producer.properties.delivery.timeout.ms}")
    private val deliveryTimeout: String? = null

    @Value("\${spring.kafka.producer.properties.linger.ms}")
    private val linger: String? = null

    @Value("\${spring.kafka.producer.properties.request.timeout.ms}")
    private val requestTimeout: String? = null

    @Value("\${spring.kafka.producer.properties.enable.idempotence}")
    private val idempotence = false

    @Value("\${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
    private val inflightRequests = 0

    fun producerConfigs(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers!!
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = keySerializer!!
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = valueSerializer!!
        props[ProducerConfig.ACKS_CONFIG] = acks!!
        props[ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG] = deliveryTimeout!!
        props[ProducerConfig.LINGER_MS_CONFIG] = linger!!
        props[ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG] = requestTimeout!!

        props[ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG] = idempotence
        props[ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION] =
            inflightRequests

        return props
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(producerFactory())
    }

    @Bean
    fun createWithdrawTopic(): NewTopic {
        return TopicBuilder.name(withdrawTopicName!!).partitions(3).replicas(3).build()
    }

    @Bean
    fun createDepositTopic(): NewTopic {
        return TopicBuilder.name(depositTopicName!!).partitions(3).replicas(3).build()
    }

}