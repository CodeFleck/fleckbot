package br.com.codefleck.tradebot.repository;

import br.com.codefleck.tradebot.domainobjects.exchange.ExchangeConfig;

/**
 * The Exchange configuration repository.
 */
public interface ExchangeConfigRepository {

  ExchangeConfig get();

  ExchangeConfig save(ExchangeConfig config);
}
