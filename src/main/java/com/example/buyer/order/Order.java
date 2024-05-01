package com.example.buyer.order;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Table(name = "order_tb")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private Integer productId;
    private String payment;

    //구매 수량
    private Integer buyQty;

    // 이거 없으면 join으로 들고와야 함
    private Integer price;

    //구매 상태
    private Boolean status;  //true = 주문완료, false = 취소완료 //String으로 넣으면 안됨! 범주가 없어서 위험! Enum을 쓰든 Boolean사용!

    @Transient
    private Integer indexNum;

    @CreationTimestamp
    private LocalDateTime createdAt;



}
