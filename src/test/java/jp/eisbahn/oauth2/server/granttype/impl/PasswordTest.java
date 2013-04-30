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
import jp.eisbahn.oauth2.server.granttype.impl.Password;
import jp.eisbahn.oauth2.server.models.AccessToken;
import jp.eisbahn.oauth2.server.models.AuthInfo;
import jp.eisbahn.oauth2.server.models.Request;

public class PasswordTest {

	private Password target;

	@Before
	public void setUp() {
		target = new Password();
		target.setClientCredentialFetcher(new ClientCredentialFetcherImpl());
	}

	@After
	public void tearDown() {
		target = null;
	}

	@Test
	public void testHandleRequestUsernameNotFound() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("username")).andReturn(null);
		DataHandler dataHandler = createDataHandlerMock(request);
		replay(request, dataHandler);
		try {
			target.handleRequest(dataHandler);
			fail("Error.InvalidRequest not occurred.");
		} catch (OAuthError.InvalidRequest e) {
			assertEquals("'username' not found", e.getDescription());
		}
	}

	@Test
	public void testHandleRequestPasswordNotFound() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("username")).andReturn("username1");
		expect(request.getParameter("password")).andReturn(null);
		DataHandler dataHandler = createDataHandlerMock(request);
		replay(request, dataHandler);
		try {
			target.handleRequest(dataHandler);
			fail("Error.InvalidRequest not occurred.");
		} catch (OAuthError.InvalidRequest e) {
			assertEquals("'password' not found", e.getDescription());
		}
	}

	@Test
	public void testHandleRequestUserIdNotFound() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("username")).andReturn("username1");
		expect(request.getParameter("password")).andReturn("password1");
		DataHandler dataHandler = createDataHandlerMock(request);
		expect(dataHandler.getUserId("username1", "password1")).andReturn(null);
		replay(request, dataHandler);
		try {
			target.handleRequest(dataHandler);
			fail("Error.InvalidGrant not occurred.");
		} catch (OAuthError.InvalidGrant e) {
		}
	}

	@Test
	public void testHandleRequestAuthInfoNotFound() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("username")).andReturn("username1");
		expect(request.getParameter("password")).andReturn("password1");
		expect(request.getParameter("scope")).andReturn("scope1");
		DataHandler dataHandler = createDataHandlerMock(request);
		expect(dataHandler.getUserId("username1", "password1")).andReturn("userId1");
		expect(dataHandler.createOrUpdateAuthInfo("clientId1", "userId1", "scope1")).andReturn(null);
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
		expect(request.getParameter("username")).andReturn("username1");
		expect(request.getParameter("password")).andReturn("password1");
		expect(request.getParameter("scope")).andReturn("scope1");
		DataHandler dataHandler = createDataHandlerMock(request);
		expect(dataHandler.getUserId("username1", "password1")).andReturn("userId1");
		AuthInfo authInfo = new AuthInfo();
		authInfo.setClientId("clientId2");
		expect(dataHandler.createOrUpdateAuthInfo("clientId1", "userId1", "scope1")).andReturn(authInfo);
		replay(request, dataHandler);
		try {
			target.handleRequest(dataHandler);
			fail("Error.InvalidClient not occurred.");
		} catch (OAuthError.InvalidClient e) {
		}
	}

	@Test
	public void testHandleRequestSimple() throws Exception {
		Request request = createRequestMock();
		expect(request.getParameter("username")).andReturn("username1");
		expect(request.getParameter("password")).andReturn("password1");
		expect(request.getParameter("scope")).andReturn("scope1");
		DataHandler dataHandler = createDataHandlerMock(request);
		expect(dataHandler.getUserId("username1", "password1")).andReturn("userId1");
		AuthInfo authInfo = new AuthInfo();
		authInfo.setClientId("clientId1");
		expect(dataHandler.createOrUpdateAuthInfo("clientId1", "userId1", "scope1")).andReturn(authInfo);
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
		expect(request.getParameter("username")).andReturn("username1");
		expect(request.getParameter("password")).andReturn("password1");
		expect(request.getParameter("scope")).andReturn("scope1");
		DataHandler dataHandler = createDataHandlerMock(request);
		expect(dataHandler.getUserId("username1", "password1")).andReturn("userId1");
		AuthInfo authInfo = new AuthInfo();
		authInfo.setClientId("clientId1");
		authInfo.setRedirectUri("redirectUri1");
		authInfo.setRefreshToken("refreshToken1");
		authInfo.setScope("scope1");
		expect(dataHandler.createOrUpdateAuthInfo("clientId1", "userId1", "scope1")).andReturn(authInfo);
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
