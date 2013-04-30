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

package jp.eisbahn.oauth2.server.data;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Test;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.models.AccessToken;
import jp.eisbahn.oauth2.server.models.AuthInfo;
import jp.eisbahn.oauth2.server.models.Request;

public class DataHandlerTest {

	private class Target extends DataHandler {

		public Target(Request request) {
			super(request);
		}

		@Override
		public boolean validateClient(String clientId, String clientSecret,
				String grantType) {
			return false;
		}

		@Override
		public String getUserId(String userName, String password) {
			return null;
		}

		@Override
		public AuthInfo createOrUpdateAuthInfo(String clientId, String userId,
				String scope) {
			return null;
		}

		@Override
		public AccessToken createOrUpdateAccessToken(AuthInfo authInfo) {
			return null;
		}

		@Override
		public AuthInfo getAuthInfoByCode(String code) {
			return null;
		}

		@Override
		public AuthInfo getAuthInfoByRefreshToken(String refreshToken) {
			return null;
		}

		@Override
		public String getClientUserId(String clientId, String clientSecret) {
			return null;
		}

		@Override
		public AccessToken getAccessToken(String token) {
			return null;
		}

		@Override
		public AuthInfo getAuthInfoById(String id) {
			return null;
		}

		@Override
		public boolean validateClientById(String clientId) {
			return true;
		}

		@Override
		public boolean validateUserById(String userId) {
			return true;
		}

	}

	@Test
	public void testSimple() throws Exception {
		Request request = EasyMock.createMock(Request.class);
		DataHandler target = new Target(request);
		assertEquals(request, target.getRequest());
		assertTrue(target.validateClientById(null));
		assertTrue(target.validateUserById(null));
	}

}
