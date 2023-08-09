package com.example.domain.entity;

import com.example.domain.util.OrderItemSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
  private Integer id;

  private String customerId;

  private BigDecimal totalPrice;

  private OrderStatus status;

  private LocalDateTime createTime;

  private LocalDateTime updateTime;

  @JsonSerialize(using = OrderItemSerializer.class)
  private List<OrderItem> orderItems;
}