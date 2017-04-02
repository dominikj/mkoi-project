package pl.mkoi.project.signs;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class EcdsaSign implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private BigDecimal factorR;
  private BigInteger factorS;

  public EcdsaSign(BigDecimal factorR, BigInteger factorS) {
    this.factorR = factorR;
    this.factorS = factorS;
  }

  public BigDecimal getFactorR() {
    return factorR;
  }

  public void setFactorR(BigDecimal factorR) {
    this.factorR = factorR;
  }

  public BigInteger getFactorS() {
    return factorS;
  }

  public void setFactorS(BigInteger factorS) {
    this.factorS = factorS;
  }


}
