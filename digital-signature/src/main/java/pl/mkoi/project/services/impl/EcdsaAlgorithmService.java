package pl.mkoi.project.services.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.keys.EcdsaKeyPair;
import pl.mkoi.project.keys.KeyPair;
import pl.mkoi.project.points.Point;
import pl.mkoi.project.services.SignatureAlgorithmService;
import pl.mkoi.project.signs.EcdsaSign;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;

@SuppressFBWarnings
@Component("EcdsaAlgorithmService")
@PropertySource("configuration-default.properties")
public class EcdsaAlgorithmService implements SignatureAlgorithmService {

  private final CryptoFacade cryptoUtils;

  @Value("${ecdsa.modulus.p}")
  private BigInteger modulusP;

  @Value("${ecdsa.primeOrder.n}")
  private BigInteger primeOrderN;

  @Value("${ecdsa.coefficient.a}")
  private BigDecimal coefficientA;

  private final BigInteger coefficientB;

  private final Point generatorPoint;

  /**
   * Default constructor.
   * 
   * @param cryptoUtils autowired bean
   */
  @Autowired
  public EcdsaAlgorithmService(final CryptoFacade cryptoUtils,
      @Value("${ecdsa.curveGeneratorPoint.g.x}") final String generatorPointx,
      @Value("${ecdsa.curveGeneratorPoint.g.y}") final String generatorPointy,
      @Value("${ecdsa.coefficient.b}") final String coefficientB) {
    this.cryptoUtils = cryptoUtils;

    this.generatorPoint = new Point();
    this.generatorPoint.setX(new BigDecimal(new BigInteger(generatorPointx, 16)));
    this.generatorPoint.setY(new BigDecimal(new BigInteger(generatorPointy, 16)));
    this.coefficientB = new BigInteger(coefficientB, 16);


  }

  @Override
  public byte[] signFile(byte[] file, KeyPair keys) {

    byte[] hashedMessage = cryptoUtils.hash(file);
    BigInteger privateKey = (BigInteger) keys.getPrivateKey();
    BigInteger numberK;
    BigDecimal factorR;

    do {
      do {
        numberK = new BigInteger(primeOrderN.bitLength(), new SecureRandom());
        numberK = numberK.mod(primeOrderN);
      } while (numberK.compareTo(BigInteger.ZERO) == 0);

      Point pointR = new Point(generatorPoint);

      pointR.multiplyByScalar(numberK, coefficientA);

      factorR = pointR.getX();
    } while (factorR.compareTo(BigDecimal.ZERO) == 0);

    BigInteger sign = numberK.modInverse(primeOrderN);
    BigInteger daMultiplyFactorR = privateKey.multiply(factorR.toBigInteger()).mod(primeOrderN);
    BigInteger hashPlus = (new BigInteger(hashedMessage)).add(daMultiplyFactorR);
    sign = sign.multiply(hashPlus).mod(primeOrderN);


    EcdsaSign signEntity = new EcdsaSign(factorR, sign);


    return cryptoUtils.serializeAndCodeByte64(signEntity);
  }

  @Override
  public KeyPair genarateKeys(int keySize) {
    // private key da
    BigInteger scalarDa = new BigInteger(primeOrderN.bitLength(), new SecureRandom());
    // public key Q = da*G
    Point vectorQ = new Point(generatorPoint);
    vectorQ.multiplyByScalar(scalarDa, coefficientA);

    EcdsaKeyPair keyPair = new EcdsaKeyPair();
    keyPair.setPrivateKey(scalarDa);
    keyPair.setPublicKey(vectorQ);

    return keyPair;
  }

  @Override
  public boolean verifySign(byte[] file, byte[] sign, KeyPair keys)
      throws ClassNotFoundException, IOException {

    EcdsaSign signEntity = cryptoUtils.decodeBase64AndDeserialize(sign);

    BigInteger partS = new BigInteger("123");
    BigInteger partR = new BigInteger("123");
    BigInteger hash = new BigInteger("123");

    BigInteger wtmp = partS.modInverse(primeOrderN);
    BigInteger u1 = (hash.multiply(wtmp)).mod(primeOrderN);
    BigInteger u2 = (partR.multiply(wtmp)).mod(primeOrderN);
    Point pointG = new Point(generatorPoint);
    Point point1 = pointG.multiplyByScalar(u1, coefficientA);
    Point point2 = ((Point) keys.getPublicKey()).multiplyByScalar(u2, coefficientA);
    Point resultPoint = point1.add(point2);

    if ((partR.mod(primeOrderN)).equals(resultPoint.getX().toBigInteger())) {
      return true;
    }


    return false;
  }
}
