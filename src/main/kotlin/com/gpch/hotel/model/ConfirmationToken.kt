package com.gpch.hotel.model

import java.util.Date
import java.util.UUID
import javax.persistence.*

@Entity
class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    var tokenid: Long = 0

    @Column(name = "confirmation_token")
    var confirmationToken: String? = null

    @Temporal(TemporalType.TIMESTAMP)
    var createdDate: Date? = null

    @OneToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    var user: User? = null

    constructor()

    constructor(user: User) {
        this.user = user
        createdDate = Date()
        confirmationToken = UUID.randomUUID().toString()
    }
}
