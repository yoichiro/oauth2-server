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

package jp.eisbahn.oauth2.server.exceptions;

import static org.junit.Assert.*;

import org.junit.Test;

import jp.eisbahn.oauth2.server.exceptions.OAuthError;
import jp.eisbahn.oauth2.server.exceptions.OAuthError.AccessDenied;
import jp.eisbahn.oauth2.server.exceptions.OAuthError.ExpiredToken;
import jp.eisbahn.oauth2.server.exceptions.OAuthError.InsufficientScope;
import jp.eisbahn.oauth2.server.exceptions.OAuthError.InvalidClient;
import jp.eisbahn.oauth2.server.exceptions.OAuthError.InvalidGrant;
import jp.eisbahn.oauth2.server.exceptions.OAuthError.InvalidRequest;
import jp.eisbahn.oauth2.server.exceptions.OAuthError.InvalidScope;
import jp.eisbahn.oauth2.server.exceptions.OAuthError.InvalidToken;
import jp.eisbahn.oauth2.server.exceptions.OAuthError.RedirectUriMismatch;
import jp.eisbahn.oauth2.server.exceptions.OAuthError.UnauthorizedClient;
import jp.eisbahn.oauth2.server.exceptions.OAuthError.UnsupportedGrantType;
import jp.eisbahn.oauth2.server.exceptions.OAuthError.UnsupportedResponseType;

public class OAuthErrorTest {

	@SuppressWarnings("serial")
	private static class Target extends OAuthError {

		public Target(int code, String description) {
			super(code, description);
		}

		public Target(String description) {
			super(description);
		}

		@Override
		public String getType() {
			return null;
		}

	}

	@Test
	public void testErrorSimple() {
		OAuthError target = new Target(401, "desc1");
		assertEquals(401, target.getCode());
		assertEquals("desc1", target.getDescription());

		target = new Target("desc2");
		assertEquals(400, target.getCode());
		assertEquals("desc2", target.getDescription());
	}

	@Test
	public void testInvalidRequest() {
		InvalidRequest target = new InvalidRequest("desc1");
		assertEquals(400, target.getCode());
		assertEquals("desc1", target.getDescription());
		assertEquals("invalid_request", target.getType());
	}

	@Test
	public void testInvalidClient() {
		InvalidClient target = new InvalidClient("desc1");
		assertEquals(401, target.getCode());
		assertEquals("desc1", target.getDescription());
		assertEquals("invalid_client", target.getType());
	}

	@Test
	public void testUnauthorizedClient() {
		UnauthorizedClient target = new UnauthorizedClient("desc1");
		assertEquals(401, target.getCode());
		assertEquals("desc1", target.getDescription());
		assertEquals("unauthorized_client", target.getType());
	}

	@Test
	public void testRedirectUriMismatch() {
		RedirectUriMismatch target = new RedirectUriMismatch("desc1");
		assertEquals(401, target.getCode());
		assertEquals("desc1", target.getDescription());
		assertEquals("redirect_uri_mismatch", target.getType());
	}

	@Test
	public void testAccessDenied() {
		AccessDenied target = new AccessDenied("desc1");
		assertEquals(401, target.getCode());
		assertEquals("desc1", target.getDescription());
		assertEquals("access_denied", target.getType());
	}

	@Test
	public void testUnsupportedResponseType() {
		UnsupportedResponseType target = new UnsupportedResponseType("desc1");
		assertEquals(400, target.getCode());
		assertEquals("desc1", target.getDescription());
		assertEquals("unsupported_response_type", target.getType());
	}

	@Test
	public void testInvalidGrant() {
		InvalidGrant target = new InvalidGrant("desc1");
		assertEquals(401, target.getCode());
		assertEquals("desc1", target.getDescription());
		assertEquals("invalid_grant", target.getType());
	}

	@Test
	public void testUnsupportedGrantType() {
		UnsupportedGrantType target = new UnsupportedGrantType("desc1");
		assertEquals(400, target.getCode());
		assertEquals("desc1", target.getDescription());
		assertEquals("unsupported_grant_type", target.getType());
	}

	@Test
	public void testInvalidScope() {
		InvalidScope target = new InvalidScope("desc1");
		assertEquals(401, target.getCode());
		assertEquals("desc1", target.getDescription());
		assertEquals("invalid_scope", target.getType());
	}

	@Test
	public void testInvalidToken() {
		InvalidToken target = new InvalidToken("desc1");
		assertEquals(401, target.getCode());
		assertEquals("desc1", target.getDescription());
		assertEquals("invalid_token", target.getType());
	}

	@Test
	public void testExpiredToken() {
		ExpiredToken target = new ExpiredToken();
		assertEquals(401, target.getCode());
		assertEquals("The access token expired", target.getDescription());
		assertEquals("invalid_token", target.getType());
	}

	@Test
	public void testInsufficientScope() {
		InsufficientScope target = new InsufficientScope("desc1");
		assertEquals(401, target.getCode());
		assertEquals("desc1", target.getDescription());
		assertEquals("insufficient_scope", target.getType());
	}

}
