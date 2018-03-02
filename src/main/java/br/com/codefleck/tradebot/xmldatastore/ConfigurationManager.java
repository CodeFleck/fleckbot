package br.com.codefleck.tradebot.xmldatastore;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * The generic configuration manager loads config from a given XML config file.
 *
 */
public final class ConfigurationManager {

  private static final Logger LOG = LogManager.getLogger();
  private final static Object MUTEX = new Object();

  private ConfigurationManager() {
  }

  /*
   * Loads and returns the requested configuration.
   */
  public static <T> T loadConfig(Class<T> configClass, String xmlConfigFile, String xmlSchemaFile) {

    LOG.info(() -> "Loading configuration for [" + configClass + "] from: " + xmlConfigFile + " ...");

    try {
      final JAXBContext jaxbContext = JAXBContext.newInstance(configClass.getPackage().getName());
      final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

      // optional schema validation
      if (xmlSchemaFile != null) {
        final InputStream xsdStream = ConfigurationManager.class.getClassLoader().getResourceAsStream(xmlSchemaFile);
        final StreamSource xsdSource = new StreamSource(xsdStream);
        final SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema = sf.newSchema(xsdSource);
        unmarshaller.setSchema(schema);
      }

      synchronized (MUTEX) {
        final InputStream xmlStream = ConfigurationManager.class.getClassLoader().getResourceAsStream(xmlConfigFile);
        final JAXBElement<?> requestedConfigRootXmlElement = (JAXBElement<?>) unmarshaller.unmarshal(xmlStream);
        final T requestedConfig = (T) requestedConfigRootXmlElement.getValue();
        xmlStream.close();

        LOG.info(() -> "Loaded and set configuration for [" + configClass + "] successfully!");
        return requestedConfig;
      }

    } catch (JAXBException | SAXException e) {
      final String errorMsg = "Failed to load [" + xmlConfigFile + "] file and validate it using XML Schema [" + xmlSchemaFile + "]";
      LOG.error(errorMsg, e);
      throw new IllegalArgumentException(errorMsg, e);
    } catch (IOException e) {
      final String errorMsg = "Failed to find or read [" + xmlConfigFile + "] config";
      LOG.error(errorMsg, e);
      throw new IllegalStateException(errorMsg, e);
    }
  }

  /*
   * Saves given config to filesystem.
   */
  public static <T> void saveConfig(Class<T> configClass, T config, String xmlConfigFile) {

    LOG.info(() -> "Saving configuration for [" + configClass + "] to: " + xmlConfigFile + " ...");

    try {
      final JAXBContext context = JAXBContext.newInstance(config.getClass());
      final Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      synchronized (MUTEX) {
        final FileOutputStream fileOutputStream = new FileOutputStream(xmlConfigFile);
        marshaller.marshal(config, fileOutputStream);
        fileOutputStream.close();
      }

    } catch (JAXBException e) {
      final String errorMsg = "Failed to save config to [" + xmlConfigFile + "] file.";
      LOG.error(errorMsg, e);
      throw new IllegalArgumentException(errorMsg, e);
    } catch (IOException e) {
      final String errorMsg = "Failed to find or read [" + xmlConfigFile + "] config";
      LOG.error(errorMsg, e);
      throw new IllegalStateException(errorMsg, e);
    }
  }
}
