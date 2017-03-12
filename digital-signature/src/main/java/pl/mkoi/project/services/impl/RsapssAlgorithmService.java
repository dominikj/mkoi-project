package pl.mkoi.project.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.services.SignatureAlgorithmService;

@Component
public class RsapssAlgorithmService implements SignatureAlgorithmService {

  private final CryptoFacade cryptoUtils;
  private static final Logger LOGGER = LoggerFactory.getLogger(RsapssAlgorithmService.class);

  @Autowired
  public RsapssAlgorithmService(CryptoFacade cryptoUtils) {
    this.cryptoUtils = cryptoUtils;
  }

  @Override
  public String signFile(byte[] file) {
    LOGGER.info("Liczba liczb względnie pierwszych: {}", cryptoUtils
        .eulerFunction(cryptoUtils.getPrimeNumber(512, 4), cryptoUtils.getPrimeNumber(512, 4)));
    return null;
  }

}
