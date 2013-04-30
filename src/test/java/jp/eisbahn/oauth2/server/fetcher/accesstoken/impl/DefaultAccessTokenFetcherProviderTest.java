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

import static org.junit.Assert.*;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.AccessTokenFetcher;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.impl.AuthHeader;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.impl.DefaultAccessTokenFetcherProvider;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.impl.RequestParameter;

import org.junit.Test;

public class DefaultAccessTokenFetcherProviderTest {

	@Test
	public void testSimple() throws Exception {
		DefaultAccessTokenFetcherProvider target = new DefaultAccessTokenFetcherProvider();
		AccessTokenFetcher[] accessTokenFetchers = target.getAccessTokenFetchers();
		assertEquals(2, accessTokenFetchers.length);
		assertTrue(accessTokenFetchers[0] instanceof AuthHeader);
		assertTrue(accessTokenFetchers[1] instanceof RequestParameter);
	}

}
