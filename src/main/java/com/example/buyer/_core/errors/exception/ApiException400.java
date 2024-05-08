package com.example.buyer._core.errors.exception;

public class ApiException400 extends RuntimeException{

    public ApiException400(String msg) {
        super(msg);
        System.out.println("안녕하세요");
    }
}