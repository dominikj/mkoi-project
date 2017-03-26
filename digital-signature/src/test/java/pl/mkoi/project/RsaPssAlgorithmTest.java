package pl.mkoi.project;

import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.keys.KeyPair;
import pl.mkoi.project.keys.RsaKeyPair;
import pl.mkoi.project.services.impl.RsaCryptoService;
import pl.mkoi.project.services.impl.RsapssAlgorithmService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(MockitoJUnitRunner.class)
public class RsaPssAlgorithmTest {
  @Spy
  CryptoFacade cryptoUtils = new CryptoFacade();

  @Spy
  RsaCryptoService rsacryptoService = new RsaCryptoService(cryptoUtils);

  @InjectMocks
  private RsapssAlgorithmService rsapssAlgorithmService;

  private byte[] fileToTest;

  @Test
  public void testSignFileWithoutEncryption() throws IOException, ClassNotFoundException {
    Path path = Paths.get("src/main/webapp/rsapss.jsp");
    fileToTest = Files.readAllBytes(path);

    doAnswer(returnsFirstArg()).when(rsacryptoService).encrypt(any(), any());
    doAnswer(returnsFirstArg()).when(rsacryptoService).decrypt(any(), any());

    KeyPair keys = Mockito.spy(RsaKeyPair.class);

    byte[] sign = rsapssAlgorithmService.signFile(fileToTest, keys);

    assertTrue(rsapssAlgorithmService.verifySign(fileToTest, sign, keys));

  }

  @Test
  public void testSignFileWithEncryption() throws IOException, ClassNotFoundException {
    Path path = Paths.get("src/main/webapp/rsapss.jsp");
    fileToTest = Files.readAllBytes(path);

    KeyPair keys = rsacryptoService.generateKeys(1024);
    byte[] sign = rsapssAlgorithmService.signFile(fileToTest, keys);

    assertTrue(rsapssAlgorithmService.verifySign(fileToTest, sign, keys));

  }
}
