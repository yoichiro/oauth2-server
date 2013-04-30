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

import jp.eisbahn.oauth2.server.fetcher.accesstoken.AccessTokenFetcher;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.AccessTokenFetcherProvider;

/**
 * This class is the implementation class of the AccessTokenFetcherProvider.
 * This implementation provides two instances to fetch an access token
 * "AuthHeader" and "RequestParameter".
 * 
 * @author Yoichiro Tanaka
 *
 */
public class DefaultAccessTokenFetcherProvider extends AccessTokenFetcherProvider {

	/**
	 * This constructor creates instances of AuthHeader and RequestParameter
	 * implementation classes.
	 */
	public DefaultAccessTokenFetcherProvider() {
		super();
		setAccessTokenFetchers(new AccessTokenFetcher[] {
				new AuthHeader(),
				new RequestParameter()
		});
	}
	
}
