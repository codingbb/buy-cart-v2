package com.example.buyer.order;

import com.example.buyer.product.Product;
import com.example.buyer.user.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

public class OrderResponse {

    //장바구니 주문 품목
    @Data
    public static class SaveFormDTO {
        private Integer cartId;
        private Integer userId;
        private Integer productId;
        private Integer buyQty;
        private Boolean status;
        private String pName;
        private Integer price;

        private Integer sum;

        @Builder
        public SaveFormDTO(Integer cartId, Integer userId, Integer productId, Integer buyQty, Boolean status, String pName, Integer price, Integer sum) {
            this.cartId = cartId;
            this.userId = userId;
            this.productId = productId;
            this.buyQty = buyQty;
            this.status = status;
            this.pName = pName;
            this.price = price;
            this.sum = sum;
        }
    }


    // 주문하려는 물품 확인 폼
//    @Data
//    public static class SaveFormDTO {
//        // 유저 정보
//        private Integer userId;
//        private String name;    //유저 성명
//        private String address;
//        private String phone;
//
//        //주문 상품 정보 //product
//        private Integer productId;
//        private String productName;     //상품 이름
//        private Integer price;
//
//        //주문한 상품 수량
//        private Integer buyQty;
//        private Integer sum;
//
//        //cart 부분
//        private Integer cartId;
//
//        @Builder
//        public SaveFormDTO(User user, Product product, Integer buyQty, Integer sum) {
//            this.userId = user.getId();
//            this.name = user.getName();
//            this.address = user.getAddress();
//            this.phone = user.getPhone();
//            this.productId = product.getId();
//            this.productName = product.getName();
//            this.price = product.getPrice();
//            this.buyQty = buyQty;
//            this.sum = sum;
//        }
//    }


    //주문 상세보기 dto
    @Data
    public static class DetailDTO {
        private Integer id;     //order id
        private Integer buyQty;     //주문한 수량
        private Integer productId;     //상품 id
        private Integer sum;        //총합
        private String payment;
        private Integer userId;     //user id
        private String status;

        private String uName;    //성함
        private String address;
        private String phone;

        private String pName;   //상품명
        private Integer price;  //상품 가격

        //라디오 버튼용
        private Boolean isCredit = false;
        private Boolean isAccount = false;

        //주문 취소 버튼 안 보이는 용
        private Boolean isNotCancel = true;

        @Builder
        public DetailDTO(Integer id, Integer buyQty, Integer productId, Integer sum, String payment, Integer userId, String status, String uName, String address, String phone, String pName, Integer price) {
            this.id = id;
            this.buyQty = buyQty;
            this.productId = productId;
            this.sum = sum;
            this.payment = payment;
            this.userId = userId;
            this.status = status;
            this.uName = uName;
            this.address = address;
            this.phone = phone;
            this.pName = pName;
            this.price = price;
            payCheck();
//            isOrderCancel();
        }

        public void payCheck() {
            if ("계좌이체".equals(payment)) {
                this.isAccount = true;
            } if ("신용카드".equals(payment)) {
                this.isCredit = true;
            }
        }

//        public void isOrderCancel() {
//            if ("주문취소".equals(status)) {
//                this.isNotCancel = false;
////                System.out.println("나오니~~~??");
//            }
//
//        }

    }


    //내 구매목록 DTO
    @Data
    public static class ListDTO {
        private Integer orderId;
        private Integer userId;
        private String pName;
        private String payment;
        private Integer sum;
        private Integer buyQty;
        private Integer productId;
//        private Integer count;  //외 ~~ 이렇게 넣을것임
        private Boolean status;
        private LocalDate createdAt;

//        private String nowStatus;   //값 뿌릴려니까 false 값만 들어오길래.. 취소완료로 뿌려주려고 함
        private Integer indexNum;   // index 용
        private Integer totalSum;

        @Builder
        public ListDTO(Integer orderId, Integer userId, String pName, String payment, Integer sum, Integer buyQty, Integer productId, Boolean status, LocalDate createdAt, Integer indexNum, Integer totalSum) {
            this.orderId = orderId;
            this.userId = userId;
            this.pName = pName;
            this.payment = payment;
            this.sum = sum;
            this.buyQty = buyQty;
            this.productId = productId;
            this.status = status;
            this.createdAt = createdAt;
            this.indexNum = indexNum;
            this.totalSum = totalSum;
        }


    }

}
