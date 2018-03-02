package br.com.codefleck.tradebot.domainobjects.emailalerts;

import com.google.common.base.MoreObjects;

/**
 * Domain object representing the SMTP config used for Email Alerts.
 */
public class SmtpConfig {

  private String host;
  private int tlsPort;
  private String accountUsername;
  private String accountPassword;
  private String fromAddress;
  private String toAddress;

  // required for jackson
  public SmtpConfig() {
  }

  public SmtpConfig(String host, int tlsPort, String accountUsername, String accountPassword, String fromAddress, String toAddress) {
    this.host = host;
    this.tlsPort = tlsPort;
    this.accountUsername = accountUsername;
    this.accountPassword = accountPassword;
    this.fromAddress = fromAddress;
    this.toAddress = toAddress;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getTlsPort() {
    return tlsPort;
  }

  public void setTlsPort(int tlsPort) {
    this.tlsPort = tlsPort;
  }

  public String getAccountUsername() {
    return accountUsername;
  }

  public void setAccountUsername(String accountUsername) {
    this.accountUsername = accountUsername;
  }

  public String getAccountPassword() {
    return accountPassword;
  }

  public void setAccountPassword(String accountPassword) {
    this.accountPassword = accountPassword;
  }

  public String getFromAddress() {
    return fromAddress;
  }

  public void setFromAddress(String fromAddress) {
    this.fromAddress = fromAddress;
  }

  public String getToAddress() {
    return toAddress;
  }

  public void setToAddress(String toAddress) {
    this.toAddress = toAddress;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("host", host)
        .add("tlsPort", tlsPort)
        .add("accountUsername", accountUsername)
        // Careful with the password!
//                .add("accountPassword", accountPassword)
        .add("fromAddress", fromAddress)
        .add("toAddress", toAddress)
        .toString();
  }
}
