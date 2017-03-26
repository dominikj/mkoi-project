package pl.mkoi.project;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.keys.KeyPair;
import pl.mkoi.project.keys.RsaKey;
import pl.mkoi.project.services.impl.RsaCryptoService;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class RsaAlgorithmTest {
  @Spy
  private CryptoFacade cryptoUtils = new CryptoFacade();

  @InjectMocks
  private RsaCryptoService rsacryptoService = new RsaCryptoService(cryptoUtils);
  
  private byte[] fileToTest;
  
  @Test
  public void testGenerateKeys() {
    KeyPair keys = rsacryptoService.generateKeys(1024);
    assertTrue(keys.getPrivateKey().toString().contains(";"));
    assertTrue(keys.getPublicKey().toString().contains(";"));

  }
  
  @Test
  public void testEncryptDecryptFromTable() throws ClassNotFoundException, IOException{
    KeyPair keys = rsacryptoService.generateKeys(1024);
    byte[] message = new byte[]{0,34,43,(byte)250,(byte)130};
    
    byte[] encrypted = rsacryptoService.encrypt(message, (RsaKey) keys.getPublicKey());
    byte[] decrypted = rsacryptoService.decrypt(encrypted, (RsaKey) keys.getPrivateKey());
    assertTrue(Arrays.equals(Arrays.copyOfRange(message, 1, message.length),decrypted));
  }
  
  @Test
  public void testEncryptDecryptFromFile() throws IOException, ClassNotFoundException{
    Path path = Paths.get("src/main/webapp/home.jsp");
    fileToTest = Files.readAllBytes(path);
    KeyPair keys = rsacryptoService.generateKeys(1024);
    byte[] encrypted = rsacryptoService.encrypt(new BigInteger(fileToTest).toByteArray(), (RsaKey) keys.getPublicKey());
    byte[] decrypted = rsacryptoService.decrypt(encrypted, (RsaKey) keys.getPrivateKey());
    assertTrue(Arrays.equals(fileToTest,decrypted));
  }
  
}
