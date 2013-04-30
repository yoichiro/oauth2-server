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

package jp.eisbahn.oauth2.server.fetcher.accesstoken;

import java.util.Map;

import jp.eisbahn.oauth2.server.models.Request;

/**
 * This interface defines how to fetch an access token from a request object.
 * This match() method has an ability to judge whether the request has an
 * access token or not. The actual fetching process is executed in the fetch()
 * method. This fetch() method must be called when a result of the match()
 * method is true only.
 * 
 * @author Yoichiro Tanaka
 *
 */
public interface AccessTokenFetcher {

	/**
	 * Judge whether a request has an access token or not.
	 * @param request The request object.
	 * @return If the request has an access token, return true.
	 */
	public boolean match(Request request);

	/**
	 * Retrieve an access token from a request object.
	 * This method must be called when a result of the match() method
	 * is true only.
	 * @param request The request object.
	 * @return The fetched access token.
	 */
	public FetchResult fetch(Request request);

	/**
	 * This is a holder class to has an access token with the AccessTokenFetcher
	 * instance.
	 * 
	 * @author Yoichiro Tanaka
	 *
	 */
	public static class FetchResult {

		private String token;
		private Map<String, String> params;

		/**
		 * This constructor initializes like both parameters become a null value.
		 */
		public FetchResult() {
			this(null, null);
		}

		/**
		 * This constructor initializes with the specified argument values.
		 * @param token The access token string.
		 * @param params Other parameters.
		 */
		public FetchResult(String token, Map<String, String> params) {
			super();
			this.token = token;
			this.params = params;
		}

		/**
		 * Set the access token.
		 * @param token The token string.
		 */
		public void setToken(String token) {
			this.token = token;
		}

		/**
		 * Retrieve the access token.
		 * @return The token string.
		 */
		public String getToken() {
			return token;
		}

		/**
		 * Set other parameters.
		 * @param params The other parameter map object.
		 */
		public void setParams(Map<String, String> params) {
			this.params = params;
		}

		/**
		 * Retrieve the other parameters.
		 * @return The other parameter map object.
		 */
		public Map<String, String> getParams() {
			return params;
		}

	}

}
