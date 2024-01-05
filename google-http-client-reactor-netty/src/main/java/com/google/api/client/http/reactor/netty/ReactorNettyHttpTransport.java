/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.http.reactor.netty;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Beta;
import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.Executors;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;

/**
 * Thread-safe HTTP transport based on the Apache HTTP Client library.
 *
 * <p>Implementation is thread-safe, as long as any parameter modification to the {@link
 * #getHttpClient() Apache HTTP Client} is only done at initialization time. For maximum efficiency,
 * applications should use a single globally-shared instance of the HTTP transport.
 *
 * <p>Default settings are specified in {@link #newDefaultHttpClient()}. Use the {@link
 * #ReactorNettyHttpTransport(HttpClient)} constructor to override the Apache HTTP Client used. Please
 * read the <a
 * href="https://hc.apache.org/httpcomponents-client-4.5.x/current/tutorial/pdf/httpclient-tutorial.pdf">
 * Apache HTTP Client connection management tutorial</a> for more complex configuration options.
 *
 * @since 1.30
 * @author Yaniv Inbar
 */
@Beta
public final class ReactorNettyHttpTransport extends HttpTransport {


  /** Apache HTTP client. */
  private final HttpClient httpClient;

  /** If the HTTP client uses mTLS channel. */
  private final boolean isMtls;

  /**
   * Constructor that uses {@link #newDefaultHttpClient()} for the Apache HTTP client.
   *
   * @since 1.30
   */
  public ReactorNettyHttpTransport() {
    this(newDefaultHttpClient(), false);
  }

  /**
   * Constructor that allows an alternative Apache HTTP client to be used.
   *
   * <p>Note that in the previous version, we overrode several settings. However, we are no longer
   * able to do so.
   *
   * <p>If you choose to provide your own Apache HttpClient implementation, be sure that
   *
   * <ul>
   *   <li>HTTP version is set to 1.1.
   *   <li>Redirects are disabled (google-http-client handles redirects).
   *   <li>Retries are disabled (google-http-client handles retries).
   * </ul>
   *
   * @param httpClient Apache HTTP client to use
   * @since 1.30
   */
  public ReactorNettyHttpTransport(HttpClient httpClient) {
    this.httpClient = httpClient;
    this.isMtls = false;
  }

  /**
   * {@link Beta} <br>
   * Constructor that allows an alternative Apache HTTP client to be used.
   *
   * <p>Note that in the previous version, we overrode several settings. However, we are no longer
   * able to do so.
   *
   * <p>If you choose to provide your own Apache HttpClient implementation, be sure that
   *
   * <ul>
   *   <li>HTTP version is set to 1.1.
   *   <li>Redirects are disabled (google-http-client handles redirects).
   *   <li>Retries are disabled (google-http-client handles retries).
   * </ul>
   *
   * @param httpClient Apache HTTP client to use
   * @param isMtls If the HTTP client is mutual TLS
   * @since 1.38
   */
  public ReactorNettyHttpTransport(HttpClient httpClient, boolean isMtls) {
    this.httpClient = httpClient;
    this.isMtls = isMtls;
  }

  /**
   * Creates a new instance of the Apache HTTP client that is used by the {@link
   * #ReactorNettyHttpTransport()} constructor.
   *
   * <p>Settings:
   *
   * <ul>
   *   <li>The client connection manager is set to {@link PoolingHttpClientConnectionManager}.
   *   <li><The retry mechanism is turned off using {@link
   *       HttpClientBuilder#disableRedirectHandling}.
   *   <li>The route planner uses {@link SystemDefaultRoutePlanner} with {@link
   *       ProxySelector#getDefault()}, which uses the proxy settings from <a
   *       href="https://docs.oracle.com/javase/7/docs/api/java/net/doc-files/net-properties.html">system
   *       properties</a>.
   * </ul>
   *
   * @return new instance of the Apache HTTP client
   * @since 1.30
   */
  public static HttpClient newDefaultHttpClient() {
    return newDefaultHttpClientBuilder();
  }

  /**
   * Creates a new Apache HTTP client builder that is used by the {@link #ReactorNettyHttpTransport()}
   * constructor.
   *
   * <p>Settings:
   *
   * <ul>
   *   <li>The client connection manager is set to {@link PoolingHttpClientConnectionManager}.
   *   <li><The retry mechanism is turned off using {@link
   *       HttpClientBuilder#disableRedirectHandling}.
   *   <li>The route planner uses {@link SystemDefaultRoutePlanner} with {@link
   *       ProxySelector#getDefault()}, which uses the proxy settings from <a
   *       href="http://docs.oracle.com/javase/7/docs/api/java/net/doc-files/net-properties.html">system
   *       properties</a>.
   * </ul>
   *
   * @return new instance of the Apache HTTP client
   * @since 1.31
   */
  public static HttpClient newDefaultHttpClientBuilder() {
    System.out.println("ReactorNettyHttpTransport.newDefaultHttpClientBuilder");
    return HttpClient.create();
  }

  @Override
  public boolean supportsMethod(String method) {
    return true;
  }

  @Override
  protected ReactorNettyHttpRequest buildRequest(String method, String url) {

      return new ReactorNettyHttpRequest(httpClient,  method,  url);
  }

  /**
   * Shuts down the connection manager and releases allocated resources. This closes all
   * connections, whether they are currently used or not.
   *
   * @since 1.30
   */
  @Override
  public void shutdown() throws IOException {

  }

  /**
   * Returns the Apache HTTP client.
   *
   * @since 1.30
   */
  public HttpClient getHttpClient() {
    return httpClient;
  }

  /** Returns if the underlying HTTP client is mTLS. */
  @Override
  public boolean isMtls() {
    return isMtls;
  }
}
