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

package com.google.api.client.http.springwebflux.webclient;

import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

/**
 * @author Yaniv Inbar
 */
final class SpringWebFluxWebClientHttpRequest extends LowLevelHttpRequest {
    private final WebClient webClient;

    private final HttpHeaders httpHeaders;
    private final String method;
    private final String url;

    SpringWebFluxWebClientHttpRequest(WebClient webClient, String method, String url) {
        this.webClient = webClient;
        this.method = method;
        this.url = url;
        this.httpHeaders = new HttpHeaders();
    }

    @Override
    public void addHeader(String name, String value) {
        httpHeaders.add(name, value);
    }

    @Nullable
    private Duration timeout;

    @Override
    public void setTimeout(int connectTimeoutInMs, int readTimeoutInMs) throws IOException {
        timeout = Duration.ofMillis((long) connectTimeoutInMs + readTimeoutInMs);
    }

    @Override
    public LowLevelHttpResponse execute() throws IOException {

        var currentClient = webClient.method(HttpMethod.valueOf(method)).uri(url);

        RequestHeadersSpec<?> requestHeadersSpec;
        if (getStreamingContent() != null) {

            var s = new ByteArrayOutputStream();
            getStreamingContent().writeTo(s);

            requestHeadersSpec = currentClient.bodyValue(s.toByteArray());
            if (getContentLength() == -1) {
                requestHeadersSpec.header(HttpHeaders.TRANSFER_ENCODING, "chunked");
            }
            else {
                requestHeadersSpec.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(getContentLength()));

            }

            requestHeadersSpec.header(HttpHeaders.CONTENT_TYPE, getContentType());
            requestHeadersSpec.header(HttpHeaders.CONTENT_ENCODING, getContentEncoding());
        }
        else {
            requestHeadersSpec = currentClient;
        }

        requestHeadersSpec.headers(headers -> headers.addAll(httpHeaders));

        var responseSpec = requestHeadersSpec.retrieve();

        try {
            var responseEntityMono = responseSpec.toEntity(InputStream.class);

            if (timeout != null) {
                responseEntityMono = responseEntityMono.timeout(timeout);
            }

            var responseEntity = responseEntityMono.block();
            return new SpringWebFluxWebClientHttpResponse(responseEntity);
        }
        catch (WebClientResponseException webClientException) {
            return new SpringWebFluxWebClientHttpResponse(

                new ResponseEntity<>(

                    new ByteArrayInputStream(webClientException.getResponseBodyAsByteArray()),

                    webClientException.getHeaders(),

                    webClientException.getStatusCode()));
        }

    }
}
