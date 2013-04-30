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

package jp.eisbahn.oauth2.server.fetcher.accesstoken;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.eisbahn.oauth2.server.fetcher.accesstoken.impl.AuthHeader;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.impl.RequestParameter;
import jp.eisbahn.oauth2.server.models.Request;

public class AccessTokenFetcherProviderTest {

	private AccessTokenFetcherProvider target;

	@Before
	public void setUp() {
		target = new AccessTokenFetcherProvider();
		target.setAccessTokenFetchers(new AccessTokenFetcher[]{
			new AuthHeader(),
			new RequestParameter()
		});
	}

	@After
	public void tearDown() {
		target = null;
	}
	
	@Test
	public void testFetchers() throws Exception {
		AccessTokenFetcher[] accessTokenFetchers = target.getAccessTokenFetchers();
		assertEquals(2, accessTokenFetchers.length);
		assertTrue(accessTokenFetchers[0] instanceof AuthHeader);
		assertTrue(accessTokenFetchers[1] instanceof RequestParameter);
	}

	@Test
	public void testGetParamParserAuthHeader() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn("Bearer access_token_value");
		replay(request);
		AccessTokenFetcher paramParser = target.getFetcher(request);
		assertTrue(paramParser instanceof AuthHeader);
	}

	@Test
	public void testGetParamParserRequestParameter() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn(null);
		expect(request.getParameter("oauth_token")).andReturn("access_token_value");
		expect(request.getParameter("access_token")).andReturn("access_token_value");
		replay(request);
		AccessTokenFetcher paramParser = target.getFetcher(request);
		assertTrue(paramParser instanceof RequestParameter);
	}

	@Test
	public void testGetParamParserNotFound() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn(null);
		expect(request.getParameter("oauth_token")).andReturn(null);
		expect(request.getParameter("access_token")).andReturn(null);
		replay(request);
		AccessTokenFetcher paramParser = target.getFetcher(request);
		assertNull(paramParser);
	}

}
