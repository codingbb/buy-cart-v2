package com.example.buyer.cart;

import com.example.buyer.product.Product;
import lombok.Builder;
import lombok.Data;

public class CartResponse {

    @Data
    public static class QtyDTO {
        private Integer qty;

        @Builder
        public QtyDTO(Integer qty) {
            this.qty = qty;
        }
    }


    // 장바구니용 dto
    @Data
    public static class CartDTO {
        private Integer id; //cart id
        private Integer userId;     //유저id
        private Integer buyQty;
        private String imgFileName;
        private Integer productId;  //상품 id
        private String pName;    //상품명
        private Integer price;

        @Builder
        public CartDTO(Integer id, Integer userId, Integer buyQty, String imgFileName, Integer productId, String pName, Integer price) {
            this.id = id;
            this.userId = userId;
            this.buyQty = buyQty;
            this.imgFileName = imgFileName;
            this.productId = productId;
            this.pName = pName;
            this.price = price;
        }
    }


}
