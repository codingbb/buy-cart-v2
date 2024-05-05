package com.example.buyer.order;

import com.example.buyer.cart.CartRepository;
import com.example.buyer.orderItem.OrderItemRepository;
import com.example.buyer.product.Product;
import com.example.buyer.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final CartRepository cartRepo;

    //구매하기 로직
    @Transactional
    public void saveOrder(OrderRequest.SaveDTO requestDTO) {
        System.out.println("으악" + requestDTO);

        //order 저장
        Integer orderId = orderRepo.save(requestDTO);

        //각각 sum 계산
//        Integer sum;
//        List<Integer> price = requestDTO.getPrice();
//        List<Integer> buyQty = requestDTO.getBuyQty();
//
//        List<Integer> sums = new ArrayList<>();
//
//        for (int i = 0; i < requestDTO.getProductId().size(); i++) {
//            sum = price.get(i) * buyQty.get(i);
//            sums.add(sum);
//        }
//
//        requestDTO.setSum(sums);

        //orderItem 저장
        orderItemRepo.save(requestDTO, orderId);

        //수량 반영
        orderRepo.updateQty(requestDTO);

        cartRepo.deleteBySelectId(requestDTO.getCartId());


    }



    //주문 취소하기!!
    @Transactional
    public void orderCancel(List<OrderRequest.CancelDTO> requestDTO) {
        requestDTO.forEach(cancelDTO -> {
            // 1. order_tb 테이블 업데이트
            // 2. product_tb 테이블 업데이트
        });

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


    //내 구매목록 로직 ssar 유저가 구매한 내역만 나와야함
    public List<OrderResponse.ListDTO> orderList(Integer sessionUserId) {

        List<OrderResponse.ListDTO> orderList = orderRepo.findAllOrder(sessionUserId);

        // orderId가 중복되어서 촤차아악 나오길래 중복제거 (대표 물품만 1개 나오게)
        Map<Integer, OrderResponse.ListDTO> orderDistinct =
                orderList.stream().collect(Collectors.toMap(
                        list -> list.getOrderId(),  //orderId가 키값
                        list -> list,           // 값
                        (first, second) -> first    //같은 키를 가진 요소가 있으면 첫번째 값 사용
                ));

        // Map의 values 컬렉션을 List로 변환하여 반환
        List<OrderResponse.ListDTO> distinctOrderList = new ArrayList<>(orderDistinct.values());
        // 주문 ID(orderId)를 기준으로 내림차순 정렬
        distinctOrderList.sort((order1, order2) -> order2.getOrderId().compareTo(order1.getOrderId()));

        // 화면의 No용
        Integer indexNum = distinctOrderList.size();
        for (OrderResponse.ListDTO listNum : distinctOrderList) {
            listNum.setIndexNum(indexNum--);
        }

        return distinctOrderList;

    }


//    //주문 취소 로직
    public List<OrderResponse.ListDTO> orderCancelList(Integer sessionUserId) {
        List<OrderResponse.ListDTO> orderList = orderRepo.findAllCancelOrder(sessionUserId);

        // orderList에서 orderId 별로 sum 을 합한 걸 totalSum 으로 해야함 ..
        // TODO: 이거 모르겠어요 너무 어려워요!!! 뤼튼 코드입니다
//        Map<Integer, Integer> totalSum = new HashMap<>();
//        for (OrderResponse.ListDTO order : orderList) {
//            totalSum.put(order.getOrderId(), totalSum.getOrDefault(order.getOrderId(), 0) + order.getSum());
//        }

        // orderId가 중복되어서 촤차아악 나오길래 중복제거 (대표 물품만 1개 나오게)
        Map<Integer, OrderResponse.ListDTO> orderDistinct =
                orderList.stream().collect(Collectors.toMap(
                        list -> list.getOrderId(),  //orderId가 키값
                        list -> list,           // 값
                        (first, second) -> first    //같은 키를 가진 요소가 있으면 첫번째 값 사용
                ));

//        Integer totalSum = orderList.stream().mapToInt(value -> value.getSum()).sum();

        // 중복 제거된 목록에서 totalSum 설정
//        orderDistinct.values().forEach(order -> order.setTotalSum(totalSum.get(order.getOrderId())));

        // Map의 values 컬렉션을 List로 변환하여 반환
        List<OrderResponse.ListDTO> distinctOrderList = new ArrayList<>(orderDistinct.values());
        // 주문 ID(orderId)를 기준으로 내림차순 정렬
        distinctOrderList.sort((order1, order2) -> order2.getOrderId().compareTo(order1.getOrderId()));

        // 화면의 No용
        Integer indexNum = distinctOrderList.size();
        for (OrderResponse.ListDTO listNum : distinctOrderList) {
            listNum.setIndexNum(indexNum--);
        }

        return distinctOrderList;


    }

}
