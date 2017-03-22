package pl.mkoi.project.keys;

public class RsaKeyPair implements KeyPair {

  private RsaKey privateKey;
  private RsaKey publicKey;

  @Override
  public Object getPublicKey() {
    return publicKey;
  }

  @Override
  public void setPublicKey(Object publicKey) {
    this.publicKey = (RsaKey) publicKey;
  }

  @Override
  public Object getPrivateKey() {
    return privateKey;
  }

  @Override
  public void setPrivateKey(Object privateKey) {
    this.privateKey = (RsaKey) privateKey;
  }



}
