package pl.mkoi.project.points;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;

public class Point {
  private BigInteger multiplier;
  private BigInteger coordinateX;
  private BigInteger coordinateY;
  private BigInteger coefficientA;
  private BigInteger coefficientB;
  private BigInteger primeP;
  private Point generatorPoint;

  /**
   * Default constructor for point entity.
   */
  public Point() {
    coordinateX = BigInteger.ZERO;
    coordinateY = BigInteger.ZERO;
    multiplier = BigInteger.ONE;


  }

  /**
   * Copy constructor.
   * 
   * @param point point to copy
   */
  public Point(Point point) {
    this.generatorPoint = point.generatorPoint;
    coordinateX = point.coordinateX;
    coordinateY = point.coordinateY;
    this.coefficientA = point.coefficientA;
    this.coefficientB = point.coefficientB;
    this.primeP = point.primeP;
    this.multiplier = point.multiplier;

  }



  public Point getGeneratorPoint() {
    return generatorPoint;
  }

  /**
   * Set this point as generator point.
   */
  public void setGeneratorPoint() {
    this.generatorPoint = new Point();
    this.generatorPoint.setX(coordinateX);
    this.generatorPoint.setY(coordinateY);
    this.generatorPoint.setCoefficientA(coefficientA);
    this.generatorPoint.setPrimeOrderN(primeP);
    this.generatorPoint.setCoefficientB(coefficientB);
  }

  public BigInteger getX() {
    return coordinateX;
  }

  public void setX(BigInteger coordinateX) {
    this.coordinateX = coordinateX;
  }

  public BigInteger getY() {
    return coordinateY;
  }

  public void setY(BigInteger coordinateY) {
    this.coordinateY = coordinateY;
  }

  public BigInteger getCoefficientA() {
    return coefficientA;
  }

  public void setCoefficientA(BigInteger coefficientA) {
    this.coefficientA = coefficientA;
  }

  public BigInteger getCoefficientB() {
    return coefficientB;
  }

  public void setCoefficientB(BigInteger coefficientB) {
    this.coefficientB = coefficientB;
  }

  public BigInteger getPrimeP() {
    return primeP;
  }

  public void setPrimeOrderN(BigInteger primeP) {
    this.primeP = primeP;
  }

  /**
   * Makes double point R = P + P.
   * 
   * @return this point
   */
  public Point doublePoint() {
    // s = (3*px^2 + a)/2*py
    BigInteger factorS = this.coordinateX.pow(2).multiply(new BigInteger("3")).add(coefficientA);
    factorS = factorS.mod(primeP);
    factorS = factorS.multiply(this.getY().multiply(new BigInteger("2")).modInverse(primeP));

    // rx = s^2 - 2*px
    BigInteger coordinateX = factorS.pow(2).subtract(this.getX().multiply(new BigInteger("2")));
    coordinateX = coordinateX.mod(primeP);

    // ry = s*(px-rx)-py
    BigInteger coordinateY =
        this.getX().subtract(coordinateX).multiply(factorS).subtract(this.getY()).mod(primeP);

    checkPointIsOnCurve(coordinateX, coordinateY);
    this.setX(coordinateX);
    this.setY(coordinateY);

    return this;
  }

  /**
   * Adds point to this point - R = P + Q. Adding based on observation that we can present all
   * points in x*G form, where G is generator point, thus we can define adding as P + Q = p*G + q*G
   * = (p+q)(mod n)*G.
   * 
   * @param pointQ added point
   * @return this point
   */
  public Point add(Point pointQ) {
    this.multiplier = this.multiplier.add(pointQ.multiplier);
    Point multiplied = new Point(generatorPoint).multiplyByScalar(this.multiplier.mod(primeP));
    this.coordinateX = multiplied.getX();
    this.coordinateY = multiplied.getY();
    return multiplied;
  }

  // adding as defined in definition
  private Point addInternal(Point pointQ) {

    // Point (0,0) is used as point in infinity
    if (getX().compareTo(BigInteger.ZERO) == 0 && getY().compareTo(BigInteger.ZERO) == 0) {
      this.setX(pointQ.getX());
      this.setY(pointQ.getY());
      return this;
    }
    // s = (py - qy)/(px - qx)
    BigInteger factorS = (this.getY().subtract(pointQ.getY()))
        .multiply((this.getX().subtract(pointQ.getX()).modInverse(primeP)));

    // rx = s^2 - px - qx
    BigInteger coordinateX =
        factorS.pow(2).subtract(this.getX()).subtract(pointQ.getX()).mod(primeP);

    // s(px - rx) - py
    BigInteger coordinateY =
        factorS.multiply((this.getX().subtract(coordinateX))).subtract(this.getY()).mod(primeP);

    checkPointIsOnCurve(coordinateX, coordinateY);
    this.setX(coordinateX);
    this.setY(coordinateY);

    return this;

  }

  /**
   * Makes scalar multiplication.
   * 
   * @param scalar scalar to multiple
   * @return this point
   */
  public Point multiplyByScalar(BigInteger scalar) {
    char[] scalarTable = scalar.toString(2).toCharArray();
    ArrayUtils.reverse(scalarTable);

    Point multipliedPoint = new Point();
    multipliedPoint.setCoefficientA(this.coefficientA);
    multipliedPoint.setCoefficientB(coefficientB);
    multipliedPoint.setPrimeOrderN(primeP);

    for (char digit : scalarTable) {
      if ("1".equals(String.valueOf(digit))) {
        multipliedPoint.addInternal(this);
      }
      doublePoint();
    }

    checkPointIsOnCurve(multipliedPoint.getX(), multipliedPoint.getY());
    this.multiplier = this.multiplier.multiply(scalar);
    this.setX(multipliedPoint.getX());
    this.setY(multipliedPoint.getY());

    return this;
  }

  private boolean checkPointIsOnCurve(BigInteger coordinateX, BigInteger coordinateY) {
    BigInteger tmp = coefficientA.multiply(coordinateX).add(coefficientB);
    tmp = coordinateX.pow(3).add(tmp);
    tmp = tmp.mod(primeP);
    BigInteger tmp2 = coordinateY.pow(2).mod(primeP);
    if (tmp2.compareTo(tmp) == 0) {
      return true;
    }
    throw new RuntimeException("Critical error: Point is not on curve!");
  }

}
