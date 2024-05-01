package com.example.buyer.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final EntityManager em;

    //로그인
    public User findByUsernameAndPassword(UserRequest.LoginDTO requestDTO) {
        String q = """
                select * from user_tb where username =? and password = ?
                """;
        Query query = em.createNativeQuery(q, User.class);
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());

        User user = (User) query.getSingleResult();
        return user;
    }


    //회원가입
    public void save(UserRequest.JoinDTO requestDTO) {
        String q = """
                insert into user_tb 
                (username, password, name, phone, birth, address, created_at) 
                values (?, ?, ?, ?, ?, ?, now())
                """;

        Query query = em.createNativeQuery(q);
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());
        query.setParameter(3, requestDTO.getName());
        query.setParameter(4, requestDTO.getPhone());
        query.setParameter(5, requestDTO.getBirth());
        query.setParameter(6, requestDTO.getAddress());

        query.executeUpdate();
    }


}
