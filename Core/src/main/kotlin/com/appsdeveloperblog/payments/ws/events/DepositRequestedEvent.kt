package com.appsdeveloperblog.payments.ws.events

import java.math.BigDecimal

class DepositRequestedEvent {
     var senderId: String? = null
     var recepientId: String? = null
     var amount: BigDecimal? = null

    constructor() {
    }

    constructor(senderId: String?, recepientId: String?, amount: BigDecimal?) {
        this.senderId = senderId
        this.recepientId = recepientId
        this.amount = amount
    }
}