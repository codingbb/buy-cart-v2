package com.example.buyer.orderItem;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Table(name = "order_item_tb")
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;

    //구매 수량
    private Integer buyQty;
    private Integer sum;    //고정값이라 받아옴. 값이 바뀌는 order같은건 price*qty로 연산하자

    private Integer orderId;

    @CreationTimestamp
    private LocalDateTime createdAt;



}
