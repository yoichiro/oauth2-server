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

package jp.eisbahn.oauth2.server.fetcher.clientcredential;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.eisbahn.oauth2.server.models.ClientCredential;
import jp.eisbahn.oauth2.server.models.Request;
import jp.eisbahn.oauth2.server.utils.Util;

/**
 * This implementation provides a client credential information
 * from a request header or request parameters
 * 
 * @author Yoichiro Tanaka
 *
 */
public class ClientCredentialFetcherImpl implements ClientCredentialFetcher {

	private static final Pattern REGEXP_BASIC =
			Pattern.compile("^\\s*(Basic)\\s+(.*)$");

	/**
	 * Fetch a client credential sent from the client as a request header
	 * or request parameters. First, this method attempts to fetch it from
	 * the Authorization request header. For instance, it is expected as the
	 * format "Basic [Base64 encoded]". The encoded value actually has two
	 * parameter "Client ID" and "Client secret" delimited by ":" character.
	 * If it does not exist, this method attempt to fetch it from request
	 * parameter named "client_id" and "client_secret".
	 * 
	 * @param request The request object.
	 * @return The client credential information.
	 */
	@Override
	public ClientCredential fetch(Request request) {
		String header = request.getHeader("Authorization");
		if (header != null) {
			Matcher matcher = REGEXP_BASIC.matcher(header);
			if (matcher.find()) {
				String decoded = Util.decodeBase64(matcher.group(2));
				if (decoded.indexOf(':') > 0) {
					String[] credential = decoded.split(":", 2);
					return new ClientCredential(credential[0], credential[1]);
				}
			}
		}
		return new ClientCredential(
			request.getParameter("client_id"),
			request.getParameter("client_secret"));
	}

}
