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

    //주문 취소 로직
    @PostMapping("/order-cancel")
    public @ResponseBody String orderCancel(@RequestBody List<OrderRequest.CancelDTO> requestDTO) {
        System.out.println("주문 취소 DTO : " + requestDTO);
//        orderService.orderCancel(requestDTO);
        return "redirect:/order-list";
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
        requestDTO.setStatus(Boolean.TRUE);
        orderService.saveOrder(requestDTO);

        return "redirect:/order-list";

    }

    // 주문하려는 물품 확인 폼
    @GetMapping("/order-save-form")
    public String orderCheckForm(@RequestParam("productId") Integer productId, @RequestParam("buyQty") Integer buyQty, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        //dto 사용해서 한 번에 다 담기
        OrderResponse.SaveFormDTO orderCheck = orderService.orderCheck(sessionUser.getId(), productId, buyQty);
        System.out.println("주문폼 dto 값 확인 : " + orderCheck);
        request.setAttribute("order", orderCheck);

        return "/order/order-save-form";
    }

    // 주문폼 //Get 요청이겠지?? POST? GET? POST? GET?
//    @PostMapping("/order-form")
//    public String orderFormPost() {
//
//        return "/order/order-form";
//    }


}
