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

/**
 * This model class has some parameters to authorize.
 * A life-cycle of this model is different each grant types.
 * If the Authorization Code grant type is applied, this model will be created
 * after the authentication of the user. In other case, if the Client
 * Credentials grant type is applied, this model will be created at the same
 * time of receiving the request to issue the access token.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class AuthInfo {

	private String id;
	private String userId;
	private String clientId;
	private String scope;
	private String refreshToken;
	private String code;
	private String redirectUri;

	/**
	 * Set the ID of this model.
	 * This ID value will be kept by some access token's information.
	 * @param id The ID value.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Retrieve the ID of this model.
	 * @return The ID string value.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the user's ID who is authenticated.
	 * @param userId The user's ID value.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Retrieve the user's ID.
	 * @return The user's ID string.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set the client ID.
	 * @param clientId The client ID.
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * Retrieve the client ID.
	 * @return The client ID string.
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * Set the scope string.
	 * This scope string has some scope identifiers delimited by a whitespace.
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

	/**
	 * Set the refresh token.
	 * If the specified grant type should not return a refresh token,
	 * this method must be set with a null as an argument.
	 * @param refreshToken The refresh token string.
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * Retrieve the refresh token.
	 * @return The refresh token string.
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * Set the authorization code.
	 * If the grant type which doesn't need the authorization code is required,
	 * this method must not be called.
	 * @param code The authorization code value.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Retrieve the authorization code.
	 * @return The authorization code value.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Set the redirect_uri string.
	 * @param redirectUri The redirect_uri string.
	 */
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	/**
	 * Retrieve the redirect_uri string.
	 * @return The redirect_uri string.
	 */
	public String getRedirectUri() {
		return redirectUri;
	}

}
