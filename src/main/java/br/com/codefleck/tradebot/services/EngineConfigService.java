package br.com.codefleck.tradebot.services;

import br.com.codefleck.tradebot.models.engine.EngineConfig;

/**
 * The Engine configuration service.
 */
public interface EngineConfigService {

  EngineConfig getEngineConfig();

  EngineConfig updateEngineConfig(EngineConfig config);
}
