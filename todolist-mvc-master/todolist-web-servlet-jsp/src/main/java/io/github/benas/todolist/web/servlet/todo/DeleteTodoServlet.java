
 

package io.github.benas.todolist.web.servlet.todo;

import io.github.todolist.core.domain.Todo;
import io.github.todolist.core.service.api.TodoService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import static io.github.benas.todolist.web.util.Views.ERROR_PAGE;

/**
 * Servlet that controls todo deletion.
 *
 * 
 */

@WebServlet(name = "DeleteTodoServlet", urlPatterns = "/todos/delete.do")
public class DeleteTodoServlet extends HttpServlet {

    private TodoService todoService;

    private ResourceBundle resourceBundle;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
        todoService = applicationContext.getBean(TodoService.class);
        resourceBundle = ResourceBundle.getBundle("todolist");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String id = request.getParameter("todoId");
        try {
            long todoId = Long.parseLong(id);
            Todo todo = todoService.getTodoById(todoId);
            if (todo != null) {
                todoService.remove(todo);
                request.getRequestDispatcher("/todos").forward(request, response);
            } else {
                redirectToErrorPage(request, response, id);
            }
        } catch (NumberFormatException e) {
            redirectToErrorPage(request, response, id);
        }
    }

    private void redirectToErrorPage(HttpServletRequest request, HttpServletResponse response, String todoId) throws ServletException, IOException {
        request.setAttribute("error", MessageFormat.format(resourceBundle.getString("no.such.todo"), todoId));
        request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
    }

}
