package com.vbqkma.libarybackend.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

@Component
public class CORSFilter implements Filter {
  public static final Logger logger = LogManager.getLogger(CORSFilter.class);

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = null;
    HttpServletResponse response = null;
    if ((req instanceof HttpServletRequest)) {
      request = (HttpServletRequest) req;
    }

    if ((res instanceof HttpServletResponse)) {
      response = (HttpServletResponse) res;
    }

    if (response != null) {
      response.setHeader("Access-Control-Allow-Origin", "*");
      response.setHeader("Access-Control-Allow-Methods", "*");
      response.setHeader("Access-Control-Allow-Credentials", "true");
      response.setHeader("Access-Control-Allow-Headers", "*");
      response.setHeader("Access-Control-Max-Age", "86400");
      if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
        response.setStatus(HttpServletResponse.SC_OK);
      } else {
        doFilter(request, response, chain);
      }
    }
  }

  @Override
  public void init(FilterConfig filterConfig) {
  }

  @Override
  public void destroy() {
  }

  private void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
    throws IOException, ServletException {
    res.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
    res.setHeader("Access-Control-Allow-Credentials", "true");
    chain.doFilter(req, res);
  }

}
