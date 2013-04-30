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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.eisbahn.oauth2.server.fetcher.clientcredential.ClientCredentialFetcherImpl;
import jp.eisbahn.oauth2.server.models.ClientCredential;
import jp.eisbahn.oauth2.server.models.Request;

public class ClientCredentialFetcherImplTest {

	private ClientCredentialFetcherImpl target;

	@Before
	public void setUp() {
		target = new ClientCredentialFetcherImpl();
	}

	@After
	public void tearDown() {
		target = null;
	}

	@Test
	public void testFetchBasic() throws Exception {
		Request request = createMock(Request.class);
		String encoded = "Y2xpZW50X2lkX3ZhbHVlOmNsaWVudF9zZWNyZXRfdmFsdWU=";
		expect(request.getHeader("Authorization")).andReturn("Basic " + encoded);
		replay(request);
		ClientCredential clientCredential = target.fetch(request);
		assertEquals("client_id_value", clientCredential.getClientId());
		assertEquals("client_secret_value", clientCredential.getClientSecret());
	}

	@Test
	public void testFetchParameter() throws Exception {
		Request request = createMock(Request.class);
		expect(request.getHeader("Authorization")).andReturn(null);
		expect(request.getParameter("client_id")).andReturn("client_id_value");
		expect(request.getParameter("client_secret")).andReturn("client_secret_value");
		replay(request);
		ClientCredential clientCredential = target.fetch(request);
		assertEquals("client_id_value", clientCredential.getClientId());
		assertEquals("client_secret_value", clientCredential.getClientSecret());
	}

}
