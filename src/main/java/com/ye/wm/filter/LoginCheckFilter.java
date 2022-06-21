package com.ye.wm.filter;

import com.alibaba.fastjson.JSON;
import com.ye.wm.common.BaseContext;
import com.ye.wm.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登录
 */

@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {



        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        log.info("拦截到请求：{}",request.getRequestURI());

        String[] urls = new  String[]{
          "/employee/login",
          "/employee/logout",
          "/backend/**",
          "/front/**",
          "/user/sendMsg",
          "/user/login"
        };


        boolean check = check(urls, requestURI);

        if (check){
            filterChain.doFilter(request,response);
            return;
        }

        if (request.getSession().getAttribute("employee") != null){

            Long employee = (Long) request.getSession().getAttribute("employee");

            BaseContext.setCurrentId(employee);

            filterChain.doFilter(request,response);
            return;
        }

        if (request.getSession().getAttribute("user") != null){

            Long employee = (Long) request.getSession().getAttribute("user");

            BaseContext.setCurrentId(employee);

            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        return;



    }

    public boolean check(String[] urls,String requestURI){
        for (String uri:urls) {
            boolean match = PATH_MATCHER.match(uri, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
