package com.example.buyer.cart;

import lombok.Data;

public class CartRequest {

    //cart-save DTO
    @Data
    public static class SaveDTO {
        private Integer productId;
        private Integer buyQty;

    }

    // cart-update 그 넘어가는거
    @Data
    public static class UpdateDTO {
        private Integer cartId; //cartId
        private Integer productId;
        private Integer buyQty; //수량 변경
        private Boolean status;

    }

}
