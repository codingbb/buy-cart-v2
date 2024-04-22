package com.example.buyer.product;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ProductController {
    private final ProductService productService;

    //TODO: 이것부터 해야함! 상품 목록 보기
    @GetMapping("/product-list")
    public String productList(HttpServletRequest request) {

        return "/product/product-list";
    }

    //내 구매목록
    @GetMapping("/buy-list")
    public String buyList(HttpServletRequest request) {
        List<ProductResponse.BuyListDTO> productList = productService.findAllList();
        request.setAttribute("productList", productList);
        return "/product/buy-list";
    }


    //상품 상세보기
    @GetMapping("/product/{id}")
    public String detail(@PathVariable Integer id, HttpServletRequest request) {
        ProductResponse.DetailDTO product = productService.findByIdDetail(id);
        request.setAttribute("product", product);
        return "/product/detail";
    }


    //메인 페이지
    @GetMapping("/")
    public String main(HttpServletRequest request) {
        List<ProductResponse.MainDTO> productList = productService.findAllMain();
        request.setAttribute("productList", productList);
        return "/index";
    }

    //상품명 실시간 중복체크
//    @GetMapping("/product/name-check")
//    public @ResponseBody String nameSameCheck(String name) {
//        Product product = productService.findByName(name);
//        if (product == null) {
//            return "true"; //상품 등록 가능
//        } else {
//            return "false"; //상품 등록 불가
//        }
//    }

    //상품명 실시간 중복체크 (업데이트용)
//    @GetMapping("/product/name-check/update")
//    public @ResponseBody String nameSameCheckUpdate(String name, Integer id) {
//        Product product = productService.findByNameUpdate(name, id);
//        if (product == null) {
//            return "true"; //상품 등록 가능
//        } else {
//            return "false"; //상품 등록 불가
//        }
//    }

}
