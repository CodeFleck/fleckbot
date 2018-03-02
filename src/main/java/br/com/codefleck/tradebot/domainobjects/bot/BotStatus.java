package br.com.codefleck.tradebot.domainobjects.bot;

import com.google.common.base.MoreObjects;

/**
 * Domain object representing the Bot's status.
 */
public class BotStatus {

  private String botId;
  private String displayName;
  private String status;

  // required for jackson
  public BotStatus() {
  }

  public BotStatus(String botId, String displayName, String status) {

    this.botId = botId;
    this.displayName = displayName;
    this.status = status;
  }

  public String getBotId() {
    return botId;
  }

  public void setBotId(String botId) {
    this.botId = botId;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("botId", botId)
        .add("displayName", displayName)
        .add("status", status)
        .toString();
  }
}
