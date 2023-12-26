package com.mlc.controller;

import com.mlc.entity.*;
import com.mlc.exception.MlcException;
import com.mlc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/token")
    public ResponseEntity<String> getAccessTocken(@RequestParam("userName") String userName, @RequestParam String password){
        String reponse = userService.getAccessToken(userName,password);
        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }
    @PostMapping("/user/create")
    public ResponseEntity<Response> createUser(@RequestBody UserMaster userMaster) throws MlcException {
        String token = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null ) {
            Jwt jwt =(Jwt) authentication.getPrincipal();
            token = jwt.getTokenValue();
        }
        Response reponse = userService.createUser(userMaster, token);
        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }
    @PostMapping("/role/create")
    public ResponseEntity<Response> createRole(@RequestBody Roles roles){
        String token = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null ) {
            Jwt jwt =(Jwt) authentication.getPrincipal();
            token = jwt.getTokenValue();
        }
        Response response = userService.createRole(roles, token);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/user/view/all")
    @PreAuthorize("hasRole('realm_admin')")
    public ResponseEntity<List<UserMaster>> viewAllUser( ){
        return userService.viewAllUser();
    }

    @GetMapping("/hello")
    public  ResponseEntity<String> getKeycloakToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null ) {
            Jwt jwt =(Jwt) authentication.getPrincipal();
            String token = jwt.getTokenValue();
            return new ResponseEntity<>(token,HttpStatus.OK);
        }
        return new ResponseEntity<>("",HttpStatus.NOT_FOUND);
    }
    @PostMapping("/user/role/mapping")
    public ResponseEntity<Response> assignRoleToUser(@RequestBody UserRoles userRoles){
        String token = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null ) {
            Jwt jwt =(Jwt) authentication.getPrincipal();
            token = jwt.getTokenValue();
        }
        Response response = userService.assignRoleToUser(userRoles, token);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/user/password/reset")
    public ResponseEntity<Response> resetUserPassword(@RequestBody UserMaster userMaster){
        String token = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null ) {
            Jwt jwt =(Jwt) authentication.getPrincipal();
            token = jwt.getTokenValue();
        }
        Response response = userService.resetUserPassword(userMaster, token);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PostMapping("/realm/create")
    public ResponseEntity<Response> createRealm(@RequestBody Realm realm){
        String token = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null ) {
            Jwt jwt =(Jwt) authentication.getPrincipal();
            token = jwt.getTokenValue();
        }
        Response response = userService.createRealm(realm, token);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/client/create")
    public ResponseEntity<Response> createClient(@RequestBody Client client){
        String token = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null ) {
            Jwt jwt =(Jwt) authentication.getPrincipal();
            token = jwt.getTokenValue();
        }
        Response response = userService.createClient(client, token);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
