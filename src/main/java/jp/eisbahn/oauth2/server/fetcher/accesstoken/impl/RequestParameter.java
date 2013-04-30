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

package jp.eisbahn.oauth2.server.fetcher.accesstoken.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import jp.eisbahn.oauth2.server.fetcher.accesstoken.AccessTokenFetcher;
import jp.eisbahn.oauth2.server.models.Request;

/**
 * This class fetches an access token from request parameters.
 * Actually, the access token is picked up from an "oauth_token" or "access_token"
 * parameter's value.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class RequestParameter implements AccessTokenFetcher {

	/**
	 * Return whether an access token is included in the request parameter's
	 * values. Actually, this method confirms whether the "oauth_token" or
	 * "access_token" request parameter exists or not.
	 * 
	 * @param request The request object.
	 * @return If either parameter exists, this result is true.
	 */
	@Override
	public boolean match(Request request) {
		String oauthToken = request.getParameter("oauth_token");
		String accessToken = request.getParameter("access_token");
		return StringUtils.isNotEmpty(accessToken)
			|| StringUtils.isNotEmpty(oauthToken);
	}

	/**
	 * Fetch an access token from a request parameter and return it.
	 * This method must be called when a result of the match() method is true
	 * only.
	 * 
	 * @param request The request object.
	 * @return the fetched access token.
	 */
	@Override
	public FetchResult fetch(Request request) {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> parameterMap = request.getParameterMap();
		params.putAll(parameterMap);
		String token = params.get("access_token");
		params.remove("access_token");
		if (StringUtils.isNotEmpty(params.get("oauth_token"))) {
			token = params.get("oauth_token");
			params.remove("oauth_token");
		}
		return new FetchResult(token, params);
	}

}
