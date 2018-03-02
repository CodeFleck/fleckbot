package br.com.codefleck.tradebot.repository.impl;

import static br.com.codefleck.tradebot.xmldatastore.FileLocations.EMAIL_ALERTS_CONFIG_XML_FILENAME;
import static br.com.codefleck.tradebot.xmldatastore.FileLocations.EMAIL_ALERTS_CONFIG_XSD_FILENAME;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.codefleck.tradebot.domainobjects.emailalerts.EmailAlertsConfig;
import br.com.codefleck.tradebot.domainobjects.emailalerts.SmtpConfig;
import br.com.codefleck.tradebot.repository.EmailAlertsConfigRepository;
import br.com.codefleck.tradebot.xmldatastore.ConfigurationManager;
import br.com.codefleck.tradebot.xmldatastore.emailalerts.generated.EmailAlertsType;
import br.com.codefleck.tradebot.xmldatastore.emailalerts.generated.SmtpConfigType;

/**
 * An XML datastore implementation of the Email Alerts configuration repository.
 */
@Repository("emailAlertsConfigRepository")
@Transactional
public class EmailAlertsConfigRepositoryXmlDatastore implements EmailAlertsConfigRepository {

  private static final Logger LOG = LogManager.getLogger();

  @Override
  public EmailAlertsConfig get() {

    LOG.info(() -> "Fetching EmailAlertsConfig...");

    final EmailAlertsType internalEmailAlertsConfig = ConfigurationManager.loadConfig(EmailAlertsType.class,
        EMAIL_ALERTS_CONFIG_XML_FILENAME, EMAIL_ALERTS_CONFIG_XSD_FILENAME);
    return adaptInternalToExternalConfig(internalEmailAlertsConfig);
  }

  @Override
  public EmailAlertsConfig save(EmailAlertsConfig config) {

    LOG.info(() -> "About to save EmailAlertsConfig: " + config);

    final EmailAlertsType internalEmailAlertsConfig = adaptExternalToInternalConfig(config);
    ConfigurationManager.saveConfig(EmailAlertsType.class, internalEmailAlertsConfig, EMAIL_ALERTS_CONFIG_XML_FILENAME);

    final EmailAlertsType savedEmailAlertsConfig = ConfigurationManager.loadConfig(EmailAlertsType.class,
        EMAIL_ALERTS_CONFIG_XML_FILENAME, EMAIL_ALERTS_CONFIG_XSD_FILENAME);
    return adaptInternalToExternalConfig(savedEmailAlertsConfig);
  }

  // ------------------------------------------------------------------------------------------------
  // Adapter methods
  // ------------------------------------------------------------------------------------------------

  private static EmailAlertsConfig adaptInternalToExternalConfig(EmailAlertsType internalEmailAlertsConfig) {

    final SmtpConfig smtpConfig = new SmtpConfig();
    smtpConfig.setHost(internalEmailAlertsConfig.getSmtpConfig().getSmtpHost());
    smtpConfig.setTlsPort(internalEmailAlertsConfig.getSmtpConfig().getSmtpTlsPort());
    smtpConfig.setToAddress(internalEmailAlertsConfig.getSmtpConfig().getToAddr());
    smtpConfig.setFromAddress(internalEmailAlertsConfig.getSmtpConfig().getFromAddr());
    smtpConfig.setAccountUsername(internalEmailAlertsConfig.getSmtpConfig().getAccountUsername());
    smtpConfig.setAccountPassword(internalEmailAlertsConfig.getSmtpConfig().getAccountPassword());

    final EmailAlertsConfig emailAlertsConfig = new EmailAlertsConfig();
    emailAlertsConfig.setEnabled(internalEmailAlertsConfig.isEnabled());
    emailAlertsConfig.setSmtpConfig(smtpConfig);
    return emailAlertsConfig;
  }

  private static EmailAlertsType adaptExternalToInternalConfig(EmailAlertsConfig externalEmailAlertsConfig) {

    final SmtpConfigType smtpConfig = new SmtpConfigType();
    smtpConfig.setSmtpHost(externalEmailAlertsConfig.getSmtpConfig().getHost());
    smtpConfig.setSmtpTlsPort(externalEmailAlertsConfig.getSmtpConfig().getTlsPort());
    smtpConfig.setAccountUsername(externalEmailAlertsConfig.getSmtpConfig().getAccountUsername());
    smtpConfig.setAccountPassword(externalEmailAlertsConfig.getSmtpConfig().getAccountPassword());
    smtpConfig.setFromAddr(externalEmailAlertsConfig.getSmtpConfig().getFromAddress());
    smtpConfig.setToAddr(externalEmailAlertsConfig.getSmtpConfig().getToAddress());

    final EmailAlertsType emailAlertsConfig = new EmailAlertsType();
    emailAlertsConfig.setEnabled(externalEmailAlertsConfig.isEnabled());
    emailAlertsConfig.setSmtpConfig(smtpConfig);
    return emailAlertsConfig;
  }
}
