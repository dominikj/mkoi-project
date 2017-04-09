package pl.mkoi.project.services.impl;

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
import java.math.BigInteger;
import java.security.SecureRandom;

@Component("EcdsaAlgorithmService")
@PropertySource("configuration-default.properties")
public class EcdsaAlgorithmService implements SignatureAlgorithmService {

  private final CryptoFacade cryptoUtils;

  @Value("${ecdsa.modulus.p}")
  private BigInteger modulusP;

  @Value("${ecdsa.primeOrder.n}")
  private BigInteger primeOrderN;

  @Value("${ecdsa.coefficient.a}")
  private BigInteger coefficientA;

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
    this.generatorPoint.setX(new BigInteger(generatorPointx, 16));
    this.generatorPoint.setY(new BigInteger(generatorPointy, 16));
    this.coefficientB = new BigInteger(coefficientB, 16);
    this.generatorPoint.setCoefficientA(this.coefficientA);
    this.generatorPoint.setCoefficientB(this.coefficientB);
    this.generatorPoint.setPrimeOrderN(this.primeOrderN);
    this.generatorPoint.setGeneratorPoint();


  }

  @Override
  public byte[] signFile(byte[] file, KeyPair keys) {

    byte[] hashedMessage = cryptoUtils.hash(file);
    BigInteger privateKey = (BigInteger) keys.getPrivateKey();
    BigInteger numberK;
    BigInteger factorR;

    do {
      do {
        // selects k: 0 < k < n
        numberK = new BigInteger(primeOrderN.bitLength(), new SecureRandom());
      } 
      while (numberK.compareTo(BigInteger.ZERO) == 0);

      // R = k*G, r = rx (mod n)
      Point pointR = new Point(generatorPoint);
      pointR.multiplyByScalar(numberK);
      factorR = pointR.getX().mod(primeOrderN);
    } 
    while (factorR.compareTo(BigInteger.ZERO) == 0);

    // s = (e + da*r)/k (mod n)
    BigInteger sign = numberK.modInverse(primeOrderN);
    BigInteger daMultiplyFactorR = privateKey.multiply(factorR);
    BigInteger hashPlus = (new BigInteger(hashedMessage)).add(daMultiplyFactorR);
    sign = sign.multiply(hashPlus.mod(primeOrderN));

    EcdsaSign signEntity = new EcdsaSign(factorR, sign);

    return cryptoUtils.serializeAndCodeByte64(signEntity);
  }

  @Override
  public KeyPair genarateKeys(int keySize) {
    // private key da
    BigInteger scalarDa = new BigInteger(primeOrderN.bitLength(), new SecureRandom());
    // public key Q = da*G
    Point vectorQ = new Point(generatorPoint);
    vectorQ.multiplyByScalar(scalarDa);

    EcdsaKeyPair keyPair = new EcdsaKeyPair();
    keyPair.setPrivateKey(scalarDa);
    keyPair.setPublicKey(vectorQ);
    return keyPair;
  }

  @Override
  public boolean verifySign(byte[] file, byte[] sign, KeyPair keys)
      throws ClassNotFoundException, IOException {

    EcdsaSign signEntity = cryptoUtils.decodeBase64AndDeserialize(sign);

    BigInteger partS = signEntity.getFactorS();
    BigInteger partR = signEntity.getFactorR();
    BigInteger hash = new BigInteger(cryptoUtils.hash(file));

    // w = s^-1
    BigInteger wtmp = partS.modInverse(primeOrderN);

    // u1 = e*w (mod n)
    BigInteger u1 = (hash.multiply(wtmp)).mod(primeOrderN);
    // u2 = r*w (mod n)
    BigInteger u2 = (partR.multiply(wtmp).mod(primeOrderN));
    
    // P = u1*G + u2*Qa
    Point point1 = new Point(generatorPoint);
    point1 = point1.multiplyByScalar(u1);
    Point point2 = ((Point) keys.getPublicKey()).multiplyByScalar(u2);
    Point resultPoint = point1.add(point2);

    //checks r = px (mod n)
    if (partR.mod(primeOrderN).compareTo(resultPoint.getX().mod(primeOrderN)) == 0) {
      return true;
    }   
    return false;
  }
}
