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
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RunWith(MockitoJUnitRunner.class)
public class EcdsaAlgorithmTest {
  @Spy
  private CryptoFacade cryptoUtils = new CryptoFacade();

  private String coeffA = "-3";
  private String coeffB =
      "b3312fa7e23ee7e4988e056be3f82d19181d9c6efe8141120314088f5013875ac656398d8a2ed19d2a85c8edd3ec2aef";
  private String generatorCoorX =
      "aa87ca22be8b05378eb1c71ef320ad746e1d3b628ba79b9859f741e082542a385502f25dbf55296c3a545e3872760ab7";
  private String generatorCoorY =
      "3617de4a96262c6f5d9e98bf9292dc29f8f41dbd289a147ce9da3113b5f0b8c00a60b1ce1d7e819d7a431d7c90ea0e5f";
  private String primeP =
      "39402006196394479212279040100143613805079739270465446667948293404245721771496870329047266088258938001861606973112319";

  @InjectMocks
  private EcdsaAlgorithmService ecdsaAlgorithmService =
      new EcdsaAlgorithmService(cryptoUtils, generatorCoorX, generatorCoorY, coeffB);

  private byte[] fileToTest;

  @Before
  public void init() {
    ReflectionTestUtils.setField(ecdsaAlgorithmService, "primeOrderN", new BigInteger(primeP));
    ReflectionTestUtils.setField(ecdsaAlgorithmService, "coefficientA", new BigInteger(coeffA));

    Point generatorPoint = new Point();
    generatorPoint.setX(new BigInteger(generatorCoorX, 16));
    generatorPoint.setY(new BigInteger(generatorCoorY, 16));
    generatorPoint.setCoefficientA(new BigInteger(coeffA));
    generatorPoint.setCoefficientB(new BigInteger(coeffB, 16));
    generatorPoint.setPrimeOrderN(new BigInteger(primeP));
    generatorPoint.setGeneratorPoint();
    
    ReflectionTestUtils.setField(ecdsaAlgorithmService, "generatorPoint", generatorPoint);
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
