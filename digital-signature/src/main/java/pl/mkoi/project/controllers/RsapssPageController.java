package pl.mkoi.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RsapssPageController {

  @RequestMapping(value = "/rsapss", method = RequestMethod.GET)
  public ModelAndView showRsapssPage() {
    return new ModelAndView("rsapss");
  }
}
