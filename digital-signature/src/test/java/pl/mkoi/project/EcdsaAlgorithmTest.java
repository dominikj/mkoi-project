package pl.mkoi.project;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.keys.EcdsaKeyPair;
import pl.mkoi.project.keys.KeyPair;
import pl.mkoi.project.points.Point;
import pl.mkoi.project.services.impl.EcdsaAlgorithmService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RunWith(MockitoJUnitRunner.class)
public class EcdsaAlgorithmTest {
  @Spy
  private CryptoFacade cryptoUtils = new CryptoFacade();

  private String generatorPointx =
      "a";

  private String generatorPointy =
      "a";

  private String coefficientB =
      "a";

  @InjectMocks
  private EcdsaAlgorithmService ecdsaAlgorithmService =
      new EcdsaAlgorithmService(cryptoUtils, generatorPointx, generatorPointy, coefficientB);

  private byte[] fileToTest;

  @Before
  public void init() {
    ReflectionTestUtils.setField(ecdsaAlgorithmService, "primeOrderN", new BigInteger(
        "7"));
    ReflectionTestUtils.setField(ecdsaAlgorithmService, "coefficientA", new BigInteger("-3"));
  }

  @Test
  public void testGenerateKeys() {
    KeyPair keys = ecdsaAlgorithmService.genarateKeys(1024);
    System.out.println(((BigInteger) keys.getPrivateKey()).toString());
    Point publicKey = (Point) keys.getPublicKey();
    System.out.println(publicKey.getX().toString() + " : " + publicKey.getY().toString());

  }

  @Test
  public void testSign() throws IOException, ClassNotFoundException {
    KeyPair keys = ecdsaAlgorithmService.genarateKeys(1024);
    Path path = Paths.get("src/main/webapp/rsapss.jsp");
    fileToTest = Files.readAllBytes(path);

    byte[] sign = ecdsaAlgorithmService.signFile(fileToTest, (EcdsaKeyPair) keys);
    assertTrue(ecdsaAlgorithmService.verifySign(fileToTest, sign, keys));
  }
}
