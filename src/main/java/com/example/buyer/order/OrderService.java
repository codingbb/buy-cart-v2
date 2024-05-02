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

    //구매하기 로직
    @Transactional
    public void saveOrder(OrderRequest.SaveDTO requestDTO) {
        System.out.println("에헤헤" + requestDTO);
        Integer orderId = orderRepo.save(requestDTO);
        orderRepo.updateQty(requestDTO);


    }

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

    public User findUser(Integer sessionUserId) {
        User user = orderRepo.findByUserId(sessionUserId);
        return user;
    }

    //장바구니에 있는거 주문하는 폼
    public List<OrderResponse.SaveFormDTO> orderCartList(Integer sessionUserId) {
        List<OrderResponse.SaveFormDTO> orderList = orderRepo.findStatusAndUserId(sessionUserId);

        Integer sum;    //물건 각각 합계
//        Integer totalSum = 0;   //토탈 총액
        for (OrderResponse.SaveFormDTO order : orderList) {
            sum = order.getPrice() * order.getBuyQty();
            order.setSum(sum);
        }

        return orderList;

    }


    //내 구매목록 로직
//    public List<OrderResponse.ListDTO> orderList(Integer sessionUserId) {
//        List<OrderResponse.ListDTO> orderList = orderRepo.findAllOrder();
//
//        //ssar 유저가 구매한 내역만 나와야함
//        //필터를 쓰는구나..............!!!!!!!!!!!!!!
//        List<OrderResponse.ListDTO> findUserOrderList = orderList.stream().filter(list ->
//                sessionUserId != null && sessionUserId.equals(list.getUserId()))
//                .map(item -> {
//                    Integer sum = item.getPrice() * item.getBuyQty();
//                    item.setSum(sum);
//                    return item;
//                })
//                .collect(Collectors.toList());
//
////        Integer sum = orderList.stream().mapToInt(value -> value.getPrice() * value.getBuyQty()).sum();
//
//        // 화면의 No용
//        Integer indexNum = findUserOrderList.size();
//        for (OrderResponse.ListDTO listNum : findUserOrderList) {
//            listNum.setIndexNum(indexNum--);
//        }
//
//        return findUserOrderList;
//
//    }
//
//
//    //주문 취소 로직
//    public List<OrderResponse.ListDTO> orderCancelList(Integer sessionUserId) {
//        List<OrderResponse.ListDTO> orderList = orderRepo.findAllCancelOrder();
//
//        //ssar 유저가 구매한 내역만 나와야함
//        //필터를 써보고싶었어요
//        List<OrderResponse.ListDTO> findUserOrderList = orderList.stream()
//                .filter(list ->
//                        sessionUserId != null && sessionUserId.equals(list.getUserId()))
////                listDTO
//                .map(item -> {
//                    Integer sum = item.getPrice() * item.getBuyQty();
//                    item.setSum(sum);
//                    return item;
//                })
//
//                .map(item -> {
//                    if (item.getStatus().equals(false)) {
//                        item.setNowStatus("취소완료");
//                    }
//                    return item;
//                })
//                .collect(Collectors.toList());
//
////        Integer sum = orderList.stream().mapToInt(value -> value.getPrice() * value.getBuyQty()).sum();
//
//        // 화면의 No용
//        Integer indexNum = findUserOrderList.size();
//        for (OrderResponse.ListDTO listNum : findUserOrderList) {
//            listNum.setIndexNum(indexNum--);
//        }
//
//        return findUserOrderList;
//
//    }

}
