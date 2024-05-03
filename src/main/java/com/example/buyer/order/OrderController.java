package com.example.buyer.order;

import com.example.buyer.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final OrderService orderService;
    private final HttpSession session;

    //취소목록
//    @GetMapping("/order-cancel-list")
//    public String orderCancelList(HttpServletRequest request) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
//
//        List<OrderResponse.ListDTO> orderList = orderService.orderCancelList(sessionUser.getId());
////        System.out.println("오더 리스트 : " + orderList);
//        request.setAttribute("orderList", orderList);
//
//        return "/order/cancel-list";
//    }

    //주문 취소 로직
    @PostMapping("/order-cancel")
    public @ResponseBody String orderCancel(@RequestBody List<OrderRequest.CancelDTO> requestDTO) {
        System.out.println("주문 취소 DTO : " + requestDTO);
        orderService.orderCancel(requestDTO);
        return "/order/cancel-list";
    }

    //내가 주문한 상품 상세보기 폼 //주문한 내역이 나와야함
    @GetMapping("/order-detail")
    public String detail(HttpServletRequest request, @RequestParam Integer orderId) {
        OrderResponse.DetailDTO orderDetail = orderService.orderDetail(orderId);
//        System.out.println("주문상세보기 DTO : " + orderDetail);
        request.setAttribute("orderDetail", orderDetail);
        return "/order/order-detail-form";
    }


    //내 구매목록 리스트
    @GetMapping("/order-list")
    public String orderList(HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        List<OrderResponse.ListDTO> orderList = orderService.orderList(sessionUser.getId());
        System.out.println("오더 리스트 : " + orderList);
        request.setAttribute("orderList", orderList);

        return "/order/order-list";
    }

    // 주문하기 = 구매하기
    @PostMapping("/order-save")
    public String save(OrderRequest.SaveDTO requestDTO) {
//        System.out.println("구매하기 : " + requestDTO);
        orderService.saveOrder(requestDTO);

        return "redirect:/order-list";

    }

    // 주문하려는 물품 확인 폼
    @GetMapping("/order-save-form")
    public String orderCheckForm(HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User user = orderService.findUser(sessionUser.getId());

        List<OrderResponse.SaveFormDTO> orderList = orderService.orderCartList(sessionUser.getId());
        System.out.println("일단이거만찍어보자" + orderList);

        //totalSum 계산용...
        Integer totalSum = orderList.stream().mapToInt(value -> value.getSum()).sum();

        // 모델에(request) 담기 .... 한 번에 담고싶다  !!
        request.setAttribute("orderList", orderList);
        request.setAttribute("totalSum", totalSum);
        request.setAttribute("user", user);

        return "/order/order-save-form";
    }

}
