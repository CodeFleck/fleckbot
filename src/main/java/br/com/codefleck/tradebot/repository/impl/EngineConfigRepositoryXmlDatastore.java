package br.com.codefleck.tradebot.repository.impl;

import static br.com.codefleck.tradebot.xmldatastore.FileLocations.ENGINE_CONFIG_XML_FILENAME;
import static br.com.codefleck.tradebot.xmldatastore.FileLocations.ENGINE_CONFIG_XSD_FILENAME;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.codefleck.tradebot.models.engine.EngineConfig;
import br.com.codefleck.tradebot.repository.EngineConfigRepository;
import br.com.codefleck.tradebot.xmldatastore.ConfigurationManager;
import br.com.codefleck.tradebot.xmldatastore.engine.generated.EngineType;

/**
 * An XML datastore implementation of the Engine config repository.
 */
@Repository("engineConfigRepository")
@Transactional
public class EngineConfigRepositoryXmlDatastore implements EngineConfigRepository {

  private static final Logger LOG = LogManager.getLogger();

  @Override
  public EngineConfig get() {

    LOG.info(() -> "Fetching EngineConfig...");

    final EngineType internalEngineConfig = ConfigurationManager.loadConfig(EngineType.class,
        ENGINE_CONFIG_XML_FILENAME, ENGINE_CONFIG_XSD_FILENAME);
    return adaptInternalToExternalConfig(internalEngineConfig);
  }

  @Override
  public EngineConfig save(EngineConfig config) {

    LOG.info(() -> "About to save EngineConfig: " + config);

    final EngineType internalEngineConfig = adaptExternalToInternalConfig(config);
    ConfigurationManager.saveConfig(EngineType.class, internalEngineConfig, ENGINE_CONFIG_XML_FILENAME);

    final EngineType savedEngineConfig = ConfigurationManager.loadConfig(EngineType.class,
        ENGINE_CONFIG_XML_FILENAME, ENGINE_CONFIG_XSD_FILENAME);
    return adaptInternalToExternalConfig(savedEngineConfig);
  }

  // ------------------------------------------------------------------------------------------------
  // Adapter methods
  // ------------------------------------------------------------------------------------------------

  private static EngineConfig adaptInternalToExternalConfig(EngineType internalEngineConfig) {

    final EngineConfig externalEngineConfig = new EngineConfig();
    externalEngineConfig.setBotId(internalEngineConfig.getBotId());
    externalEngineConfig.setBotName(internalEngineConfig.getBotName());
    externalEngineConfig.setEmergencyStopCurrency(internalEngineConfig.getEmergencyStopCurrency());
    externalEngineConfig.setEmergencyStopBalance(internalEngineConfig.getEmergencyStopBalance());
    externalEngineConfig.setTradeCycleInterval(internalEngineConfig.getTradeCycleInterval());
    return externalEngineConfig;
  }

  private static EngineType adaptExternalToInternalConfig(EngineConfig externalEngineConfig) {

    final EngineType internalEngineConfig = new EngineType();
    internalEngineConfig.setBotId(externalEngineConfig.getBotId());
    internalEngineConfig.setBotName(externalEngineConfig.getBotName());
    internalEngineConfig.setEmergencyStopCurrency(externalEngineConfig.getEmergencyStopCurrency());
    internalEngineConfig.setEmergencyStopBalance(externalEngineConfig.getEmergencyStopBalance());
    internalEngineConfig.setTradeCycleInterval(externalEngineConfig.getTradeCycleInterval());
    return internalEngineConfig;
  }
}
