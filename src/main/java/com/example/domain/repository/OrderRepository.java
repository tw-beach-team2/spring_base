package com.example.domain.repository;

import com.example.domain.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface OrderRepository {
  List<Order> findByCustomerId(String customerId);

  String save(Order order) throws JsonProcessingException;
}
