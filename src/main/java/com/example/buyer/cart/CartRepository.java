package com.example.buyer.cart;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CartRepository {
    private final EntityManager em;

    //장바구니 수량 변경
    public void updateQtyAndStatus(List<CartRequest.UpdateDTO> requestDTOs) {
        for (CartRequest.UpdateDTO requestDTO : requestDTOs) {

            String q = """
                update cart_tb set buy_qty = ?, status = ? where id = ?
                """;

            Query query = em.createNativeQuery(q);
            query.setParameter(1, requestDTO.getBuyQty());
            query.setParameter(2, requestDTO.getStatus());
            query.setParameter(3, requestDTO.getCartId());
            query.executeUpdate();

        }
    }


    //장바구니 삭제
    public void deleteById(Integer id) {
        String q = """
                delete from cart_tb where id = ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, id);
        query.executeUpdate();

    }

    // 장바구니에 있는 거 구매하면 삭제되는 것
    public void deleteBySelectId(List<Integer> cartId) {
        for (int i = 0; i < cartId.size(); i++) {
            String q = """
                    delete from cart_tb where id = ?
                    """;
            Query query = em.createNativeQuery(q);
            query.setParameter(1, cartId.get(i));
            query.executeUpdate();
        }

    }


    //장바구니 목록 보기
    public List<CartResponse.CartDTO> findAll() {
        String q = """
                select c.id, c.user_id, c.buy_qty, p.img_file_name, p.id, p.name, p.price from cart_tb c 
                inner join product_tb p 
                on c.product_id = p.id 
                order by c.id desc;
                """;
        Query query = em.createNativeQuery(q);

        List<Object[]> rows = query.getResultList();
        List<CartResponse.CartDTO> cartList = new ArrayList<>();

        for (Object[] row : rows) {
            Integer id = (Integer) row[0];
            Integer userId = (Integer) row[1];
            Integer buyQty = (Integer) row[2];
            String imgFileName = (String) row[3];
            Integer productId = (Integer) row[4];
            String pName = (String) row[5];
            Integer price = (Integer) row[6];

            CartResponse.CartDTO cartDTO = CartResponse.CartDTO.builder()
                    .id(id)
                    .userId(userId)
                    .buyQty(buyQty)
                    .imgFileName(imgFileName)
                    .productId(productId)
                    .pName(pName)
                    .price(price)
                    .build();

            cartList.add(cartDTO);

        }

//        System.out.println("레파 : " + cartList);

        return cartList;
    }


    //장바구니 담기(추가)
    public void save(Integer userId, CartRequest.SaveDTO requestDTO) {
        String q = """
                insert into cart_tb (user_id, product_id, buy_qty, status, created_at) values (?, ?, ?, ?, now())
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, userId);
        query.setParameter(2, requestDTO.getProductId());
        query.setParameter(3, requestDTO.getBuyQty());
        query.setParameter(4, false);
        query.executeUpdate();

    }

    //장바구니 중복 체크용
    public Cart findByUserAndProductId(Integer userId, Integer productId) {
        try {
            String q = """
                    select * from cart_tb where user_id = ? and product_id = ?
                    """;
            Query query = em.createNativeQuery(q, Cart.class);
            query.setParameter(1, userId);
            query.setParameter(2, productId);
            Cart cartList = (Cart) query.getSingleResult();
            return cartList;

        } catch (NoResultException e) {
            return null;
        }
    }
}
