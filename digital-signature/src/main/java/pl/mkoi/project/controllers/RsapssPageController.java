package pl.mkoi.project.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pl.mkoi.project.builders.ZipBuilder;
import pl.mkoi.project.keys.KeyPair;
import pl.mkoi.project.services.SignatureAlgorithmService;
import pl.mkoi.project.services.impl.RsapssAlgorithmService;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

@Controller
@RequestMapping(value = "/rsapss")
public class RsapssPageController extends PageController {

  private static final Logger LOGGER = LoggerFactory.getLogger(RsapssPageController.class);

  private final SignatureAlgorithmService signService;

  @Autowired
  public RsapssPageController(
      @Qualifier("RsapssAlgorithmService") RsapssAlgorithmService signService) {
    this.signService = signService;
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ModelAndView showRsapssPage() {
    return new ModelAndView("rsapss");
  }

  /**
   * Method gets request with file for signing.
   * 
   * @param file - file obtained from user
   * @return - text file with sign
   */
  @RequestMapping(value = "/sign-file", method = RequestMethod.POST)
  @ResponseBody
  public HttpEntity<byte[]> signFile(@RequestParam("file") MultipartFile file,
      @RequestParam("key") MultipartFile key) {

    LOGGER.info("Rozpoczęto podpisywanie pliku: {} kluczem: {}", file.getOriginalFilename(),
        key.getOriginalFilename());

    // Mocked
    // signService.signFile(file.getBytes(), key);
    byte[] fileSignature = "8787678AB7776CD87676F".getBytes(Charset.defaultCharset());

    String filename = "sign_".concat(file.getOriginalFilename()).concat(".txt");

    return new HttpEntity<byte[]>(fileSignature,
        prepareHeaders(fileSignature.length, filename, MediaType.TEXT_PLAIN));

  }

  /**
   * Generates zip file with keys.
   * 
   * @return zip file
   */
  @RequestMapping(value = "/generate-keys", method = RequestMethod.GET)
  @ResponseBody
  public HttpEntity<byte[]> generateKeys() {

    KeyPair keys = signService.genarateKeys(1024);
    ZipBuilder builder = getZipBuilder();

    builder.addFile("private_key.txt",
        keys.getPrivateKey().toString().getBytes(Charset.defaultCharset()));

    builder.addFile("public_key.txt",
        keys.getPublicKey().toString().getBytes(Charset.defaultCharset()));

    String filename = "Keys_".concat(LocalDateTime.now().toString().concat(".zip"));

    byte[] zipFile = builder.build();

    return new HttpEntity<byte[]>(zipFile,
        prepareHeaders(zipFile.length, filename, MediaType.MULTIPART_FORM_DATA));

  }
}
