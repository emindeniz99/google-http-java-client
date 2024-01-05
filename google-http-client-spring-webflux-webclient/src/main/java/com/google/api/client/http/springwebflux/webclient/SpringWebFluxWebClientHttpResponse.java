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

import com.google.api.client.http.LowLevelHttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map.Entry;

final class SpringWebFluxWebClientHttpResponse extends LowLevelHttpResponse {

    private final ResponseEntity<InputStream> responseEntity;
    private final ArrayList<Entry<String, String>> headers;

    SpringWebFluxWebClientHttpResponse(ResponseEntity<InputStream> responseEntity) {
        this.responseEntity = responseEntity;
        this.headers = new ArrayList<>(responseEntity.getHeaders().toSingleValueMap().entrySet());
    }

    @Override
    public int getStatusCode() {
        if (!isSuccessful()) {
            return -1;
        }
        return responseEntity.getStatusCode().value();
    }

    @Override
    public InputStream getContent() throws IOException {
        if (!isSuccessful()) {
            return null;
        }
        return responseEntity.getBody();
    }

    @Override
    public String getContentEncoding() {
        if (!isSuccessful()) {
            return null;
        }
        return responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING);
    }

    @Override
    public long getContentLength() {
        if (!isSuccessful()) {
            return -1;
        }
        var val = responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_LENGTH);
        if (val != null) {
            return Long.parseLong(val);
        }
        return 0;
    }

    @Override
    public String getContentType() {
        if (!isSuccessful()) {
            return null;
        }
        return responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
    }

    @Override
    public String getReasonPhrase() {
        if (!isSuccessful()) {
            return null;
        }
        return responseEntity.getHeaders().getFirst("Reason-Phrase");
    }

    @Override
    public String getStatusLine() {
        if (!isSuccessful()) {
            return null;
        }
        return responseEntity.getHeaders().getFirst("Status-Line");
    }

    @Override
    public int getHeaderCount() {
        if (!isSuccessful()) {
            return 0;
        }
        return headers.size();
    }

    @Override
    public String getHeaderName(int index) {
        if (!isSuccessful()) {
            return null;
        }
        return headers.get(index).getKey();
    }

    @Override
    public String getHeaderValue(int index) {
        if (!isSuccessful()) {
            return null;
        }
        return headers.get(index).getValue();
    }

    private boolean isSuccessful() {
        return responseEntity != null;
    }
}
