package com.gpch.hotel.repository

import com.gpch.hotel.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("userRepository")
interface UserRepository : JpaRepository<User, String> {

    fun findByEmail(email: String): User?
}
