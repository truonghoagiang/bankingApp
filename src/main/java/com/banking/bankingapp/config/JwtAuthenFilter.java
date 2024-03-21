package com.banking.bankingapp.config;

import com.banking.bankingapp.service.UserDetailServiceImp;
import com.banking.bankingapp.service.UserDetailsImp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@Service
@Slf4j
public class JwtAuthenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailServiceImp userDetailServiceImp;

    @Autowired
    Gson gson = new Gson();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = jwtparse(request);

        try{
            if(jwtToken != null && jwtTokenProvider.validateToken(jwtToken)) {
                String data = jwtTokenProvider.decodeToken(jwtToken);
                String username = jwtTokenProvider.getUsername(data);
                Type listType = new TypeToken<List<SimpleGrantedAuthority>>() {}.getType();
                List<GrantedAuthority> listRoles = gson.fromJson(data,listType);
                UserDetails userDetails = userDetailServiceImp.loadUserByUsername(username);
                logger.info("Check userdetails:" + userDetails);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, listRoles);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception e){
            logger.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request,response);
    }

    public String jwtparse(HttpServletRequest request){
        String headerAuthen = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuthen) && headerAuthen.startsWith("Bearer")) {
            return headerAuthen.substring(7);
        }
        return null;
    }


}
