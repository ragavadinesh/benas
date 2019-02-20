

package io.github.benas.todolist.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for Index/About pages.
 *
 * 
 */
@Controller
public class HomeController {
    public static final String INDEX = "index";

    @RequestMapping(value = {"/index", "/"})
    public String redirectToIndexPage() {
        return INDEX;
    }

    @RequestMapping("/about")
    public ModelAndView redirectToAboutPage() {
        ModelAndView modelAndView = new ModelAndView("about");
        modelAndView.addObject("aboutTabStyle", "active");
        return modelAndView;
    }

}
