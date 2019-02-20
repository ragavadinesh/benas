
 

package io.github.benas.todolist.web.common.tags;

import io.github.benas.todolist.web.common.util.TodoListUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * Utility tag to transcode todo status to label value.
 *
 * 
 */

public class StatusLabelTag extends SimpleTagSupport {

    /**
     * The todo status.
     */
    private boolean status;

    @Override
    public void doTag() throws JspException, IOException {

        JspWriter out = getJspContext().getOut();
        String statusLabel = TodoListUtils.getStatusLabel(status);
        out.print(statusLabel);

    }

    /*
     * setters for tag attributes
     */

    public void setStatus(boolean status) {
        this.status = status;
    }

}
