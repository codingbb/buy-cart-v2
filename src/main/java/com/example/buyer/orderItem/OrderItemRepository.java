package com.example.buyer.orderItem;

import com.example.buyer.order.OrderRequest;
import com.example.buyer.order.OrderResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderItemRepository {
    private final EntityManager em;

    //주문 취소 로직
    public List<OrderItemResponse.CancelDTO> findByOrderId(Integer id) {
        String q = """
                select o.id, o.sum, o.payment, o.status, o.created_at, p.id, p.name, oi.buy_qty 
                from order_item_tb oi
                inner join order_tb o on oi.order_id = o.id
                inner join product_tb p on oi.product_id = p.id
                where order_id = ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, id);

        List<Object[]> rows = query.getResultList();
        List<OrderItemResponse.CancelDTO> orderList = new ArrayList<>();

        for (Object[] row : rows) {
            //listDTO
            Integer orderId = (Integer) row[0];
            Integer sum = (Integer) row[1];
            String payment = (String) row[2];
            Boolean status = (Boolean) row[3];
            LocalDate createdAt = ((Timestamp) row[4]).toLocalDateTime().toLocalDate();
            Integer productId = (Integer) row[5];
            String pName = (String) row[6];
            Integer buyQty = (Integer) row[7];

            OrderItemResponse.CancelDTO listDTO = OrderItemResponse.CancelDTO.builder()
                    .orderId(orderId)
                    .sum(sum)
                    .payment(payment)
                    .status(status)
                    .createdAt(createdAt)
                    .productId(productId)
                    .name(pName)
                    .buyQty(buyQty)
                    .build();

            orderList.add(listDTO);

        }

        return orderList;

    }

    public void save(OrderRequest.SaveDTO requestDTO, Integer orderId) {
        for (int i = 0; i < requestDTO.getPName().size(); i++) {

            String q = """
                insert into order_item_tb (buy_qty, order_id, product_id, created_at)
                values (?, ?, ?, now());
                """;
            Query query = em.createNativeQuery(q);
            query.setParameter(1, requestDTO.getBuyQty().get(i));
            query.setParameter(2, orderId);
            query.setParameter(3, requestDTO.getProductId().get(i));

            query.executeUpdate();
        }
    }
}
