package pl.mkoi.project.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.keys.KeyPair;
import pl.mkoi.project.keys.RsaKey;
import pl.mkoi.project.services.SignatureAlgorithmService;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.util.Arrays;


@Component("RsapssAlgorithmService")
public class RsapssAlgorithmService implements SignatureAlgorithmService {

  private final RsaCryptoService rsaService;
  private final CryptoFacade cryptoUtils;
  private static final Logger LOGGER = LoggerFactory.getLogger(RsapssAlgorithmService.class);
  private static final int SIGN_SIZE = 1024 / 8;

  @Autowired
  public RsapssAlgorithmService(RsaCryptoService rsaService, CryptoFacade cryptoUtils) {
    this.rsaService = rsaService;
    this.cryptoUtils = cryptoUtils;
  }

  @Override
  public byte[] signFile(byte[] file, KeyPair keys) {
    byte[] sign = encode(file);
    return rsaService.encrypt(sign, (RsaKey) keys.getPrivateKey());
  }

  private byte[] encode(byte[] message) {
    // mHash = Hash(m)
    byte[] mhash = cryptoUtils.hash(message);

    // From RFC 3447 “Typical salt lengths in octets are hLen (the length of the output of the hash
    // function Hash) and 0"
    int saltLength = mhash.length;

    int emLength = SIGN_SIZE;

    BigInteger salt = new BigInteger(saltLength * 8, new SecureRandom());
    while (salt.toByteArray().length != saltLength) {
      salt = new BigInteger(saltLength * 8, new SecureRandom());
    }

    // zero-byte series is used as a padding
    byte[] padding1 = new byte[8];
    // m' = padding || mHash || salt
    byte[] messagePrime = cryptoUtils.concatenateArrays(padding1, mhash, salt.toByteArray());
    // H = Hash(m')
    byte[] hashPrime = cryptoUtils.hash(messagePrime);
    // PS
    byte[] padding2 = new byte[emLength - 2 * saltLength - 2];
    // DB = PS || 0x01 || salt
    byte[] dbValue = cryptoUtils.concatenateArrays(padding2, new BigInteger("1").toByteArray(),
        salt.toByteArray());
    // dbMask = MGF(H, emLen - hLen -1)
    byte[] dbMask = cryptoUtils.generateMaskMgf1(emLength - saltLength - 1, hashPrime);
    // MaskedDB = DB xor dbMask
    byte[] maskedDb = cryptoUtils.xorArrays(dbMask, dbValue);

    // em = maskedDB || H || 0xBC
    return cryptoUtils.concatenateArrays(maskedDb, hashPrime, new BigInteger("188").toByteArray());

  }

  private boolean verify(byte[] message, byte[] sign) {
    // mHash = Hash(m)
    byte[] mhash = cryptoUtils.hash(message);
    int saltLength = mhash.length;
    int emLength = SIGN_SIZE;

    // End if the rightmost octet of sign does not have hexadecimal value 0xBC
    if (sign[sign.length - 1] != (byte) 0xBC) {
      throw new InvalidParameterException("Bad sign format");
    }

    // maskedDB = the leftmost emLen − hLen − 1 octets of sign
    int maskedDbSize = emLength - saltLength - 1;
    byte[] maskedDb = Arrays.copyOfRange(sign, 0, maskedDbSize);
    // H = the next hLen octets
    byte[] tableH = Arrays.copyOfRange(sign, maskedDbSize, maskedDbSize + saltLength);
    // dbMask = MGF(H, emLen − hLen − 1)
    byte[] dbMask = cryptoUtils.generateMaskMgf1(maskedDbSize, tableH);
    // DB = maskedDB xor dbMask
    byte[] dbValue = cryptoUtils.xorArrays(dbMask, maskedDb);

    // End if byte at position emLen − hLen − sLen − 1 does not have hexadecimal value 0x01
    if (dbValue[emLength - 2 * saltLength - 2] != (byte) 0x01) {
      throw new InvalidParameterException("Bad sign format");
    }
    // the last sLen octets of DB
    byte[] salt = Arrays.copyOfRange(dbValue, dbValue.length - saltLength, dbValue.length);
    byte[] padding1 = new byte[8];
    // m' = (0x)00 00 00 00 00 00 00 00 || mHash || salt
    byte[] messagePrime = cryptoUtils.concatenateArrays(padding1, mhash, salt);
    // H' = Hash(m')
    byte[] primeH = cryptoUtils.hash(messagePrime);

    return Arrays.equals(primeH, tableH);

  }

  @Override
  public KeyPair genarateKeys(int keySize) {
    return rsaService.generateKeys(keySize);
  }

  @Override
  public boolean verifySign(byte[] file, byte[] sign, KeyPair keys)
      throws ClassNotFoundException, IOException {
    byte[] decodedData = rsaService.decrypt(sign, (RsaKey) keys.getPublicKey());
    return verify(file, decodedData);
  }

}
