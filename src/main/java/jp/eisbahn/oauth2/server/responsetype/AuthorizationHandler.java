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

package jp.eisbahn.oauth2.server.responsetype;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.exceptions.OAuthError;
import jp.eisbahn.oauth2.server.models.AuthInfo;
import jp.eisbahn.oauth2.server.models.IdToken;

/**
 * This interface defines some methods of how to authorize against the request.
 * 
 * @author Yoichiro Tanaka
 *
 */
public interface AuthorizationHandler {

	/**
	 * Return the supported response type.
	 * This handler deals with this returned response type.
	 * @return The response type string.
	 */
	public String getResponseType();
	
	/**
	 * Prepare for processing at the authorization endpoint.
	 * For instance, this implementation judges that what is the response type.
	 * Then, some parameters to support the response type are read.
	 * The object which has the parameters is returned as this result.
	 * @param dataHandler The DataHandler instance. The request object will be
	 * retrieved from this instance.
	 * @return The object which has some parameters read from the request object.
	 * @throws OAuthError If a needed parameter not found in the request object.
	 */
	public AuthorizationRequest prepare(DataHandler dataHandler,
			AuthorizationRequest request) throws OAuthError;
	
	/**
	 * Issue the result information for when the user allowed the authorization
	 * required by the client.
	 * @param userId The ID to specify the authenticated user.
	 * @param dataHandler The DataHandler instance. The request object will be
	 * retrieved from this instance.
	 * @param request The request instance which has each parameter.
	 * @param response The instance to hold each result of processing.
	 * @return The response object passed as this argument.
	 * @throws OAuthError If an error occurred.
	 */
	public AuthorizationResponse allow(
			String userId,
			DataHandler dataHandler,
			AuthorizationRequest request,
			AuthorizationResponse response) throws OAuthError;
	
	/**
	 * This class has an information which the request had.
	 * 
	 * @author Yoichiro Tanaka
	 *
	 */
	public static class AuthorizationRequest {

		private String clientId;
		private String[] scopes;
		private String redirectUri;
		private String state;
		private String nonce;
		private String display;
		private String prompt;
		private Long maxAge;
		private String[] uiLocales;
		private String[] claimsLocales;
		private String idTokenHint;
		private String loginHint;
		private String[] acrValues;
		private String claims;
		private String registration;

		/**
		 * OAuth 2.0 Client Identifier.
		 * @return the clientId
		 */
		public String getClientId() {
			return clientId;
		}
		
		/**
		 * OAuth 2.0 Client Identifier.
		 * @param clientId the clientId to set
		 */
		public void setClientId(String clientId) {
			this.clientId = clientId;
		}
		
		/**
		 * Space delimited, case sensitive list of ASCII OAuth 2.0 scope values.
		 * OpenID Connect requests MUST contain the openid scope value.
		 * OPTIONAL scope values of profile, email, address, phone, and
		 * offline_access are also defined.
		 * @return the scope
		 */
		public String[] getScopes() {
			return scopes;
		}
		
		/**
		 * Space delimited, case sensitive list of ASCII OAuth 2.0 scope values.
		 * OpenID Connect requests MUST contain the openid scope value.
		 * OPTIONAL scope values of profile, email, address, phone, and
		 * offline_access are also defined.
		 * @param scope the scope to set
		 */
		public void setScopes(String[] scopes) {
			this.scopes = scopes;
		}
		
		/**
		 * Redirection URI to which the response will be sent. This MUST be
		 * pre-registered with the OpenID Provider. If the Client uses the
		 * OAuth implicit grant type, the redirection URI MUST NOT use the http
		 * scheme unless the Client is a native application, in which case it
		 * MAY use the http: scheme with localhost as the hostname. If the
		 * Client only uses the OAuth authorization_code grant type, the
		 * redirection URI MAY use the http scheme, provided that the Client
		 * Type is confidential, as defined in Section 2.1 of OAuth 2.0.
		 * @return the redirectUri
		 */
		public String getRedirectUri() {
			return redirectUri;
		}
		
