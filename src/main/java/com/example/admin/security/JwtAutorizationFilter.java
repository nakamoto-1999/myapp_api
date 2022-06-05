package com.example.admin.security;

import com.example.admin.entity.Administrator;
import com.example.admin.service.AdministratiorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAutorizationFilter extends OncePerRequestFilter {

    @Autowired
    JwtManager jwtManager;

    @Autowired
    UserDetailsService detailsService;

    @Autowired
    AdministratiorService administratiorService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //ヘッダーからJWTを抽出する
        final String authorizationHeader = request.getHeader("Authorization");
        String authToken = jwtManager.extractTokenFromRequest(request);
        String name = null;

        //JWTを解析して主キーを抽出し、それを元に管理者エンティティを取得する
        //取得したエンティティの持つnameの値を取得する
        if(authToken != null) {
            Integer adminId = Integer.parseInt(jwtManager.extractSubject(authToken));
            name = administratiorService.getEntityByAdminId(adminId).getName();
        }

        //nameからDtailsを取得し、jwt
        if(name != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails details = this.detailsService.loadUserByUsername(name);
            if(!jwtManager.isTokenExpired(authToken)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        details , null , details.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request , response);

    }
}
