package br.com.codefleck.tradebot.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.codefleck.tradebot.domainobjects.emailalerts.EmailAlertsConfig;
import br.com.codefleck.tradebot.repository.EmailAlertsConfigRepository;
import br.com.codefleck.tradebot.services.EmailAlertsConfigService;

/**
 * Implementation of the Email Alerts configuration service.
 */
@Service("emailAlertsConfigService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class EmailAlertsConfigServiceImpl implements EmailAlertsConfigService {

  private static final Logger LOG = LogManager.getLogger();

  private final EmailAlertsConfigRepository emailAlertsConfigRepository;

  @Autowired
  public EmailAlertsConfigServiceImpl(EmailAlertsConfigRepository emailAlertsConfigRepository) {
    this.emailAlertsConfigRepository = emailAlertsConfigRepository;
  }

  @Override
  public EmailAlertsConfig getEmailAlertsConfig() {
    return emailAlertsConfigRepository.get();
  }

  @Override
  public EmailAlertsConfig updateEmailAlertsConfig(EmailAlertsConfig config) {
    LOG.info(() -> "About to update Email Alerts config: " + config);
    return emailAlertsConfigRepository.save(config);
  }
}
