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

package jp.eisbahn.oauth2.server.integration;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import jp.eisbahn.oauth2.server.endpoint.Token;
import jp.eisbahn.oauth2.server.endpoint.Token.Response;
import jp.eisbahn.oauth2.server.models.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-token-scenario.xml")
public class TokenScenarioTest {

	@Autowired
	private Token token;

	@Test
	public void testAuthorizationCodeGrant() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getParameter("grant_type")).andReturn("authorization_code");
		expect(request.getHeader("Authorization")).andReturn(null).times(2);
		expect(request.getParameter("client_id")).andReturn("clientId1").times(2);
		expect(request.getParameter("client_secret")).andReturn("clientSecret1").times(2);
		expect(request.getParameter("code")).andReturn("code1");
		expect(request.getParameter("redirect_uri")).andReturn("redirectUri1");
		replay(request);
		Response response = token.handleRequest(request);
		assertEquals(200, response.getCode());
		assertEquals(
			"{\"token_type\":\"Bearer\","
				+ "\"access_token\":\"accessToken1\","
				+ "\"refresh_token\":\"refreshToken1\","
				+ "\"expires_in\":900,"
				+ "\"scope\":\"scope1\"}",
			response.getBody());
		verify(request);
	}

	@Test
	public void testRefreshTokenGrant() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getParameter("grant_type")).andReturn("refresh_token");
		expect(request.getHeader("Authorization")).andReturn(null).times(2);
		expect(request.getParameter("client_id")).andReturn("clientId1").times(2);
		expect(request.getParameter("client_secret")).andReturn("clientSecret1").times(2);
		expect(request.getParameter("refresh_token")).andReturn("refreshToken1");
		replay(request);
		Response response = token.handleRequest(request);
		assertEquals(200, response.getCode());
		assertEquals(
			"{\"token_type\":\"Bearer\","
				+ "\"access_token\":\"accessToken1\","
				+ "\"refresh_token\":\"refreshToken1\","
				+ "\"expires_in\":900,"
				+ "\"scope\":\"scope1\"}",
			response.getBody());
		verify(request);
	}

	@Test
	public void testResourceOwnerPasswordCredentialsGrant() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getParameter("grant_type")).andReturn("password");
		expect(request.getHeader("Authorization")).andReturn(null).times(2);
		expect(request.getParameter("client_id")).andReturn("clientId1").times(2);
		expect(request.getParameter("client_secret")).andReturn("clientSecret1").times(2);
		expect(request.getParameter("username")).andReturn("username1");
		expect(request.getParameter("password")).andReturn("password1");
		expect(request.getParameter("scope")).andReturn("scope1");
		replay(request);
		Response response = token.handleRequest(request);
		assertEquals(200, response.getCode());
		assertEquals(
			"{\"token_type\":\"Bearer\","
				+ "\"access_token\":\"accessToken1\","
				+ "\"refresh_token\":\"refreshToken1\","
				+ "\"expires_in\":900,"
				+ "\"scope\":\"scope1\"}",
			response.getBody());
		verify(request);
	}

	@Test
	public void testClientCredentialsGrant() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getParameter("grant_type")).andReturn("client_credentials");
		expect(request.getHeader("Authorization")).andReturn(null).times(2);
		expect(request.getParameter("client_id")).andReturn("clientId1").times(2);
		expect(request.getParameter("client_secret")).andReturn("clientSecret1").times(2);
		expect(request.getParameter("scope")).andReturn("scope1");
		replay(request);
		Response response = token.handleRequest(request);
		assertEquals(200, response.getCode());
		assertEquals(
			"{\"token_type\":\"Bearer\","
				+ "\"access_token\":\"accessToken1\","
				+ "\"refresh_token\":\"refreshToken1\","
				+ "\"expires_in\":900,"
				+ "\"scope\":\"scope1\"}",
			response.getBody());
		verify(request);
	}

}
