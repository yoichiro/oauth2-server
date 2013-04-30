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

import jp.eisbahn.oauth2.server.models.AccessToken;
import jp.eisbahn.oauth2.server.models.AuthInfo;
import jp.eisbahn.oauth2.server.models.Request;

/**
 * This abstract class defines some functions to provide and store each
 * information regarding OAuth2.0 authorization.
 * 
 * <p>This sub-class is used to process each OAuth2.0 flows. Some procedures to
 * process followed by OAuth2.0 depend on each provider which want to support
 * OAuth2.0. The provider must implement this DataHandler and provide them.</p>
 * 
 * <p>Methods to be used are different for each grant type.</p>
 * 
 * <p>[Authorization phases]</p>
 * 
 * <p>
 * Authorization Code Grant:<br />
 *   <ul>
 *   <li>validateClient(clientId, clientSecret, grantType)</li>
 *   <li>getAuthInfoByCode(code)</li>
 *   <li>createOrUpdateAccessToken(authInfo)</li>
 *   </ul>
 * </p>
 * 
 * <p>
 * Refresh Token Grant:<br />
 *   <ul>
 *   <li>validateClient(clientId, clientSecret, grantType)</li>
 *   <li>getAuthInfoByRefreshToken(refreshToken)</li>
 *   <li>createOrUpdateAccessToken(authInfo)</li>
 *   </ul>
 * </p>
 * 
 * <p>
 * Resource Owner Password Credentials Grant:<br />
 *   <ul>
 *   <li>validateClient(clientId, clientSecret, grantType)</li>
 *   <li>getUserId(username, password)</li>
 *   <li>createOrUpdateAuthInfo(clientId, userId, scope)</li>
 *   <li>createOrUpdateAccessToken(authInfo)</li>
 *   </ul>
 * </p>
 * 
 * <p>
 * Client Credentials Grant:<br />
 *   <ul>
 *   <li>validateClient(clientId, clientSecret, grantType)</li>
 *   <li>getClientUserId(clientId, clientSecret)</li>
 *   <li>createOrUpdateAuthInfo(clientId, userId, scope)</li>
 *   <li>createOrUpdateAccessToken(authInfo)</li>
 *   </ul>
 * </p>
 *   
 * <p>
 * [Access to Protected Resource phase]<br />
 *   <ul>
 *   <li>getAccessToken(token)</li>
 *   <li>getAuthInfoById(authId)</li>
 *   <li>validateClientById(clientId)</li>
 *   <li>validateUserById(userId)</li>
 *   </ul>
 * </p>
 * 
 * @author Yoichiro Tanaka
 *
 */
public abstract class DataHandler {

	private Request request;

	/**
	 * Initialize this instance with the request information.
	 * This constructor calls the init() method to initialize a connection
	 * to your database, a preparation of your cache and so on.
	 * @param request The request instance.
	 */
	public DataHandler(Request request) {
		this.request = request;
		init();
	}

	/**
	 * This method is called at creating this instance.
	 * You should implement an initialization procedure in this method.
	 */
	protected void init() {
	}

	/**
	 * Retrieve the request instance passed at creating this instance.
	 * @return The request instance.
	 */
	public Request getRequest() {
		return request;
	}

	/**
	 * Validate the client and return the result.
	 * This method is called at first for all grant types.
	 * You should check whether the client specified by clientId value exists
	 * or not, whether the client secret is valid or not, and whether
	 * the client supports the grant type or not. If there are other things
	 * to have to check, they must be implemented in this method.
	 * @param clientId The client ID.
	 * @param clientSecret The client secret string.
	 * @param grantType The grant type string which the client required.
	 * @return True if the client is valid.
	 */
	public abstract boolean validateClient(
			String clientId, String clientSecret, String grantType);

	/**
	 * Retrieve the user's ID from the user's credential.
	 * This method is used for the Resource Owner Password Credential Grant only.
	 * Normally, you should implement this process like retrieving the user's ID
	 * from your database and checking the password.
	 * If the null value or the empty string is returned from this method as the
	 * result, the error type "invalid_grant" will be sent to the client.
	 * @param username The user name inputed by the user his/herself.
	 * @param password The password string inputed by the user.
	 * @return The user's ID string. If the user is not found, you must return
	 * a null value or an empty string.
	 */
	public abstract String getUserId(String username, String password);

