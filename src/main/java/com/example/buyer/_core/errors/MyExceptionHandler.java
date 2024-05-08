package com.example.buyer._core.errors;

import com.example.buyer._core.errors.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {

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
