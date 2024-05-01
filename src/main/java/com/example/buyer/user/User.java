package com.example.buyer.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Table(name = "user_tb")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;        //아이디

    @Column(nullable = false)
    private String password;

    @Column(length = 15)
    private String name;            //이름(성명)
//    private String compName;            //기업명(성명)

//    private String compNum;           //사업자등록번호

    @Column(nullable = false)
    private String phone;           //전화번호

    @Column(nullable = false)
    private LocalDate birth;    //생년월일(user) //설립일(comp)

    @Column(nullable = false)
    private String address;

//    private Integer role;       //개인 - 1, 기업 - 2

    @CreationTimestamp
    private LocalDateTime createdAt;


}
