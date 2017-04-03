package pl.mkoi.project.points;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;

public class Point {
  private BigInteger coordinateX;
  private BigInteger coordinateY;

  /**
   * Default constructor for point entity.
   */
  public Point() {
    coordinateX = new BigInteger("0");
    coordinateY = new BigInteger("0");

  }

  public Point(Point point) {
    coordinateX = point.coordinateX;
    coordinateY = point.coordinateY;
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

  /**
   * Makes double point R = P + P.
   * 
   * @param coefficientA elliptic curve coefficient
   * @return this point
   */
  public Point doublePoint(BigInteger coefficientA) {
    // s = (3*px^2 + a)/2*py
    BigInteger factorS = this.coordinateX.pow(2).multiply(new BigInteger("3")).add(coefficientA);
    factorS = factorS.divide(this.getY().multiply(new BigInteger("2")));

    // rx = s^2 - 2*px
    BigInteger coordinateX = factorS.pow(2).subtract(this.getX().multiply(new BigInteger("2")));

    // ry = s*(px-rx)-py
    BigInteger coordinateY =
        this.getX().subtract(coordinateX).multiply(factorS).subtract(this.getY());

    this.setX(coordinateX);
    this.setY(coordinateY);

    return this;
  }

  /**
   * Adds point to this point - R = P + Q.
   * 
   * @param pointQ added point
   * @return this point
   */
  public Point add(Point pointQ) {
    // s = (py - qy)/(px - qx)
    BigInteger factorS =
        (this.getY().subtract(pointQ.getY())).divide((this.getX().subtract(pointQ.getX())));

    // rx = s^2 - px - qx
    BigInteger coordinateX = factorS.pow(2).subtract(this.getX()).subtract(pointQ.getX());

    // s(px - rx) - py
    BigInteger coordinateY =
        factorS.multiply((this.getX().subtract(coordinateX))).subtract(this.getY());

    this.setX(coordinateX);
    this.setY(coordinateY);

    return this;

  }

  /**
   * Makes scalar multiplication.
   * 
   * @param scalar scalar to multiple
   * @param coefficientA elliptic curve coefficient
   * @return this point
   */
  public Point multiplyByScalar(BigInteger scalar, BigInteger coefficientA) {
    char[] scalarTable = scalar.toString(2).toCharArray();
    ArrayUtils.reverse(scalarTable);

    Point multipliedPoint = new Point();
    for (char digit : scalarTable) {
      if ("1".equals(String.valueOf(digit))) {
        multipliedPoint.add(this);
      }
      doublePoint(coefficientA);
    }

    this.setX(multipliedPoint.getX());
    this.setY(multipliedPoint.getY());

    return this;
  }

}
