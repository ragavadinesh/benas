


package io.github.benas.todolist.web.servlet.user;

import io.github.benas.todolist.web.common.form.LoginForm;
import io.github.benas.todolist.web.common.util.TodoListUtils;
import io.github.todolist.core.domain.User;
import io.github.todolist.core.service.api.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Set;

import static io.github.benas.todolist.web.util.Views.LOGIN_PAGE;

/**
 * Servlet that controls the login process.
 *
 * Get requests to "/login" redirects to login page.
 * Post requests to "/login.do" processes user login.
 *
 * 
 */

@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/login.do"})
public class LoginServlet extends HttpServlet {

    private UserService userService;

    private ResourceBundle resourceBundle;

    private Validator validator;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //initialize Spring user service
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
        userService = applicationContext.getBean(UserService.class);

        //initialize JSR 303 validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        resourceBundle = ResourceBundle.getBundle("todolist");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("loginTabStyle", "active");
        request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        LoginForm loginForm = new LoginForm();
        loginForm.setEmail(email);
        loginForm.setPassword(password);

        String nextPage = LOGIN_PAGE;

        validateCredentials(request, loginForm);

        if (isInvalid(request)) {
            request.getRequestDispatcher(nextPage).forward(request, response);
            return;
        }

        if (isInvalidCombination(email, password)) {
            request.setAttribute("error", resourceBundle.getString("login.error.global.invalid"));
        } else {
            HttpSession session = request.getSession(true);//create session
            User user = userService.getUserByEmail(email);
            session.setAttribute(TodoListUtils.SESSION_USER, user);
            nextPage = "/todos";
        }
        request.getRequestDispatcher(nextPage).forward(request, response);
    }

    private void validateCredentials(HttpServletRequest request, LoginForm loginForm) {
        validateEmail(request, loginForm);

        validatePassword(request, loginForm);
    }

    private boolean isInvalidCombination(String email, String password) {
        return !userService.login(email, password);
    }

    private void validatePassword(HttpServletRequest request, LoginForm loginForm) {
        Set<ConstraintViolation<LoginForm>> constraintViolations = validator.validateProperty(loginForm, "password");
        if (!constraintViolations.isEmpty()) {
            request.setAttribute("errorPassword", constraintViolations.iterator().next().getMessage());
            addGlobalLoginErrorAttribute(request);
        }
    }

    private void validateEmail(HttpServletRequest request, LoginForm loginForm) {
        Set<ConstraintViolation<LoginForm>> constraintViolations = validator.validateProperty(loginForm, "email");
        if (!constraintViolations.isEmpty()) {
            request.setAttribute("errorEmail", constraintViolations.iterator().next().getMessage());
            addGlobalLoginErrorAttribute(request);
        }
    }

    private void addGlobalLoginErrorAttribute(HttpServletRequest request) {
        request.setAttribute("error", resourceBundle.getString("login.error.global"));
    }

    private boolean isInvalid(HttpServletRequest request) {
        return request.getAttribute("error") != null;
    }

}