		/**
		 * Redirection URI to which the response will be sent. This MUST be
		 * pre-registered with the OpenID Provider. If the Client uses the
		 * OAuth implicit grant type, the redirection URI MUST NOT use the http
		 * scheme unless the Client is a native application, in which case it
		 * MAY use the http: scheme with localhost as the hostname. If the
		 * Client only uses the OAuth authorization_code grant type, the
		 * redirection URI MAY use the http scheme, provided that the Client
		 * Type is confidential, as defined in Section 2.1 of OAuth 2.0.
		 * @param redirectUri the redirectUri to set
		 */
		public void setRedirectUri(String redirectUri) {
			this.redirectUri = redirectUri;
		}
		
		/**
		 * Opaque value used to maintain state between the request and the
		 * callback; it can serve as a protection against XSRF attacks, among
		 * other uses.
		 * @return the state
		 */
		public String getState() {
			return state;
		}
		
		/**
		 * Opaque value used to maintain state between the request and the
		 * callback; it can serve as a protection against XSRF attacks, among
		 * other uses.
		 * @param state the state to set
		 */
		public void setState(String state) {
			this.state = state;
		}
		
		/**
		 * Random, unique string value used to mitigate replay attacks. Use of
		 * the nonce is REQUIRED for all requests where an ID Token is returned
		 * directly from the Authorization Endpoint.
		 * @return the nonce
		 */
		public String getNonce() {
			return nonce;
		}
		
		/**
		 * Random, unique string value used to mitigate replay attacks. Use of
		 * the nonce is REQUIRED for all requests where an ID Token is returned
		 * directly from the Authorization Endpoint.
		 * @param nonce the nonce to set
		 */
		public void setNonce(String nonce) {
			this.nonce = nonce;
		}
		
		/**
		 * ASCII string value that specifies how the Authorization Server
		 * displays the authentication and consent user interface pages to the
		 * End-User.
		 * @return the display
		 */
		public String getDisplay() {
			return display;
		}
		
		/**
		 * ASCII string value that specifies how the Authorization Server
		 * displays the authentication and consent user interface pages to the
		 * End-User.
		 * @param display the display to set
		 */
		public void setDisplay(String display) {
			this.display = display;
		}
		
		/**
		 * Space delimited, case sensitive list of ASCII string values that
		 * specifies whether the Authorization Server prompts the End-User for
		 * reauthentication and consent.
		 * @return the prompt
		 */
		public String getPrompt() {
			return prompt;
		}
		
		/**
		 * Space delimited, case sensitive list of ASCII string values that
		 * specifies whether the Authorization Server prompts the End-User for
		 * reauthentication and consent.
		 * @param prompt the prompt to set
		 */
		public void setPrompt(String prompt) {
			this.prompt = prompt;
		}
		
		/**
		 * Maximum Authentication Age. Specifies that the End-User must be
		 * actively authenticated if the End-User was authenticated longer ago
		 * than the specified number of seconds.
		 * @return the maxAge
		 */
		public Long getMaxAge() {
			return maxAge;
		}
		
		/**
		 * Maximum Authentication Age. Specifies that the End-User must be
		 * actively authenticated if the End-User was authenticated longer ago
		 * than the specified number of seconds.
		 * @param maxAge the maxAge to set
		 */
		public void setMaxAge(Long maxAge) {
			this.maxAge = maxAge;
		}
		
		/**
		 * End-User's preferred languages and scripts for the user interface,
		 * represented as a space-separated list of BCP47 [RFC5646] language
		 * tag values, ordered by preference. For instance, the value
		 * "fr-CA fr en" represents a preference for French as spoken in Canada,
		 * then French (without a region designation), followed by English
		 * (without a region designation). An error SHOULD NOT result if some
		 * or all of the requested locales are not supported by the OpenID
		 * Provider.
		 * @return the uiLocales
		 */
		public String[] getUiLocales() {
			return uiLocales;
		}
		
		/**
		 * End-User's preferred languages and scripts for the user interface,
		 * represented as a space-separated list of BCP47 [RFC5646] language
		 * tag values, ordered by preference. For instance, the value
		 * "fr-CA fr en" represents a preference for French as spoken in Canada,
		 * then French (without a region designation), followed by English
		 * (without a region designation). An error SHOULD NOT result if some
		 * or all of the requested locales are not supported by the OpenID
		 * Provider.
		 * @param uiLocales the uiLocales to set
		 */
		public void setUiLocales(String[] uiLocales) {
			this.uiLocales = uiLocales;
		}
		
