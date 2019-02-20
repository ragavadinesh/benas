
 

package io.github.benas.todolist.web.servlet.user;

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

import static io.github.benas.todolist.web.util.Views.HOME_PAGE;

/**
 * Servlet that controls action on the user's home page.
 *
 * 
 */

@WebServlet(name = "HomeServlet", urlPatterns = "/todos")
public class HomeServlet extends HttpServlet {

    private TodoService todoService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
        todoService = applicationContext.getBean(TodoService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(TodoListUtils.SESSION_USER);
        List<Todo> todoList = todoService.getTodoListByUser(user.getId());

        //todo list is request scoped to avoid storing and synchronizing it in session for each CRUD operation
        request.setAttribute("todoList", todoList);
        request.setAttribute("homeTabStyle", "active");

        int totalCount = todoList.size();
        int doneCount = TodoListUtils.countTotalDone(todoList);
        int todoCount = totalCount - doneCount;
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("doneCount", doneCount);
        request.setAttribute("todoCount", todoCount);

        request.getRequestDispatcher(HOME_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
