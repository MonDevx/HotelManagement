package com.gpch.hotel.service

import com.gpch.hotel.model.ConfirmationToken
import com.gpch.hotel.model.User
import com.gpch.hotel.repository.ConfirmationTokenRepository
import com.gpch.hotel.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("userService")
@Transactional
class UserService @Autowired constructor(
    @Qualifier("userRepository") private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val confirmationTokenRepository: ConfirmationTokenRepository
) {

    fun findUserByEmail(email: String): User? = userRepository.findByEmail(email)

    fun deleteUserById(id: String) = userRepository.deleteById(id)

    fun findUserById(id: String): User? = userRepository.findById(id).orElse(null)

    fun findAll(): List<User> = userRepository.findAll()

    fun saveUser(user: User) {
        user.password = bCryptPasswordEncoder.encode(user.password)
        user.active = 1
        userRepository.save(user)
    }

    fun updateUser(user: User) {
        val userFromDb = userRepository.findById(user.id!!).orElse(null)!!
        userFromDb.name = user.name
        userFromDb.lastName = user.lastName
        userFromDb.roles = user.roles
        userRepository.save(userFromDb)
    }

    fun updateAccount(user: User) {
        val userFromDb = userRepository.findById(user.id!!).orElse(null)!!
        userFromDb.name = user.name
        userFromDb.lastName = user.lastName
        userRepository.save(userFromDb)
    }

    fun changePassword(id: String, newPassword: String) {
        val userFromDb = userRepository.findById(id).orElse(null)!!
        userFromDb.password = bCryptPasswordEncoder.encode(newPassword)
        userRepository.save(userFromDb)
    }

    fun forgetPassword(email: String, newPassword: String) {
        val userFromDb = userRepository.findByEmail(email)!!
        userFromDb.password = bCryptPasswordEncoder.encode(newPassword)
        userRepository.save(userFromDb)
    }

    fun deleteToken(confirmationToken: String) {
        val token = confirmationTokenRepository.findByConfirmationToken(confirmationToken)
        if (token != null) confirmationTokenRepository.delete(token)
    }

    fun findConfirmationToken(confirmationToken: String): ConfirmationToken? =
        confirmationTokenRepository.findByConfirmationToken(confirmationToken)

    fun createToken(existingUser: User): String {
        val confirmationToken = ConfirmationToken(existingUser)
        confirmationTokenRepository.save(confirmationToken)
        return confirmationToken.confirmationToken!!
    }
}
