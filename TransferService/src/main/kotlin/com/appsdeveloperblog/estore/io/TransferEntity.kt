package com.appsdeveloperblog.estore.io

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable
import java.math.BigDecimal


@Entity
@Table(name = "transfers")
data class TransferEntity(
    @Id
    @Column(nullable = false)
    var transferId: String,
    @Column(nullable = false)
    var senderId: String,
    @Column(nullable = false)
    var recepientId: String,
    @Column(nullable = false)
    var amount: BigDecimal
): Serializable {
}
