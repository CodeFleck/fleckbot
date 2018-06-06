package br.com.codefleck.tradebot.services;

import br.com.codefleck.tradebot.models.exchange.ExchangeConfig;

/**
 * The Exchange configuration service.
 */
public interface ExchangeConfigService {

  ExchangeConfig getExchangeConfig();

  ExchangeConfig updateExchangeConfig(ExchangeConfig config);
}
