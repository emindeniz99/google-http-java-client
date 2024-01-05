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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.testing.http.apache.MockHttpClient;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Java11HttpClientHttpRequestTest {

  @Test
  public void testContentLengthSet() throws Exception {
//    HttpExtensionMethod base = new HttpExtensionMethod("POST", );
    Java11HttpClientHttpRequest request = new Java11HttpClientHttpRequest(HttpClient.newHttpClient(), HttpRequest.newBuilder().uri(new URI("http://www.google.com")), "POST");
    HttpContent content =
        new ByteArrayContent("text/plain", "sample".getBytes(StandardCharsets.UTF_8));
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
    Java11HttpClientHttpRequest request = new Java11HttpClientHttpRequest(HttpClient.newHttpClient(), HttpRequest.newBuilder().uri(new URI("http://www.google.com")), "POST");
    HttpContent content = new InputStreamContent("text/plain", new ByteArrayInputStream(buf));
    request.setStreamingContent(content);
    request.execute();

//    assertTrue(base.getEntity().isChunked());
//    assertEquals(-1, base.getEntity().getContentLength());
  }
}
