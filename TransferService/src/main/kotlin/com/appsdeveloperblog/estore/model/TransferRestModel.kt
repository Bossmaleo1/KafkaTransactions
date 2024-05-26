package com.appsdeveloperblog.estore.model

import java.math.BigDecimal

data class TransferRestModel (
     var senderId: String?,
     var recepientId: String,
     var amount: BigDecimal?
)