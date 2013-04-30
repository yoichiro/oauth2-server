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

package jp.eisbahn.oauth2.server.granttype;

import static org.junit.Assert.*;

import org.junit.Test;

import jp.eisbahn.oauth2.server.granttype.GrantHandler.GrantHandlerResult;

public class GrantHandlerResultTest {

	@Test
	public void testSimple() {
		GrantHandlerResult target = new GrantHandlerResult(
			"tokenType1", "accessToken1");
		assertEquals("tokenType1", target.getTokenType());
		assertEquals("accessToken1", target.getAccessToken());

		long expiresIn = 123L;
		target.setExpiresIn(expiresIn);
		assertEquals(expiresIn, (long)target.getExpiresIn());
		String refreshToken = "refreshToken1";
		target.setRefreshToken(refreshToken);
		assertEquals(refreshToken, target.getRefreshToken());
		String scope = "scope1";
		target.setScope(scope);
		assertEquals(scope, target.getScope());
	}

}
