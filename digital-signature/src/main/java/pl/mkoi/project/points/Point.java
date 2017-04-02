package pl.mkoi.project.points;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Point {
  private BigDecimal coordinateX;
  private BigDecimal coordinateY;

  /**
   * Default constructor for point entity.
   */
  public Point() {
    coordinateX = new BigDecimal("0");
    coordinateY = new BigDecimal("0");

  }

  public Point(Point point) {
    coordinateX = point.coordinateX;
    coordinateY = point.coordinateY;
  }

  public BigDecimal getX() {
    return coordinateX;
  }

  public void setX(BigDecimal coordinateX) {
    this.coordinateX = coordinateX;
  }

  public BigDecimal getY() {
    return coordinateY;
  }

  public void setY(BigDecimal coordinateY) {
    this.coordinateY = coordinateY;
  }

  /**
   * Makes double point R = P + P.
   * 
   * @param coefficientA elliptic curve coefficient
   * @return this point
   */
  public Point doublePoint(BigDecimal coefficientA) {
    // s = (3*px^2 + a)/2*py
    BigDecimal factorS = this.coordinateX.pow(2).multiply(new BigDecimal("3")).add(coefficientA);
    factorS = factorS.divide(this.getY().multiply(new BigDecimal("2")), 10, RoundingMode.HALF_UP);

    // rx = s^2 - 2*px
    BigDecimal coordinateX = factorS.pow(2).subtract(this.getX().multiply(new BigDecimal("2")));

    // ry = s*(px-rx)-py
    BigDecimal coordinateY =
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
    BigDecimal factorS = (this.getY().subtract(pointQ.getY()))
        .divide((this.getX().subtract(pointQ.getX())), 10, RoundingMode.HALF_UP);

    // rx = s^2 - px - qx
    BigDecimal coordinateX = factorS.pow(2).subtract(this.getX()).subtract(pointQ.getX());

    // s(px - rx) - py
    BigDecimal coordinateY =
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
  public Point multiplyByScalar(BigInteger scalar, BigDecimal coefficientA) {
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
