package pl.mslawin.notes.interceptor;

import static pl.mslawin.notes.constants.NotesConstants.USER_PARAM;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import pl.mslawin.notes.annotation.RequiresAuthentication;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            if (method.isAnnotationPresent(RequiresAuthentication.class)
                    || method.getDeclaringClass().isAnnotationPresent(RequiresAuthentication.class)) {
                if (request.getSession().getAttribute(USER_PARAM) == null) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value());
                    return false;
                }
            }
        }
        return true;
    }
}