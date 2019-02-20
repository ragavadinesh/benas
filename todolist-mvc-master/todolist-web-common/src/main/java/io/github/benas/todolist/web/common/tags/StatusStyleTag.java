

package io.github.benas.todolist.web.common.tags;

import io.github.benas.todolist.web.common.util.TodoListUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * Utility tag to transcode todo status to css style value.
 *
 * 
 */

public class StatusStyleTag extends SimpleTagSupport {

    /**
     * The todo status.
     */
    private boolean status;

    @Override
    public void doTag() throws JspException, IOException {

        JspWriter out = getJspContext().getOut();
        String statusStyle = TodoListUtils.getStatusStyle(status);
        out.print(statusStyle);

    }

    /*
     * setters for tag attributes
     */

    public void setStatus(boolean status) {
        this.status = status;
    }

}
