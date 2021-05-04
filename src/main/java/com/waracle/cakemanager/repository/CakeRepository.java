package com.waracle.cakemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waracle.cakemanager.model.CakeEntity;

public interface CakeRepository extends JpaRepository<CakeEntity, Long> {
}
