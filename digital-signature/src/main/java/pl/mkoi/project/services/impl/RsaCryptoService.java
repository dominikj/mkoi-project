package pl.mkoi.project.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.keys.RsaKey;
import pl.mkoi.project.keys.RsaKeyPair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class RsaCryptoService {

  private final CryptoFacade cryptoUtils;
  private static final String MIN_EXPONENT = "65537";
  private static final int MAX_DATA_CHUNK_SIZE = 127;

  @Autowired
  public RsaCryptoService(CryptoFacade cryptoUtils) {
    this.cryptoUtils = cryptoUtils;
  }

  /**
   * Generates public and private key pair.
   * 
   * @param bitLength key length in bits
   * @return pair of keys
   */
  public RsaKeyPair generateKeys(int bitLength) {
    BigInteger primeP = cryptoUtils.getPrimeNumber(bitLength / 2);
    BigInteger primeQ = cryptoUtils.getPrimeNumber(bitLength / 2);
    BigInteger phi = eulerFunction(primeP, primeQ);

    // Gets coprime number from 65537 < e < phi(p*q)
    // Gets randomly prime number which may be coprime to phi, thus we need to do primality test. If
    // number is not coprime, increments it and tests again
    BigInteger floor = new BigInteger(MIN_EXPONENT);
    BigInteger primeE = cryptoUtils.getPrimeNumber(floor.bitCount());
    while (phi.gcd(primeE).compareTo(BigInteger.ONE) > 0 && primeE.compareTo(phi) < 0) {
      primeE = primeE.add(BigInteger.ONE);
    }

    BigInteger factorN = primeP.multiply(primeQ);

    RsaKey publicKey = new RsaKey();

    // Public key is (n,e)
    publicKey.setModulus(factorN);
    publicKey.setExponent(primeE);

    // Solves d*e = 1*(mod phi)
    BigInteger factorD = primeE.modInverse(phi);

    RsaKey privateKey = new RsaKey();
    // Private key is (n,d)
    privateKey.setModulus(factorN);
    privateKey.setExponent(factorD);

    RsaKeyPair keyPair = new RsaKeyPair();
    keyPair.setPublicKey(publicKey);
    keyPair.setPrivateKey(privateKey);

    return keyPair;
  }

  /**
   * Counts number of coprime numbers to p*q product in special case, when p, q are primes. In range
   * 0 < k < p*q there are p−1 distinct multiples of q and q−1 distinct multiples of p, and a bit of
   * thought shows that these two sets cannot overlap, as any positive number that was a multiple of
   * both p and q would have to be at least as large as p*q. So, in 0 < k < p*q range are (p*q-1)
   * number and (q-1) + (p -1) are not coprimes, thus phi(p*q) = (p*q-1) - (q-1) - (p-1) =
   * (q-1)*(p-1)
   * 
   * @param p first prime number
   * @param q second prime number
   * @return number of relatively prime numbers
   */
  private BigInteger eulerFunction(BigInteger primeP, BigInteger primeQ) {
    return primeP.subtract(BigInteger.ONE).multiply(primeQ.subtract(BigInteger.ONE));
  }

  /**
   * Encrypts data.
   * 
   * @param message data to encrypt
   * @param key key for encrypt
   * @return encrypted data as serialized object
   */
  public byte[] encrypt(byte[] message, RsaKey key) {
    // data splitted on chunks smaller than modulus
    List<byte[]> preparedData = prepareData(message, MAX_DATA_CHUNK_SIZE);
    List<byte[]> encryptedData = new ArrayList<>();

    for (byte[] b : preparedData) {
      encryptedData
          .add((new BigInteger(b)).modPow(key.getExponent(), key.getModulus()).toByteArray());
    }
    return cryptoUtils.serializeAndCodeByte64(encryptedData);
  }

  /**
   * Decrypts data.
   * 
   * @param message encrypted data
   * @param key key
   * @return decrypted data
   * @throws IOException exception during access to {@link ByteArrayOutputStream}
   * @throws ClassNotFoundException exception during deserialization object
   */
  public byte[] decrypt(byte[] message, RsaKey key) throws IOException, ClassNotFoundException {
    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

    // makes data to byte arrays list
    List<byte[]> data = cryptoUtils.decodeBase64AndDeserialize(message);

    data.stream().forEach(b -> {
      byte[] decodedChunk =
          (new BigInteger(1, b)).modPow(key.getExponent(), key.getModulus()).toByteArray();
      try {
        byteOutput.write(decodedChunk);
      } catch (IOException e) {
        throw new RuntimeException();
      }
    });
    byte[] decodedData = byteOutput.toByteArray();
    // remove additional zero at start of decoded block
    if (decodedData[0] == 0) {
      decodedData = Arrays.copyOfRange(decodedData, 1, decodedData.length);
    }
    return decodedData;
  }

  private List<byte[]> prepareData(byte[] data, int blockSize) {

    int blockCount = (data.length + blockSize - 1) / blockSize;
    List<byte[]> preparedData = new ArrayList<byte[]>();
    // Gets blocks of data with size blockSize, concatenates with zero byte at the start, and adds
    // to list
    for (int i = 1; i < blockCount; i++) {
      int idx = (i - 1) * blockSize;
      byte[] tmpArr = new byte[blockSize + 1];
      System.arraycopy(data, idx, tmpArr, 1, blockSize);
      preparedData.add(tmpArr);
    }

    // Last chunk
    int end = -1;
    if (data.length % blockSize == 0) {
      end = data.length;
    } else {
      end = data.length % blockSize + blockSize * (blockCount - 1);
    }

    byte[] tempArr = Arrays.copyOfRange(data, (blockCount - 1) * blockSize, end);
    byte[] addZeroArr = new byte[tempArr.length + 1];

    System.arraycopy(tempArr, 0, addZeroArr, 1, tempArr.length);
    preparedData.add(addZeroArr);
    return preparedData;
  }

}
