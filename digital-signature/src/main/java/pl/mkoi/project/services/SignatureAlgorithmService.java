package pl.mkoi.project.services;

import pl.mkoi.project.keys.KeyPair;

public interface SignatureAlgorithmService {

  byte[] signFile(byte[] file, KeyPair keys);

  KeyPair genarateKeys(int keySize);

  boolean verifySign(byte[] file, byte[] sign, KeyPair keys);
}
