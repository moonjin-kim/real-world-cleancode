package com.moonjin.realworld.common.config;

import com.moonjin.realworld.common.annotation.AuthRequired;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.common.exception.Unauthorized;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod method) {
            if(isNotAuthRequired(method)) {
                return true;
            }

            HttpSession session = request.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("user") : null;

            if (user == null) {
                throw new Unauthorized();
            }
        }
        return true;
    }

    private boolean isNotAuthRequired(HandlerMethod method) {
        boolean hasAnnotation =
                method.getMethodAnnotation(AuthRequired.class) != null ||
                        method.getBeanType().getAnnotation(AuthRequired.class) != null;

        return !hasAnnotation;
    }
}
