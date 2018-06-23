package com.comunityapp.security.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CityAppCORSFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public void destroy() {

        // unimplemented method

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "PUT, DELETE, POST, GET, HEAD, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Authorization, Access-Control-Request-Headers");
        if ("options".equalsIgnoreCase(request.getMethod())) {
            return;
        }
        logger.info("Cors passed");
        filterChain.doFilter(req, res);

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

        // unimplemented method
    }

}
