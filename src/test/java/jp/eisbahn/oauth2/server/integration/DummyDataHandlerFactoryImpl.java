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

import java.util.Date;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.data.DataHandlerFactory;
import jp.eisbahn.oauth2.server.models.AccessToken;
import jp.eisbahn.oauth2.server.models.AuthInfo;
import jp.eisbahn.oauth2.server.models.Request;

public class DummyDataHandlerFactoryImpl implements DataHandlerFactory {

	@Override
	public DataHandler create(Request request) {
		return new DummyDataHandler(request);
	}

	public static class DummyDataHandler extends DataHandler {

		public DummyDataHandler(Request request) {
			super(request);
		}

		@Override
		public boolean validateClient(String clientId, String clientSecret,
				String grantType) {
			return true;
		}

		@Override
		public String getUserId(String username, String password) {
			return "userId1";
		}

		@Override
		public AuthInfo createOrUpdateAuthInfo(String clientId, String userId,
				String scope) {
			AuthInfo authInfo = new AuthInfo();
			authInfo.setClientId(clientId);
			authInfo.setUserId(userId);
			authInfo.setRefreshToken("refreshToken1");
			authInfo.setScope(scope);
			return authInfo;
		}

		@Override
		public AccessToken createOrUpdateAccessToken(AuthInfo authInfo) {
			AccessToken accessToken = new AccessToken();
			accessToken.setToken("accessToken1");
			accessToken.setExpiresIn(900L);
			return accessToken;
		}

		@Override
		public AuthInfo getAuthInfoByCode(String code) {
			AuthInfo authInfo = new AuthInfo();
			authInfo.setClientId("clientId1");
			authInfo.setRedirectUri("redirectUri1");
			authInfo.setRefreshToken("refreshToken1");
			authInfo.setScope("scope1");
			authInfo.setCode(code);
			return authInfo;
		}

		@Override
		public AuthInfo getAuthInfoByRefreshToken(String refreshToken) {
			AuthInfo authInfo = new AuthInfo();
			authInfo.setClientId("clientId1");
			authInfo.setRedirectUri("redirectUri1");
			authInfo.setRefreshToken(refreshToken);
			authInfo.setScope("scope1");
			return authInfo;
		}

		@Override
		public String getClientUserId(String clientId, String clientSecret) {
			return "clientUserId";
		}

		@Override
		public AccessToken getAccessToken(String token) {
			AccessToken accessToken = new AccessToken();
			accessToken.setAuthId("authId1");
			accessToken.setCreatedOn(new Date());
			accessToken.setExpiresIn(3600);
			return accessToken;
		}

		@Override
		public AuthInfo getAuthInfoById(String id) {
			AuthInfo authInfo = new AuthInfo();
			authInfo.setClientId("clientId1");
			authInfo.setId(id);
			authInfo.setUserId("userId1");
			authInfo.setScope("scope1");
			return authInfo;
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

}
