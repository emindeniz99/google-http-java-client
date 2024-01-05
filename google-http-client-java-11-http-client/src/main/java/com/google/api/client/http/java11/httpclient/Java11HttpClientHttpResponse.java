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

package com.google.api.client.http.java11.httpclient;

import com.google.api.client.http.LowLevelHttpResponse;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

final class Java11HttpClientHttpResponse extends LowLevelHttpResponse {

  private final HttpRequest request;
 @Nullable
 private final HttpResponse<InputStream> response;

@Nullable private final Map<String, List<String>> headers;
//      *   HttpResponse<InputStream> response = client
  //     *     .send(request, BodyHandlers.ofInputStream());

  Java11HttpClientHttpResponse(HttpRequest request, HttpResponse<InputStream> response) {
    this.request = request;
    this.response = response;
    this.headers = response.headers().map();
  }
  @Override
  public int getStatusCode() {
    if (response == null) {
      return 0;
    }
    return response.statusCode();
  }

  @Override
  public InputStream getContent() throws IOException {
    if (response == null) {
      return null;
    }
    return response.body();
  }

  @Override
  public String getContentEncoding() {
    if (response == null) {
      return null;
    }
    return headers.get("Content-Encoding").get(0);

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
    return response.headers().firstValueAsLong("Content-Length").orElse(-1L);

//    HttpEntity entity = response.getEntity();
//    return entity == null ? -1 : entity.getContentLength();
  }

  @Override
  public String getContentType() {

    if (response == null) {
      return null;
    }
    return response.headers().firstValue("Content-Type").orElse(null);

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
    return response.headers().firstValue("Reason-Phrase").orElse(null);

//    StatusLine statusLine = response.getStatusLine();
//    return statusLine == null ? null : statusLine.getReasonPhrase();
  }

  @Override
  public String getStatusLine() {

        if (response == null) {
        return null;
        }
        return response.headers().firstValue("Status-Line").orElse(null);
//    StatusLine statusLine = response.getStatusLine();
//    return statusLine == null ? null : statusLine.toString();
  }

  public String getHeaderValue(String name) {
    if (response == null) {
      return null;
    }
    return response.headers().firstValue(name).orElse(null);
  }

  @Override
  public int getHeaderCount() {
    if (response == null) {
      return 0;
    }
    return headers.size();
  }

  @Override
  public String getHeaderName(int index) {
    if (response == null) {
      return null;
    }

    return headers.keySet().toArray(new String[0])[index];
  }

  @Override
  public String getHeaderValue(int index) {
    if (response == null) {
      return null;
    }

    return headers.get(getHeaderName(index)).get(0);
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
