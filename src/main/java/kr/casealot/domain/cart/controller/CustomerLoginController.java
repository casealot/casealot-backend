package kr.casealot.domain.cart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class CustomerLoginController {

    public ResponseEntity<?> kakaoLogin(){

        return ResponseEntity.ok("success");
    }
}
