package com.routeme.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.routeme.model.RouteEntity;

public interface RouteRepository extends Repository<RouteEntity, String> {

    void delete(RouteEntity deleted);

    List<RouteEntity> findAll();

    Optional<RouteEntity> findOne(String id);

    Optional<RouteEntity> findByPioId(String pioId);

    RouteEntity save(RouteEntity saved);
}