		/**
		 * End-User's preferred languages and scripts for Claims being returned,
		 * represented as a space-separated list of BCP47 [RFC5646] language
		 * tag values, ordered by preference. An error SHOULD NOT result if
		 * some or all of the requested locales are not supported by the
		 * OpenID Provider.
		 * @return the claimsLocales
		 */
		public String[] getClaimsLocales() {
			return claimsLocales;
		}
		
		/**
		 * End-User's preferred languages and scripts for Claims being returned,
		 * represented as a space-separated list of BCP47 [RFC5646] language
		 * tag values, ordered by preference. An error SHOULD NOT result if
		 * some or all of the requested locales are not supported by the
		 * OpenID Provider.
		 * @param claimsLocales the claimsLocales to set
		 */
		public void setClaimsLocales(String[] claimsLocales) {
			this.claimsLocales = claimsLocales;
		}
		
		/**
		 * Previously issued ID Token passed to the Authorization Server as a
		 * hint about the End-User's current or past authenticated session with
		 * the Client. This SHOULD be present when prompt=none is used. If the
		 * End-User identified by the ID Token is logged in or is logged in by
		 * the request, then the Authorization Server returns a positive
		 * response; otherwise, it SHOULD return a negative response.
		 * @return the idTokenHint
		 */
		public String getIdTokenHint() {
			return idTokenHint;
		}
		
		/**
		 * Previously issued ID Token passed to the Authorization Server as a
		 * hint about the End-User's current or past authenticated session with
		 * the Client. This SHOULD be present when prompt=none is used. If the
		 * End-User identified by the ID Token is logged in or is logged in by
		 * the request, then the Authorization Server returns a positive
		 * response; otherwise, it SHOULD return a negative response.
		 * @param idTokenHint the idTokenHint to set
		 */
		public void setIdTokenHint(String idTokenHint) {
			this.idTokenHint = idTokenHint;
		}
		
		/**
		 * Hint to the Authorization Server about the login identifier the
		 * End-User may use to log in (if necessary). This hint can be used by
		 * an RP if it first asks the End-User for their e-mail address (or
		 * other identifier) and then wants to pass that value as a hint to the
		 * discovered authorization service. It is RECOMMENDED that the hint
		 * value match the value used for discovery. This value MAY also be a
		 * phone number in the format specified for the phone_number Claim. The
		 * use of this parameter is left to the OP's discretion.
		 * @return the loginHint
		 */
		public String getLoginHint() {
			return loginHint;
		}
		
		/**
		 * Hint to the Authorization Server about the login identifier the
		 * End-User may use to log in (if necessary). This hint can be used by
		 * an RP if it first asks the End-User for their e-mail address (or
		 * other identifier) and then wants to pass that value as a hint to the
		 * discovered authorization service. It is RECOMMENDED that the hint
		 * value match the value used for discovery. This value MAY also be a
		 * phone number in the format specified for the phone_number Claim. The
		 * use of this parameter is left to the OP's discretion.
		 * @param loginHint the loginHint to set
		 */
		public void setLoginHint(String loginHint) {
			this.loginHint = loginHint;
		}
		
		/**
		 * Requested Authentication Context Class Reference values.
		 * Space-separated string that specifies the acr values that the
		 * Authorization Server MUST use for processing requests from this
		 * Client.
		 * @return the acrValues
		 */
		public String[] getAcrValues() {
			return acrValues;
		}
		
		/**
		 * Requested Authentication Context Class Reference values.
		 * Space-separated string that specifies the acr values that the
		 * Authorization Server MUST use for processing requests from this
		 * Client.
		 * @param acrValues the acrValues to set
		 */
		public void setAcrValues(String[] acrValues) {
			this.acrValues = acrValues;
		}
		
		/**
		 * This parameter is used to request that specific Claims be returned.
		 * @return the claims
		 */
		public String getClaims() {
			return claims;
		}
		
