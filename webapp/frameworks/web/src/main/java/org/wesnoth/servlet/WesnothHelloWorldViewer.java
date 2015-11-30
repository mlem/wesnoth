package org.wesnoth.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WesnothHelloWorldViewer {

    @RequestMapping("/proto")
    public ModelAndView helloWorld() {

        return new ModelAndView("viewer-prototype");
    }
}
