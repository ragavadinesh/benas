
package io.github.benas.todolist.web.util;

import io.github.todolist.core.domain.User;

import java.io.Serializable;
import java.util.Locale;

/**
 * Bean holding session data.
 *
 * 
 */
public class SessionData implements Serializable {

    /**
     * The logged user
     */
    private User user;

    /**
     * The session Locale
     */
    private Locale locale;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