		/**
		 * This parameter is used to request that specific Claims be returned.
		 * @param claims the claims to set
		 */
		public void setClaims(String claims) {
			this.claims = claims;
		}
		
		/**
		 * This parameter is used by the Client to provide information about
		 * itself to a Self-Issued OP that would normally be provided to an OP
		 * during Dynamic Client Registration.
		 * @return the registration
		 */
		public String getRegistration() {
			return registration;
		}
		
		/**
		 * This parameter is used by the Client to provide information about
		 * itself to a Self-Issued OP that would normally be provided to an OP
		 * during Dynamic Client Registration.
		 * @param registration the registration to set
		 */
		public void setRegistration(String registration) {
			this.registration = registration;
		}
		
	}
	
	/**
	 * This class has some information as the result of processing at the
	 * authorization endpoint.
	 * 
	 * @author Yoichiro Tanaka
	 * 
	 */
	public static class AuthorizationResponse {
		
		private String state;
		private String accessToken;
		private String tokenType;
		private Long expiresIn;
		private String code;
		private IdToken idToken;
		private String scope;
		
		private AuthInfo authInfo;
		
		/**
		 * An opaque value used by the client to maintain state between the
		 * request and callback.  The authorization server includes this value
		 * when redirecting the user-agent back to the client.
		 * @return the state
		 */
		public String getState() {
			return state;
		}
		
		/**
		 * An opaque value used by the client to maintain state between the
		 * request and callback.  The authorization server includes this value
		 * when redirecting the user-agent back to the client.
		 * @param state the state to set
		 */
		public void setState(String state) {
			this.state = state;
		}
		
		/**
		 *  OAuth 2.0 Bearer Token.
		 * @return the accessToken
		 */
		public String getAccessToken() {
			return accessToken;
		}
		
		/**
		 *  OAuth 2.0 Bearer Token.
		 * @param accessToken the accessToken to set
		 */
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		
		/**
		 * If a type of the access_token is a Bearer token, this value must be
		 * 'Bearer'.
		 * @return the tokenType
		 */
		public String getTokenType() {
			return tokenType;
		}
		
		/**
		 * If a type of the access_token is a Bearer token, this value must be
		 * 'Bearer'.
		 * @param tokenType the tokenType to set
		 */
		public void setTokenType(String tokenType) {
			this.tokenType = tokenType;
		}
		
		/**
		 * The lifetime in seconds of the access token. For example, the value
		 * "3600" denotes that the access token will expire in one hour from
		 * the time the response was generated.
		 * @return the expiresIn
		 */
		public Long getExpiresIn() {
			return expiresIn;
		}
		
		/**
		 * The lifetime in seconds of the access token. For example, the value
		 * "3600" denotes that the access token will expire in one hour from
		 * the time the response was generated.
		 * @param expiresIn the expiresIn to set
		 */
		public void setExpiresIn(Long expiresIn) {
			this.expiresIn = expiresIn;
		}
		
		/**
		 * The authorization code generated by the authorization server.
		 * The authorization code MUST expire shortly after it is issued to
		 * mitigate the risk of leaks. A maximum authorization code lifetime
		 * of 10 minutes is RECOMMENDED. 
		 * @return the code
		 */
		public String getCode() {
			return code;
		}
		
		/**
		 * The authorization code generated by the authorization server.
		 * The authorization code MUST expire shortly after it is issued to
		 * mitigate the risk of leaks. A maximum authorization code lifetime
		 * of 10 minutes is RECOMMENDED.
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}
		
		/**
		 * ID Token value associated with the authenticated session.
		 * @return the idToken
		 */
		public IdToken getIdToken() {
			return idToken;
		}
		
		/**
		 * ID Token value associated with the authenticated session.
		 * @param idToken the idToken to set
		 */
		public void setIdToken(IdToken idToken) {
			this.idToken = idToken;
		}
		
		public String getScope() {
			return scope;
		}
		
		public void setScope(String scope) {
			this.scope = scope;
		}
		
		public AuthInfo getAuthInfo() {
			return authInfo;
		}
		
		public void setAuthInfo(AuthInfo authInfo) {
			this.authInfo = authInfo;
		}
		
	}

}
