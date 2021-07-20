package com.gpch.hotel.repository;


import com.gpch.hotel.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;



public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
}