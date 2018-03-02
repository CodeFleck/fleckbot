package br.com.codefleck.tradebot.repository;

import java.util.List;

import br.com.codefleck.tradebot.domainobjects.strategy.StrategyConfig;

/**
 * The Strategy configuration repository.
 */
public interface StrategyConfigRepository {

  List<StrategyConfig> findAll();

  StrategyConfig findById(String id);

  StrategyConfig save(StrategyConfig config);

  StrategyConfig delete(String id);
}
