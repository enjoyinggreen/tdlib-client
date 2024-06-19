package com.rodgers.repository;

import com.rodgers.model.WebOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<WebOrder, Integer> {
}