package com.example.buyer.cart;

import lombok.Data;

public class CartRequest {

    //cart-save DTO
    @Data
    public static class SaveDTO {
        private Integer productId;
        private Integer buyQty;
        private Boolean status;

    }

    // cart-update 그 넘어가는거
    @Data
    public static class UpdateDTO {
        private Integer cartId; //cartId
//        private Integer productId; //  안받아도 된다. cart 객체에 담겨있니까. -> 카트를 업데이트할 때는 상품의 수량이나 상태만 변경하면 되고, 어떤 상품인지를 다시 지정할 필요가 없다
        private Integer buyQty; //수량 변경
        private Boolean status;

    }

}
