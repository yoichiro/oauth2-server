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

package jp.eisbahn.oauth2.server.endpoint;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.data.DataHandlerFactory;
import jp.eisbahn.oauth2.server.endpoint.ProtectedResource.Response;
import jp.eisbahn.oauth2.server.exceptions.OAuthError;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.AccessTokenFetcher;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.AccessTokenFetcherProvider;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.impl.AuthHeader;
import jp.eisbahn.oauth2.server.models.AccessToken;
import jp.eisbahn.oauth2.server.models.AuthInfo;
import jp.eisbahn.oauth2.server.models.Request;

public class ProtectedResourceTest {

	@Test
	public void testHandleRequestAccessTokenFetcherNotFound() throws Exception {
		Request request = createMock(Request.class);
		replay(request);
		ProtectedResource target = new ProtectedResource();
		AccessTokenFetcherProvider accessTokenFetcherProvider = new AccessTokenFetcherProvider();
		accessTokenFetcherProvider.setAccessTokenFetchers(new AccessTokenFetcher[]{});
		target.setAccessTokenFetcherProvider(accessTokenFetcherProvider);
		try {
			target.handleRequest(request);
			fail("OAuthError not occurred.");
		} catch (OAuthError e) {
			assertTrue(e instanceof OAuthError.InvalidRequest);
		}
		verify(request);
	}
	
