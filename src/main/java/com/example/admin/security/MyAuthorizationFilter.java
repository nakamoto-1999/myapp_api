package com.example.admin.security;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class MyAuthorizationFilter extends BasicAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private MyUserDetailsService userDetailsService;

    public MyAuthorizationFilter(AuthenticationManager authenticationManager , MyUserDetailsService userDetailsService){
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String authToken = null;

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            System.out.println(cookies);
            for (Cookie c : cookies) {
                if (c.getName().equals("Authorization")) {
                    authToken = c.getValue();
                }
            }
        }

        if(authToken == null || !authToken.startsWith("Bearer-")){
            chain.doFilter(request,response);
            return;
        }
        //トークンから認証を取得
        Authentication authentication = this.getAuthentication(authToken);

        //認可処理
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request , response);

    }

    private Authentication getAuthentication(String authToken){

        if(authToken == null) {return null;}

        JwtUtil jwtUtil = new JwtUtil();

        //トークンが期限切れならばnullを返す
        final String jwt = authToken.replace("Bearer-" , "");
        if(jwtUtil.isTokenExpired(jwt)){return null;}

        //トークンによってユーザーが見つからなければnullを返す
        UserDetails userDetails = null;
        Long userId = Long.parseLong(jwtUtil.extractSubject(jwt));
        userDetails = this.userDetailsService.loadUserByUserId(userId);
        //System.out.println(userDetails.getUsername());
        if(userDetails == null){return null;}

        return new UsernamePasswordAuthenticationToken(userDetails , null , userDetails.getAuthorities());
    }


}
