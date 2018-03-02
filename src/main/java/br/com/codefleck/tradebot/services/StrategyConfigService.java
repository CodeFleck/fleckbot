package br.com.codefleck.tradebot.services;

import java.util.List;

import br.com.codefleck.tradebot.domainobjects.strategy.StrategyConfig;

/**
 * The Strategy configuration service.
 */
public interface StrategyConfigService {

  List<StrategyConfig> getAllStrategyConfig();

  StrategyConfig getStrategyConfig(String id);

  StrategyConfig updateStrategyConfig(StrategyConfig config);

  StrategyConfig createStrategyConfig(StrategyConfig config);

  StrategyConfig deleteStrategyConfig(String id);
}
