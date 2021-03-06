package br.com.codefleck.tradebot.xmldatastore.exchange.generated;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for optional-configType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="optional-configType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="config-item" type="{}config-itemType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "optional-configType", propOrder = {
    "configItem"
})
public class OptionalConfigType {

  @XmlElement(name = "config-item", required = true)
  protected List<ConfigItemType> configItem;

  /**
   * Gets the value of the configItem property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the configItem property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getConfigItem().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ConfigItemType }
   *
   *
   */
  public List<ConfigItemType> getConfigItems() {
    if (configItem == null) {
      configItem = new ArrayList<ConfigItemType>();
    }
    return this.configItem;
  }

}
