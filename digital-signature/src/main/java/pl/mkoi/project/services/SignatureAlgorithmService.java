package pl.mkoi.project.services;

import pl.mkoi.project.keys.KeyPair;

import java.io.IOException;

public interface SignatureAlgorithmService {

  byte[] signFile(byte[] file, KeyPair keys);

  KeyPair genarateKeys(int keySize);

  boolean verifySign(byte[] file, byte[] sign, KeyPair keys)
      throws ClassNotFoundException, IOException;
}
