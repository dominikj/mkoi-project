package pl.mkoi.project.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pl.mkoi.project.services.SignatureAlgorithmService;

import java.io.IOException;

@Controller
public class RsapssPageController {

  private static final Logger LOGGER = LoggerFactory.getLogger(RsapssPageController.class);

  private final SignatureAlgorithmService signService;

  @Autowired
  public RsapssPageController(@Qualifier("RsapssAlgorithmService") SignatureAlgorithmService signService) {
    this.signService = signService;
  }

  @RequestMapping(value = "/rsapss", method = RequestMethod.GET)
  public ModelAndView showRsapssPage() {
    return new ModelAndView("rsapss");
  }

  /**
   * Method gets request with file for signing. 
   * @param file - file obtained from user
   * @param model - model data
   * @return - rsa-pss view
   */
  @RequestMapping(value = "/sign-file", method = RequestMethod.POST)
  public String signFile(@RequestParam("file") MultipartFile file, Model model) {
    LOGGER.info("Rozpoczęto podpisywanie pliku: {}", file.getOriginalFilename());
    try {
      signService.signFile(file.getBytes());
    } catch (IOException e) {
      LOGGER.error("Plik do podpisania jest uszkodzony", e);
    }

    // Mocked
    String fileSignature = "8787678AB7776CD87676F";
    model.addAttribute("fileSignature", fileSignature);
    return "rsapss";
  }
}
