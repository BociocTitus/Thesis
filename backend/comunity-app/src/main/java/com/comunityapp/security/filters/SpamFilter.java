package com.comunityapp.security.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

public class SpamFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(SpamFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if(session == null){
            chain.doFilter(request, response);
        } else {
            LOGGER.info("Spam");
        }

    }

    @Override
    public void destroy() {}
}
