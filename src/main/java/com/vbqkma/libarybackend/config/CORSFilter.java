package com.vbqkma.libarybackend.config;

import com.vbqkma.libarybackend.utils.AppUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final String APP_NAME = "VUBAQUANG-LIBRARY-KMA";

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
        if (!AppUtils.requestValidation(request)) {
            logger.info("Request denied !!!");
            response.setStatus(401);
            return;
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
        Date startTime = new Date();
        Date endTime = new Date();
        Long duration = Long.valueOf(endTime.getTime() - startTime.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss:SSS");
        String startTimeStr = dateFormat.format(startTime);
        String ipRemote = req.getRemoteAddr();
        String uri = req.getRequestURI().replace(req.getContextPath(), "");
        String logPath = req.getServerName() + ":" + req.getServerPort();
        String linkFull = req.getRequestURI();
        String logStart = MessageFormat.format("{0}|{1}|{2}|{3}|{4}|{5}|{6}|{7}|{8}|{9}|{10}",
                new Object[]{"start_action", APP_NAME, startTimeStr, ipRemote, logPath, uri,
                        getAllParameter(req), linkFull, duration, ""});
        logger.info(logStart);
    }

    private String getAllParameter(HttpServletRequest req) {
        StringBuilder params = new StringBuilder();
        try {
            Enumeration<String> parameterNames = req.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                params.append(paramName).append(":");
                String[] paramValues = req.getParameterValues(paramName);
                for (String paramValue : paramValues) {
                    params.append(paramValue).append(";");
                }
            }
        } catch (Exception ex) {
            logger.error("loi xay ra:", ex);
        }
        return params.toString();
    }
}
