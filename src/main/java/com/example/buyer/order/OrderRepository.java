package com.example.buyer.order;

import com.example.buyer.product.Product;
import com.example.buyer.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepository {
    private final EntityManager em;

    public List<OrderResponse.SaveFormDTO> findStatusAndUserId(Integer sessionUserId) {
        String q = """
                select c.id, c.user_id, c.product_id, c.buy_qty, c.status, p.name, p.price 
                from cart_tb c
                inner join product_tb p 
                on c.product_id = p.id 
                where c.status = ? and c.user_id = ?
                """;

        Query query = em.createNativeQuery(q);
        query.setParameter(1, true);
        query.setParameter(2, sessionUserId);

        List<Object[]> rows = query.getResultList();
        List<OrderResponse.SaveFormDTO> orderList = new ArrayList<>();

        for (Object[] row : rows) {
            //listDTO
            Integer cartId = (Integer) row[0];
            Integer userId = (Integer) row[1];
            Integer productId = (Integer) row[2];
            Integer buyQty = (Integer) row[3];
            Boolean status = (Boolean) row[4];
            String pName = (String) row[5];
            Integer price = (Integer) row[6];

            OrderResponse.SaveFormDTO listDTO = OrderResponse.SaveFormDTO.builder()
                    .cartId(cartId)
                    .userId(userId)
                    .productId(productId)
                    .buyQty(buyQty)
                    .status(status)
                    .pName(pName)
                    .price(price)
                    .build();

            orderList.add(listDTO);

        }

        return orderList;

    }


    //주문 취소 쿼리문 join 쓰고싶어서 씀 (product_tb 수량 변경, order_tb 상태값 변경)
    public void findByIdAndUpdateStatus(List<OrderRequest.CancelDTO> requestDTO) {
        for (OrderRequest.CancelDTO request : requestDTO) {
            String q = """
                    update order_tb o 
                    inner join product_tb p on o.product_id = p.id 
                    set o.status = ?, p.qty = qty + ? where o.id = ?;
                    """;

            Query query = em.createNativeQuery(q);

            query.setParameter(1, request.getStatus());   //false 고정값으로 받아와도 되는걸까..
            query.setParameter(2, request.getBuyQty());
            query.setParameter(3, request.getOrderId());
            query.executeUpdate();

        }
    }


    //TODO: 만약 똑같은 쿼리문을 UserRepository에서 사용한다고 하면 그걸 끌어와서 써야하는지?
    // 유저 조회
    public User findByUserId(Integer id) {
        String q = """
                select * from user_tb where id = ?
                """;
        Query query = em.createNativeQuery(q, User.class);
        query.setParameter(1, id);
        User user = (User) query.getSingleResult();
        return user;
    }

    //상품 조회
//    public Product findByProductId(Integer id) {
//        String q = """
//                select * from product_tb where id = ?
//                """;
//        Query query = em.createNativeQuery(q, Product.class);
//        query.setParameter(1, id);
//        Product product = (Product) query.getSingleResult();
//        return product;
//    }


    //구매하기 !!
    public Integer save(OrderRequest.SaveDTO requestDTO) {
        String q = """
                insert into order_tb (user_id, payment, status, created_at) 
                values (?, ?, ?, now())
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, requestDTO.getUserId());
        query.setParameter(2, requestDTO.getPayment());
        query.setParameter(3, true);

        query.executeUpdate();

        String a = """
                select max(id) from order_tb
                """;

        Query query1 = em.createNativeQuery(a);
        Integer orderId = (Integer) query1.getSingleResult();

        return orderId;
    }

    //상품을 구매하면 재고 차감
//    public void updateQty(OrderRequest.SaveDTO requestDTO) {
//        String q = """
//                update product_tb set qty = qty - ? where id = ?
//                """;
//        Query query = em.createNativeQuery(q);
//        query.setParameter(1, requestDTO.getBuyQty());
//        query.setParameter(2, requestDTO.getProductId());
//        query.executeUpdate();
//    }

    //주문내역 폼 (order-detail-form) 조회용
    public OrderResponse.DetailDTO findUserProductByOrderId(Integer orderId) {
        String q = """
                select o.id, o.buy_qty, o.product_id, o.payment, o.user_id, o.status, 
                u.name uName, u.address, u.phone, p.name pName, p.price 
                from order_tb o 
                inner join user_tb u on o.user_id = u.id 
                inner join product_tb p on o.product_id = p.id 
                where o.id = ?;
                """;

        Query query = em.createNativeQuery(q);
        query.setParameter(1, orderId);

        Object[] row = (Object[]) query.getSingleResult();
        Integer id = (Integer) row[0];
        Integer buyQty = (Integer) row[1];
        Integer productId = (Integer) row[2];
        String payment = (String) row[3];
        Integer userId = (Integer) row[4];
        String status = (String) row[5];
        String uName = (String) row[6];
        String address = (String) row[7];
        String phone = (String) row[8];
        String pName = (String) row[9];
        Integer price = (Integer) row[10];

        OrderResponse.DetailDTO detailDTO = OrderResponse.DetailDTO.builder()
                .id(id)
                .buyQty(buyQty)
                .productId(productId)
                .payment(payment)
                .userId(userId)
                .status(status)
                .uName(uName)
                .address(address)
                .phone(phone)
                .pName(pName)
                .price(price)
                .build();

        return detailDTO;

    }

    //order-cancel-list 조회용
//    public List<OrderResponse.ListDTO> findAllCancelOrder() {
//        String q = """
//                select o.id, o.user_id, o.price, o.buy_qty, o.payment, o.status, o.created_at, p.name
//                from order_tb o
//                inner join product_tb p on o.product_id = p.id
//                where o.status = ?
//                order by o.id desc;
//                """;
//        Query query = em.createNativeQuery(q);
//        query.setParameter(1, 0);
//
//        //Object 배열 타입으로 받아야함.
//        List<Object[]> rows = query.getResultList();
//        List<OrderResponse.ListDTO> orderList = new ArrayList<>();
//
//        for (Object[] row : rows) {
//            //listDTO
//            Integer id = (Integer) row[0];
//            Integer userId = (Integer) row[1];
//            Integer price = (Integer) row[2];
//            Integer buyQty = (Integer) row[3];
//            String payment = (String) row[4];
//            Boolean status = (Boolean) row[5];
//            LocalDate createdAt = ((Timestamp) row[6]).toLocalDateTime().toLocalDate();
//            String name = (String) row[7];
//
//            OrderResponse.ListDTO listDTO = OrderResponse.ListDTO.builder()
//                    .id(id)
//                    .userId(userId)
//                    .price(price)
//                    .buyQty(buyQty)
//                    .payment(payment)
//                    .status(status)
//                    .createdAt(createdAt)
//                    .name(name)
//                    .build();
//
//            orderList.add(listDTO);
//        }
//
////        System.out.println("db값 확인용..." + orderList);
//
//        return orderList;
//
//    }


    // TODO: 돌아가는지 테스트 좀 하고 써라! 까먹지마~!!
    //order-list 조회용
//    public List<OrderResponse.ListDTO> findAllOrder() {
//        String q = """
//                select o.id, o.user_id, o.price, o.buy_qty, o.payment, o.status, o.created_at, p.name
//                from order_tb o
//                inner join product_tb p on o.product_id = p.id
//                where o.status = ?
//                order by o.id desc;
//                """;
//        Query query = em.createNativeQuery(q);
//        query.setParameter(1, 1);
//
//        //Object 배열 타입으로 받아야함.
//        List<Object[]> rows = query.getResultList();
//        List<OrderResponse.ListDTO> orderList = new ArrayList<>();
//
//        for (Object[] row : rows) {
//            //listDTO
//            Integer id = (Integer) row[0];
//            Integer userId = (Integer) row[1];
//            Integer price = (Integer) row[2];
//            Integer buyQty = (Integer) row[3];
//            String payment = (String) row[4];
//            Boolean status = (Boolean) row[5];
//            LocalDate createdAt = ((Timestamp) row[6]).toLocalDateTime().toLocalDate();
//            String name = (String) row[7];
//
//            OrderResponse.ListDTO listDTO = OrderResponse.ListDTO.builder()
//                    .id(id)
//                    .userId(userId)
//                    .price(price)
//                    .buyQty(buyQty)
//                    .payment(payment)
//                    .status(status)
//                    .createdAt(createdAt)
//                    .name(name)
//                    .build();
//
//            orderList.add(listDTO);
//        }
//
////        System.out.println("db값 확인용..." + orderList);
//
//        return orderList;
//
//    }
}
