package pl.mkoi.project;

import org.junit.Test;
import pl.mkoi.project.points.Point;

import java.math.BigInteger;

public class PointTest {

  private BigInteger coeffA = new BigInteger("3");
  private BigInteger coeffB = new BigInteger("5");
  private BigInteger coorX = new BigInteger("1");
  private BigInteger coorY = new BigInteger("3");
  private BigInteger primeP = new BigInteger("7");

  @Test
  public void testDoublePoint() {
    Point point = new Point();
    point.setX(coorX);
    point.setY(coorY);
    point.setCoefficientA(coeffA);
    point.setCoefficientB(coeffB);
    point.setPrimeOrderN(primeP);
    point.doublePoint();

  }

  @Test
  public void testAddPoints() {
    Point point1 = new Point();


    point1.setX(coorX);
    point1.setY(coorY);
    point1.setCoefficientA(coeffA);
    point1.setCoefficientB(coeffB);
    point1.setPrimeOrderN(primeP);
    point1.setGeneratorPoint();
    Point point2 = new Point(point1);
    
    point1.doublePoint();

    point1.add(point2);


  }

  @Test
  public void testMultipleByScalar() {
    Point point = new Point();
    point.setX(coorX);
    point.setY(coorY);
    point.setCoefficientA(coeffA);
    point.setCoefficientB(coeffB);
    point.setPrimeOrderN(primeP);

    point.multiplyByScalar(new BigInteger("12"));

  }
}
