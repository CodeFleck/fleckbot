package br.com.codefleck.tradebot.xmldatastore.engine.generated;


import java.math.BigDecimal;

import javax.xml.bind.annotation.*;

/**
 * <p>Java class for engineType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="engineType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="bot-id"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[a-zA-Z0-9/_\- ]*"/&gt;
 *               &lt;minLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="bot-name"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="emergency-stop-currency"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="emergency-stop-balance"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="trade-cycle-interval"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;minInclusive value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "engineType", propOrder = {
    "botId",
    "botName",
    "emergencyStopCurrency",
    "emergencyStopBalance",
    "tradeCycleInterval"
})
@XmlRootElement(name="engine")
public class EngineType {

  @XmlElement(name = "bot-id", required = true)
  protected String botId;
  @XmlElement(name = "bot-name", required = true)
  protected String botName;
  @XmlElement(name = "emergency-stop-currency", required = true)
  protected String emergencyStopCurrency;
  @XmlElement(name = "emergency-stop-balance", required = true)
  protected BigDecimal emergencyStopBalance;
  @XmlElement(name = "trade-cycle-interval")
  protected int tradeCycleInterval;

  /**
   * Gets the value of the botId property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getBotId() {
    return botId;
  }

  /**
   * Sets the value of the botId property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setBotId(String value) {
    this.botId = value;
  }

  /**
   * Gets the value of the botName property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getBotName() {
    return botName;
  }

  /**
   * Sets the value of the botName property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setBotName(String value) {
    this.botName = value;
  }

  /**
   * Gets the value of the emergencyStopCurrency property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getEmergencyStopCurrency() {
    return emergencyStopCurrency;
  }

  /**
   * Sets the value of the emergencyStopCurrency property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setEmergencyStopCurrency(String value) {
    this.emergencyStopCurrency = value;
  }

  /**
   * Gets the value of the emergencyStopBalance property.
   *
   * @return
   *     possible object is
   *     {@link BigDecimal }
   *
   */
  public BigDecimal getEmergencyStopBalance() {
    return emergencyStopBalance;
  }

  /**
   * Sets the value of the emergencyStopBalance property.
   *
   * @param value
   *     allowed object is
   *     {@link BigDecimal }
   *
   */
  public void setEmergencyStopBalance(BigDecimal value) {
    this.emergencyStopBalance = value;
  }

  /**
   * Gets the value of the tradeCycleInterval property.
   *
   */
  public int getTradeCycleInterval() {
    return tradeCycleInterval;
  }

  /**
   * Sets the value of the tradeCycleInterval property.
   *
   */
  public void setTradeCycleInterval(int value) {
    this.tradeCycleInterval = value;
  }

}

