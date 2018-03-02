package br.com.codefleck.tradebot.domainobjects.emailalerts;

import com.google.common.base.MoreObjects;

/**
 * Domain object representing the Email Alerts config.
 */
public class EmailAlertsConfig {

  private boolean enabled;
  private SmtpConfig smtpConfig;

  // required for jackson
  public EmailAlertsConfig() {
  }

  public EmailAlertsConfig(boolean enabled, SmtpConfig smtpConfig) {
    this.enabled = enabled;
    this.smtpConfig = smtpConfig;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public SmtpConfig getSmtpConfig() {
    return smtpConfig;
  }

  public void setSmtpConfig(SmtpConfig smtpConfig) {
    this.smtpConfig = smtpConfig;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("enabled", enabled)
        .add("smtpConfig", smtpConfig)
        .toString();
  }
}
