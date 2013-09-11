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

package jp.eisbahn.oauth2.server.granttype.impl;

import org.apache.commons.lang3.StringUtils;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.exceptions.OAuthError;
import jp.eisbahn.oauth2.server.fetcher.clientcredential.ClientCredentialFetcher;
import jp.eisbahn.oauth2.server.granttype.GrantHandler;
import jp.eisbahn.oauth2.server.models.AccessToken;
import jp.eisbahn.oauth2.server.models.AuthInfo;
import jp.eisbahn.oauth2.server.models.Request;
import jp.eisbahn.oauth2.server.utils.Util;

/**
 * This abstract class provides some common functions for this sub classes.
 * 
 * @author Yoichiro Tanaka
 *
 */
public abstract class AbstractGrantHandler implements GrantHandler {

	private ClientCredentialFetcher clientCredentialFetcher;

	/**
	 * Set the client credential fetcher instance.
	 * @param clientCredentialFetcher The fetcher object to fetch a client
	 * credential.
	 */
	public void setClientCredentialFetcher(ClientCredentialFetcher clientCredentialFetcher) {
		this.clientCredentialFetcher = clientCredentialFetcher;
	}

	/**
	 * Retrieve the client credential fetcher instance.
	 * @return The fetcher object to fetch a client credential.
	 */
	protected ClientCredentialFetcher getClientCredentialFetcher() {
		return clientCredentialFetcher;
	}

	/**
	 * Issue an access token and relating information and return it.
	 * Actually, issuing the access token is delegated to the specified data
	 * handler. If the issued result has a expires_in, refresh token and/or
	 * scope string, each parameter is included to the result of this method.
	 * @param dataHandler The data handler instance to access to your database
	 * and issue an access token.
	 * @param authInfo The authorization information created in advance.
	 * @return The result object which has an access token and etc.
	 */
	protected GrantHandlerResult issueAccessToken(DataHandler dataHandler,
			AuthInfo authInfo) {
		AccessToken accessToken = dataHandler.createOrUpdateAccessToken(authInfo);
		GrantHandlerResult result =
				new GrantHandlerResult("Bearer", accessToken.getToken());
		if (accessToken.getExpiresIn() > 0) {
			result.setExpiresIn(accessToken.getExpiresIn());
		}
		if (StringUtils.isNotEmpty(authInfo.getRefreshToken())) {
			result.setRefreshToken(authInfo.getRefreshToken());
		}
		if (StringUtils.isNotEmpty(authInfo.getScope())) {
			result.setScope(authInfo.getScope());
		}
		return result;
	}

	/**
	 * Retrieve the parameter value against the parameter name.
	 * 
	 * @param request The request object which has each parameters.
	 * @param name The parameter name which you want to retrieve.
	 * @return The parameter value. This never be null.
	 * @throws OAuthError.InvalidRequest If the parameter is not found or is
	 * empty string.
	 */
	protected String getParameter(Request request, String name)
			throws OAuthError.InvalidRequest {
		return Util.getParameter(request, name);
	}

}
