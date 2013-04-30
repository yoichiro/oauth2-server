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

package jp.eisbahn.oauth2.server.granttype.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.exceptions.OAuthError;
import jp.eisbahn.oauth2.server.fetcher.clientcredential.ClientCredentialFetcherImpl;
import jp.eisbahn.oauth2.server.granttype.GrantHandler.GrantHandlerResult;
import jp.eisbahn.oauth2.server.granttype.impl.AuthorizationCode;
import jp.eisbahn.oauth2.server.models.AccessToken;
import jp.eisbahn.oauth2.server.models.AuthInfo;
import jp.eisbahn.oauth2.server.models.Request;

public class AuthorizationCodeTest {

	private AuthorizationCode target;

	@Before
	public void setUp() {
		target = new AuthorizationCode();
		target.setClientCredentialFetcher(new ClientCredentialFetcherImpl());
	}

	@After
	public void tearDown() {
		target = null;
	}

	@Test
	public void testHandleRequestCodeNotFound() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("code")).andReturn(null);
		DataHandler dataHandler = createDataHandlerMock(request);
		replay(request, dataHandler);
		try {
			target.handleRequest(dataHandler);
			fail("Error.InvalidRequest not occurred.");
		} catch (OAuthError.InvalidRequest e) {
			assertEquals("'code' not found", e.getDescription());
		}
	}

	@Test
	public void testHandleRequestRedirectUriNotFound() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("code")).andReturn("code1");
		expect(request.getParameter("redirect_uri")).andReturn(null);
		DataHandler dataHandler = createDataHandlerMock(request);
		replay(request, dataHandler);
		try {
			target.handleRequest(dataHandler);
			fail("Error.InvalidRequest not occurred.");
		} catch (OAuthError.InvalidRequest e) {
			assertEquals("'redirect_uri' not found", e.getDescription());
		}
	}

	@Test
	public void testHandleRequestAuthInfoNotFound() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("code")).andReturn("code1");
		expect(request.getParameter("redirect_uri")).andReturn("redirectUri1");
		DataHandler dataHandler = createDataHandlerMock(request);
		expect(dataHandler.getAuthInfoByCode("code1")).andReturn(null);
		replay(request, dataHandler);
		try {
			target.handleRequest(dataHandler);
			fail("Error.InvalidGrant not occurred.");
		} catch (OAuthError.InvalidGrant e) {
		}
	}

	@Test
	public void testHandleRequestClientIdMismatch() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("code")).andReturn("code1");
		expect(request.getParameter("redirect_uri")).andReturn("redirectUri1");
		DataHandler dataHandler = createDataHandlerMock(request);
		AuthInfo authInfo = new AuthInfo();
		authInfo.setClientId("clientId2");
		expect(dataHandler.getAuthInfoByCode("code1")).andReturn(authInfo);
		replay(request, dataHandler);
		try {
			target.handleRequest(dataHandler);
			fail("Error.InvalidClient not occurred.");
		} catch (OAuthError.InvalidClient e) {
		}
	}

	@Test
	public void testHandleRequestRedirectUriNotSet() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("code")).andReturn("code1");
		expect(request.getParameter("redirect_uri")).andReturn("redirectUri1");
		DataHandler dataHandler = createDataHandlerMock(request);
		AuthInfo authInfo = new AuthInfo();
		authInfo.setClientId("clientId1");
		authInfo.setRedirectUri("");
		expect(dataHandler.getAuthInfoByCode("code1")).andReturn(authInfo);
		replay(request, dataHandler);
		try {
			target.handleRequest(dataHandler);
			fail("Error.RedirectUriMismatch not occurred.");
		} catch (OAuthError.RedirectUriMismatch e) {
		}
	}

	@Test
	public void testHandleRequestRedirectUriMismatch() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("code")).andReturn("code1");
		expect(request.getParameter("redirect_uri")).andReturn("redirectUri1");
		DataHandler dataHandler = createDataHandlerMock(request);
		AuthInfo authInfo = new AuthInfo();
		authInfo.setClientId("clientId1");
		authInfo.setRedirectUri("redirectUri2");
		expect(dataHandler.getAuthInfoByCode("code1")).andReturn(authInfo);
		replay(request, dataHandler);
		try {
			target.handleRequest(dataHandler);
			fail("Error.RedirectUriMismatch not occurred.");
		} catch (OAuthError.RedirectUriMismatch e) {
		}
	}

	@Test
	public void testHandleRequestSimple() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("code")).andReturn("code1");
		expect(request.getParameter("redirect_uri")).andReturn("redirectUri1");
		DataHandler dataHandler = createDataHandlerMock(request);
		AuthInfo authInfo = new AuthInfo();
		authInfo.setClientId("clientId1");
		authInfo.setRedirectUri("redirectUri1");
		expect(dataHandler.getAuthInfoByCode("code1")).andReturn(authInfo);
		AccessToken accessToken = new AccessToken();
		accessToken.setToken("accessToken1");
		expect(dataHandler.createOrUpdateAccessToken(authInfo)).andReturn(accessToken);
		replay(request, dataHandler);
		GrantHandlerResult result = target.handleRequest(dataHandler);
		assertEquals("Bearer", result.getTokenType());
		assertEquals("accessToken1", result.getAccessToken());
		assertNull(result.getExpiresIn());
		assertNull(result.getRefreshToken());
		assertNull(result.getScope());
	}

	@Test
	public void testHandleRequestFull() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("code")).andReturn("code1");
		expect(request.getParameter("redirect_uri")).andReturn("redirectUri1");
		DataHandler dataHandler = createDataHandlerMock(request);
		AuthInfo authInfo = new AuthInfo();
		authInfo.setClientId("clientId1");
		authInfo.setRedirectUri("redirectUri1");
		authInfo.setRefreshToken("refreshToken1");
		authInfo.setScope("scope1");
		expect(dataHandler.getAuthInfoByCode("code1")).andReturn(authInfo);
		AccessToken accessToken = new AccessToken();
		accessToken.setToken("accessToken1");
		accessToken.setExpiresIn(123L);
		expect(dataHandler.createOrUpdateAccessToken(authInfo)).andReturn(accessToken);
		replay(request, dataHandler);
		GrantHandlerResult result = target.handleRequest(dataHandler);
		assertEquals("Bearer", result.getTokenType());
		assertEquals("accessToken1", result.getAccessToken());
		assertEquals(123L, (long)result.getExpiresIn());
		assertEquals("refreshToken1", result.getRefreshToken());
		assertEquals("scope1", result.getScope());
	}

	private Request createRequestMock() {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn(null);
		expect(request.getParameter("client_id")).andReturn("clientId1");
		expect(request.getParameter("client_secret")).andReturn("clientSecret1");
		return request;
	}

	private DataHandler createDataHandlerMock(Request request) {
		DataHandler dataHandler = createMock(DataHandler.class);
		expect(dataHandler.getRequest()).andReturn(request);
		return dataHandler;
	}

}
