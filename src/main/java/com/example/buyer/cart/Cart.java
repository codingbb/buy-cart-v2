package com.example.buyer.cart;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Table(name = "cart_tb")
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private Integer productId;

    //구매 수량
    private Integer buyQty;

    //sum은 x. 연산해서 넣어라  //price랑 productName은 넣는게 나을까 ? ? join안하게?

    @CreationTimestamp
    private LocalDateTime createdAt;


}
