package pl.mkoi.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainPageController extends PageController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView showMainPage() {
    return new ModelAndView("home");
  }
}
