
 
package io.github.benas.todolist.web.servlet.todo;

import io.github.benas.todolist.web.common.util.TodoListUtils;
import io.github.todolist.core.domain.Todo;
import io.github.todolist.core.domain.User;
import io.github.todolist.core.service.api.TodoService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static io.github.benas.todolist.web.util.Views.SEARCH_PAGE;

/**
 * Servlet that controls todo list search.
 *
 * 
 */

@WebServlet(name = "SearchServlet", urlPatterns = "/todos/search")
public class SearchServlet extends HttpServlet {

    private TodoService todoService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
        todoService = applicationContext.getBean(TodoService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String title = request.getParameter("title");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(TodoListUtils.SESSION_USER);

        List<Todo> todoList = todoService.searchTodoListByTitle(user.getId(), title);
        request.setAttribute("todoList", todoList);

        request.getRequestDispatcher(SEARCH_PAGE).forward(request, response);

    }

}
