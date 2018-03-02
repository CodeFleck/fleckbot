package br.com.codefleck.tradebot.xmldatastore;

/**
 * Locations of XML and XSD files for the entities.
 */
public final class FileLocations {

  /*
   * Location of the XML config files relative to project/installation root.
   */
  public static final String EMAIL_ALERTS_CONFIG_XML_FILENAME = "xml/email-alerts.xml";
  public static final String ENGINE_CONFIG_XML_FILENAME = "xml/engine.xml";
  public static final String EXCHANGE_CONFIG_XML_FILENAME = "xml/exchange.xml";
  public static final String MARKETS_CONFIG_XML_FILENAME = "xml/markets.xml";
  public static final String STRATEGIES_CONFIG_XML_FILENAME = "xml/strategies.xml";

  /*
   * XSD schema files for validating the XML config - their location in the main/resources folder.
   */
  public static final String EMAIL_ALERTS_CONFIG_XSD_FILENAME = "xsd/email-alerts.xsd";
  public static final String ENGINE_CONFIG_XSD_FILENAME = "xsd/engine.xsd";
  public static final String EXCHANGE_CONFIG_XSD_FILENAME = "xsd/exchange.xsd";
  public static final String MARKETS_CONFIG_XSD_FILENAME = "xsd/markets.xsd";
  public static final String STRATEGIES_CONFIG_XSD_FILENAME = "xsd/strategies.xsd";

  private FileLocations() {
  }
}