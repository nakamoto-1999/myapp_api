package com.example.admin.controller;

import com.example.admin.request.AdministratorAuthenticationRequest;
import com.example.admin.request.AdministratorCreateUpdateRequest;
import com.example.admin.response.AdministratorAuthenticationResponse;
import com.example.admin.response.AdministratorResponse;
import com.example.admin.security.JwtManager;
import com.example.admin.service.AdministratiorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
public class AdministratorController {

    @Autowired
    AdministratiorService administratiorService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtManager jwtManager;

    @PostMapping("/administrator/create")
    public ResponseEntity<?> create(@Validated @RequestBody AdministratorCreateUpdateRequest request){
        administratiorService.create(request.getName() , request.getPassword());
        return ResponseEntity.ok(null);
    }

    @GetMapping("/administrator/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(
                administratiorService.getAllResponse()
        );
    }

    @GetMapping("/administrator/{adminId}")
    public ResponseEntity<?> getByAdminId(@PathVariable Integer adminId){
        return ResponseEntity.ok(
                administratiorService.getResponseByAdminId(adminId)
        );
    }

    @PutMapping("/administrator/update")
    public ResponseEntity<?> update(HttpServletRequest request, @RequestBody AdministratorCreateUpdateRequest requestBody){

        String jwtToken = jwtManager.extractTokenFromRequest(request);
        Integer adminId = null;
        if(jwtToken != null){
            adminId = Integer.parseInt(jwtManager.extractSubject(jwtToken));
        }
        if(adminId != null) {
            administratiorService.update(adminId, requestBody.getName(), requestBody.getPassword());
        }
        return ResponseEntity.ok(null);

    }

    @DeleteMapping("/administrator/delete")
    public ResponseEntity<?> delete(HttpServletRequest request){

        String jwtToken = jwtManager.extractTokenFromRequest(request);
        Integer adminId = null;
        if(jwtToken != null){
            adminId = Integer.parseInt(jwtManager.extractSubject(jwtToken));
        }
        if(adminId != null) {
            administratiorService.delete(adminId);
        }
        return ResponseEntity.ok(null);

    }

    @PostMapping("/administrator/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AdministratorAuthenticationRequest request){

        Authentication auth = new UsernamePasswordAuthenticationToken(request.getName() , request.getPassword());
        authenticationManager.authenticate(auth);

        AdministratorResponse administratorResponse = administratiorService.getResponseByName(auth.getName());
        //主キーからJwtを生成する
        String jwtToken = jwtManager.generateToken(administratorResponse.getAdminId().toString());

        return ResponseEntity.ok(
                new AdministratorAuthenticationResponse(jwtToken, administratorResponse)
        );

    }

}
