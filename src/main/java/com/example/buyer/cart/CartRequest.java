package com.example.buyer.cart;

import lombok.Data;

public class CartRequest {

    //cart-save DTO
    @Data
    public static class SaveDTO {
        private Integer productId;
        private Integer buyQty;
    }

    @Data
    public static class UpdateDTO {
        private Integer cartId; //cartId
        private Integer buyQty; //수량 변경

    }

}
