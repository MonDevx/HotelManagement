package com.gpch.hotel.repository;

import com.gpch.hotel.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("positionRepository")
public interface PositionRepository extends JpaRepository<Position, Integer> {
}
