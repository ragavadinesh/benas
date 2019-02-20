

package io.github.benas.todolist.web.common.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility tag to highlight text patterns with css style.
 *
 * 
 */

public class HighlightTag extends SimpleTagSupport {

    /**
     * The pattern to highlight.
     */
    private String pattern;

    /**
     * the css class to apply for highlighting patterns.
     */
    private String cssClass;

    /**
     * Case sensitivity parameter for pattern search/replace.
     */
    private boolean caseSensitive;

    @Override
    public void doTag() throws JspException, IOException {

        JspWriter out = getJspContext().getOut();
        StringWriter stringWriter = new StringWriter();
        getJspBody().invoke(stringWriter);
        String highlightedValue = doHighlight(stringWriter.toString());
        out.print(highlightedValue);

    }

    /**
     * Apply a search/replace of the pattern in the input text.
     *
     * @param input text to which apply the style for each pattern matched
     * @return the transformed text
     */
    private String doHighlight(final String input) {

        String startSpanTag = "<span class=\"" + cssClass + "\">";
        String endSpanTag = "</span>";

        StringBuilder stringBuilder = new StringBuilder(startSpanTag);
        stringBuilder.append(pattern);
        stringBuilder.append(endSpanTag);

        Pattern pattern;

        if (caseSensitive) {
            pattern = Pattern.compile(this.pattern);
        } else {
            pattern = Pattern.compile(this.pattern, Pattern.CASE_INSENSITIVE);
        }
        Matcher matcher = pattern.matcher(input);

        return matcher.replaceAll(stringBuilder.toString());

    }

    /*
     * setters for tag attributes
     */

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}
