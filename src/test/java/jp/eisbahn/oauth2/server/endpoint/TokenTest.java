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
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.data.DataHandlerFactory;
import jp.eisbahn.oauth2.server.endpoint.Token;
import jp.eisbahn.oauth2.server.endpoint.Token.Response;
import jp.eisbahn.oauth2.server.fetcher.clientcredential.ClientCredentialFetcherImpl;
import jp.eisbahn.oauth2.server.granttype.GrantHandler;
import jp.eisbahn.oauth2.server.granttype.GrantHandlerProvider;
import jp.eisbahn.oauth2.server.granttype.impl.RefreshToken;
import jp.eisbahn.oauth2.server.models.AccessToken;
import jp.eisbahn.oauth2.server.models.AuthInfo;
import jp.eisbahn.oauth2.server.models.Request;

import org.junit.Test;

public class TokenTest {

	@Test
	public void testHandleRequestGrantTypeNotFound() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getParameter("grant_type")).andReturn(null);
		replay(request);
		Token target = new Token();
		Response response = target.handleRequest(request);
		assertEquals(400, response.getCode());
		assertEquals(
			"{\"error\":\"invalid_request\","
				+ "\"error_description\":\"'grant_type' not found\"}",
			response.getBody());
		verify(request);
	}

	@Test
	public void testHandleRequestGrantTypeNotSupported() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getParameter("grant_type")).andReturn("evil");
		DataHandlerFactory factory = createMock(DataHandlerFactory.class);
		replay(request, factory);
		Token target = createToken(factory);
		Response response = target.handleRequest(request);
		assertEquals(400, response.getCode());
		assertEquals(
			"{\"error\":\"unsupported_grant_type\"}",
			response.getBody());
		verify(request, factory);
	}

	@Test
	public void testHandleRequestClientIdNotFound() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getParameter("grant_type")).andReturn("refresh_token");
		expect(request.getHeader("Authorization")).andReturn(null);
		expect(request.getParameter("client_id")).andReturn(null);
		expect(request.getParameter("client_secret")).andReturn(null);
		DataHandlerFactory factory = createMock(DataHandlerFactory.class);
		expect(factory.create(request)).andReturn(null);
		replay(request, factory);
		Token target = createToken(factory);
		Response response = target.handleRequest(request);
		assertEquals(400, response.getCode());
		assertEquals(
			"{\"error\":\"invalid_request\","
				+ "\"error_description\":\"'client_id' not found\"}",
			response.getBody());
		verify(request, factory);
	}

	@Test
	public void testHandleRequestClientSecretNotFound() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getParameter("grant_type")).andReturn("refresh_token");
		expect(request.getHeader("Authorization")).andReturn(null);
		expect(request.getParameter("client_id")).andReturn("clientId1");
		expect(request.getParameter("client_secret")).andReturn(null);
		DataHandlerFactory factory = createMock(DataHandlerFactory.class);
		expect(factory.create(request)).andReturn(null);
		replay(request, factory);
		Token target = createToken(factory);
		Response response = target.handleRequest(request);
		assertEquals(400, response.getCode());
		assertEquals(
			"{\"error\":\"invalid_request\","
				+ "\"error_description\":\"'client_secret' not found\"}",
			response.getBody());
		verify(request, factory);
	}

	@Test
	public void testHandleRequestClientInvalid() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getParameter("grant_type")).andReturn("refresh_token");
		expect(request.getHeader("Authorization")).andReturn(null);
		expect(request.getParameter("client_id")).andReturn("clientId1");
		expect(request.getParameter("client_secret")).andReturn("clientSecret1");
		DataHandlerFactory factory = createMock(DataHandlerFactory.class);
		DataHandler dataHandler = createMock(DataHandler.class);
		expect(dataHandler.validateClient(
			"clientId1", "clientSecret1", "refresh_token")).andReturn(false);
		expect(factory.create(request)).andReturn(dataHandler);
		replay(request, factory, dataHandler);
		Token target = createToken(factory);
		Response response = target.handleRequest(request);
		assertEquals(401, response.getCode());
		assertEquals(
			"{\"error\":\"invalid_client\"}",
			response.getBody());
		verify(request, factory, dataHandler);
	}

	@Test
	public void testHandleRequestSimple() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getParameter("grant_type")).andReturn("refresh_token");
		expect(request.getHeader("Authorization")).andReturn(null).times(2);
		expect(request.getParameter("client_id")).andReturn("clientId1").times(2);
		expect(request.getParameter("client_secret")).andReturn("clientSecret1").times(2);
		expect(request.getParameter("refresh_token")).andReturn("refreshToken1");
		DataHandlerFactory factory = createMock(DataHandlerFactory.class);
		DataHandler dataHandler = createMock(DataHandler.class);
		expect(dataHandler.validateClient(
			"clientId1", "clientSecret1", "refresh_token")).andReturn(true);
		expect(dataHandler.getRequest()).andReturn(request);
		AuthInfo authInfo = new AuthInfo();
		authInfo.setClientId("clientId1");
		expect(dataHandler.getAuthInfoByRefreshToken("refreshToken1")).andReturn(authInfo);
		AccessToken accessToken = new AccessToken();
		accessToken.setToken("accessToken1");
		expect(dataHandler.createOrUpdateAccessToken(authInfo)).andReturn(accessToken);
		expect(factory.create(request)).andReturn(dataHandler);
		replay(request, factory, dataHandler);
		Token target = createToken(factory);
		Response response = target.handleRequest(request);
		assertEquals(200, response.getCode());
		assertEquals(
			"{\"token_type\":\"Bearer\",\"access_token\":\"accessToken1\"}",
			response.getBody());
		verify(request, factory, dataHandler);
	}

	@SuppressWarnings("serial")
	private Token createToken(DataHandlerFactory factory) {
		Token token = new Token();
		token.setDataHandlerFactory(factory);
		token.setClientCredentialFetcher(new ClientCredentialFetcherImpl());
		GrantHandlerProvider provider = new GrantHandlerProvider();
		final RefreshToken refreshToken = new RefreshToken();
		refreshToken.setClientCredentialFetcher(new ClientCredentialFetcherImpl());
		provider.setGrantHandlers(new HashMap<String, GrantHandler>() {
			{
				put("refresh_token", refreshToken);
			}
		});
		token.setGrantHandlerProvider(provider);
		return token;
	}

}
