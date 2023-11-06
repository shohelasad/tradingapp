package com.trading.app.repository;

import com.trading.app.entity.Signal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignalRepository extends JpaRepository<Signal, Integer> {
}