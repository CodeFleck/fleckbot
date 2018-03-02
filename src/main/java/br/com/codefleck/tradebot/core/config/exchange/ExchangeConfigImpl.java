package br.com.codefleck.tradebot.core.config.exchange;

import com.google.common.base.MoreObjects;

import br.com.codefleck.tradebot.exchangeapi.AuthenticationConfig;
import br.com.codefleck.tradebot.exchangeapi.ExchangeConfig;
import br.com.codefleck.tradebot.exchangeapi.NetworkConfig;
import br.com.codefleck.tradebot.exchangeapi.OptionalConfig;

/**
 * Exchange API Exchange config.
 *
 */
public class ExchangeConfigImpl implements ExchangeConfig {

  private String exchangeName;
  private String exchangeAdapter;
  private AuthenticationConfig authenticationConfig;
  private NetworkConfig networkConfig;
  private OptionalConfig optionalConfig;

  @Override
  public String getExchangeName() {
    return exchangeName;
  }

  public void setExchangeName(String exchangeName) {
    this.exchangeName = exchangeName;
  }

  @Override
  public String getExchangeAdapter() {
    return exchangeAdapter;
  }

  public void setExchangeAdapter(String exchangeAdapter) {
    this.exchangeAdapter = exchangeAdapter;
  }

  @Override
  public AuthenticationConfig getAuthenticationConfig() {
    return authenticationConfig;
  }

  public void setAuthenticationConfig(AuthenticationConfig authenticationConfig) {
    this.authenticationConfig = authenticationConfig;
  }

  public void setNetworkConfig(NetworkConfig networkConfig) {
    this.networkConfig = networkConfig;
  }

  @Override
  public NetworkConfig getNetworkConfig() {
    return networkConfig;
  }

  public OptionalConfig getOptionalConfig() {
    return optionalConfig;
  }

  public void setOptionalConfig(OptionalConfig optionalConfig) {
    this.optionalConfig = optionalConfig;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("exchangeName", exchangeName)
        .add("exchangeAdapter", exchangeAdapter)
        // WARNING - careful showing this!
        //.add("authenticationConfig", authenticationConfig)
        .add("networkConfig", networkConfig)
        .add("optionalConfig", optionalConfig)
        .toString();
  }
}
