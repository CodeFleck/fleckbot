package br.com.codefleck.tradebot.repository;

import br.com.codefleck.tradebot.models.engine.EngineConfig;

/**
 * The Engine configuration repository.
 */
public interface EngineConfigRepository {

  EngineConfig get();

  EngineConfig save(EngineConfig config);
}
