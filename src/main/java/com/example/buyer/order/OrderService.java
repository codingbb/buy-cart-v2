package com.example.buyer.order;

import com.example.buyer.product.Product;
import com.example.buyer.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepo;

    //주문 취소하기!!
    @Transactional
    public void orderCancel(List<OrderRequest.CancelDTO> requestDTO) {
        orderRepo.findByIdAndUpdateStatus(requestDTO);


    }


    //내 주문 내역 폼 order-detail-form
    public OrderResponse.DetailDTO orderDetail(Integer orderId) {
        OrderResponse.DetailDTO orderDetail = orderRepo.findUserProductByOrderId(orderId);

//        System.out.println("dto 값 확인용!! " + orderDetail);

        return orderDetail;
    }


    //주문폼 orderViewForm //responseDTO인가 ? ? ?
    public OrderResponse.SaveFormDTO orderCheck(Integer sessionUserId, Integer productId, Integer buyQty) {
        User user = orderRepo.findByUserId(sessionUserId);
        Product product = orderRepo.findByProductId(productId);

        Integer sum = buyQty * product.getPrice();

        return new OrderResponse.SaveFormDTO(user, product, buyQty, sum);
    }


    //구매하기 로직
    @Transactional
    public void saveOrder(OrderRequest.SaveDTO requestDTO) {
        orderRepo.save(requestDTO);
        orderRepo.updateQty(requestDTO);

    }

    //내 구매목록 로직
    public List<OrderResponse.ListDTO> orderList(Integer sessionUserId) {
        List<OrderResponse.ListDTO> orderList = orderRepo.findAllOrder();

        //ssar 유저가 구매한 내역만 나와야함
        //필터를 쓰는구나..............!!!!!!!!!!!!!!
        List<OrderResponse.ListDTO> findUserOrderList = orderList.stream().filter(list ->
                sessionUserId != null && sessionUserId.equals(list.getUserId()))
                .map(item -> {
                    Integer sum = item.getPrice() * item.getBuyQty();
                    item.setSum(sum);
                    return item;
                })
                .collect(Collectors.toList());

//        Integer sum = orderList.stream().mapToInt(value -> value.getPrice() * value.getBuyQty()).sum();

        // 화면의 No용
        Integer indexNum = findUserOrderList.size();
        for (OrderResponse.ListDTO listNum : findUserOrderList) {
            listNum.setIndexNum(indexNum--);
        }

        return findUserOrderList;

    }


    //주문 취소 로직
    public List<OrderResponse.ListDTO> orderCancelList(Integer sessionUserId) {
        List<OrderResponse.ListDTO> orderList = orderRepo.findAllCancelOrder();

        //ssar 유저가 구매한 내역만 나와야함
        //필터를 쓰는구나..............!!!!!!!!!!!!!!
        List<OrderResponse.ListDTO> findUserOrderList = orderList.stream().filter(list ->
                        sessionUserId != null && sessionUserId.equals(list.getUserId()))
                .map(item -> {
                    Integer sum = item.getPrice() * item.getBuyQty();
                    item.setSum(sum);
                    return item;
                })
                .collect(Collectors.toList());

//        Integer sum = orderList.stream().mapToInt(value -> value.getPrice() * value.getBuyQty()).sum();

        // 화면의 No용
        Integer indexNum = findUserOrderList.size();
        for (OrderResponse.ListDTO listNum : findUserOrderList) {
            listNum.setIndexNum(indexNum--);
        }

        return findUserOrderList;

    }

}
