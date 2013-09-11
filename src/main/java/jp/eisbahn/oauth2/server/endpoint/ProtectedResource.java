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

package jp.eisbahn.oauth2.server.endpoint;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.data.DataHandlerFactory;
import jp.eisbahn.oauth2.server.exceptions.OAuthError;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.AccessTokenFetcher;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.AccessTokenFetcher.FetchResult;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.AccessTokenFetcherProvider;
import jp.eisbahn.oauth2.server.models.AccessToken;
import jp.eisbahn.oauth2.server.models.AuthInfo;
import jp.eisbahn.oauth2.server.models.Request;

/**
 * This class provides the function to judge whether an access to protected
 * resources can be applied or not.
 * 
 * For instance, this instance validates an access token sent to each end points
 * of API. If the access token is valid, this handleResponse() method returns
 * the information (a client ID, an ID of a remote user and a scope value).
 * If the access token is invalid, OAuthError will be thrown. The exception
 * has the reason why the token was judged as invalid.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class ProtectedResource {

	private AccessTokenFetcherProvider accessTokenFetcherProvider;
	private DataHandlerFactory dataHandlerFactory;

	/**
	 * This method handles a request and judges whether the request can be
	 * applied or not.
	 * 
	 * @param request This argument value has the information of the request.
	 * @return If the request is valid, this result has three informations (
	 * Client ID, Scope and User ID).
	 * @throws OAuthError If the request is invalid. This exception has a reason
	 * why this request was judged as invalid.
	 */
	public Response handleRequest(Request request) throws OAuthError {
		AccessTokenFetcher accessTokenFetcher = accessTokenFetcherProvider.getFetcher(request);
		if (accessTokenFetcher == null) {
			throw new OAuthError.InvalidRequest("Access token was not specified.");
		}
		FetchResult fetchResult = accessTokenFetcher.fetch(request);
		String token = fetchResult.getToken();
		DataHandler dataHandler = dataHandlerFactory.create(request);
		AccessToken accessToken = dataHandler.getAccessToken(token);
		if (accessToken == null) {
			throw new OAuthError.InvalidToken("Invalid access token.");
		}
		long now = System.currentTimeMillis();
		if (accessToken.getCreatedOn().getTime() + accessToken.getExpiresIn() * 1000 <= now) {
			throw new OAuthError.ExpiredToken();
		}
		AuthInfo authInfo = dataHandler.getAuthInfoById(accessToken.getAuthId());
		if (authInfo == null) {
			throw new OAuthError.InvalidToken("Invalid access token.");
		}
		if (!dataHandler.validateClientById(authInfo.getClientId())) {
			throw new OAuthError.InvalidToken("Invalid client.");
		}
		if (!dataHandler.validateUserById(authInfo.getUserId())) {
			throw new OAuthError.InvalidToken("Invalid user.");
		}
		return new Response(
			authInfo.getUserId(),
			authInfo.getClientId(),
			authInfo.getScope());
	}

	/**
	 * Set a provider of fetchers to fetch an access token from a request.
	 * @param accessTokenFetcherProvider The instance of the provider.
	 */
	public void setAccessTokenFetcherProvider(AccessTokenFetcherProvider accessTokenFetcherProvider) {
		this.accessTokenFetcherProvider = accessTokenFetcherProvider;
	}

	/**
	 * Set a factory of DataHandler.
	 * @param dataHandlerFactory The instance of the factory.
	 */
	public void setDataHandlerFactory(DataHandlerFactory dataHandlerFactory) {
		this.dataHandlerFactory = dataHandlerFactory;
	}

	/**
	 * This class has the information about an OAuth2.0 response.
	 * 
	 * If a check of the request is passed by {@link ProtectedResource},
	 * this instance will be created. Each endpoint of API supported by
	 * {@link ProtectedResource} can know the user ID, the client ID and
	 * the authorized scopes.
	 * 
	 * @author Yoichiro Tanaka
	 *
	 */
	public static class Response {

		private String remoteUser;
		private String clientId;
		private String scope;

		/**
		 * This constructor initializes this instance.
		 * @param remoteUser The remote user's ID.
		 * @param clientId The client ID.
		 * @param scope The scope string authorized by the remote user.
		 */
		public Response(String remoteUser, String clientId, String scope) {
			this.remoteUser = remoteUser;
			this.clientId = clientId;
			this.scope = scope;
		}

		/**
		 * Retrieve the remote user's ID.
		 * @return The user ID.
		 */
		public String getRemoteUser() {
			return remoteUser;
		}

		/**
		 * Retrieve the client ID.
		 * @return The client ID.
		 */
		public String getClientId() {
			return clientId;
		}

		/**
		 * Retrieve the scope string.
		 * @return The scope string authorized by the remote user.
		 */
		public String getScope() {
			return scope;
		}

	}

}
