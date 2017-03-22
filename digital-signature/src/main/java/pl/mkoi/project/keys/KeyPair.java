package pl.mkoi.project.keys;

public interface KeyPair {

  Object getPublicKey();

  void setPublicKey(Object publicKey);


  Object getPrivateKey();

  void setPrivateKey(Object privateKey);
}
