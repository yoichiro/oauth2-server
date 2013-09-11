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

import java.util.Arrays;
import java.util.List;

import jp.eisbahn.oauth2.server.data.DataHandler;
import jp.eisbahn.oauth2.server.data.DataHandlerFactory;
import jp.eisbahn.oauth2.server.exceptions.OAuthError;
import jp.eisbahn.oauth2.server.models.Request;
import jp.eisbahn.oauth2.server.responsetype.AuthorizationHandler;
import jp.eisbahn.oauth2.server.responsetype.AuthorizationHandler.AuthorizationRequest;
import jp.eisbahn.oauth2.server.responsetype.AuthorizationHandlerProvider;
import jp.eisbahn.oauth2.server.utils.Util;

import org.apache.commons.lang3.StringUtils;

/**
 * This class provides some convenience methods to deal with an authorization.
 * For instance, a validation for a request of the authorization, procedures
 * at applying/denying by an user allows you to implement easier.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class Authorization {

	private DataHandlerFactory dataHandlerFactory;
	private List<String> supportedResponseTypes;
	private AuthorizationHandlerProvider authorizationHanderProvider;
	
	/**
	 * Prepare some processes for OAuth2.0 authorization.
	 * For instance, validate each parameters.
	 * 
	 * @param request The request object.
	 * @return The response object.
	 * @throws OAuthError If the validation failed.
	 */
	public AuthorizationRequest prepare(Request request) throws OAuthError {
		DataHandler dataHandler = dataHandlerFactory.create(request);
		AuthorizationRequest result = new AuthorizationRequest();
		String[] responseTypes = getResponseTypes(request);
		for (String responseType : responseTypes) {
			AuthorizationHandler authorizationHandler =
					authorizationHanderProvider.getAuthorizationHandler(responseType);
			result = authorizationHandler.prepare(dataHandler, result);
		}
		return result;
	}

	private String[] getResponseTypes(Request request) throws OAuthError {
		String responseTypeStr = Util.getParameter(request, "response_type");
		if (StringUtils.isEmpty(responseTypeStr)) {
			throw new OAuthError.InvalidRequest("'response_type' not found");
		}
		
		String[] responseTypes = StringUtils.split(responseTypeStr, ' ');
		for (String responseType : responseTypes) {
			if (!supportedResponseTypes.contains(responseType)) {
				throw new OAuthError.InvalidRequest("'response_type' not allowed");
			}
		}
		return responseTypes;
	}
	
	/**
	 * Set a factory of DataHandler.
	 * @param dataHandlerFactory The instance of the factory.
	 */
	public void setDataHandlerFactory(DataHandlerFactory dataHandlerFactory) {
		this.dataHandlerFactory = dataHandlerFactory;
	}

	/**
	 * Set supported response type array.
	 * 
	 * @param The array object which has some supported response_type strings.
	 */
	public void setSupportedResponseTypes(String[] supportedResponseTypes) {
		this.supportedResponseTypes = Arrays.asList(supportedResponseTypes);
	}
	
	public void setAuthorizationHandlerProvider(
			AuthorizationHandlerProvider authorizationHandlerProvider) {
		this.authorizationHanderProvider = authorizationHandlerProvider;
	}

}
