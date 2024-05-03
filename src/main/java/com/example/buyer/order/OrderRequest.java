package com.example.buyer.order;

import com.example.buyer.product.Product;
import com.example.buyer.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class OrderRequest {

    //save-order-form DTO 주문하기 전 나오는 페이지
    @Data
    public static class SaveOrderDTO {
        private Integer cartId;
        private Integer productId;
        private Integer buyQty;
        private Boolean status;

    }


    //주문 취소용 정보 받는 dto
    @Data
    public static class CancelDTO {
        private Integer orderId;
        private Integer buyQty;
        private Boolean status;
    }



    //order save용 DTO
    @Data
    public static class SaveDTO {
        // user 들고 오는 부분
        private Integer userId;
        private String payment;

        private List<Integer> cartId;

        //product 들고 오는 부분
        private List<Integer> productId;
        private List<String> pName;
        private List<Integer> buyQty;    //선택한 수량
        private List<Integer> price;  //계산된 가격

        //order에 넣는 부분
        private List<Integer> sum; //합계
        private Boolean status;



    }

}
