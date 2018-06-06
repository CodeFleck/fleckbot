package br.com.codefleck.tradebot.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.codefleck.tradebot.models.exchange.ExchangeConfig;
import br.com.codefleck.tradebot.repository.ExchangeConfigRepository;
import br.com.codefleck.tradebot.services.ExchangeConfigService;

/**
 * Implementation of the Exchange config service.
 */
@Service("exchangeConfigService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class ExchangeConfigServiceImpl implements ExchangeConfigService {

  private static final Logger LOG = LogManager.getLogger();

  private final ExchangeConfigRepository exchangeConfigRepository;

  @Autowired
  public ExchangeConfigServiceImpl(ExchangeConfigRepository exchangeConfigRepository) {
    this.exchangeConfigRepository = exchangeConfigRepository;
  }

  @Override
  public ExchangeConfig getExchangeConfig() {
    return exchangeConfigRepository.get();
  }

  @Override
  public ExchangeConfig updateExchangeConfig(ExchangeConfig config) {
    LOG.info(() -> "About to update Exchange config: " + config);
    return exchangeConfigRepository.save(config);
  }
}

