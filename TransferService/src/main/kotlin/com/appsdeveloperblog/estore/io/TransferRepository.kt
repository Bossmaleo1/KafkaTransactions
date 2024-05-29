package com.appsdeveloperblog.estore.io

import org.springframework.data.jpa.repository.JpaRepository

interface TransferRepository: JpaRepository<TransferEntity, String> {

}