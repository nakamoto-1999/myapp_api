package com.example.admin.security;

import com.example.admin.request.UserAuthForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;


public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    public MyAuthenticationFilter(AuthenticationManager authenticationManager , PasswordEncoder passwordEncoder) {

        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        //問題の箇所
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login" , "POST"));

        this.setUsernameParameter("email");
        this.setPasswordParameter("password");

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try{
            ObjectMapper mapper = new ObjectMapper();
            UserAuthForm form = mapper.readValue(request.getInputStream() , UserAuthForm.class);
            return this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getEmail() , form.getPassword() , new ArrayList<>())
            );
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    //認証成功時の処理
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {


        MyUserDetails userDetails =
               authResult.getPrincipal() instanceof MyUserDetails ?
                       (MyUserDetails)authResult.getPrincipal() : null ;

        //主キーからJWTを生成
        //トークンの期限（秒）
        if(userDetails == null)return;
        Long duraSec = 60 * 60 * 24L ;
        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.generateToken(
                userDetails.getUserId().toString(),
                duraSec
        );

        ResponseCookie cookie = ResponseCookie
                .from("Authorization" , "Bearer-" + jwt)
                .domain("localhost")
                .maxAge(duraSec)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();
        response.addHeader("Set-Cookie" , cookie.toString());

    }
}
