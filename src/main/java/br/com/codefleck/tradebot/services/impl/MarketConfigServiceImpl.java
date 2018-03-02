package br.com.codefleck.tradebot.services.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.codefleck.tradebot.domainobjects.market.MarketConfig;
import br.com.codefleck.tradebot.repository.MarketConfigRepository;
import br.com.codefleck.tradebot.services.MarketConfigService;

/**
 * Implementation of the Market config service.
 */
@Service("marketConfigService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class MarketConfigServiceImpl implements MarketConfigService {

  private static final Logger LOG = LogManager.getLogger();

  private final MarketConfigRepository marketConfigRepository;

  @Autowired
  public MarketConfigServiceImpl(MarketConfigRepository marketConfigRepository) {
    this.marketConfigRepository = marketConfigRepository;
  }

  @Override
  public List<MarketConfig> getAllMarketConfig() {
    return marketConfigRepository.findAll();
  }

  @Override
  public MarketConfig getMarketConfig(String id) {
    LOG.info(() -> "Fetching Market config for id: " + id);
    return marketConfigRepository.findById(id);
  }

  @Override
  public MarketConfig updateMarketConfig(MarketConfig config) {
    LOG.info(() -> "About to update Market config: " + config);
    return marketConfigRepository.save(config);
  }

  @Override
  public MarketConfig createMarketConfig(MarketConfig config) {
    LOG.info(() -> "About to create Market config: " + config);
    return marketConfigRepository.save(config);
  }

  @Override
  public MarketConfig deleteMarketConfig(String id) {
    LOG.info(() -> "About to delete Market config for id: " + id);
    return marketConfigRepository.delete(id);
  }
}
