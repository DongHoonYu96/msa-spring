package org.example.userservice.vo;

import java.util.Date;

public class ResponseOrder {
    private String productId;
    private Integer qty; //수량
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;

    private String orderId; //생성될 주문 id
}
