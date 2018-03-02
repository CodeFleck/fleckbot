package br.com.codefleck.tradebot.xmldatastore.exchange.generated;


import javax.xml.bind.annotation.*;

/**
 * <p>Java class for exchangeType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="exchangeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="adapter"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="authentication-config" type="{}authentication-configType" minOccurs="0"/&gt;
 *         &lt;element name="network-config" type="{}network-configType" minOccurs="0"/&gt;
 *         &lt;element name="optional-config" type="{}optional-configType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "exchangeType", propOrder = {
    "name",
    "adapter",
    "authenticationConfig",
    "networkConfig",
    "optionalConfig"
})
@XmlRootElement(name="exchange")
public class ExchangeType {

  @XmlElement(required = true)
  protected String name;
  @XmlElement(required = true)
  protected String adapter;
  @XmlElement(name = "authentication-config")
  protected AuthenticationConfigType authenticationConfig;
  @XmlElement(name = "network-config")
  protected NetworkConfigType networkConfig;
  @XmlElement(name = "optional-config")
  protected OptionalConfigType optionalConfig;

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
   * Gets the value of the adapter property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getAdapter() {
    return adapter;
  }

  /**
   * Sets the value of the adapter property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setAdapter(String value) {
    this.adapter = value;
  }

  /**
   * Gets the value of the authenticationConfig property.
   *
   * @return
   *     possible object is
   *     {@link AuthenticationConfigType }
   *
   */
  public AuthenticationConfigType getAuthenticationConfig() {
    return authenticationConfig;
  }

  /**
   * Sets the value of the authenticationConfig property.
   *
   * @param value
   *     allowed object is
   *     {@link AuthenticationConfigType }
   *
   */
  public void setAuthenticationConfig(AuthenticationConfigType value) {
    this.authenticationConfig = value;
  }

  /**
   * Gets the value of the networkConfig property.
   *
   * @return
   *     possible object is
   *     {@link NetworkConfigType }
   *
   */
  public NetworkConfigType getNetworkConfig() {
    return networkConfig;
  }

  /**
   * Sets the value of the networkConfig property.
   *
   * @param value
   *     allowed object is
   *     {@link NetworkConfigType }
   *
   */
  public void setNetworkConfig(NetworkConfigType value) {
    this.networkConfig = value;
  }

  /**
   * Gets the value of the optionalConfig property.
   *
   * @return
   *     possible object is
   *     {@link OptionalConfigType }
   *
   */
  public OptionalConfigType getOptionalConfig() {
    return optionalConfig;
  }

  /**
   * Sets the value of the optionalConfig property.
   *
   * @param value
   *     allowed object is
   *     {@link OptionalConfigType }
   *
   */
  public void setOptionalConfig(OptionalConfigType value) {
    this.optionalConfig = value;
  }

}
