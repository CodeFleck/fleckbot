package br.com.codefleck.tradebot.xmldatastore.strategy.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import br.com.codefleck.tradebot.xmldatastore.exchange.generated.OptionalConfigType;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the generated package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

  private final static QName _TradingStrategies_QNAME = new QName("", "trading-strategies");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
   *
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link TradingStrategiesType }
   *
   */
  public TradingStrategiesType createTradingStrategiesType() {
    return new TradingStrategiesType();
  }

  /**
   * Create an instance of {@link StrategyType }
   *
   */
  public StrategyType createStrategyType() {
    return new StrategyType();
  }

  /**
   * Create an instance of {@link OptionalConfigType }
   *
   */
  public OptionalConfigType createOptionalConfigType() {
    return new OptionalConfigType();
  }

  /**
   * Create an instance of {@link ConfigItemType }
   *
   */
  public ConfigItemType createConfigItemType() {
    return new ConfigItemType();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link TradingStrategiesType }{@code >}}
   *
   */
  @XmlElementDecl(namespace = "", name = "trading-strategies")
  public JAXBElement<TradingStrategiesType> createTradingStrategies(TradingStrategiesType value) {
    return new JAXBElement<TradingStrategiesType>(_TradingStrategies_QNAME, TradingStrategiesType.class, null, value);
  }

}
