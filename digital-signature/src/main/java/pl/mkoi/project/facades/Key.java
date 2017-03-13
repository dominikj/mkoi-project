package pl.mkoi.project.facades;

import java.math.BigInteger;

public class Key {

  private BigInteger modulus;
  private BigInteger exponent;

  public Key(BigInteger modulus, BigInteger exponent) {
    this.modulus = modulus;
    this.exponent = exponent;
  }

  public Key() {}

  public BigInteger getModulus() {
    return modulus;
  }

  public void setModulus(BigInteger modulus) {
    this.modulus = modulus;
  }

  public BigInteger getExponent() {
    return exponent;
  }

  public void setExponent(BigInteger exponent) {
    this.exponent = exponent;
  }

}
