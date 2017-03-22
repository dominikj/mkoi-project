package pl.mkoi.project.keys;

import java.math.BigInteger;

public class DsaKeyPair implements KeyPair {

  private BigInteger privateKey;
  private BigInteger publicKey;

  public DsaKeyPair(BigInteger privateKey, BigInteger publicKey) {
    this.privateKey = (BigInteger) privateKey;
    this.publicKey = (BigInteger) publicKey;
  }

  @Override
  public Object getPublicKey() {
    return publicKey;
  }

  @Override
  public void setPublicKey(Object publicKey) {
    this.publicKey = (BigInteger) publicKey;
  }

  @Override
  public Object getPrivateKey() {
    return privateKey;
  }

  @Override
  public void setPrivateKey(Object privateKey) {
    this.privateKey = (BigInteger) privateKey;
  }

}
