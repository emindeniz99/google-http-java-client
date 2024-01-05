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

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.InputStreamContent;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Ignore
public class SpringWebFluxWebClientHttpRequestTest {

    @Test
    public void testContentLengthSet() throws Exception {
        //    HttpExtensionMethod base = new HttpExtensionMethod("POST", );
        SpringWebFluxWebClientHttpRequest request = new SpringWebFluxWebClientHttpRequest(SpringWebFluxWebClientHttpTransport.newDefaultHttpClient(),
                                                                                          "POST",
                                                                                          "http://www.google.com");
        HttpContent content = new ByteArrayContent("text/plain", "sample".getBytes(StandardCharsets.UTF_8));
        request.setStreamingContent(content);
        request.setContentLength(content.getLength());
        request.execute();

        //    assertFalse(base.getEntity().isChunked());
        //    assertEquals(6, base.getEntity().getContentLength());
    }

    @Test
    public void testChunked() throws Exception {
        byte[] buf = new byte[300];
        Arrays.fill(buf, (byte) ' ');
        SpringWebFluxWebClientHttpRequest request = new SpringWebFluxWebClientHttpRequest(SpringWebFluxWebClientHttpTransport.newDefaultHttpClient(),
                                                                                          "POST",
                                                                                          "http://www.google.com");
        HttpContent content = new InputStreamContent("text/plain", new ByteArrayInputStream(buf));
        request.setStreamingContent(content);
        request.execute();

        //    assertTrue(base.getEntity().isChunked());
        //    assertEquals(-1, base.getEntity().getContentLength());
    }
}
