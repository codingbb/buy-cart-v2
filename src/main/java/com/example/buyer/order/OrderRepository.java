package com.example.buyer.order;

import com.example.buyer.orderItem.OrderItem;
import com.example.buyer.orderItem.OrderItemResponse;
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

//    public List<Integer> findByOrderIdAndProductId(OrderRequest.CancelDTO cancelDTO) {
//        String q = """
//                select product_id from order_item_tb where id = ?
//                """;
//        Query query = em.createNativeQuery(q, OrderItem.class);
//        query.setParameter(1, cancelDTO.getOrderId());
//        List<Integer> orderItems = query.getResultList();
//        return orderItems;
//    }


    //주문 취소 쿼리문 join 쓰고싶어서 씀 (product_tb 수량 변경, order_tb 상태값 변경)
    public void updateStatus(OrderItemResponse.CancelDTO cancelDTO) {
        System.out.println("---------------------------------");
        System.out.println(cancelDTO);
        String q = """
                update order_tb set status = ? where id = ?
                """;

        Query query = em.createNativeQuery(q);

        query.setParameter(1, false);   //false 고정값으로 받아와도 되는걸까..
        query.setParameter(2, cancelDTO.getOrderId());
        query.executeUpdate();
    }

    public void updateQtyPlus(OrderItemResponse.CancelDTO cancelDTO) {
        System.out.println("---------------------------------");
        System.out.println(cancelDTO);
        String q = """
                update product_tb set qty = qty + ? where id = ?
                """;
        Query query = em.createNativeQuery(q);

        query.setParameter(1, cancelDTO.getBuyQty());   //false 고정값으로 받아와도 되는걸까..
        query.setParameter(2, cancelDTO.getProductId());
        query.executeUpdate();
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
                insert into order_tb (user_id, payment, status, sum, created_at) 
                values (?, ?, ?, ?, now())
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, requestDTO.getUserId());
        query.setParameter(2, requestDTO.getPayment());
        query.setParameter(3, true);
        query.setParameter(4, requestDTO.getTotalSum());

        query.executeUpdate();

        String a = """
                select max(id) from order_tb
                """;

        Query query1 = em.createNativeQuery(a);
        Integer orderId = (Integer) query1.getSingleResult();

        return orderId;
    }

    //상품을 구매하면 재고 차감
    public void updateQty(OrderRequest.SaveDTO requestDTO) {

        for (int i = 0; i < requestDTO.getProductId().size(); i++) {
            String q = """
                    update product_tb set qty = qty - ? where id = ?
                    """;
            Query query = em.createNativeQuery(q);
            query.setParameter(1, requestDTO.getBuyQty().get(i));
            query.setParameter(2, requestDTO.getProductId().get(i));
            query.executeUpdate();
        }

    }

    //주문내역 폼 (order-detail-form) 조회용
    public OrderResponse.DetailDTO findUserProductByOrderId(Integer orderId) {
        String q = """
                select o.id, o.buy_qty, o.product_id, o.payment, o.user_id, o.status, 
                u.name uName, u.address, u.phone, p.name pName, p.price 
                from order_tb o 
                inner join user_tb u on o.user_id = u.id 
                inner join product_tb p on o.product_id = p.id 
                where o.id = ?
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
    public List<OrderResponse.ListDTO> findAllCancelOrder(Integer sessionUserId) {

        String q = """
                select o.id, o.sum, o.user_id, o.payment, o.created_at, o.status, p.name
                from order_item_tb oi
                inner join order_tb o on oi.order_id = o.id
                inner join product_tb p on oi.product_id = p.id
                where o.user_id = ? and o.status = ? order by o.id desc
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, sessionUserId);
        query.setParameter(2, false);

        //Object 배열 타입으로 받아야함.
        List<Object[]> rows = query.getResultList();
        List<OrderResponse.ListDTO> orderList = new ArrayList<>();

        for (Object[] row : rows) {
            //listDTO
            Integer orderId = (Integer) row[0];
            Integer sum = (Integer) row[1];
            Integer userId = (Integer) row[2];
            String payment = (String) row[3];
            LocalDate createdAt = ((Timestamp) row[4]).toLocalDateTime().toLocalDate();
            Boolean status = (Boolean) row[5];
            String pName = (String) row[6];

            OrderResponse.ListDTO listDTO = OrderResponse.ListDTO.builder()
                    .orderId(orderId)
                    .sum(sum)
                    .userId(userId)
                    .payment(payment)
                    .createdAt(createdAt)
                    .status(status)
                    .pName(pName)
                    .build();

            orderList.add(listDTO);
        }

//        System.out.println("db값 확인용..." + orderList);

        return orderList;


    }


    //order-list 조회용
    public List<OrderResponse.ListDTO> findAllOrder(Integer sessionUserId) {
        //TODO: 여기도 sum 조회요...
        String q = """
                select o.id, o.sum, o.user_id, oi.buy_qty, oi.product_id, o.payment, o.created_at, o.status, p.name
                from order_item_tb oi
                inner join order_tb o on oi.order_id = o.id
                inner join product_tb p on oi.product_id = p.id
                where o.user_id = ? and o.status = ? order by o.id desc
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, sessionUserId);
        query.setParameter(2, true);


        //Object 배열 타입으로 받아야함.
        List<Object[]> rows = query.getResultList();
        List<OrderResponse.ListDTO> orderList = new ArrayList<>();

        for (Object[] row : rows) {
            //listDTO
            Integer orderId = (Integer) row[0];
            Integer sum = (Integer) row[1];
            Integer userId = (Integer) row[2];
            Integer buyQty = (Integer) row[3];
            Integer productId = (Integer) row[4];
            String payment = (String) row[5];
            LocalDate createdAt = ((Timestamp) row[6]).toLocalDateTime().toLocalDate();
            Boolean status = (Boolean) row[7];
            String pName = (String) row[8];

            OrderResponse.ListDTO listDTO = OrderResponse.ListDTO.builder()
                    .orderId(orderId)
                    .sum(sum)
                    .userId(userId)
                    .buyQty(buyQty)
                    .productId(productId)
                    .payment(payment)
                    .createdAt(createdAt)
                    .status(status)
                    .pName(pName)
                    .build();

            orderList.add(listDTO);
        }

        System.out.println("db값 확인용..." + orderList);

        return orderList;

    }

    //count 용
    public Long findOrderItemCount(Integer orderId) {
        String q = """
            select count(id) 
            from order_item_tb 
            where order_id = ?
            """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, orderId);

        Long count = (Long) query.getSingleResult();
        return count;
    }



}
