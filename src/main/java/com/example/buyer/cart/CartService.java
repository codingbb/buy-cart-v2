package com.example.buyer.cart;

import com.example.buyer.product.Product;
import com.example.buyer.product.ProductResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepo;

    //장바구니 수량 변경
    @Transactional
    public void updateCart(List<CartRequest.UpdateDTO> requestDTOs) {
        cartRepo.updateQtyAndStatus(requestDTOs);
    }


    //장바구니 삭제하기
    @Transactional
    public void cartDelete(Integer id) {
        cartRepo.deleteById(id);

    }


    //장바구니 목록 보기
    public List<CartResponse.CartDTO> getCartList(Integer sessionUserId) {
        List<CartResponse.CartDTO> cartList = cartRepo.findAll();

        //유저별로 장바구니 관리
        List<CartResponse.CartDTO> cartWithUser = cartList.stream().filter(cart ->
                sessionUserId != null && sessionUserId.equals(cart.getUserId()))
                        .collect(Collectors.toList());

//        System.out.println("카트리스트~ " + cartList);

        return cartWithUser;
    }


    //장바구니 넣기
    @Transactional
    public Boolean save(Integer userId, CartRequest.SaveDTO requestDTO) {
        Cart cart = cartRepo.findByUserAndProductId(userId, requestDTO.getProductId());

        //장바구니에 중복된 상품이 들어오면 저장 안되어야함
//        if (cart.getUserId().equals(userId) && cart.getProductId().equals(productId)) {
        if (cart != null) {
            return false;

        } else {
            cartRepo.save(userId, requestDTO);
            return true;
        }
    }


}
