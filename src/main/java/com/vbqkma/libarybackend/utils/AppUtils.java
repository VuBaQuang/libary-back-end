package com.vbqkma.libarybackend.utils;

import com.vbqkma.libarybackend.LibaryBackEndApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class AppUtils {
  private static final Logger logger = LogManager.getLogger(AppUtils.class);
  private static final List<String> ALLOW_LIST = Arrays.asList(
    "/auth/login",
    "/favicon.ico"
  );

  public static String getToken(HttpServletRequest request) {
    String auth = request.getHeader("Authorization");
    if (auth == null) {
      return "";
    }
    String sessionToken = "";
    if (auth.split(" ").length > 1) {
      sessionToken = auth.split(" ")[1];
    }
    return sessionToken;
  }

  public static Boolean requestValidation(HttpServletRequest request) {
    if (request == null) {
      return false;
    }


    String uri = request.getRequestURI();
    if (!StringUtils.isEmpty(uri)) {

      boolean match = ALLOW_LIST.stream().anyMatch(s -> uri.contains(s));
      if (match) {
        return true;
      }
      String auth = request.getHeader("Authorization");

      if (StringUtils.isEmpty(auth)) {
        logger.info("requestValidation: Authorization not defined (" + uri + ")");
        return false;
      }
      String sessionToken = "";
      if (auth.split(" ").length > 1) {
        sessionToken = auth.replace("Basic ", "");
      }
      if (StringUtils.isEmpty(sessionToken)) {
        logger.info("Validate token --> token empty");
        return false;
      }
      ValueOperations opsVal = LibaryBackEndApplication.ctx.getBean(RedisTemplate.class).opsForValue();
      if (opsVal.get(sessionToken) != null) {
        return false;
      }
    }
    return false;
  }
}
