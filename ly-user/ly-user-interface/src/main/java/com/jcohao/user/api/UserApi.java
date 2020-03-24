package com.jcohao.user.api;

import com.jcohao.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


public interface UserApi {
    @GetMapping("query")
    ResponseEntity<User> queryUser(@RequestParam("username") String username,
                                   @RequestParam("password") String password);
}
