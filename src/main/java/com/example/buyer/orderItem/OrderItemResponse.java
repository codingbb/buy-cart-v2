package com.example.buyer.orderItem;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

public class OrderItemResponse {

    //주문 취소용 정보 받는 dto
    @Data
    public static class CancelDTO {
        private Integer orderId;
        private Integer sum;    //총액
        private String payment;
        private Boolean status; //order status
        private LocalDate createdAt;

        private Integer productId;
        private String name;

        private Integer buyQty;

        @Builder
        public CancelDTO(Integer orderId, Integer sum, String payment, Boolean status, LocalDate createdAt, Integer productId, String name, Integer buyQty) {
            this.orderId = orderId;
            this.sum = sum;
            this.payment = payment;
            this.status = status;
            this.createdAt = createdAt;
            this.productId = productId;
            this.name = name;
            this.buyQty = buyQty;
        }
    }

}
