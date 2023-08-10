package com.example.application.service

import com.example.domain.entity.Order
import com.example.domain.entity.OrderStatus
import com.example.domain.entity.Product
import com.example.domain.entity.ProductDetail
import com.example.domain.repository.OrderRepository
import com.example.domain.repository.ProductRepository
import com.example.presentation.vo.*
import org.assertj.core.api.Assertions
import spock.lang.Specification

import java.time.LocalDateTime

class OrderApplicationServiceTest extends Specification {
    ProductRepository productRepository = Mock()
    OrderRepository orderRepository = Mock()
    OrderApplicationService orderApplicationService = new OrderApplicationService(productRepository, orderRepository)

    def "should return correct order id"() {
        given:
        Integer PRODUCT_ID = 11
        Integer ORDER_ID = 1
        Long QUANTITY = 10L

        List<OrderProductReqDto> orderProducts = List.of(new OrderProductReqDto(PRODUCT_ID, QUANTITY))
        OrderReqDto orderReqDto = new OrderReqDto("customerId", BigDecimal.TEN, orderProducts)

        Product product = new Product(PRODUCT_ID, "testProduct", BigDecimal.TEN, ProductStatus.VALID)
        productRepository.findById(PRODUCT_ID) >> product

        orderRepository.save(_) >> ORDER_ID

        when:
        Integer result = orderApplicationService.createOrder(orderReqDto)

        then:
        Assertions.assertThat(result == 1)
    }

    def "should retrieve order by consumer id"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", price: BigDecimal.valueOf(10L), amount: 2)]

        List<Order> OrderDetails = [
                new Order(
                        id: 1,
                        customerId: "dcabcfac-6b08-47cd-883a-76c5dc366d88",
                        orderId: "order id",
                        totalPrice: BigDecimal.valueOf(10L),
                        status: OrderStatus.CREATED,
                        createTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                        updateTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                        productDetails: productDetailList
                ),
        ]

        orderRepository.findByCustomerId(_) >> OrderDetails

        List<OrderProductDetailDto> orderProductDetails = [new OrderProductDetailDto(id: 1, name: "water", price: BigDecimal.valueOf(10L), amount: 2)]
        List<OrderListDto> expectedOrderList = [
                new OrderListDto(
                        id: 1,
                        customerId: "dcabcfac-6b08-47cd-883a-76c5dc366d88",
                        totalPrice: BigDecimal.valueOf(10L),
                        status: OrderStatus.CREATED,
                        productDetails: orderProductDetails
                ),
        ]

        when:
        def result = orderApplicationService.findByCustomerId("dcabcfac-6b08-47cd-883a-76c5dc366d88")

        then:
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedOrderList)
    }
}
