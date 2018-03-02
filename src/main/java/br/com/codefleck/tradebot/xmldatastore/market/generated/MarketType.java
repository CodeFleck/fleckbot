package br.com.codefleck.tradebot.xmldatastore.market.generated;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for marketType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="marketType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="name"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="base-currency"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="counter-currency"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="trading-strategy-id"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[a-zA-Z0-9/_\- ]*"/&gt;
 *               &lt;minLength value="1"/&gt;
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
@XmlType(name = "marketType", propOrder = {
    "id",
    "name",
    "baseCurrency",
    "counterCurrency",
    "enabled",
    "tradingStrategyId"
})
public class MarketType {

  @XmlElement(required = true)
  protected String id;
  @XmlElement(required = true)
  protected String name;
  @XmlElement(name = "base-currency", required = true)
  protected String baseCurrency;
  @XmlElement(name = "counter-currency", required = true)
  protected String counterCurrency;
  protected boolean enabled;
  @XmlElement(name = "trading-strategy-id", required = true)
  protected String tradingStrategyId;

  /**
   * Gets the value of the id property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the value of the id property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setId(String value) {
    this.id = value;
  }

  /**
   * Gets the value of the name property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the value of the name property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setName(String value) {
    this.name = value;
  }

  /**
   * Gets the value of the baseCurrency property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getBaseCurrency() {
    return baseCurrency;
  }

  /**
   * Sets the value of the baseCurrency property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setBaseCurrency(String value) {
    this.baseCurrency = value;
  }

  /**
   * Gets the value of the counterCurrency property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getCounterCurrency() {
    return counterCurrency;
  }

  /**
   * Sets the value of the counterCurrency property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setCounterCurrency(String value) {
    this.counterCurrency = value;
  }

  /**
   * Gets the value of the enabled property.
   *
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Sets the value of the enabled property.
   *
   */
  public void setEnabled(boolean value) {
    this.enabled = value;
  }

  /**
   * Gets the value of the tradingStrategyId property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getTradingStrategyId() {
    return tradingStrategyId;
  }

  /**
   * Sets the value of the tradingStrategyId property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setTradingStrategyId(String value) {
    this.tradingStrategyId = value;
  }

}
