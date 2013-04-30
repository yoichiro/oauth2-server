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

package jp.eisbahn.oauth2.server.models;

import static org.junit.Assert.assertEquals;
import jp.eisbahn.oauth2.server.models.AuthInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthInfoTest {

	private AuthInfo target;

	@Before
	public void setUp() {
		target = new AuthInfo();
	}

	@After
	public void tearDown() {
		target = null;
	}

	@Test
	public void testIdProperty() throws Exception {
		target.setId("id1");
		assertEquals("id1", target.getId());
	}

	@Test
	public void testUserIdProperty() throws Exception {
		target.setUserId("userId1");
		assertEquals("userId1", target.getUserId());
	}

	@Test
	public void testClientIdProperty() throws Exception {
		target.setClientId("clientId1");
		assertEquals("clientId1", target.getClientId());
	}

	@Test
	public void testScopeProperty() throws Exception {
		target.setScope("scope1");
		assertEquals("scope1", target.getScope());
	}

	@Test
	public void testRefreshTokenProperty() throws Exception {
		target.setRefreshToken("refreshToken1");
		assertEquals("refreshToken1", target.getRefreshToken());
	}

	@Test
	public void testCodeProperty() throws Exception {
		target.setCode("code1");
		assertEquals("code1", target.getCode());
	}

	@Test
	public void testRedirectUriProperty() throws Exception {
		target.setRedirectUri("redirectUri1");
		assertEquals("redirectUri1", target.getRedirectUri());
	}

}
