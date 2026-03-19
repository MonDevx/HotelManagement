package com.gpch.hotel.repository

import com.gpch.hotel.model.ConfirmationToken
import org.springframework.data.repository.CrudRepository

interface ConfirmationTokenRepository : CrudRepository<ConfirmationToken, String> {

    fun findByConfirmationToken(confirmationToken: String): ConfirmationToken?
}
