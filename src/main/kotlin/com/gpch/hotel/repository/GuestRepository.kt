package com.gpch.hotel.repository

import com.gpch.hotel.model.Guest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository("guestRepository")
interface GuestRepository : JpaRepository<Guest, Long> {
    @Query(
        "select g from Guest g where lower(coalesce(g.firstName, '')) like lower(concat('%', :keyword, '%')) " +
            "or lower(coalesce(g.lastName, '')) like lower(concat('%', :keyword, '%')) " +
            "or lower(coalesce(g.phone, '')) like lower(concat('%', :keyword, '%')) " +
            "or lower(coalesce(g.email, '')) like lower(concat('%', :keyword, '%')) " +
            "or lower(coalesce(g.identityNumber, '')) like lower(concat('%', :keyword, '%'))"
    )
    fun search(@Param("keyword") keyword: String): List<Guest>
}
