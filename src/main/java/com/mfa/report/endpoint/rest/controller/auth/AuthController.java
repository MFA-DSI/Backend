package com.mfa.report.endpoint.rest.controller.auth;


import com.mfa.report.endpoint.rest.model.Auth;
import com.mfa.report.endpoint.rest.model.SignUp;
import com.mfa.report.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users/")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private  final AuthService authService;


    @PostMapping("/login")
    public String signIn (@RequestBody Auth auth){
        return authService.signIn(auth);
    }

    @PostMapping("/signup")
    public String signUp (@RequestBody SignUp sign){
        return authService.signUp(sign);
    }

}
