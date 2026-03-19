package com.gpch.hotel.model

import javax.persistence.*

@Entity
@Table(name = "employee")
class Employee {

    @Id
    @Column(name = "employee_id")
    var id: Long = 0

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null

    @Column(name = "salary")
    var salary: Int = 0

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH])
    @JoinTable(
        name = "position_role",
        joinColumns = [JoinColumn(name = "employee_id")],
        inverseJoinColumns = [JoinColumn(name = "position_id")]
    )
    var positions: Position? = null

    @Column(name = "phone")
    var phone: String? = null

    @Column(name = "address")
    var address: String? = null
}
