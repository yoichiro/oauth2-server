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

import jp.eisbahn.oauth2.server.models.Request;

/**
 * This class provides AccessTokenFetcher implementation classes supported.
 * For instance, the request is passed, then this class checks whether
 * a fetcher class which can fetch an access token from the request exists or
 * not with a match() method of each implementation class.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class AccessTokenFetcherProvider {

	private AccessTokenFetcher[] fetchers;

	/**
	 * Find a fetcher instance which can fetch an access token from the request
	 * passed as an argument value and return it. The match() method of each
	 * implementation class is used to confirm whether the access token can be
	 * fetched or not.
	 * 
	 * @param request The request object.
	 * @return The fetcher instance which can fetch an access token from
	 * the request. If not found, return null.
	 */
	public AccessTokenFetcher getFetcher(Request request) {
		for (AccessTokenFetcher fetcher : fetchers) {
			if (fetcher.match(request)) {
				return fetcher;
			}
		}
		return null;
	}

	/**
	 * Set AccessTokenFetcher implementation instances you support.
	 * @param fetchers The implementation instance of the AccessTokenFetcher
	 * class.
	 */
	public void setAccessTokenFetchers(AccessTokenFetcher[] fetchers) {
		this.fetchers = fetchers;
	}
	
	/**
	 * Retrieve the supported AccessTokenFetcher instances.
	 * @return The fetcher instances.
	 */
	public AccessTokenFetcher[] getAccessTokenFetchers() {
		return fetchers;
	}

}
