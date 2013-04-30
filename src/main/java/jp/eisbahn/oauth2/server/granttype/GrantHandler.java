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

package jp.eisbahn.oauth2.server.granttype;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.exceptions.OAuthError;

/**
 * This interface defines how to issue a token for each grant type.
 * The issued token is passed to the client as the GrantHandlerResult instance.
 * 
 * @author Yoichiro Tanaka
 *
 */
public interface GrantHandler {

	/**
	 * Handle a request to issue a token and issue it.
	 * This method should be implemented for each grant type of OAuth2
	 * specification. For instance, the procedure uses a DataHandler instance
	 * to access to your database. Each grant type has some validation rule,
	 * if the validation is failed, this method will throw an OAuthError
	 * exception.
	 * 
	 * @param dataHandler The DataHandler instance to access to your database.
	 * @return The issued token information as the result of calling this method.
	 * @throws OAuthError If the validation was failed.
	 */
	public GrantHandlerResult handleRequest(DataHandler dataHandler) throws OAuthError;

	/**
	 * This class has the information about issued token.
	 * 
	 * @author Yoichiro Tanaka
	 *
	 */
	@JsonSerialize(include = Inclusion.NON_NULL)
	@JsonPropertyOrder({"token_type",
		"access_token",
		"refresh_token",
		"expires_in",
		"scope"})
	public static class GrantHandlerResult {

		@JsonProperty("token_type")
		private String tokenType;
		@JsonProperty("access_token")
		private String accessToken;
		@JsonProperty("expires_in")
		private Long expiresIn;
		@JsonProperty("refresh_token")
		private String refreshToken;
		private String scope;

		/**
		 * Initialize this instance with these arguments.
		 * These parameters is required.
		 * 
		 * @param tokenType The token type string.
		 * @param accessToken The access token string.
		 */
		public GrantHandlerResult(String tokenType, String accessToken) {
			super();
			this.tokenType = tokenType;
			this.accessToken = accessToken;
		}

		/**
		 * Retrieve the token type.
		 * @return The issued token's type.
		 */
		public String getTokenType() {
			return tokenType;
		}

		/**
		 * Retrieve the access token.
		 * @return The issued access token string.
		 */
		public String getAccessToken() {
			return accessToken;
		}

		/**
		 * Set the expires_in parameter's value.
		 * An unit of this value is second.
		 * @param expiresIn The expires_in value.
		 */
		public void setExpiresIn(Long expiresIn) {
			this.expiresIn = expiresIn;
		}

		/**
		 * Retrieve the expires_in value.
		 * @return The expires_in value (this unit is second).
		 */
		public Long getExpiresIn() {
			return expiresIn;
		}

		/**
		 * Set the refresh token.
		 * If a grant type which issues the refresh token is specified,
		 * the issued refresh token is passed to the client via this method.
		 * @param refreshToken The refresh token string.
		 */
		public void setRefreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
		}

		/**
		 * Retrieve the refresh token.
		 * @return The issued refresh token string.
		 */
		public String getRefreshToken() {
			return refreshToken;
		}

		/**
		 * Set the scope string.
		 * This scope string specified by the client is passed to
		 * this method untouched.
		 * @param scope The scope string.
		 */
		public void setScope(String scope) {
			this.scope = scope;
		}

		/**
		 * Retrieve the scope string.
		 * @return The scope string.
		 */
		public String getScope() {
			return scope;
		}

	}

}
