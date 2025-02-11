package org.example.userservice.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseOrder {
    private String productId;
    private Integer qty; //수량
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;

    private String orderId; //생성될 주문 id
}
