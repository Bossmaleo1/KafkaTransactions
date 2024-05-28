package com.appsdeveloperblog.estore.WithdrawalService

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.client.RestTemplate
import org.springframework.context.annotation.Bean

@SpringBootApplication
class WithdrawalServiceApplication

fun main(args: Array<String>) {
	runApplication<WithdrawalServiceApplication>(*args)
}
