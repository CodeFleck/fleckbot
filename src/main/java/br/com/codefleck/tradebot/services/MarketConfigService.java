package br.com.codefleck.tradebot.services;

import java.util.List;

import br.com.codefleck.tradebot.models.market.MarketConfig;

/**
 * The Market configuration service.
 */
public interface MarketConfigService {

  List<MarketConfig> getAllMarketConfig();

  MarketConfig getMarketConfig(String id);

  MarketConfig createMarketConfig(MarketConfig config);

  MarketConfig updateMarketConfig(MarketConfig config);

  MarketConfig deleteMarketConfig(String id);
}
