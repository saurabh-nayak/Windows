package com.saurabh.operation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.saurabh.POJO.MyUserDetails;
import com.saurabh.POJO.UserDetail;
import com.saurabh.service.UserDetailService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javassist.NotFoundException;


@Service
public class JWTGenerator {
	@Autowired
	UserDetailService userDetailService;
	
	   private String SECRET_KEY = "secret";

	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }
	    private Claims extractAllClaims(String token) {
	        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	    }

	    private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }

	    public String generateToken(MyUserDetails myUserDetails) {
	        Map<String, Object> claims = new HashMap<>();
	        int id=myUserDetails.getId();
//	        UserDetail userDetail;
			try {
				Optional<UserDetail> optionalUserDetail = userDetailService.searchById(id);
				optionalUserDetail.orElseThrow(() -> new NotFoundException("not found any user"));
				UserDetail userDetail=optionalUserDetail.get();
		        claims.put("address",userDetail.getAddress());
		        claims.put("id",id);
		        claims.put("mobile",userDetail.getMobile());
			} catch(Exception e)
			{}
	
	        return createToken(claims, myUserDetails.getUsername());
	    }

	    private String createToken(Map<String, Object> claims, String subject) {

	        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1))
	                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	    }

	    public Boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }
}
