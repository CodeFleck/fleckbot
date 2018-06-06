package br.com.codefleck.tradebot.models.engine;

import java.math.BigDecimal;

import com.google.common.base.MoreObjects;

/**
 * Domain object representing the Engine config.
 */
public class EngineConfig {

  private String botId;
  private String botName;
  private String emergencyStopCurrency;
  private BigDecimal emergencyStopBalance;
  private int tradeCycleInterval;

  // required for jackson
  public EngineConfig() {
  }

  public EngineConfig(String botId, String botName, String emergencyStopCurrency, BigDecimal emergencyStopBalance,
                      int tradeCycleInterval) {

    this.botId = botId;
    this.botName = botName;
    this.emergencyStopCurrency = emergencyStopCurrency;
    this.emergencyStopBalance = emergencyStopBalance;
    this.tradeCycleInterval = tradeCycleInterval;
  }

  public String getBotId() {
    return botId;
  }

  public void setBotId(String botId) {
    this.botId = botId;
  }

  public String getBotName() {
    return botName;
  }

  public void setBotName(String botName) {
    this.botName = botName;
  }

  public String getEmergencyStopCurrency() {
    return emergencyStopCurrency;
  }

  public void setEmergencyStopCurrency(String emergencyStopCurrency) {
    this.emergencyStopCurrency = emergencyStopCurrency;
  }

  public BigDecimal getEmergencyStopBalance() {
    return emergencyStopBalance;
  }

  public void setEmergencyStopBalance(BigDecimal emergencyStopBalance) {
    this.emergencyStopBalance = emergencyStopBalance;
  }

  public int getTradeCycleInterval() {
    return tradeCycleInterval;
  }

  public void setTradeCycleInterval(int tradeCycleInterval) {
    this.tradeCycleInterval = tradeCycleInterval;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("botId", botId)
        .add("botName", botName)
        .add("emergencyStopCurrency", emergencyStopCurrency)
        .add("emergencyStopBalance", emergencyStopBalance)
        .add("tradeCycleInterval", tradeCycleInterval)
        .toString();
  }
}
