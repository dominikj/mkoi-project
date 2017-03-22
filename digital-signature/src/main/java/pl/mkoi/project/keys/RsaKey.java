package pl.mkoi.project.keys;

import java.math.BigInteger;

public class RsaKey {

  private BigInteger modulus;
  private BigInteger exponent;

  public RsaKey(BigInteger modulus, BigInteger exponent) {
    this.modulus = modulus;
    this.exponent = exponent;
  }

  public RsaKey() {}

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

  @Override
  public String toString() {
    return modulus.toString().concat(";").concat(exponent.toString());

  }

}
