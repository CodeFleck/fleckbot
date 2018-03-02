package br.com.codefleck.tradebot.domainobjects.exchange;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.MoreObjects;

/**
 * Domain object representing the Exchange Network config.
 */
public class NetworkConfig {

  private Integer connectionTimeout;
  private List<Integer> nonFatalErrorCodes;
  private List<String> nonFatalErrorMessages;


  public NetworkConfig() {
    nonFatalErrorCodes = new ArrayList<>();
    nonFatalErrorMessages = new ArrayList<>();
  }

  public Integer getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(Integer connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public List<Integer> getNonFatalErrorCodes() {
    return nonFatalErrorCodes;
  }

  public void setNonFatalErrorCodes(List<Integer> nonFatalErrorCodes) {
    this.nonFatalErrorCodes = nonFatalErrorCodes;
  }

  public List<String> getNonFatalErrorMessages() {
    return nonFatalErrorMessages;
  }

  public void setNonFatalErrorMessages(List<String> nonFatalErrorMessages) {
    this.nonFatalErrorMessages = nonFatalErrorMessages;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("connectionTimeout", connectionTimeout)
        .add("nonFatalErrorCodes", nonFatalErrorCodes)
        .add("nonFatalErrorMessages", nonFatalErrorMessages)
        .toString();
  }
}
