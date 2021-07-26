package com.xl.study.studytest.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xl.study.studytest.common.exception.TokenException;
import com.xl.study.studytest.domin.po.TokenToUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtTokenUtil {
    public static final String secret = "aabbcc";
    //过期时间:秒
    public static final int EXPIRE = 5;

    /**
     * MD5 2次加密
     */
    public static String MD5(String secret,String password) {
        //密码MD5加盐加密
        String hashAlgorithmName = "MD5";
        Object salt = ByteSource.Util.bytes(secret);
        Object result = new SimpleHash(hashAlgorithmName, password, salt, 2);
        return result.toString();
    }
    /**
     * 获取请求的token
     */
    public static String getRequestToken(HttpServletRequest httpRequest) {

        //从header中获取token
        String token = httpRequest.getHeader("Authorization");
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = httpRequest.getParameter("Authorization");
        }
        return token;
    }

    /**
     * 生成Token
     */
    public static String createToken(String userId, String userName,String password) {
        String token="";
        try{
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        token = JWT.create()
                .withHeader(map)//头
                .withClaim("userId", userId)
                .withClaim("userName", userName)
                .withClaim("pwd", password)
                .withSubject("政策打标系统")//
                .withIssuedAt(new Date())//签名时间
//                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))//过期时间
                .sign(Algorithm.HMAC256(secret));//签名
             }catch (Exception e){
            e.printStackTrace();
            throw new TokenException("产生token失败");
        }
        return token;
    }

    /**
     * 验证Token
     */
    public static TokenToUser verifyToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        }catch (Exception e){
            throw new RuntimeException("凭证已过期，请重新登录");
        }
        Map<String, Claim> claims = jwt.getClaims();
        String userId = claims.get("userId").toString().replace("\"","");
        String userName = claims.get("userName").toString().replace("\"","");
        String password = claims.get("pwd").toString().replace("\"","");
        TokenToUser user = new TokenToUser();
        user.setUserId(Integer.parseInt(userId));
        user.setUserName(userName);
        user.setPassword(password);
        return user;
    }

    /**
     * 解析Token
     */
    public static TokenToUser parseToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (token == null){
            throw new RuntimeException("传入token为null");
        }
        DecodedJWT decodedJWT = JWT.decode(token);
        Map<String, Claim> claims = decodedJWT.getClaims();

        String userId = claims.get("userId").toString().replace("\"","");
        String userName = claims.get("userName").toString().replace("\"","");

        TokenToUser user = new TokenToUser();
        user.setUserId(Integer.parseInt(userId));
        user.setUserName(userName);
        return user;
    }

    /**
     * 解析Token
     */
    public static TokenToUser parseToken(String token){
        if (token == null){
            throw new RuntimeException("传入token为null");
        }
        DecodedJWT decodedJWT = JWT.decode(token);
        Map<String, Claim> claims = decodedJWT.getClaims();

        String userId = claims.get("userId").toString().replace("\"","");
        String userName = claims.get("userName").toString().replace("\"","");

        TokenToUser user = new TokenToUser();
        user.setUserId(Integer.parseInt(userId));
        user.setUserName(userName);
        return user;
    }
}
