package pl.mkoi.project;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import pl.mkoi.project.points.Point;

import java.math.BigInteger;

public class PointTest {

  @Test
  public void testDoublePoint() {
    Point point = new Point();
    // let (3,5) and coefficient A = 4
    point.setX(new BigInteger("3"));
    point.setY(new BigInteger("5"));
    point.doublePoint(new BigInteger("4"));

    // px = 3.61
    // py = -6.891
    assertTrue(point.getX().compareTo(new BigInteger("3")) == 0);
    assertTrue(point.getY().compareTo(new BigInteger("-6")) == 0);
  }

  @Test
  public void testAddPoints() {
    Point point1 = new Point();
    Point point2 = new Point();

    // let (3,5)
    point1.setX(new BigInteger("3"));
    point1.setY(new BigInteger("5"));
    // let (2,4)
    point2.setX(new BigInteger("1"));
    point2.setY(new BigInteger("4"));

    point1.add(point2);

    // px = -3.75
    // py = -1.625
    assertTrue(point1.getX().compareTo(new BigInteger("-3")) == 0);
    assertTrue(point1.getY().compareTo(new BigInteger("-1")) == 0);
  }

  @Test
  public void testMultipleByScalar() {
    Point point = new Point();

    // let (3,5)
    point.setX(new BigInteger("3"));
    point.setY(new BigInteger("5"));
    // and scalar 5, coefficient A = -3
    point.multiplyByScalar(new BigInteger("8"), new BigInteger("-3"));
    System.out.println(point.getX().toString());
    System.out.println(point.getY().toString());


  }
}
