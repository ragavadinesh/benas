
 

package io.github.benas.todolist.web.interceptors;

import io.github.benas.todolist.web.util.SessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor to ensure that access to user data is allowed only for logged users.
 *
 *
 */
public class SessionHandlerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SessionData sessionData;


    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (sessionData.getUser() == null) {
            response.sendRedirect("/login");
            return false;
        } else {
            return true;
        }
    }
}
