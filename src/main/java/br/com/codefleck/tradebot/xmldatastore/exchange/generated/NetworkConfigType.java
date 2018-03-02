package br.com.codefleck.tradebot.xmldatastore.exchange.generated;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for network-configType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="network-configType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="connection-timeout" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="non-fatal-error-codes" type="{}non-fatal-error-codesType" minOccurs="0"/&gt;
 *         &lt;element name="non-fatal-error-messages" type="{}non-fatal-error-messagesType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "network-configType", propOrder = {
    "connectionTimeout",
    "nonFatalErrorCodes",
    "nonFatalErrorMessages"
})
public class NetworkConfigType {

  @XmlElement(name = "connection-timeout")
  protected Integer connectionTimeout;
  @XmlElement(name = "non-fatal-error-codes")
  protected NonFatalErrorCodesType nonFatalErrorCodes;
  @XmlElement(name = "non-fatal-error-messages")
  protected NonFatalErrorMessagesType nonFatalErrorMessages;

  /**
   * Gets the value of the connectionTimeout property.
   *
   * @return
   *     possible object is
   *     {@link Integer }
   *
   */
  public Integer getConnectionTimeout() {
    return connectionTimeout;
  }

  /**
   * Sets the value of the connectionTimeout property.
   *
   * @param value
   *     allowed object is
   *     {@link Integer }
   *
   */
  public void setConnectionTimeout(Integer value) {
    this.connectionTimeout = value;
  }

  /**
   * Gets the value of the nonFatalErrorCodes property.
   *
   * @return
   *     possible object is
   *     {@link NonFatalErrorCodesType }
   *
   */
  public NonFatalErrorCodesType getNonFatalErrorCodes() {
    return nonFatalErrorCodes;
  }

  /**
   * Sets the value of the nonFatalErrorCodes property.
   *
   * @param value
   *     allowed object is
   *     {@link NonFatalErrorCodesType }
   *
   */
  public void setNonFatalErrorCodes(NonFatalErrorCodesType value) {
    this.nonFatalErrorCodes = value;
  }

  /**
   * Gets the value of the nonFatalErrorMessages property.
   *
   * @return
   *     possible object is
   *     {@link NonFatalErrorMessagesType }
   *
   */
  public NonFatalErrorMessagesType getNonFatalErrorMessages() {
    return nonFatalErrorMessages;
  }

  /**
   * Sets the value of the nonFatalErrorMessages property.
   *
   * @param value
   *     allowed object is
   *     {@link NonFatalErrorMessagesType }
   *
   */
  public void setNonFatalErrorMessages(NonFatalErrorMessagesType value) {
    this.nonFatalErrorMessages = value;
  }

}
