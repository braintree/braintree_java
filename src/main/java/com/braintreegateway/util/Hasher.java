package com.braintreegateway.util;

public interface Hasher {

  public String hmacHash(String privateKey, String content, String shaAlgorithm);

  public byte[] shaTypeBytes(String string, String shaAlgorithm);

}
