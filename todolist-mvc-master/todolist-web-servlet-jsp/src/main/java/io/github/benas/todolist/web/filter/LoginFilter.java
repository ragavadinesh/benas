

package io.github.benas.todolist.web.filter;

import io.github.benas.todolist.web.common.util.TodoListUtils;
import io.github.todolist.core.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static io.github.benas.todolist.web.util.Views.LOGIN_PAGE;

/**
 * Filter to ensure that access to private resources is allowed only to logged users.
 *
 
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = {"/user/*", "/todos/*"})
public class LoginFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(TodoListUtils.SESSION_USER);
        if (user != null) {
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
        }
    }


    public void destroy() {
    }

}
