package pl.mkoi.project.facades;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class CryptoFacade {
  private static final Logger LOGGER = LoggerFactory.getLogger(CryptoFacade.class);

  /**
   * Returns prime number with 1 - 2**-100 probability.
   * 
   * @param bitLength - size of number
   * @return returns prime number
   */
  public BigInteger getPrimeNumber(int bitLength) {
    return BigInteger.probablePrime(bitLength, new SecureRandom());
  }

  /**
   * Makes hash SHA2-256.
   * 
   * @param message message to hash
   * @return 32B hash of message
   */
  public byte[] hash(byte[] message) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-256");
      md.update(message);
      return md.digest();
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("Hash function does not exits", e);
      return new byte[0];
    }

  }

  /**
   * converts byte array to hexadecimal string.
   * 
   * @param array byte array
   * @return hexadecimal array
   */
  public String byteArrayToString(byte[] array) {

    StringBuffer result = new StringBuffer();
    for (byte b : array) {
      result.append(String.format("%02X", b));
    }
    return result.toString();
  }

  /**
   * Generates MGF1 mask. A Mask Generation Function (MGF) is a cryptographic primitive similar to a
   * Cryptographic hash function except that while a hash function's output is a fixed size, a MGF
   * supports output of a variable length.
   * 
   * @param length length of mask
   * @param message data to hash
   * @return hash
   */
  public byte[] generateMaskMgf1(int length, byte[] message) {
    List<Byte> mask = new ArrayList<>();
    int counter = 0;

    while (mask.size() < length) {
      // Prepare and hash a new chunk of data: H(message || C)
      byte[] concatenatedData = new byte[message.length + 4];
      System.arraycopy(message, 0, concatenatedData, 0, message.length);
      System.arraycopy(i2osp(counter), 0, concatenatedData, message.length - 5, 4);

      byte[] hashedData = hash(concatenatedData);

      // Transform hashed data to list collection and append to mask
      mask.addAll(Stream.of(hashedData).flatMap(d -> {
        List<Byte> tmpList = new ArrayList<>();
        for (byte b : d) {
          tmpList.add(Byte.valueOf(b));
        }
        return tmpList.stream();
      })
          .collect(Collectors.toList()));
      ++counter;
    }

    // transform mask from list to byte array
    byte[] finalMask = new byte[length];
    for (int i = 0; i < length; ++i) {
      finalMask[i] = mask.get(i).byteValue();
    }
    return finalMask;

  }

  private byte[] i2osp(int integer) {
    byte[] stringTable = new byte[4];
    stringTable[0] = (byte) (integer >>> 24);
    stringTable[1] = (byte) (integer >>> 16);
    stringTable[2] = (byte) (integer >>> 8);
    stringTable[3] = (byte) (integer >>> 0);
    return stringTable;
  }

  /**
   * Concatenates two or three arrays.
   * 
   * @param arr1 array 1
   * @param arr2 array 2
   * @param arr3 array 3
   * @return concatenation of arrays
   */
  public byte[] concatenateArrays(byte[] arr1, byte[] arr2, byte[] arr3) {
    int arr1Len = arr1.length;
    int arr2Len = arr2.length;
    int arr3Len = 0;
    if (arr3 != null) {
      arr3Len = arr3.length;
    }

    byte[] concatenatedData = new byte[arr1Len + arr2Len + arr3Len];
    System.arraycopy(arr1, 0, concatenatedData, 0, arr1Len);
    System.arraycopy(arr2, 0, concatenatedData, arr1Len, arr2Len);

    if (arr3Len != 0) {
      System.arraycopy(arr3, 0, concatenatedData, arr1Len + arr2Len, arr3Len);
    }

    return concatenatedData;
  }

  /**
   * Makes xor operation on two arrays.
   * 
   * @param arr1 array 1
   * @param arr2 array 2
   * @return xored arrays
   */
  public byte[] xorArrays(byte[] arr1, byte[] arr2) {
    byte[] xoredArray = new byte[(arr1.length < arr2.length ? arr1.length : arr2.length)];
    int iter = 0;
    for (byte b : arr1) {
      xoredArray[iter] = (byte) (b ^ arr2[iter++]);
    }
    return xoredArray;
  }

  /**
   * Serializes and encodes data to Base64 code.
   * 
   * @param data data to coding
   * @return coded data
   */
  public <T> byte[] serializeAndCodeByte64(T data) {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos)) {
      out.writeObject(data);
      out.flush();
      return Base64.getEncoder().encode(bos.toByteArray());
    } catch (IOException e) {
      return new byte[0];
    }
  }

  /**
   * Decodes data from Base64 code and deserialize object.
   * 
   * @param data to decoding
   * @return deserialized object
   * @throws IOException error during open stream
   * @throws ClassNotFoundException error during deserialization
   */
  @SuppressWarnings("unchecked")
  public <T> T decodeBase64AndDeserialize(byte[] data) throws IOException, ClassNotFoundException {
    byte[] decodedData = Base64.getDecoder().decode(data);
    try (ByteArrayInputStream bis = new ByteArrayInputStream(decodedData);
        ObjectInput in = new ObjectInputStream(bis)) {
      return (T) in.readObject();
    }
  }

}