	/**
	 * Create or update an Authorization information.
	 * This method is used when the authorization information should be created
	 * or updated directly against receiving of the request in case of Client
	 * Credential grant or Resource Owner Password Credential grant.
	 * If the null value is returned from this method as the result, the error
	 * type "invalid_grant" will be sent to the client.
	 * @param clientId The client ID.
	 * @param userId The user's ID.
	 * @param scope The scope string.
	 * @return The created or updated the information about authorization.
	 */
	public abstract AuthInfo createOrUpdateAuthInfo(
			String clientId, String userId, String scope);

	/**
	 * Create or update an Access token.
	 * This method is used for all grant types. The access token is created or
	 * updated based on the authInfo's property values. In generally, this
	 * method never failed, because all validations should be passed before
	 * this method is called.
	 * @param authInfo The instance which has the information about authorization.
	 * @return The created or updated access token instance.
	 */
	public abstract AccessToken createOrUpdateAccessToken(AuthInfo authInfo);

	/**
	 * Retrieve the authorization information by the authorization code value.
	 * This method is used for an Authorization Code grant. The authorization
	 * information which should be returned as this result is needed to create
	 * at the authentication and authorization timing by the user. If the null
	 * value is returned as this result, the error type "invalid_grant" will be
	 * sent to the client.
	 * @param code The authorization code value.
	 * @return The authorization information instance.
	 */
	public abstract AuthInfo getAuthInfoByCode(String code);

	/**
	 * Retrieve the authorization information by the refresh token string.
	 * This method is used to re-issue an access token with the refresh token.
	 * The authorization information which has already been stored into your
	 * database should be specified by the refresh token. If you want to define
	 * the expiration of the refresh token, you must check it in this
	 * implementation. If the refresh token is not found, the refresh token is
	 * invalid or there is other reason which the authorization information
	 * should not be returned, this method must return the null value as the
	 * result.
	 * @param refreshToken The refresh token string.
	 * @return The authorization information instance.
	 */
	public abstract AuthInfo getAuthInfoByRefreshToken(String refreshToken);

	/**
	 * Determine an user ID representing the client itself and return it.
	 * This method is used for the Client Credentials grant. In this flow,
	 * there is no user to authorize, and OAuth2 provider trusts the client.
	 * Therefore, the user ID representing the client itself should be issued,
	 * and the ID can be distinguished whether it represents an user or a client.
	 * @param clientId The client ID.
	 * @param clientSecret The client secret string.
	 * @return The ID representing the client.
	 */
	public abstract String getClientUserId(
			String clientId, String clientSecret);

	/**
	 * Validate the client specified by the client ID.
	 * This method is used to check the client at accessing a protected resource.
	 * When the access token passed from the client is valid, the client status
	 * may be invalid in the OAuth provider side. In this case, this method must
	 * return false to refuse the access to all API endpoints.
	 * @param clientId The client ID.
	 * @return If the client status is invalid, return false, otherwise, return
	 * true.
	 */
	public abstract boolean validateClientById(String clientId);

	/**
	 * Validate the user specified by the user ID.
	 * This method is used to check the user at accessing a protected resource.
	 * When the access token passed from the client is valid, the user status
	 * may be invalid or may be left in the OAuth provider side. In these case,
	 * this method must return false to refuse the access to all API endpoints.
	 * @param userId The user's ID.
	 * @return If the user's status is invalid, return false, otherwise, return
	 * true.
	 */
	public abstract boolean validateUserById(String userId);

	/**
	 * Retrieve the access token from the token string.
	 * This method is used at accessing a protected resource. This sub class
	 * should fetch the access token information from your database or etc and
	 * return it. If the access token has been revoked by the user or there is
	 * other reason, this method must return the null value to refuse the access
	 * to all API endpoints.
	 * @param token The access token string.
	 * @return The object which has the information for the access token.
	 */
	public abstract AccessToken getAccessToken(String token);

	/**
	 * Retrieve the authorization information by the ID.
	 * This method is used at accessing a protected resource. The getAccessTkoken()
	 * method is called before this method calling. The result has a ID of the
	 * authorization information. The ID is passed to this method as an
	 * argument. This sub class must return the authorization information instance
	 * to the client. If the ID has already been invalid or there is other reason,
	 * this implementation must return the null value.
	 * @param id The ID to specify the authorization information.
	 * @return The object which has the information about the authorization.
	 */
	public abstract AuthInfo getAuthInfoById(String id);

}
