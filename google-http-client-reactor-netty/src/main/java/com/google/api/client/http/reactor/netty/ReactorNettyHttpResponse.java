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

import com.google.api.client.http.LowLevelHttpResponse;
import reactor.netty.http.client.HttpClientResponse;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

final class ReactorNettyHttpResponse extends LowLevelHttpResponse {

  private final HttpClientResponse httpClientResponse;
 @Nullable
 private final InputStream response;

//      *   HttpResponse<InputStream> response = client
  //     *     .send(request, BodyHandlers.ofInputStream());

  ReactorNettyHttpResponse(HttpClientResponse httpClientResponse, InputStream response) {
    this.httpClientResponse = httpClientResponse;
    this.response = response;
  }
  @Override
  public int getStatusCode() {
    if (httpClientResponse == null) {
      return 0;
    }
    return httpClientResponse.status().code();
  }

  @Override
  public InputStream getContent() throws IOException {
      return response;
  }

  @Override
  public String getContentEncoding() {
    if (response == null) {
      return null;
    }
    return httpClientResponse.responseHeaders().get("Content-Encoding");

//HttpEntity entity = response.getEntity();
//    if (entity != null) {
//      Header contentEncodingHeader = entity.getContentEncoding();
//      if (contentEncodingHeader != null) {
//        return contentEncodingHeader.getValue();
//      }
//    }
//    return null;
  }

  @Override
  public long getContentLength() {
    if (response == null) {
      return -1;
    }
    return httpClientResponse.responseHeaders().getInt("Content-Length");

//    HttpEntity entity = response.getEntity();
//    return entity == null ? -1 : entity.getContentLength();
  }

  @Override
  public String getContentType() {

    if (response == null) {
      return null;
    }
    return httpClientResponse.responseHeaders().get("Content-Type");

//    HttpEntity entity = response.getEntity();
//    if (entity != null) {
//      Header contentTypeHeader = entity.getContentType();
//      if (contentTypeHeader != null) {
//        return contentTypeHeader.getValue();
//      }
//    }
//    return null;
  }

  @Override
  public String getReasonPhrase() {

    if (response == null) {
      return null;
    }
    return httpClientResponse.responseHeaders().get("Reason-Phrase");

//    StatusLine statusLine = response.getStatusLine();
//    return statusLine == null ? null : statusLine.getReasonPhrase();
  }

  @Override
  public String getStatusLine() {

        if (response == null) {
        return null;
        }
        return httpClientResponse.responseHeaders().get("Status-Line");
//    StatusLine statusLine = response.getStatusLine();
//    return statusLine == null ? null : statusLine.toString();
  }

  public String getHeaderValue(String name) {
    if (response == null) {
      return null;
    }
    return httpClientResponse.responseHeaders().get(name);
  }

  @Override
  public int getHeaderCount() {
    if (response == null) {
      return 0;
    }
    return httpClientResponse.responseHeaders().size();
  }

  @Override
  public String getHeaderName(int index) {
    if (response == null) {
      return null;
    }

    return httpClientResponse.responseHeaders().entries().get(index).getKey();
  }

  @Override
  public String getHeaderValue(int index) {
    if (response == null) {
      return null;
    }

      return httpClientResponse.responseHeaders().entries().get(index).getValue();
  }

//  /**
//   * Aborts execution of the request.
//   *
//   * @since 1.30
//   */
//  @Override
//  public void disconnect() {
//    request.abort();
//  }

}
