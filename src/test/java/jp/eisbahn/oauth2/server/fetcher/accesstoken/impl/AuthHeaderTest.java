/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package jp.eisbahn.oauth2.server.fetcher.accesstoken.impl;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.eisbahn.oauth2.server.fetcher.accesstoken.AccessTokenFetcher.FetchResult;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.impl.AuthHeader;
import jp.eisbahn.oauth2.server.models.Request;

public class AuthHeaderTest {

	private AuthHeader target;

	@Before
	public void setUp() {
		target = new AuthHeader();
	}

	@After
	public void tearDown() {
		target = null;
	}

	@Test
	public void testMatch() {
		Request req;

		req = createRequestMock(null);
		assertFalse(target.match(req));
		verify(req);

		req = createRequestMock("OAuth token1");
		assertTrue(target.match(req));
		verify(req);

		req = createRequestMock("OAuth");
		assertTrue(target.match(req));
		verify(req);

		req = createRequestMock(" OAuth token1");
		assertTrue(target.match(req));
		verify(req);

		req = createRequestMock("OAut token1");
		assertFalse(target.match(req));
		verify(req);

		req = createRequestMock("oauth token1");
		assertFalse(target.match(req));
		verify(req);

		req = createRequestMock("Bearer token1");
		assertTrue(target.match(req));
		verify(req);

		req = createRequestMock("Bearer");
		assertTrue(target.match(req));
		verify(req);

		req = createRequestMock(" Bearer token1");
		assertTrue(target.match(req));
		verify(req);

		req = createRequestMock("Beare token1");
		assertFalse(target.match(req));
		verify(req);

		req = createRequestMock("bearer token1");
		assertFalse(target.match(req));
		verify(req);
	}

	@Test
	public void testParse() throws Exception {
		Request req;
		FetchResult parseResult;
		Map<String, String> params;

		req = createRequestMock("Bearer access_token_value");
		parseResult = target.fetch(req);
		assertEquals("access_token_value", parseResult.getToken());
		assertTrue(parseResult.getParams().isEmpty());

		req = createRequestMock("OAuth access_token_value");
		parseResult = target.fetch(req);
		assertEquals("access_token_value", parseResult.getToken());
		params = parseResult.getParams();
		assertTrue(params.isEmpty());

		req = createRequestMock(
				"Bearer access_token_value, "
					+ "algorithm=\"hmac-sha256\", "
					+ "nonce=\"s8djwd\", "
					+ "signature=\"wOJIO9A2W5mFwDgiDvZbTSMK%2FPY%3D\", "
					+ "timestamp=\"137131200\"");
		parseResult = target.fetch(req);
		assertEquals("access_token_value", parseResult.getToken());
		params = parseResult.getParams();
		assertFalse(params.isEmpty());
		assertEquals("hmac-sha256", params.get("algorithm"));
		assertEquals("s8djwd", params.get("nonce"));
		assertEquals("wOJIO9A2W5mFwDgiDvZbTSMK/PY=", params.get("signature"));
		assertEquals("137131200", params.get("timestamp"));

		req = createRequestMock(
				"OAuth access_token_value, "
					+ "algorithm=\"hmac-sha256\", "
					+ "nonce=\"s8djwd\", "
					+ "signature=\"wOJIO9A2W5mFwDgiDvZbTSMK%2FPY%3D\", "
					+ "timestamp=\"137131200\"");
		parseResult = target.fetch(req);
		assertEquals("access_token_value", parseResult.getToken());
		params = parseResult.getParams();
		assertFalse(params.isEmpty());
		assertEquals("hmac-sha256", params.get("algorithm"));
		assertEquals("s8djwd", params.get("nonce"));
		assertEquals("wOJIO9A2W5mFwDgiDvZbTSMK/PY=", params.get("signature"));
		assertEquals("137131200", params.get("timestamp"));

		req = createRequestMock("evil");
		try {
			parseResult = target.fetch(req);
			fail("IllegalStateException not occurred.");
		} catch (IllegalStateException e) {
		}
	}

	private Request createRequestMock(String authorization) {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn(authorization);
		replay(request);
		return request;
	}

}
