package br.com.codefleck.tradebot.xmldatastore.market.generated;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

/**
 * <p>Java class for marketsType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="marketsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="market" type="{}marketType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "marketsType", propOrder = {
    "market"
})
@XmlRootElement(name="markets")
public class MarketsType {

  @XmlElement(required = true)
  protected List<MarketType> market;

  /**
   * Gets the value of the market property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the market property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getMarkets().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link MarketType }
   *
   *
   */
  public List<MarketType> getMarkets() {
    if (market == null) {
      market = new ArrayList<MarketType>();
    }
    return this.market;
  }

}
