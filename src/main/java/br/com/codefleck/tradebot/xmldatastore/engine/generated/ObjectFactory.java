package br.com.codefleck.tradebot.xmldatastore.engine.generated;


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

  private final static QName _Engine_QNAME = new QName("", "engine");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
   *
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link EngineType }
   *
   */
  public EngineType createEngineType() {
    return new EngineType();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link EngineType }{@code >}}
   *
   */
  @XmlElementDecl(namespace = "", name = "engine")
  public JAXBElement<EngineType> createEngine(EngineType value) {
    return new JAXBElement<EngineType>(_Engine_QNAME, EngineType.class, null, value);
  }
}