	@Test
	public void testHandleRequestAccessTokenNotFound() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn("Bearer accessToken1").times(2);
		DataHandler dataHandler = createMock(DataHandler.class);
		expect(dataHandler.getAccessToken("accessToken1")).andReturn(null);
		DataHandlerFactory dataHandlerFactory = createMock(DataHandlerFactory.class);
		expect(dataHandlerFactory.create(request)).andReturn(dataHandler);
		replay(request, dataHandler, dataHandlerFactory);
		ProtectedResource target = new ProtectedResource();
		AccessTokenFetcherProvider accessTokenFetcherProvider = new AccessTokenFetcherProvider();
		accessTokenFetcherProvider.setAccessTokenFetchers(new AccessTokenFetcher[]{
				new AuthHeader()
		});
		target.setAccessTokenFetcherProvider(accessTokenFetcherProvider);
		target.setDataHandlerFactory(dataHandlerFactory);
		try {
			target.handleRequest(request);
			fail("OAuthError not occurred.");
		} catch (OAuthError e) {
			assertTrue(e instanceof OAuthError.InvalidToken);
		}
		verify(request);
	}
	
	private Date createDate(int daysAgo) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, daysAgo);
		return cal.getTime();
	}
	
	@Test
	public void testHandleRequestAccessTokenExpired() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn("Bearer accessToken1").times(2);
		AccessToken accessToken = new AccessToken();
		accessToken.setCreatedOn(createDate(-1));
		accessToken.setExpiresIn(0);
		DataHandler dataHandler = createMock(DataHandler.class);
		expect(dataHandler.getAccessToken("accessToken1")).andReturn(accessToken);
		DataHandlerFactory dataHandlerFactory = createMock(DataHandlerFactory.class);
		expect(dataHandlerFactory.create(request)).andReturn(dataHandler);
		replay(request, dataHandler, dataHandlerFactory);
		ProtectedResource target = new ProtectedResource();
		AccessTokenFetcherProvider accessTokenFetcherProvider = new AccessTokenFetcherProvider();
		accessTokenFetcherProvider.setAccessTokenFetchers(new AccessTokenFetcher[]{
				new AuthHeader()
		});
		target.setAccessTokenFetcherProvider(accessTokenFetcherProvider);
		target.setDataHandlerFactory(dataHandlerFactory);
		try {
			target.handleRequest(request);
			fail("OAuthError not occurred.");
		} catch (OAuthError e) {
			assertTrue(e instanceof OAuthError.ExpiredToken);
		}
		verify(request);
	}

	@Test
	public void testHandleRequestAuthInfoNotFound() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn("Bearer accessToken1").times(2);
		AccessToken accessToken = new AccessToken();
		accessToken.setCreatedOn(createDate(0));
		accessToken.setExpiresIn(3600);
		accessToken.setAuthId("authId1");
		DataHandler dataHandler = createMock(DataHandler.class);
		expect(dataHandler.getAccessToken("accessToken1")).andReturn(accessToken);
		expect(dataHandler.getAuthInfoById("authId1")).andReturn(null);
		DataHandlerFactory dataHandlerFactory = createMock(DataHandlerFactory.class);
		expect(dataHandlerFactory.create(request)).andReturn(dataHandler);
		replay(request, dataHandler, dataHandlerFactory);
		ProtectedResource target = new ProtectedResource();
		AccessTokenFetcherProvider accessTokenFetcherProvider = new AccessTokenFetcherProvider();
		accessTokenFetcherProvider.setAccessTokenFetchers(new AccessTokenFetcher[]{
				new AuthHeader()
		});
		target.setAccessTokenFetcherProvider(accessTokenFetcherProvider);
		target.setDataHandlerFactory(dataHandlerFactory);
		try {
			target.handleRequest(request);
			fail("OAuthError not occurred.");
		} catch (OAuthError e) {
			assertTrue(e instanceof OAuthError.InvalidToken);
		}
		verify(request);
	}

	@Test
	public void testHandleRequestValidateClientFailed() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn("Bearer accessToken1").times(2);
		AccessToken accessToken = new AccessToken();
		accessToken.setCreatedOn(createDate(0));
		accessToken.setExpiresIn(3600);
		accessToken.setAuthId("authId1");
		DataHandler dataHandler = createMock(DataHandler.class);
		expect(dataHandler.getAccessToken("accessToken1")).andReturn(accessToken);
		AuthInfo authInfo = new AuthInfo();
		authInfo.setClientId("clientId1");
		expect(dataHandler.getAuthInfoById("authId1")).andReturn(authInfo);
		expect(dataHandler.validateClientById("clientId1")).andReturn(false);
		DataHandlerFactory dataHandlerFactory = createMock(DataHandlerFactory.class);
		expect(dataHandlerFactory.create(request)).andReturn(dataHandler);
		replay(request, dataHandler, dataHandlerFactory);
		ProtectedResource target = new ProtectedResource();
		AccessTokenFetcherProvider accessTokenFetcherProvider = new AccessTokenFetcherProvider();
		accessTokenFetcherProvider.setAccessTokenFetchers(new AccessTokenFetcher[]{
				new AuthHeader()
		});
		target.setAccessTokenFetcherProvider(accessTokenFetcherProvider);
		target.setDataHandlerFactory(dataHandlerFactory);
		try {
			target.handleRequest(request);
			fail("OAuthError not occurred.");
		} catch (OAuthError e) {
			assertTrue(e instanceof OAuthError.InvalidToken);
		}
		verify(request);
	}

	@Test
	public void testHandleRequestValidateUserFailed() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn("Bearer accessToken1").times(2);
		AccessToken accessToken = new AccessToken();
		accessToken.setCreatedOn(createDate(0));
		accessToken.setExpiresIn(3600);
		accessToken.setAuthId("authId1");
		DataHandler dataHandler = createMock(DataHandler.class);
		expect(dataHandler.getAccessToken("accessToken1")).andReturn(accessToken);
		AuthInfo authInfo = new AuthInfo();
		authInfo.setClientId("clientId1");
		authInfo.setUserId("userId1");
		expect(dataHandler.getAuthInfoById("authId1")).andReturn(authInfo);
		expect(dataHandler.validateClientById("clientId1")).andReturn(true);
		expect(dataHandler.validateUserById("userId1")).andReturn(false);
		DataHandlerFactory dataHandlerFactory = createMock(DataHandlerFactory.class);
		expect(dataHandlerFactory.create(request)).andReturn(dataHandler);
		replay(request, dataHandler, dataHandlerFactory);
		ProtectedResource target = new ProtectedResource();
		AccessTokenFetcherProvider accessTokenFetcherProvider = new AccessTokenFetcherProvider();
		accessTokenFetcherProvider.setAccessTokenFetchers(new AccessTokenFetcher[]{
				new AuthHeader()
		});
		target.setAccessTokenFetcherProvider(accessTokenFetcherProvider);
		target.setDataHandlerFactory(dataHandlerFactory);
		try {
			target.handleRequest(request);
			fail("OAuthError not occurred.");
		} catch (OAuthError e) {
			assertTrue(e instanceof OAuthError.InvalidToken);
		}
		verify(request);
	}

	@Test
	public void testHandleRequestSuccess() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn("Bearer accessToken1").times(2);
		AccessToken accessToken = new AccessToken();
		accessToken.setCreatedOn(createDate(0));
		accessToken.setExpiresIn(3600);
		accessToken.setAuthId("authId1");
		DataHandler dataHandler = createMock(DataHandler.class);
		expect(dataHandler.getAccessToken("accessToken1")).andReturn(accessToken);
		AuthInfo authInfo = new AuthInfo();
		authInfo.setClientId("clientId1");
		authInfo.setUserId("userId1");
		authInfo.setScope("scope1");
		expect(dataHandler.getAuthInfoById("authId1")).andReturn(authInfo);
		expect(dataHandler.validateClientById("clientId1")).andReturn(true);
		expect(dataHandler.validateUserById("userId1")).andReturn(true);
		DataHandlerFactory dataHandlerFactory = createMock(DataHandlerFactory.class);
		expect(dataHandlerFactory.create(request)).andReturn(dataHandler);
		replay(request, dataHandler, dataHandlerFactory);
		ProtectedResource target = new ProtectedResource();
		AccessTokenFetcherProvider accessTokenFetcherProvider = new AccessTokenFetcherProvider();
		accessTokenFetcherProvider.setAccessTokenFetchers(new AccessTokenFetcher[]{
				new AuthHeader()
		});
		target.setAccessTokenFetcherProvider(accessTokenFetcherProvider);
		target.setDataHandlerFactory(dataHandlerFactory);
		Response response = target.handleRequest(request);
		assertEquals("userId1", response.getRemoteUser());
		assertEquals("clientId1", response.getClientId());
		assertEquals("scope1", response.getScope());
		verify(request);
	}

}
