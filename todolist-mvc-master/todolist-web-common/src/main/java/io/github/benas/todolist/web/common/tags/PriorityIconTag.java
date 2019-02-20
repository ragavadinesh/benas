

package io.github.benas.todolist.web.common.tags;

import io.github.benas.todolist.web.common.util.TodoListUtils;
import io.github.todolist.core.domain.Priority;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * Utility tag to transcode todo priority to icon value.
 *
 * 
 */

public class PriorityIconTag extends SimpleTagSupport {

    /**
     * The todo's priority.
     */
    private String priority;

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        String priorityIcon = TodoListUtils.getPriorityIcon(Priority.valueOf(priority));
        out.print(priorityIcon);
    }

    /*
     * setters for tag attributes
     */

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
