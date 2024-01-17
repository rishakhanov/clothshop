package com.example.clothshop.util.jwt;

import com.example.clothshop.entity.Person;
import com.example.clothshop.entity.Roles;
import com.example.clothshop.repository.PersonRepository;
import com.example.clothshop.service.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import static io.jsonwebtoken.Jwts.parser;

@Component
public class JwtUtils {

    private static final Logger logger  = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${clothshop.app.jwtSecret}")
    private String jwtSecret;

    @Value("${clothshop.app.jwtExpirationMs}")
    private int jwtExpirationMs;


    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .claim("id", userPrincipal.getId())
                .claim("email", userPrincipal.getEmail())
                //.claim("password", userPrincipal.getPassword())//убрать
                .claim("roles", userPrincipal.getAuthorities())
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return parser().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Integer getUserIdFromJwtToken(String token) {
        return (Integer) parser().setSigningKey(key()).build().parseClaimsJws(token).getBody().get("id");
    }

    public String getUserEmailFromJwtToken(String token) {
        return (String) parser().setSigningKey(key()).build().parseClaimsJws(token).getBody().get("email");
    }

    public String getUserPasswordFromJwtToken(String token) {
        return (String) parser().setSigningKey(key()).build().parseClaimsJws(token).getBody().get("password");
    }

    public List<GrantedAuthority> getUserAuthoritiesFromJwtToken(String token) {

        ArrayList<LinkedHashMap<String, String>> rolesArrayList = (ArrayList<LinkedHashMap<String, String>>) Jwts
                .parser()
                .setSigningKey(key()).build().parseClaimsJws(token).getBody().get("roles");

        List<String> rolesList = new ArrayList<>();

        for (LinkedHashMap<String, String> item: rolesArrayList) {
            rolesList.add(item.get("authority"));
        }

        List<GrantedAuthority> authorities = rolesList.stream()
                .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());

        return authorities;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            parser().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }


}
