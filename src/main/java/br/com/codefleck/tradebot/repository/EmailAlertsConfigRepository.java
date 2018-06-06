package br.com.codefleck.tradebot.repository;

import br.com.codefleck.tradebot.models.emailalerts.EmailAlertsConfig;

/**
 * The Email Alerts configuration repository.
 */
public interface EmailAlertsConfigRepository {

  EmailAlertsConfig get();

  EmailAlertsConfig save(EmailAlertsConfig config);
}
