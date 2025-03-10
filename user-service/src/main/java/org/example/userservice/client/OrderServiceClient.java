package org.example.userservice.client;

import org.example.userservice.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {
    @GetMapping("{userId}/orders") //api gateway를 거치지 않음
    List<ResponseOrder> getOrders(@PathVariable String userId);
}
