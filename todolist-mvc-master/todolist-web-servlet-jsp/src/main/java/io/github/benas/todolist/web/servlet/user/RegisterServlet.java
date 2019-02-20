
 
package io.github.benas.todolist.web.servlet.user;

import io.github.benas.todolist.web.common.form.RegistrationForm;
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
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Set;

import static io.github.benas.todolist.web.util.Views.REGISTER_PAGE;

/**
 * Servlet that controls the registration process.
 *
 * 
 */

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register", "/register.do"})
public class RegisterServlet extends HttpServlet {

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
        request.setAttribute("registerTabStyle", "active");
        request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmationPassword = request.getParameter("confirmationPassword");


        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setName(name);
        registrationForm.setEmail(email);
        registrationForm.setPassword(password);
        registrationForm.setConfirmationPassword(confirmationPassword);

        String nextPage = REGISTER_PAGE;

        validateRegistrationForm(request, registrationForm);

        checkPasswordsMatch(request, password, confirmationPassword);

        if (isInvalid(request)) {
            request.getRequestDispatcher(nextPage).forward(request, response);
            return;
        }

        if (isAlreadyUsed(email)) {
            request.setAttribute("error", MessageFormat.format(resourceBundle.getString("register.error.global.account"), email));
            request.getRequestDispatcher(nextPage).forward(request, response);
            return;
        }

        User user = new User(name, email, password);
        user = userService.create(user);
        HttpSession session = request.getSession(true);
        session.setAttribute(TodoListUtils.SESSION_USER, user);
        request.getRequestDispatcher("/todos").forward(request, response);

    }

    private boolean isAlreadyUsed(String email) {
        return userService.getUserByEmail(email) != null;
    }

    private void validateRegistrationForm(HttpServletRequest request, RegistrationForm registrationForm) {
        validateName(request, registrationForm);

        validateEmail(request, registrationForm);

        validatePassword(request, registrationForm);

        validateConfirmationPassword(request, registrationForm);
    }

    private boolean isInvalid(HttpServletRequest request) {
        return request.getAttribute("error") != null;
    }

    private void checkPasswordsMatch(HttpServletRequest request, String password, String confirmationPassword) {
        if (!confirmationPassword.equals(password)) {
            request.setAttribute("errorConfirmationPasswordMatching", resourceBundle.getString("register.error.password.confirmation.error"));
            addGlobalRegistrationErrorAttribute(request);
        }
    }

    private void validateConfirmationPassword(HttpServletRequest request, RegistrationForm registrationForm) {
        Set<ConstraintViolation<RegistrationForm>> constraintViolations
                = validator.validateProperty(registrationForm, "confirmationPassword");
        if (!constraintViolations.isEmpty()) {
            request.setAttribute("errorConfirmationPassword", constraintViolations.iterator().next().getMessage());
            addGlobalRegistrationErrorAttribute(request);
        }
    }

    private void validatePassword(HttpServletRequest request, RegistrationForm registrationForm) {
        Set<ConstraintViolation<RegistrationForm>> constraintViolations
                = validator.validateProperty(registrationForm, "password");
        if (!constraintViolations.isEmpty()) {
            request.setAttribute("errorPassword", constraintViolations.iterator().next().getMessage());
            addGlobalRegistrationErrorAttribute(request);
        }
    }

    private void validateEmail(HttpServletRequest request, RegistrationForm registrationForm) {
        Set<ConstraintViolation<RegistrationForm>> constraintViolations
                = validator.validateProperty(registrationForm, "email");
        if (!constraintViolations.isEmpty()) {
            request.setAttribute("errorEmail", constraintViolations.iterator().next().getMessage());
            addGlobalRegistrationErrorAttribute(request);
        }
    }

    private void validateName(HttpServletRequest request, RegistrationForm registrationForm) {
        Set<ConstraintViolation<RegistrationForm>> constraintViolations
                = validator.validateProperty(registrationForm, "name");
        if (!constraintViolations.isEmpty()) {
            request.setAttribute("errorName", constraintViolations.iterator().next().getMessage());
            addGlobalRegistrationErrorAttribute(request);
        }
    }

    private void addGlobalRegistrationErrorAttribute(HttpServletRequest request) {
        request.setAttribute("error", resourceBundle.getString("register.error.global"));
    }

}
