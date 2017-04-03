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


  }

  @Override
  public byte[] signFile(byte[] file, KeyPair keys) {

    byte[] hashedMessage = cryptoUtils.hash(file);
    BigInteger privateKey = (BigInteger) keys.getPrivateKey();
    BigInteger numberK;
    BigInteger factorR;



    do {
      do {
        numberK = new BigInteger(primeOrderN.bitLength(), new SecureRandom());
        numberK = numberK.mod(primeOrderN);
      } while (numberK.compareTo(BigInteger.ZERO) == 0);

      Point pointR = new Point(generatorPoint);

      pointR.multiplyByScalar(numberK, coefficientA);

      factorR = pointR.getX();
    } while (factorR.compareTo(BigInteger.ZERO) == 0);

    BigInteger sign = numberK.modInverse(primeOrderN);
    BigInteger daMultiplyFactorR = privateKey.multiply(factorR).mod(primeOrderN);
    BigInteger hashPlus = (new BigInteger(hashedMessage)).add(daMultiplyFactorR);
    sign = sign.multiply(hashPlus).mod(primeOrderN);


    /*
     * do { do { numberK = new BigInteger(primeOrderN.bitLength(), new SecureRandom());
     * 
     * } while (numberK.mod(primeOrderN).compareTo(BigInteger.ZERO) == 0);
     * 
     * Point pointR = new Point(generatorPoint);
     * 
     * pointR.multiplyByScalar(numberK, coefficientA);
     * 
     * factorR = pointR.getX().toBigInteger().mod(primeOrderN); } while
     * (factorR.compareTo(BigInteger.ZERO) == 0);
     * 
     * BigInteger sign = ((numberK.modInverse(primeOrderN))
     * .multiply(hash.add(privateKey.multiply(factorR)).mod(primeOrderN))).mod(primeOrderN);
     */



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

    BigInteger partS = signEntity.getFactorS();
    BigInteger partR = signEntity.getFactorR();
    BigInteger hash = new BigInteger(cryptoUtils.hash(file));

    BigInteger wtmp = partS.modInverse(primeOrderN);
    BigInteger u1 = (hash.multiply(wtmp)).mod(primeOrderN);
    BigInteger u2 = (partR.multiply(wtmp).mod(primeOrderN));
    Point pointG = new Point(generatorPoint);
    Point point1 = pointG.multiplyByScalar(u1, coefficientA);
    Point point2 = ((Point) keys.getPublicKey()).multiplyByScalar(u2, coefficientA);
    Point resultPoint = point1.add(point2);

    // To jest źle samo w sobie! Użyj compareTo() zamiast equals()
    if (partR.equals(resultPoint.getX().mod(primeOrderN))) {
      return true;

    }


    System.out.println("R: " + partR);
    System.out.println("X: " + resultPoint.getX());
    System.out.println("Xmod: " + resultPoint.getX().mod(primeOrderN));
    System.out.println("Rmod: " + partR.mod(primeOrderN));
    System.out.println("R-X mod: " + resultPoint.getX().subtract(partR).mod(primeOrderN));
    return false;
  }
}
