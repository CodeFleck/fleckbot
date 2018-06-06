package br.com.codefleck.tradebot.services.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.codefleck.tradebot.models.strategy.StrategyConfig;
import br.com.codefleck.tradebot.repository.StrategyConfigRepository;
import br.com.codefleck.tradebot.services.StrategyConfigService;

/**
 * Implementation of the Strategy config service.
 */
@Service("strategyConfigService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class StrategyConfigServiceImpl implements StrategyConfigService {

  private static final Logger LOG = LogManager.getLogger();

  private final StrategyConfigRepository strategyConfigRepository;

  @Autowired
  public StrategyConfigServiceImpl(StrategyConfigRepository strategyConfigRepository) {
    this.strategyConfigRepository = strategyConfigRepository;
  }

  @Override
  public List<StrategyConfig> getAllStrategyConfig() {
    return strategyConfigRepository.findAll();
  }

  @Override
  public StrategyConfig getStrategyConfig(String id) {
    LOG.info(() -> "Fetching Strategy config for id: " + id);
    return strategyConfigRepository.findById(id);
  }

  @Override
  public StrategyConfig updateStrategyConfig(StrategyConfig config) {
    LOG.info(() -> "About to update Strategy config: " + config);
    return strategyConfigRepository.save(config);
  }

  @Override
  public StrategyConfig createStrategyConfig(StrategyConfig config) {
    LOG.info(() -> "About to create Strategy config: " + config);
    return strategyConfigRepository.save(config);
  }

  @Override
  public StrategyConfig deleteStrategyConfig(String id) {
    LOG.info(() -> "About to delete Strategy config for id: " + id);
    return strategyConfigRepository.delete(id);
  }
}
