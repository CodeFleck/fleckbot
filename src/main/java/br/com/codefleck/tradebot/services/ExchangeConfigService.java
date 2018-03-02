package br.com.codefleck.tradebot.services;

import br.com.codefleck.tradebot.domainobjects.exchange.ExchangeConfig;

/**
 * The Exchange configuration service.
 */
public interface ExchangeConfigService {

  ExchangeConfig getExchangeConfig();

  ExchangeConfig updateExchangeConfig(ExchangeConfig config);
}
