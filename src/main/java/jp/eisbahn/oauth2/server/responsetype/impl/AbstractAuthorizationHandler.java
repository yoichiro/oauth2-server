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

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.exceptions.OAuthError;
import jp.eisbahn.oauth2.server.models.AuthInfo;
import jp.eisbahn.oauth2.server.models.Request;
import jp.eisbahn.oauth2.server.responsetype.AuthorizationHandler;
import jp.eisbahn.oauth2.server.utils.Util;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractAuthorizationHandler implements
		AuthorizationHandler {
	
	protected void getOAuth2Parameters(AuthorizationRequest request,
			DataHandler dataHandler, final String clientId) throws OAuthError {
		String redirectUri = getValidatedParameter(
				dataHandler, "redirect_uri", new ParameterValidator() {
			@Override
			public boolean validate(String value, DataHandler dataHandler) {
				return dataHandler.validateRedirectUri(clientId, value);
			}
		});
		String[] scopes = getScopes(clientId, dataHandler);
		String state = Util.getParameter(dataHandler.getRequest(), "state");
		
		request.setRedirectUri(redirectUri);
		request.setScopes(scopes);
		request.setState(state);
	}
	
	protected void getOpenIDConnectParameters(AuthorizationRequest request,
			DataHandler dataHandler, final String clientId) throws OAuthError {
		String nonce = Util.getParameter(dataHandler.getRequest(), "nonce");
		String display = getValidatedParameter(
				dataHandler, "display", new ParameterValidator() {
			@Override
			public boolean validate(String value, DataHandler dataHandler) {
				return dataHandler.validateDisplay(clientId, value);
			}
		});
		String prompt = getValidatedParameter(
				dataHandler, "prompt", new ParameterValidator() {
			@Override
			public boolean validate(String value, DataHandler dataHandler) {
				return dataHandler.validatePrompt(clientId, value);
			}
		});
		Long maxAge = getMaxAge(dataHandler);
		String[] uiLocales = getValidatedParameters(dataHandler, "ui_locales",
				new ParametersValidator() {
			@Override
			public boolean validate(String[] values, DataHandler dataHandler) {
				return dataHandler.validateUiLocales(clientId, values);
			}
		});
		String[] claimsLocales = getValidatedParameters(dataHandler, "claims_locales",
				new ParametersValidator() {
			@Override
			public boolean validate(String[] values, DataHandler dataHandler) {
				return dataHandler.validateClaimsLocales(clientId, values);
			}
		});
		String idTokenHint = getValidatedParameter(dataHandler, "id_token_hint",
				new ParameterValidator() {
			@Override
			public boolean validate(String value, DataHandler dataHandler) {
				return dataHandler.validateIdTokenHint(clientId, value);
			}
		});
		String loginHint = getValidatedParameter(dataHandler, "login_hint",
				new ParameterValidator() {
			@Override
			public boolean validate(String value, DataHandler dataHandler) {
				return dataHandler.validateLoginHint(clientId, value);
			}
		});
		String[] acrValues = getValidatedParameters(dataHandler, "acr_values",
				new ParametersValidator() {
			@Override
			public boolean validate(String[] values, DataHandler dataHandler) {
				return dataHandler.validateAcrValues(clientId, values);
			}
		});

		request.setNonce(nonce);
		request.setDisplay(display);
		request.setPrompt(prompt);
		request.setMaxAge(maxAge);
		request.setUiLocales(uiLocales);
		request.setClaimsLocales(claimsLocales);
		request.setIdTokenHint(idTokenHint);
		request.setLoginHint(loginHint);
		request.setAcrValues(acrValues);
	}

	protected String getClientId(DataHandler dataHandler) throws OAuthError {
		Request request = dataHandler.getRequest();
		String clientId = Util.getParameter(request, "client_id");
		if (StringUtils.isEmpty(clientId)) {
			throw new OAuthError.InvalidRequest("'client_id' not found");
		}
		if (!dataHandler.validateClientById(clientId)) {
			throw new OAuthError.InvalidClient("");
		}
		if (!dataHandler.validateClientForAuthorization(clientId, getResponseType())) {
			throw new OAuthError.InvalidRequest(
					"'response_type' not allowed for this 'client_id'");
		}
		return clientId;
	}
	
	protected String[] getScopes(String clientId, DataHandler dataHandler)
			throws OAuthError {
		Request request = dataHandler.getRequest();
		String scopeStr = Util.getParameter(request, "scope");
		String[] scopes;
		if (StringUtils.isEmpty(scopeStr)) {
			scopes = new String[0];
		} else {
			scopes = scopeStr.split(" ");
		}
		if (!dataHandler.validateScope(clientId, scopes)) {
			throw new OAuthError.InvalidScope("");
		}
		return scopes;
	}
	
	protected Long getMaxAge(DataHandler dataHandler) throws OAuthError {
		Request request = dataHandler.getRequest();
		String maxAge = Util.getParameter(request, "maxAge");
		if (StringUtils.isEmpty(maxAge)) {
			return null;
		} else {
			try {
				return Long.parseLong(maxAge);
			} catch(NumberFormatException e) {
				throw new OAuthError.InvalidRequest("'max_age' is invalid");
			}
		}
	}
	
	protected String[] getValidatedParameters(DataHandler dataHandler,
			String parameterName, ParametersValidator validator) throws OAuthError {
		Request request = dataHandler.getRequest();
		String valueStr = Util.getParameter(request, parameterName);
		String[] values;
		if (StringUtils.isEmpty(valueStr)) {
			values = new String[0];
		} else {
			values = valueStr.split(" ");
		}
		if (!validator.validate(values, dataHandler)) {
			throw new OAuthError.InvalidRequest("'" + parameterName + "' is invalid");
		}
		return values;
	}
	
	protected String getValidatedParameter(DataHandler dataHandler,
			String parameterName, ParameterValidator validator) throws OAuthError {
		Request request = dataHandler.getRequest();
		String value = Util.getParameter(request, parameterName);
		if (StringUtils.isEmpty(value)) {
			value = null;
		} else {
			if (!validator.validate(value, dataHandler)) {
				throw new OAuthError.InvalidRequest("'" + parameterName + "' is invalid");
			}
		}
		return value;
	}
	
	protected interface ParameterValidator {

		public boolean validate(String value, DataHandler dataHandler);

	}
	
	protected interface ParametersValidator {

		public boolean validate(String[] values, DataHandler dataHandler);

	}
	
	protected AuthInfo createOrUpdateAuthInfo(String userId,
			AuthorizationRequest request, AuthorizationResponse response,
			DataHandler dataHandler) throws OAuthError {
		AuthInfo authInfo = response.getAuthInfo();
		if (authInfo == null) {
			authInfo = dataHandler.createOrUpdateAuthInfo(
					request.getClientId(),
					userId,
					StringUtils.join(request.getScopes(), ' '));
			response.setAuthInfo(authInfo);
		}
		return authInfo;
	}

}
