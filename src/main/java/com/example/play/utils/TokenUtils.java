package com.example.play.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.play.enity.User;
import java.util.Date;

public class TokenUtils {

    public static String getToken(User user) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 1000;//一小时有效时间
        Date end = new Date(currentTime);
        String token = "";

        token = JWT.create().withAudience(String.valueOf(user.getId())).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
