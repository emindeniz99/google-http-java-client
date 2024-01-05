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

import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Flux;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClient.RequestSender;
import reactor.netty.http.client.HttpClient.ResponseReceiver;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.ExecutionException;

/**
 * @author Yaniv Inbar
 */
final class ReactorNettyHttpRequest extends LowLevelHttpRequest {
    private final HttpClient httpClient;

    private DefaultHttpHeaders httpHeaders;
    private final String method;
    private final String url;

    //  private RequestConfig.Builder requestConfig;

    ReactorNettyHttpRequest(HttpClient httpClient, String method, String url) {
        this.httpClient = httpClient;
        this.method = method;
        this.url = url;
        this.httpHeaders = new DefaultHttpHeaders();
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
        httpHeaders.add(name, value);
    }

    //  @Override
    //  public void setTimeout(int connectTimeoutInMs, int readTimeoutInMs) throws IOException {
    //    requestBuilder.timeout(Duration.ofMillis(connectTimeoutInMs));
    //  }

    @Override
    public LowLevelHttpResponse execute() throws IOException {

        HttpClient currentClient=httpClient.headers((h)->{
    if (httpHeaders != null) {
        h.setAll(httpHeaders);
    }
});
        RequestSender jj= currentClient.request(HttpMethod.valueOf(method)).uri(url);

        ResponseReceiver<?> responseeee;
        if (getStreamingContent() != null) {

            ByteArrayOutputStream byteArrayOutputStream=  new ByteArrayOutputStream();
            getStreamingContent().writeTo(byteArrayOutputStream);
//            byteArrayOutputStream.flush();

            responseeee= jj.send(ByteBufFlux.fromInbound(Flux.just(byteArrayOutputStream.toByteArray())));



//            requestBuilder=requestBuilder.method(method, HttpRequest.BodyPublishers.ofByteArray(byteArrayOutputStream.toByteArray()));

//                  ContentEntity entity = new ContentEntity(getContentLength(), getStreamingContent());
//                  entity.setContentEncoding(getContentEncoding());
//                  entity.setContentType(getContentType());
//                  if (getContentLength() == -1) {
//                    entity.setChunked(true);
//                  }
//                  ((HttpEntityEnclosingRequest) request).setEntity(entity);
        }else {
            responseeee= jj;
        }

        return responseeee.responseSingle((res, byteBufMono) -> {

            return byteBufMono.asInputStream().map((inputStream) -> {
                return new ReactorNettyHttpResponse( res,inputStream);

            });
        }).block();
    }
}
