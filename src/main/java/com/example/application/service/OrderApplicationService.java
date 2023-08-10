package com.example.application.service;

import static com.example.application.assembler.OrderListDtoMapper.MAPPER;

import com.example.domain.convertor.ProductConvertor;
import com.example.domain.entity.Order;
import com.example.domain.entity.OrderStatus;
import com.example.domain.entity.Product;
import com.example.domain.entity.ProductDetail;
import com.example.domain.repository.OrderRepository;
import com.example.domain.repository.ProductRepository;
import com.example.presentation.vo.OrderListDto;
import com.example.presentation.vo.OrderProductReqDto;
import com.example.presentation.vo.OrderReqDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderApplicationService {
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  public List<OrderListDto> findByCustomerId(String customerId) {
    return orderRepository.findByCustomerId(customerId).stream().map(MAPPER::toDto).toList();
  }

  public Integer createOrder(OrderReqDto orderReqDto) throws JsonProcessingException {

    ArrayList<ProductDetail> productDetails = getProductDetails(orderReqDto);

    BigDecimal totalPrice = calculateTotalPrice(productDetails);

    Order order =
        new Order(
            null,
            orderReqDto.getCustomerId(),
            null, // TODO: 2023/8/10
            totalPrice,
            OrderStatus.CREATED,
            LocalDateTime.now(),
            LocalDateTime.now(),
            productDetails);
    return orderRepository.save(order);
  }

  private static BigDecimal calculateTotalPrice(ArrayList<ProductDetail> productDetails) {
    return productDetails.stream()
        .map(ProductDetail::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private ArrayList<ProductDetail> getProductDetails(OrderReqDto orderReqDto) {
    ArrayList<ProductDetail> productDetails = new ArrayList<>();

    ProductConvertor productConvertor = ProductConvertor.generateProductConvertor();

    for (OrderProductReqDto orderProduct : orderReqDto.getOrderProducts()) {
      Product product = productRepository.findById(orderProduct.getProductId());
      ProductDetail productDetail = productConvertor.toProductDetail(product);
      productDetail.setAmount(orderProduct.getQuantity());
      productDetails.add(productDetail);
    }
    return productDetails;
  }
}
