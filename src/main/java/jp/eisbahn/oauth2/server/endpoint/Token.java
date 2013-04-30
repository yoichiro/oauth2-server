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
import jp.eisbahn.oauth2.server.fetcher.clientcredential.ClientCredentialFetcher;
import jp.eisbahn.oauth2.server.granttype.GrantHandler;
import jp.eisbahn.oauth2.server.granttype.GrantHandlerProvider;
import jp.eisbahn.oauth2.server.granttype.GrantHandler.GrantHandlerResult;
import jp.eisbahn.oauth2.server.models.ClientCredential;
import jp.eisbahn.oauth2.server.models.Request;
import jp.eisbahn.oauth2.server.utils.Util;

import org.apache.commons.lang3.StringUtils;

/**
 * This class provides the ability to issue a token with each grant flow.
 * The actual procedure to issue the token is in charge of a GrantHandler
 * specified by a grant_type parameter value. This handleRequest() method
 * fetches a client credential from the request, checks whether it is valid or
 * not, and delegates issuing the token to the GrantHandler instance.
 * As the result, a HTTP status code and a JSON string which has the token
 * information.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class Token {

	private DataHandlerFactory dataHandlerFactory;
	private GrantHandlerProvider grantHandlerProvider;
	private ClientCredentialFetcher clientCredentialFetcher;

	/**
	 * Set the DataHandlerFactory instance.
	 * This class gets a DataHandler instance using this factory object.
	 * The factory instance must be passed using this method before calling
	 * the handlerRequest() method.
	 * @param dataHandlerFactory The DataHandlerFactory instance.
	 */
	public void setDataHandlerFactory(DataHandlerFactory dataHandlerFactory) {
		this.dataHandlerFactory = dataHandlerFactory;
	}

	/**
	 * Set the GrantHandlerProvider instance.
	 * This class gets a GrantHandler instance using this provider object.
	 * The GrantHandlerProvider can determine what handler should be used
	 * to issue the token. The provider instance must be passed using this method
	 * before calling the handlerRequest() method.
	 * @param grantHandlerProvider The GrantHandlerProvider instance.
	 */
	public void setGrantHandlerProvider(GrantHandlerProvider grantHandlerProvider) {
		this.grantHandlerProvider = grantHandlerProvider;
	}

	/**
	 * Set the ClientCredentialFetcher instance.
	 * This class doesn't have an ability to fetch a client credential information
	 * from the request, instead delegates this ClientCredentialFetcher instance.
	 * The fetcher instance must be passed using this method before calling the
	 * handlerRequest() method.
	 * @param clientCredentialFetcher The ClientCredentialFetcher instance.
	 */
	public void setClientCredentialFetcher(ClientCredentialFetcher clientCredentialFetcher) {
		this.clientCredentialFetcher = clientCredentialFetcher;
	}

	/**
	 * Handle the request and issue a token.
	 * This class is an entry point to issue the token. When this method receives
	 * the request, then this checks the request, determines the grant type,
	 * fetches the client credential information, and issues the token based
	 * on the content of the request. The result is composed of the status code
	 * and the JSON string. The status code will be 200 when the issuing token
	 * is succeeded. If an error occurs, the code will be the 300 series value.
	 * The JSON string has the access token, refresh token, expires_in value and
	 * the scope string.
	 * @param request The request instance.
	 * @return The response object which has the status code and JSON string.
	 */
	public Response handleRequest(Request request) {
		try {
			String type = request.getParameter("grant_type");
			if (StringUtils.isEmpty(type)) {
				throw new OAuthError.InvalidRequest("'grant_type' not found");
			}
			GrantHandler handler = grantHandlerProvider.getHandler(type);
			if (handler == null) {
				throw new OAuthError.UnsupportedGrantType("");
			}
			DataHandler dataHandler = dataHandlerFactory.create(request);
			ClientCredential clientCredential =
					clientCredentialFetcher.fetch(request);
			String clientId = clientCredential.getClientId();
			if (StringUtils.isEmpty(clientId)) {
				throw new OAuthError.InvalidRequest("'client_id' not found");
			}
			String clientSecret = clientCredential.getClientSecret();
			if (StringUtils.isEmpty(clientSecret)) {
				throw new OAuthError.InvalidRequest("'client_secret' not found");
			}
			if (!dataHandler.validateClient(clientId, clientSecret, type)) {
				throw new OAuthError.InvalidClient("");
			}
			GrantHandlerResult handlerResult = handler.handleRequest(dataHandler);
			return new Response(200, Util.toJson(handlerResult));
		} catch (OAuthError e) {
			return new Response(e.getCode(), Util.toJson(e));
		}
	}

	/**
	 * This class has two properties: A status code and JSON string as the result
	 * of issuing a token.
	 * 
	 * @author Yoichiro Tanaka
	 *
	 */
	public static class Response {

		private int code;
		private String body;

		/**
		 * Initialize this instance with arguments passed.
		 * @param code The status code as the result of issuing a token.
		 * @param body The JSON string which has a token information.
		 */
		public Response(int code, String body) {
			super();
			this.code = code;
			this.body = body;
		}

		/**
		 * Retrieve the status code value.
		 * This status code will be 200 when the issuing token is succeeded.
		 * If an error occurs, the code will be the 300 series value.
		 * @return The HTTP status code value.
		 */
		public int getCode() {
			return code;
		}

		/**
		 * Retrieve the JSON string which has a token information.
		 * The JSON string has the access token, refresh token, expires_in
		 * value and the scope string. If the issuing a token failed,
		 * this json string has the error type and description.
		 * @return The JSON string value.
		 */
		public String getBody() {
			return body;
		}

	}

}
