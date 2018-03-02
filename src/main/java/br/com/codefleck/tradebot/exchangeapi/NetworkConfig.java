package br.com.codefleck.tradebot.exchangeapi;


import java.util.List;

/**
 * Encapsulates any (optional) Network configuration for an Exchange Adapter.
 *
 */
public interface NetworkConfig {

  /**
   * Fetches (optional) list of non-fatal error codes.
   *
   * @return a list of non-fatal error codes if present, an empty list otherwise.
   */
  List<Integer> getNonFatalErrorCodes();

  /**
   * Fetches (optional) list of non-fatal error messages.
   *
   * @return list of non-fatal error messages if present, an empty list otherwise.
   */
  List<String> getNonFatalErrorMessages();

  /**
   * Fetches (optional) connection timeout value.
   *
   * @return the connection timeout value if present, null otherwise.
   */
  Integer getConnectionTimeout();
}
