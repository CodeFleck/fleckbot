package br.com.codefleck.tradebot.xmldatastore.exchange.generated;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

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

  private final static QName _Exchange_QNAME = new QName("", "exchange");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
   *
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link ExchangeType }
   *
   */
  public ExchangeType createExchangeType() {
    return new ExchangeType();
  }

  /**
   * Create an instance of {@link AuthenticationConfigType }
   *
   */
  public AuthenticationConfigType createAuthenticationConfigType() {
    return new AuthenticationConfigType();
  }

  /**
   * Create an instance of {@link NetworkConfigType }
   *
   */
  public NetworkConfigType createNetworkConfigType() {
    return new NetworkConfigType();
  }

  /**
   * Create an instance of {@link OptionalConfigType }
   *
   */
  public OptionalConfigType createOptionalConfigType() {
    return new OptionalConfigType();
  }

  /**
   * Create an instance of {@link NonFatalErrorMessagesType }
   *
   */
  public NonFatalErrorMessagesType createNonFatalErrorMessagesType() {
    return new NonFatalErrorMessagesType();
  }

  /**
   * Create an instance of {@link NonFatalErrorCodesType }
   *
   */
  public NonFatalErrorCodesType createNonFatalErrorCodesType() {
    return new NonFatalErrorCodesType();
  }

  /**
   * Create an instance of {@link ConfigItemType }
   *
   */
  public ConfigItemType createConfigItemType() {
    return new ConfigItemType();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ExchangeType }{@code >}}
   *
   */
  @XmlElementDecl(namespace = "", name = "exchange")
  public JAXBElement<ExchangeType> createExchange(ExchangeType value) {
    return new JAXBElement<ExchangeType>(_Exchange_QNAME, ExchangeType.class, null, value);
  }

}
