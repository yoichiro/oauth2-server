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
import jp.eisbahn.oauth2.server.models.AuthInfo;
import jp.eisbahn.oauth2.server.models.ClientCredential;
import jp.eisbahn.oauth2.server.models.Request;

/**
 * This class is an implementation for processing the Authorization Code Grant
 * flow of OAuth2.0.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class AuthorizationCode extends AbstractGrantHandler {

	/*
	 * (non-Javadoc)
	 * @see jp.eisbahn.oauth2.server.granttype.GrantHandler#handleRequest(jp.eisbahn.oauth2.server.data.DataHandler)
	 */
	@Override
	public GrantHandlerResult handleRequest(DataHandler dataHandler) throws OAuthError {
		Request request = dataHandler.getRequest();

		ClientCredential clientCredential = getClientCredentialFetcher().fetch(request);
		String clientId = clientCredential.getClientId();

		String code = getParameter(request, "code");
		String redirectUri = getParameter(request, "redirect_uri");

		AuthInfo authInfo = dataHandler.getAuthInfoByCode(code);
		if (authInfo == null) {
			throw new OAuthError.InvalidGrant("");
		}
		if (!authInfo.getClientId().equals(clientId)) {
			throw new OAuthError.InvalidClient("");
		}
		if (!(StringUtils.isNotEmpty(authInfo.getRedirectUri())
		&& authInfo.getRedirectUri().equals(redirectUri))) {
			throw new OAuthError.RedirectUriMismatch("");
		}

		return issueAccessToken(dataHandler, authInfo);
	}

}
