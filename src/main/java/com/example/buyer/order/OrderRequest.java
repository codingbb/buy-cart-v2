package com.example.buyer.order;

import com.example.buyer.product.Product;
import com.example.buyer.user.User;
import lombok.Builder;
import lombok.Data;

public class OrderRequest {

    //주문 취소용 정보 받는 dto
    @Data
    public static class CancelDTO {
        private Integer orderId;
        private Integer buyQty;
        private String status;
    }



    //order save용 DTO
    @Data
    public static class SaveDTO {
        // user 들고 오는 부분
        private Integer userId;
        private String name;
        private String address;
        private String phone;
        private String payment;

        //product 들고 오는 부분
        private Integer productId;
        private String pName;
        private Integer buyQty;    //선택한 수량
        private Integer price;  //계산된 가격

        //order에 넣는 부분
        private Integer sum; //합계
        private Boolean status;
    }

}
