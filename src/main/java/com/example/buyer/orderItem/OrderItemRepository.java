package com.example.buyer.orderItem;

import com.example.buyer.order.OrderRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderItemRepository {
    private final EntityManager em;

    public void save(OrderRequest.SaveDTO requestDTO, Integer orderId, Integer sum) {
        for (int i = 0; i < requestDTO.getPName().size(); i++) {

            String q = """
                insert into order_item_tb (buy_qty, order_id, product_id, sum, created_at)
                values (?, ?, ?, ?, now());
                """;
            Query query = em.createNativeQuery(q);
            query.setParameter(1, requestDTO.getBuyQty().get(i));
            query.setParameter(2, orderId);
            query.setParameter(3, requestDTO.getProductId().get(i));
            query.setParameter(4, sum);

            query.executeUpdate();
        }
    }
}
