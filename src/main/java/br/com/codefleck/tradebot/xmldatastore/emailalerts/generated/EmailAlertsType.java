package br.com.codefleck.tradebot.xmldatastore.emailalerts.generated;


import javax.xml.bind.annotation.*;

/**
 * <p>Java class for email-alertsType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="email-alertsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="smtp-config" type="{}smtp-configType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "email-alertsType", propOrder = {
    "enabled",
    "smtpConfig"
})
@XmlRootElement(name="email-alerts")
public class EmailAlertsType {

  protected boolean enabled;
  @XmlElement(name = "smtp-config")
  protected SmtpConfigType smtpConfig;

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
   * Gets the value of the smtpConfig property.
   *
   * @return
   *     possible object is
   *     {@link SmtpConfigType }
   *
   */
  public SmtpConfigType getSmtpConfig() {
    return smtpConfig;
  }

  /**
   * Sets the value of the smtpConfig property.
   *
   * @param value
   *     allowed object is
   *     {@link SmtpConfigType }
   *
   */
  public void setSmtpConfig(SmtpConfigType value) {
    this.smtpConfig = value;
  }

}
