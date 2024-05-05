package com.example.buyer.cart;

import com.example.buyer._core.errors.exception.Exception400;
import com.example.buyer._core.errors.exception.Exception401;
import com.example.buyer._core.errors.exception.Exception403;
import com.example.buyer._core.errors.exception.Exception404;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepo;

    //장바구니 수량 변경
    @Transactional
    public void updateCart(List<CartRequest.UpdateDTO> requestDTOs) {
        // 1.  카트 id 검증 (조회)  cart, cart
        for(CartRequest.UpdateDTO dto : requestDTOs){
            Cart cart = cartRepo.findById(dto.getCartId());
            if(cart == null){
                // cart 없다고 예외처리 해주기
                throw new Exception404("존재하지 않는 카트입니다.");
            }
        }

        // 2. 재고 있는지 확인
        //buyQty랑 Qty 비교
        for (CartRequest.UpdateDTO dto : requestDTOs) {
            CartResponse.QtyDTO cart = cartRepo.findByQtyWithId(dto.getCartId());
            if (cart == null || cart.getQty() < dto.getBuyQty()) {
                System.out.println("재고 부족 예외 발생 - 재고 수량: " + cart.getQty() + ", 구매 요청 수량: " + dto.getBuyQty());
                throw new Exception400("재고 부족! 구매 불가");
            }
        }

        // 3. 존재하면 업데이트
        cartRepo.updateQtyAndStatus(requestDTOs);

    }



    //장바구니 삭제하기
    @Transactional
    public void cartDelete(Integer id) {
        cartRepo.deleteById(id);

    }


    //장바구니 목록 보기
    public List<CartResponse.CartDTO> getCartList(Integer sessionUserId) {
        List<CartResponse.CartDTO> cartList = cartRepo.findAll(sessionUserId);

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
