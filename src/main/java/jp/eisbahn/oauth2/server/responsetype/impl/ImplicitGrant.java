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

package jp.eisbahn.oauth2.server.responsetype.impl;

import org.apache.commons.lang3.StringUtils;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.exceptions.OAuthError;
import jp.eisbahn.oauth2.server.models.AccessToken;
import jp.eisbahn.oauth2.server.models.AuthInfo;

public class ImplicitGrant extends AbstractAuthorizationHandler {

	@Override
	public String getResponseType() {
		return "token";
	}

	@Override
	public AuthorizationRequest prepare(DataHandler dataHandler,
			AuthorizationRequest request) throws OAuthError {
		final String clientId = getClientId(dataHandler);
		getOAuth2Parameters(request, dataHandler, clientId);
		request.setClientId(clientId);
		return request;
	}

	@Override
	public AuthorizationResponse allow(String userId, DataHandler dataHandler,
			AuthorizationRequest request, AuthorizationResponse response)
			throws OAuthError {
		AuthInfo authInfo = createOrUpdateAuthInfo(
				userId, request, response, dataHandler);
		AccessToken accessToken = dataHandler.createOrUpdateAccessToken(authInfo);
		response.setAccessToken(accessToken.getToken());
		response.setTokenType("Bearer");
		long expiresIn = accessToken.getExpiresIn();
		if (expiresIn > 0) {
			response.setExpiresIn(expiresIn);
		}
		response.setScope(StringUtils.join(request.getScopes(), ' '));
		response.setState(request.getState());
		return response;
	}

}
