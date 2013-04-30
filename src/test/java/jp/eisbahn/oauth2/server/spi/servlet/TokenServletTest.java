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

package jp.eisbahn.oauth2.server.spi.servlet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.data.DataHandlerFactory;
import jp.eisbahn.oauth2.server.models.Request;

import org.junit.Test;

public class TokenServletTest {

	@Test
	public void testSimple() throws Exception {
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getParameter("grant_type")).andReturn("authorization_code");
		expect(request.getHeader("Authorization")).andReturn("Bearer accessToken1").times(2);
		expect(request.getParameter("client_id")).andReturn("clientId1").times(2);
		expect(request.getParameter("client_secret")).andReturn("clientSecret1").times(2);
		expect(request.getParameter("code")).andReturn("code1");
		expect(request.getParameter("redirect_uri")).andReturn("redirectUri1");
		PrintWriter writer = createMock(PrintWriter.class);
		writer.write("{\"token_type\":\"Bearer\",\"access_token\":\"accessToken1\",\"refresh_token\":\"refreshToken1\",\"expires_in\":900,\"scope\":\"scope1\"}");
		writer.flush();
		HttpServletResponse response = createMock(HttpServletResponse.class);
		response.setStatus(200);
		response.setContentType("application/json; charset=UTF-8");
		expect(response.getWriter()).andReturn(writer);
		ServletConfig config = createMock(ServletConfig.class);
		expect(config.getInitParameter("dataHandlerFactory"))
			.andReturn("jp.eisbahn.oauth2.server.spi.servlet.DummyDataHandlerFactoryImpl");
		expect(config.getInitParameter("grantHandlerProvider")).andReturn(null);
		expect(config.getInitParameter("clientCredentialFetcher")).andReturn(null);
		replay(request, response, config, writer);

		TokenServlet target = new TokenServlet();
		target.init(config);
		target.doPost(request, response);

		verify(request, response, config, writer);
	}

	@Test
	public void testSimpleWithExplicitDefaultGrantHandler() throws Exception {
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getParameter("grant_type")).andReturn("authorization_code");
		expect(request.getHeader("Authorization")).andReturn("Bearer accessToken1").times(2);
		expect(request.getParameter("client_id")).andReturn("clientId1").times(2);
		expect(request.getParameter("client_secret")).andReturn("clientSecret1").times(2);
		expect(request.getParameter("code")).andReturn("code1");
		expect(request.getParameter("redirect_uri")).andReturn("redirectUri1");
		PrintWriter writer = createMock(PrintWriter.class);
		writer.write("{\"token_type\":\"Bearer\",\"access_token\":\"accessToken1\",\"refresh_token\":\"refreshToken1\",\"expires_in\":900,\"scope\":\"scope1\"}");
		writer.flush();
		HttpServletResponse response = createMock(HttpServletResponse.class);
		response.setStatus(200);
		response.setContentType("application/json; charset=UTF-8");
		expect(response.getWriter()).andReturn(writer);
		ServletConfig config = createMock(ServletConfig.class);
		expect(config.getInitParameter("dataHandlerFactory"))
			.andReturn("jp.eisbahn.oauth2.server.spi.servlet.DummyDataHandlerFactoryImpl");
		expect(config.getInitParameter("grantHandlerProvider")).andReturn(
				"jp.eisbahn.oauth2.server.granttype.impl.DefaultGrantHandlerProvider");
		expect(config.getInitParameter("clientCredentialFetcher")).andReturn(null);
		replay(request, response, config, writer);

		TokenServlet target = new TokenServlet();
		target.init(config);
		target.doPost(request, response);

		verify(request, response, config, writer);
	}

	@Test
	public void testSimpleWithExplicitClientCredentialFetcher() throws Exception {
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getParameter("grant_type")).andReturn("authorization_code");
		expect(request.getHeader("Authorization")).andReturn("Bearer accessToken1").times(2);
		expect(request.getParameter("client_id")).andReturn("clientId1").times(2);
		expect(request.getParameter("client_secret")).andReturn("clientSecret1").times(2);
		expect(request.getParameter("code")).andReturn("code1");
		expect(request.getParameter("redirect_uri")).andReturn("redirectUri1");
		PrintWriter writer = createMock(PrintWriter.class);
		writer.write("{\"token_type\":\"Bearer\",\"access_token\":\"accessToken1\",\"refresh_token\":\"refreshToken1\",\"expires_in\":900,\"scope\":\"scope1\"}");
		writer.flush();
		HttpServletResponse response = createMock(HttpServletResponse.class);
		response.setStatus(200);
		response.setContentType("application/json; charset=UTF-8");
		expect(response.getWriter()).andReturn(writer);
		ServletConfig config = createMock(ServletConfig.class);
		expect(config.getInitParameter("dataHandlerFactory"))
			.andReturn("jp.eisbahn.oauth2.server.spi.servlet.DummyDataHandlerFactoryImpl");
		expect(config.getInitParameter("grantHandlerProvider")).andReturn(
				"jp.eisbahn.oauth2.server.granttype.impl.DefaultGrantHandlerProvider");
		expect(config.getInitParameter("clientCredentialFetcher")).andReturn(
				"jp.eisbahn.oauth2.server.fetcher.clientcredential.ClientCredentialFetcherImpl");
		replay(request, response, config, writer);

		TokenServlet target = new TokenServlet();
		target.init(config);
		target.doPost(request, response);

		verify(request, response, config, writer);
	}

	@Test
	public void testClassNotFound() throws Exception {
		ServletConfig config = createMock(ServletConfig.class);
		expect(config.getInitParameter("dataHandlerFactory")).andReturn("evil");
		replay(config);

		TokenServlet target = new TokenServlet();
		try {
			target.init(config);
			fail("ServletException not occurred.");
		} catch(ServletException e) {
		}
		verify(config);
	}

	@Test
	public void testIllegalAccessException() throws Exception {
		ServletConfig config = createMock(ServletConfig.class);
		expect(config.getInitParameter("dataHandlerFactory")).andReturn(
				EvilDataHandlerFactory.class.getName());
		replay(config);

		TokenServlet target = new TokenServlet();
		try {
			target.init(config);
			fail("ServletException not occurred.");
		} catch(ServletException e) {
		}
		verify(config);
	}

	@Test
	public void testInstantiationException() throws Exception {
		ServletConfig config = createMock(ServletConfig.class);
		expect(config.getInitParameter("dataHandlerFactory")).andReturn(
				DataHandlerFactory.class.getName());
		replay(config);

		TokenServlet target = new TokenServlet();
		try {
			target.init(config);
			fail("ServletException not occurred.");
		} catch(ServletException e) {
		}
		verify(config);
	}

	public static class EvilDataHandlerFactory implements DataHandlerFactory {

		private EvilDataHandlerFactory() {
		}

		@Override
		public DataHandler create(Request request) {
			return null;
		}
		
	}
	
}
