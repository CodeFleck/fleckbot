package br.com.codefleck.tradebot.repository;

import java.util.List;

import br.com.codefleck.tradebot.domainobjects.market.MarketConfig;

/**
 * The Market configuration repository.
 */
public interface MarketConfigRepository {

  List<MarketConfig> findAll();

  MarketConfig findById(String id);

  MarketConfig save(MarketConfig config);

  MarketConfig delete(String id);
}
