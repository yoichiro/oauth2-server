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

package jp.eisbahn.oauth2.server.granttype.impl;

import static org.junit.Assert.*;

import java.util.Map;

import jp.eisbahn.oauth2.server.granttype.GrantHandler;
import jp.eisbahn.oauth2.server.granttype.impl.AuthorizationCode;
import jp.eisbahn.oauth2.server.granttype.impl.ClientCredentials;
import jp.eisbahn.oauth2.server.granttype.impl.DefaultGrantHandlerProvider;
import jp.eisbahn.oauth2.server.granttype.impl.Password;
import jp.eisbahn.oauth2.server.granttype.impl.RefreshToken;

import org.junit.Test;

public class DefaultGrantHandlerProviderTest {

	@Test
	public void testSimple() throws Exception {
		DefaultGrantHandlerProvider target = new DefaultGrantHandlerProvider();
		Map<String, GrantHandler> handlers = target.getHandlers();
		assertEquals(4, handlers.size());
		assertTrue(handlers.get("authorization_code") instanceof AuthorizationCode);
		assertTrue(handlers.get("password") instanceof Password);
		assertTrue(handlers.get("refresh_token") instanceof RefreshToken);
		assertTrue(handlers.get("client_credentials") instanceof ClientCredentials);
	}

}
