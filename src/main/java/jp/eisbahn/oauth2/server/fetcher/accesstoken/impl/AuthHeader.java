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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.eisbahn.oauth2.server.fetcher.accesstoken.AccessTokenFetcher;
import jp.eisbahn.oauth2.server.models.Request;
import jp.eisbahn.oauth2.server.utils.Util;

/**
 * This class fetches an access token from Authorization Request header.
 * Actually, the access token is clipped from the header string with a regular
 * expression.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class AuthHeader implements AccessTokenFetcher {

	private static final String HEADER_AUTHORIZATION = "Authorization";
	private static final Pattern REGEXP_AUTHORIZATION =
			Pattern.compile("^\\s*(OAuth|Bearer)\\s+([^\\s\\,]*)");
	private static final Pattern REGEXP_TRIM = Pattern.compile("^\\s*,\\s*");
	private static final Pattern REGEXP_DIV_COMMA = Pattern.compile(",\\s*");

	/**
	 * Return whether an access token is included in the Authorization
	 * request header.
	 * 
	 * @param request The request object.
	 * @return If the header value has an access token, this result is true.
	 */
	@Override
	public boolean match(Request request) {
		String header = request.getHeader(HEADER_AUTHORIZATION);
		return (header != null)
			&& (Pattern.matches("^\\s*(OAuth|Bearer)(.*)$", header));
	}

	/**
	 * Fetch an access token from an Authorization request header and return it.
	 * This method must be called when a result of the match() method is true
	 * only.
	 * 
	 * @param request The request object.
	 * @return the fetched access token.
	 */
	@Override
	public FetchResult fetch(Request request) {
		String header = request.getHeader(HEADER_AUTHORIZATION);
		Matcher matcher = REGEXP_AUTHORIZATION.matcher(header);
		if (!matcher.find()) {
			throw new IllegalStateException(
				"parse() method was called when match() result was false.");
		}
		String token = matcher.group(2);
		Map<String, String> params = new HashMap<String, String>();
		int end = matcher.end();
		if (header.length() != end) {
			header = header.substring(end);
			header = REGEXP_TRIM.matcher(header).replaceFirst("");
			String[] expList = REGEXP_DIV_COMMA.split(header);
			for (String exp : expList) {
				String[] keyValue = exp.split("=", 2);
				String value = keyValue[1].replaceFirst("^\"", "");
				value = value.replaceFirst("\"$", "");
				params.put(keyValue[0], Util.decodeParam(value));
			}
		}
		return new FetchResult(token, params);
	}

}
