package pl.mkoi.project.keys;

import pl.mkoi.project.points.Point;

import java.math.BigInteger;

public class EcdsaKeyPair implements KeyPair {

  BigInteger privateKey;
  Point publicKey;

  @Override
  public Object getPublicKey() {
    return publicKey;
  }

  @Override
  public void setPublicKey(Object publicKey) {
    this.publicKey = (Point) publicKey;
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
