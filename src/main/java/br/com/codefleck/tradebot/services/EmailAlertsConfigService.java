package br.com.codefleck.tradebot.services;

import br.com.codefleck.tradebot.domainobjects.emailalerts.EmailAlertsConfig;

/**
 * The Email Alerts configuration service.
 */
public interface EmailAlertsConfigService {

  EmailAlertsConfig getEmailAlertsConfig();

  EmailAlertsConfig updateEmailAlertsConfig(EmailAlertsConfig config);
}
