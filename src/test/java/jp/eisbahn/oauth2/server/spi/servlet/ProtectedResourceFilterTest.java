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

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.data.DataHandlerFactory;
import jp.eisbahn.oauth2.server.models.Request;

public class ProtectedResourceFilterTest {

	@Test
	public void testSuccess() throws Exception {
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getHeader("Authorization")).andReturn("Bearer accessToken1").times(2);
		request.setAttribute("client_id", "clientId1");
		request.setAttribute("remote_user", "userId1");
		request.setAttribute("scope", "scope1");
		HttpServletResponse response = createMock(HttpServletResponse.class);
		FilterConfig config = createMock(FilterConfig.class);
		FilterChain chain = createMock(FilterChain.class);
		chain.doFilter(request, response);
		expect(config.getInitParameter("dataHandlerFactory"))
			.andReturn("jp.eisbahn.oauth2.server.spi.servlet.DummyDataHandlerFactoryImpl");
		expect(config.getInitParameter("accessTokenFetcherProvider")).andReturn(null);
		replay(request, response, config, chain);
		ProtectedResourceFilter target = new ProtectedResourceFilter();
		target.init(config);
		target.doFilter(request, response, chain);
		target.destroy();
		verify(request, response, config, chain);
	}

	@Test
	public void testSuccessExplicitAccessTokenFetcherProvider() throws Exception {
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getHeader("Authorization")).andReturn("Bearer accessToken1").times(2);
		request.setAttribute("client_id", "clientId1");
		request.setAttribute("remote_user", "userId1");
		request.setAttribute("scope", "scope1");
		HttpServletResponse response = createMock(HttpServletResponse.class);
		FilterConfig config = createMock(FilterConfig.class);
		FilterChain chain = createMock(FilterChain.class);
		chain.doFilter(request, response);
		expect(config.getInitParameter("dataHandlerFactory"))
			.andReturn("jp.eisbahn.oauth2.server.spi.servlet.DummyDataHandlerFactoryImpl");
		expect(config.getInitParameter("accessTokenFetcherProvider")).andReturn(
			"jp.eisbahn.oauth2.server.fetcher.accesstoken.impl.DefaultAccessTokenFetcherProvider");
		replay(request, response, config, chain);
		ProtectedResourceFilter target = new ProtectedResourceFilter();
		target.init(config);
		target.doFilter(request, response, chain);
		target.destroy();
		verify(request, response, config, chain);
	}

	@Test
	public void testFailed() throws Exception {
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getHeader("Authorization")).andReturn(null);
		expect(request.getParameter("oauth_token")).andReturn(null);
		expect(request.getParameter("access_token")).andReturn(null);
		HttpServletResponse response = createMock(HttpServletResponse.class);
		response.setStatus(400);
		response.setHeader("WWW-Authenticate",
				"Bearer error=\"invalid_request\", "
				+ "error_description=\"Access token was not specified.\"");
		FilterConfig config = createMock(FilterConfig.class);
		FilterChain chain = createMock(FilterChain.class);
		expect(config.getInitParameter("dataHandlerFactory"))
			.andReturn("jp.eisbahn.oauth2.server.spi.servlet.DummyDataHandlerFactoryImpl");
		expect(config.getInitParameter("accessTokenFetcherProvider")).andReturn(null);
		replay(request, response, config, chain);
		ProtectedResourceFilter target = new ProtectedResourceFilter();
		target.init(config);
		target.doFilter(request, response, chain);
		target.destroy();
		verify(request, response, config, chain);
	}

	@Test
	public void testClassNotFound() throws Exception {
		FilterConfig config = createMock(FilterConfig.class);
		expect(config.getInitParameter("dataHandlerFactory")).andReturn("evil");
		replay(config);

		ProtectedResourceFilter target = new ProtectedResourceFilter();
		try {
			target.init(config);
			fail("ServletException not occurred.");
		} catch (ServletException e) {
		}
		verify(config);
	}

	@Test
	public void testIllegalAccessException() throws Exception {
		FilterConfig config = createMock(FilterConfig.class);
		expect(config.getInitParameter("dataHandlerFactory")).andReturn(
			EvilDataHandlerFactory.class.getName());
		replay(config);

		ProtectedResourceFilter target = new ProtectedResourceFilter();
		try {
			target.init(config);
			fail("ServletException not occurred.");
		} catch (ServletException e) {
		}
		verify(config);
	}

	@Test
	public void testInstantiationException() throws Exception {
		FilterConfig config = createMock(FilterConfig.class);
		expect(config.getInitParameter("dataHandlerFactory")).andReturn(
			DataHandlerFactory.class.getName());
		replay(config);

		ProtectedResourceFilter target = new ProtectedResourceFilter();
		try {
			target.init(config);
			fail("ServletException not occurred.");
		} catch (ServletException e) {
		}
		verify(config);
	}

	@Test
	public void testRequestNotHttpServletRequest() throws Exception {
		ServletRequest request = createMock(ServletRequest.class);
		replay(request);
		ProtectedResourceFilter target = new ProtectedResourceFilter();
		try {
			target.doFilter(request, null, null);
			fail("ServletException not occurred.");
		} catch (ServletException e) {
		}
		verify(request);
	}

	@Test
	public void testResponseNotHttpServletResponse() throws Exception {
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getHeader("Authorization")).andReturn(null);
		expect(request.getParameter("oauth_token")).andReturn(null);
		expect(request.getParameter("access_token")).andReturn(null);
		ServletResponse response = createMock(ServletResponse.class);
		FilterConfig config = createMock(FilterConfig.class);
		FilterChain chain = createMock(FilterChain.class);
		expect(config.getInitParameter("dataHandlerFactory"))
			.andReturn("jp.eisbahn.oauth2.server.spi.servlet.DummyDataHandlerFactoryImpl");
		expect(config.getInitParameter("accessTokenFetcherProvider")).andReturn(null);
		replay(request, response, config, chain);
		ProtectedResourceFilter target = new ProtectedResourceFilter();
		target.init(config);
		try {
			target.doFilter(request, response, chain);
			fail("ServletException not occurred.");
		} catch (ServletException e) {
		}
		target.destroy();
		verify(request, response, config, chain);
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
