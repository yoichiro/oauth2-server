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

package jp.eisbahn.oauth2.server.integration;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import jp.eisbahn.oauth2.server.endpoint.ProtectedResource;
import jp.eisbahn.oauth2.server.endpoint.ProtectedResource.Response;
import jp.eisbahn.oauth2.server.models.Request;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-protectedresource-scenario.xml")
public class ProtectedResourceScenarioTest {
	
	@Autowired
	private ProtectedResource target;

	@Test
	public void testSimple() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn("Bearer accessToken1").times(2);
		replay(request);
		Response response = target.handleRequest(request);
		assertEquals("userId1", response.getRemoteUser());
		assertEquals("clientId1", response.getClientId());
		assertEquals("scope1", response.getScope());
		verify(request);
	}

}
