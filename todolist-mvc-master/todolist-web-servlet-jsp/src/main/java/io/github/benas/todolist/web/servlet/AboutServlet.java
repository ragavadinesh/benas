
 

package io.github.benas.todolist.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.github.benas.todolist.web.util.Views.ABOUT_PAGE;

/**
 * Servlet that controls the "about" page.
 *
 * 
 */

@WebServlet(name = "AboutServlet", urlPatterns = "/about")
public class AboutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("aboutTabStyle", "active");
        request.getRequestDispatcher(ABOUT_PAGE).forward(request, response);
    }

}
