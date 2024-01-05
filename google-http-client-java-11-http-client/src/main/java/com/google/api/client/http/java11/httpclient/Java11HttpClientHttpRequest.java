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

import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.ExecutionException;

/**
 * @author Yaniv Inbar
 */
final class Java11HttpClientHttpRequest extends LowLevelHttpRequest {
    private final HttpClient httpClient;

    private HttpRequest.Builder requestBuilder;
    private final String method;

    //  private RequestConfig.Builder requestConfig;

    Java11HttpClientHttpRequest(HttpClient httpClient, HttpRequest.Builder requestBuilder,String method) {
        this.httpClient = httpClient;
        this.requestBuilder = requestBuilder;
        this.method = method;
        // disable redirects as google-http-client handles redirects
        //    this.requestConfig =
        //        RequestConfig.custom()
        //            .setRedirectsEnabled(false)
        //            .setNormalizeUri(false)
        //            // TODO(chingor): configure in HttpClientBuilder when available
        //            .setStaleConnectionCheckEnabled(false);
    }

    @Override
    public void addHeader(String name, String value) {
        requestBuilder=   requestBuilder.header(name, value);
    }

    //  @Override
    //  public void setTimeout(int connectTimeoutInMs, int readTimeoutInMs) throws IOException {
    //    requestBuilder.timeout(Duration.ofMillis(connectTimeoutInMs));
    //  }

    @Override
    public LowLevelHttpResponse execute() throws IOException {
        if (getStreamingContent() != null) {

            var byteArrayOutputStream=  new ByteArrayOutputStream();
            getStreamingContent().writeTo(byteArrayOutputStream);

            requestBuilder=requestBuilder.method(method, HttpRequest.BodyPublishers.ofByteArray(byteArrayOutputStream.toByteArray()));

//                  ContentEntity entity = new ContentEntity(getContentLength(), getStreamingContent());
//                  entity.setContentEncoding(getContentEncoding());
//                  entity.setContentType(getContentType());
//                  if (getContentLength() == -1) {
//                    entity.setChunked(true);
//                  }
//                  ((HttpEntityEnclosingRequest) request).setEntity(entity);
        }else {
            requestBuilder=requestBuilder.method(method, HttpRequest.BodyPublishers.noBody());
        }
        //    request.setConfig(requestConfig.build());
        var request = requestBuilder.build();
        try {
            var response = httpClient.sendAsync(request, BodyHandlers.ofInputStream()).get();
            return new Java11HttpClientHttpResponse(request, response);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
