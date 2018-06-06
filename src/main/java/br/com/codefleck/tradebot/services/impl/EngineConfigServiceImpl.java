package br.com.codefleck.tradebot.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.codefleck.tradebot.models.engine.EngineConfig;
import br.com.codefleck.tradebot.repository.EngineConfigRepository;
import br.com.codefleck.tradebot.services.EngineConfigService;

/**
 * Implementation of the Engine config service.
 */
@Service("engineConfigService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class EngineConfigServiceImpl implements EngineConfigService {

  private static final Logger LOG = LogManager.getLogger();

  private final EngineConfigRepository engineConfigRepository;

  @Autowired
  public EngineConfigServiceImpl(EngineConfigRepository engineConfigRepository) {
    this.engineConfigRepository = engineConfigRepository;
  }

  @Override
  public EngineConfig getEngineConfig() {
    return engineConfigRepository.get();
  }

  @Override
  public EngineConfig updateEngineConfig(EngineConfig config) {
    LOG.info(() -> "About to update Engine config: " + config);
    return engineConfigRepository.save(config);
  }
}
