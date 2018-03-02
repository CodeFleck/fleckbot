package br.com.codefleck.tradebot.xmldatastore.strategy.generated;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

/**
 * <p>Java class for trading-strategiesType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="trading-strategiesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="strategy" type="{}strategyType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "trading-strategiesType", propOrder = {
    "strategy"
})
@XmlRootElement(name="trading-strategies")
public class TradingStrategiesType {

  @XmlElement(required = true)
  protected List<StrategyType> strategy;

  /**
   * Gets the value of the strategy property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the strategy property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getStrategies().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link StrategyType }
   *
   *
   */
  public List<StrategyType> getStrategies() {
    if (strategy == null) {
      strategy = new ArrayList<StrategyType>();
    }
    return this.strategy;
  }

}
