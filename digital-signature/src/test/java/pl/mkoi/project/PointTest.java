package pl.mkoi.project;

import org.junit.Test;
import pl.mkoi.project.points.Point;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PointTest {

  @Test
  public void testDoublePoint() {
    Point point = new Point();
    // let (3,5) and coefficient A = 4
    point.setX(new BigDecimal("3"));
    point.setY(new BigDecimal("5"));
    point.doublePoint(new BigDecimal("4"));

    // px = 3.61
    // py = -6.891
    assertTrue(point.getX().compareTo(new BigDecimal("3.61")) == 0);
    assertTrue(point.getY().compareTo(new BigDecimal("-6.891")) == 0);
  }
  
  @Test
  public void testAddPoints(){
    Point point1 = new Point();
    Point point2 = new Point();
    
    // let (3,5) 
    point1.setX(new BigDecimal("3"));
    point1.setY(new BigDecimal("5"));
    // let (2,4)
    point2.setX(new BigDecimal("1"));
    point2.setY(new BigDecimal("4"));
    
    point1.add(point2);
    
    // px = -3.75
    // py = -1.625
    assertTrue(point1.getX().compareTo(new BigDecimal("-3.75")) == 0);
    assertTrue(point1.getY().compareTo(new BigDecimal("-1.625")) == 0);
  }
  
  @Test
  public void testMultipleByScalar(){
    Point point = new Point();
    
    // let (3,5) 
    point.setX(new BigDecimal("3"));
    point.setY(new BigDecimal("5"));
   //and scalar 5, coefficient A = -3
    point.multiplyByScalar(new BigInteger("8"), new BigDecimal("-3"));
    System.out.println(point.getX().toString());
    System.out.println(point.getY().toString());


  }
}
