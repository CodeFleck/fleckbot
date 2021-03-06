package br.com.codefleck.tradebot.xmldatastore.strategy.generated;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for strategyType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="strategyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[a-zA-Z0-9/_\- ]*"/&gt;
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
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="class-name"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
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
@XmlType(name = "strategyType", propOrder = {
    "id",
    "name",
    "description",
    "className",
    "optionalConfig"
})
public class StrategyType {

  @XmlElement(required = true)
  protected String id;
  @XmlElement(required = true)
  protected String name;
  protected String description;
  @XmlElement(name = "class-name", required = true)
  protected String className;
  @XmlElement(name = "optional-config")
  protected OptionalConfigType optionalConfig;

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
   * Gets the value of the description property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the value of the description property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setDescription(String value) {
    this.description = value;
  }

  /**
   * Gets the value of the className property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getClassName() {
    return className;
  }

  /**
   * Sets the value of the className property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setClassName(String value) {
    this.className = value;
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
