package pl.mkoi.project.services.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.keys.KeyPair;
import pl.mkoi.project.points.Point;
import pl.mkoi.project.services.SignatureAlgorithmService;

import java.io.IOException;
import java.math.BigInteger;

@Component("EcdsaAlgorithmService")
@PropertySource("configuration-default.properties")
public class EcdsaAlgorithmService implements SignatureAlgorithmService {

  private final CryptoFacade cryptoUtils;

  @Value("{ecdsa.modulus.p}")
  BigInteger modulusP;

  @Value("{ecdsa.primeOrder.n}")
  BigInteger primeOrderN;

  @Value("{ecdsa.coefficient.a}")
  BigInteger coefficientA;

  @Value("{ecdsa.coefficient.b}")
  BigInteger coefficientB;

  @Value("{ecdsa.curveGeneratorPoint.g.x}")
  BigInteger generatorPointx;

  @Value("{ecdsa.curveGeneratorPoint.g.y}")
  BigInteger generatorPointy;


  @Autowired
  public EcdsaAlgorithmService(final CryptoFacade cryptoUtils) {
    this.cryptoUtils = cryptoUtils;
  }

  @Override
  public byte[] signFile(byte[] file, KeyPair keys) {
    cryptoUtils.hash(file);
    return new byte[0];
  }

  @Override
  public KeyPair genarateKeys(int keySize) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean verifySign(byte[] file, byte[] sign, KeyPair keys)
      throws ClassNotFoundException, IOException {
    // TODO Auto-generated method stub
    return false;
  }

  // R = P + P
  @SuppressFBWarnings
  private Point doublePoint(Point point) {
    // s = (3*px^2 + a)/2*py
    BigInteger factorS = point.getX().pow(2).multiply(new BigInteger("3")).add(coefficientA);
    factorS = factorS.divide(point.getY().multiply(new BigInteger("2")));

    // rx = s^2 - 2*px
    BigInteger coordinateX = factorS.pow(2).subtract(point.getX().multiply(new BigInteger("2")));

    // ry = s*(px-rx)-py
    BigInteger coordinateY =
        point.getX().subtract(coordinateX).multiply(factorS).subtract(point.getY());

    Point doubledPoint = new Point();
    doubledPoint.setX(coordinateX);
    doubledPoint.setY(coordinateY);

    return doubledPoint;
  }

}
