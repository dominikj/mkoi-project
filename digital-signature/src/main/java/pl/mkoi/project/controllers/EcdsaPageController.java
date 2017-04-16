package pl.mkoi.project.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pl.mkoi.project.builders.ZipBuilder;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.keys.EcdsaKeyPair;
import pl.mkoi.project.keys.KeyPair;
import pl.mkoi.project.points.Point;
import pl.mkoi.project.services.SignatureAlgorithmService;
import pl.mkoi.project.services.impl.EcdsaAlgorithmService;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Controller
@RequestMapping(value = "/ecdsa")
public class EcdsaPageController extends PageController {

  private static final Logger LOGGER = LoggerFactory.getLogger(RsapssPageController.class);

  private final SignatureAlgorithmService signService;
  private final CryptoFacade cryptoUtils;

  @Autowired
  public EcdsaPageController(@Qualifier("EcdsaAlgorithmService") EcdsaAlgorithmService signService,
      CryptoFacade cryptoUtils) {
    this.signService = signService;
    this.cryptoUtils = cryptoUtils;
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public String showRsapssPage(Model model) {
    model.addAttribute("baseUrl", "/ecdsa");
    return "ecdsa";
  }

  /**
   * Method gets request with file for signing.
   * 
   * @param file - file obtained from user
   * @return - text file with sign
   * @throws IOException throws exception during getting file data bytes
   * @throws ClassNotFoundException throws exception during decoding key
   */
  @RequestMapping(value = "/sign-file", method = RequestMethod.POST)
  @ResponseBody
  public HttpEntity<byte[]> signFile(@RequestParam("file") MultipartFile file,
      @RequestParam("key") MultipartFile key) throws IOException, ClassNotFoundException {

    LOGGER.info("Rozpoczęto podpisywanie pliku: {} kluczem: {}", file.getOriginalFilename(),
        key.getOriginalFilename());
    KeyPair keypair = new EcdsaKeyPair();
    BigInteger privateKey = cryptoUtils.decodeBase64AndDeserialize(key.getBytes());
    keypair.setPrivateKey(privateKey);

    byte[] fileSignature = signService.signFile(file.getBytes(), keypair);

    String filename = "sign_".concat(file.getOriginalFilename()).concat(".txt");

    return new HttpEntity<byte[]>(fileSignature,
        prepareHeaders(fileSignature.length, filename, MediaType.APPLICATION_OCTET_STREAM));
  }

  /**
   * Generates zip file with keys.
   * 
   * @return zip file
   * @throws IOException exp
   */
  @RequestMapping(value = "/generate-keys", method = RequestMethod.GET)
  @ResponseBody
  public HttpEntity<byte[]> generateKeys() throws IOException {

    KeyPair keys = signService.genarateKeys(1024);
    ZipBuilder builder = getZipBuilder();

    byte[] privateKey = cryptoUtils.serializeAndCodeByte64(keys.getPrivateKey());

    byte[] publicKey = cryptoUtils.serializeAndCodeByte64(keys.getPublicKey());

    builder.addFile("private_key.txt", privateKey).addFile("public_key.txt", publicKey);

    String filename = "Keys_".concat(LocalDateTime.now().toString().concat(".zip"));

    byte[] zipFile = builder.build();

    return new HttpEntity<byte[]>(zipFile,
        prepareHeaders(zipFile.length, filename, MediaType.MULTIPART_FORM_DATA));
  }

  /**
   * Verifies sign.
   * 
   * @param file file to check
   * @param key public key
   * @param sign digital sign of file
   * @param model model
   * @return view
   * @throws IOException exception during access file data
   * @throws ClassNotFoundException exception during deserialize data
   */
  @RequestMapping(value = "/verify-sign", method = RequestMethod.POST)
  public String verifySign(@RequestParam("file") MultipartFile file,
      @RequestParam("key") MultipartFile key, @RequestParam("sign") MultipartFile sign, Model model)
      throws IOException, ClassNotFoundException {


    KeyPair keypair = new EcdsaKeyPair();
    Point publicKey = cryptoUtils.decodeBase64AndDeserialize(key.getBytes());
    keypair.setPublicKey(publicKey);

    model.addAttribute("signVerified",
        signService.verifySign(file.getBytes(), sign.getBytes(), keypair));

    model.addAttribute("baseUrl", "/rsapss");
    return "rsapss";
  }

}
