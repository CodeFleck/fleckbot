package br.com.codefleck.tradebot.core.config.exchange;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.MoreObjects;

import br.com.codefleck.tradebot.exchangeapi.NetworkConfig;

/**
 * Exchange API Network config.
 */
public class NetworkConfigImpl implements NetworkConfig {

  private Integer connectionTimeout;
  private List<Integer> nonFatalErrorCodes;
  private List<String> nonFatalErrorMessages;

  public NetworkConfigImpl() {
    nonFatalErrorCodes = new ArrayList<>();
    nonFatalErrorMessages = new ArrayList<>();
  }

  @Override
  public Integer getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(Integer connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  @Override
  public List<Integer> getNonFatalErrorCodes() {
    return nonFatalErrorCodes;
  }

  public void setNonFatalErrorCodes(List<Integer> nonFatalErrorCodes) {
    this.nonFatalErrorCodes = nonFatalErrorCodes;
  }

  @Override
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

