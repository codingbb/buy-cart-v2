package com.example.buyer._core.errors;

import com.example.buyer._core.errors.exception.*;
import com.example.buyer._core.utils.ApiUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(ApiException400.class)
    public ResponseEntity<?> exApi400(ApiException400 e){
//        System.out.println(e.getMessage() + "잘 들어오나요??");
        ApiUtil<?> apiUtil = new ApiUtil<>(400, e.getMessage());
        return new ResponseEntity<>(apiUtil, HttpStatus.BAD_REQUEST);
    }


    //애는 Exception
    @ExceptionHandler(Exception400.class)
    public String ex400(Exception400 e, HttpServletRequest request){
        request.setAttribute("msg", e.getMessage());
        return "err/400";
    }
    @ExceptionHandler(Exception401.class)
    public String ex401(Exception401 e, HttpServletRequest request){
        request.setAttribute("msg", e.getMessage());
        return "err/401";
    }


    //애네들은 RuntimeException
    @ExceptionHandler(Exception403.class)
    public String ex403(RuntimeException e, HttpServletRequest request) {
        request.setAttribute("msg", e.getMessage());
        return "err/403";
    }

    @ExceptionHandler(Exception404.class)
    public String ex404(RuntimeException e, HttpServletRequest request) {
        request.setAttribute("msg", e.getMessage());
        return "err/404";
    }

    @ExceptionHandler(Exception500.class)
    public String ex500(RuntimeException e, HttpServletRequest request) {
        request.setAttribute("msg", e.getMessage());
        return "err/500";
    }

}
