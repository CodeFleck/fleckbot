package br.com.codefleck.tradebot.xmldatastore.emailalerts.generated;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for smtp-configType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="smtp-configType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="smtp-host">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="smtp-tls-port">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="account-username">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="account-password">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="from-addr">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="to-addr">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "smtp-configType", propOrder = {
    "smtpHost",
    "smtpTlsPort",
    "accountUsername",
    "accountPassword",
    "fromAddr",
    "toAddr"
})
public class SmtpConfigType {

  @XmlElement(name = "smtp-host", required = true)
  protected String smtpHost;
  @XmlElement(name = "smtp-tls-port")
  protected int smtpTlsPort;
  @XmlElement(name = "account-username", required = true)
  protected String accountUsername;
  @XmlElement(name = "account-password", required = true)
  protected String accountPassword;
  @XmlElement(name = "from-addr", required = true)
  protected String fromAddr;
  @XmlElement(name = "to-addr", required = true)
  protected String toAddr;

  /**
   * Gets the value of the smtpHost property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getSmtpHost() {
    return smtpHost;
  }

  /**
   * Sets the value of the smtpHost property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setSmtpHost(String value) {
    this.smtpHost = value;
  }

  /**
   * Gets the value of the smtpTlsPort property.
   *
   */
  public int getSmtpTlsPort() {
    return smtpTlsPort;
  }

  /**
   * Sets the value of the smtpTlsPort property.
   *
   */
  public void setSmtpTlsPort(int value) {
    this.smtpTlsPort = value;
  }

  /**
   * Gets the value of the accountUsername property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getAccountUsername() {
    return accountUsername;
  }

  /**
   * Sets the value of the accountUsername property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setAccountUsername(String value) {
    this.accountUsername = value;
  }

  /**
   * Gets the value of the accountPassword property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getAccountPassword() {
    return accountPassword;
  }

  /**
   * Sets the value of the accountPassword property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setAccountPassword(String value) {
    this.accountPassword = value;
  }

  /**
   * Gets the value of the fromAddr property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getFromAddr() {
    return fromAddr;
  }

  /**
   * Sets the value of the fromAddr property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setFromAddr(String value) {
    this.fromAddr = value;
  }

  /**
   * Gets the value of the toAddr property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getToAddr() {
    return toAddr;
  }

  /**
   * Sets the value of the toAddr property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setToAddr(String value) {
    this.toAddr = value;
  }

}
