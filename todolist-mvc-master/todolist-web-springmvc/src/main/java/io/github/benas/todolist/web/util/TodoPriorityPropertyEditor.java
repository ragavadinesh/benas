

package io.github.benas.todolist.web.util;

import io.github.todolist.core.domain.Priority;

import java.beans.PropertyEditorSupport;

/**
 * A custom property editor to map {@link Priority} values.
 *
 * 
 */
public class TodoPriorityPropertyEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        Priority value = (Priority) getValue();
        return value.toString();
    }

    @Override
    public void setAsText(String text) {
        setValue(Priority.valueOf(text));
    }
}
