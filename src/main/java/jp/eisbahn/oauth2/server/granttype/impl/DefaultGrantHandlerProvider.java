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

import java.util.HashMap;
import java.util.Map;

import jp.eisbahn.oauth2.server.fetcher.clientcredential.ClientCredentialFetcher;
import jp.eisbahn.oauth2.server.fetcher.clientcredential.ClientCredentialFetcherImpl;
import jp.eisbahn.oauth2.server.granttype.GrantHandler;
import jp.eisbahn.oauth2.server.granttype.GrantHandlerProvider;

/**
 * This class is a default implementation for the GrantHandlerProvider interface.
 * All grant types are supported in this class. If you don't support all types,
 * you should not use this implementation, then you should provide your
 * implementation to support only types you want to use.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class DefaultGrantHandlerProvider extends GrantHandlerProvider {

	/**
	 * Initialize this instance. All grant handlers are set.
	 */
	public DefaultGrantHandlerProvider() {
		super();
		ClientCredentialFetcher fetcher = new ClientCredentialFetcherImpl();
		Map<String, GrantHandler> handlers = new HashMap<String, GrantHandler>();
		AuthorizationCode authorizationCode = new AuthorizationCode();
		authorizationCode.setClientCredentialFetcher(fetcher);
		handlers.put("authorization_code", authorizationCode);
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setClientCredentialFetcher(fetcher);
		handlers.put("refresh_token", refreshToken);
		ClientCredentials clientCredentials = new ClientCredentials();
		clientCredentials.setClientCredentialFetcher(fetcher);
		handlers.put("client_credentials", clientCredentials);
		Password password = new Password();
		password.setClientCredentialFetcher(fetcher);
		handlers.put("password", password);
		setGrantHandlers(handlers);
	}

}
