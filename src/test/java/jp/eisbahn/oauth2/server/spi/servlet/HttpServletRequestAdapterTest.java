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

package jp.eisbahn.oauth2.server.spi.servlet;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

public class HttpServletRequestAdapterTest {

	@Test
	public void test() {
		HttpServletRequest request = createMock(HttpServletRequest.class);
		expect(request.getParameter("name1")).andReturn("value1");
		expect(request.getHeader("name2")).andReturn("value2");
		@SuppressWarnings("serial")
		Map<String, String> map = new HashMap<String, String>() {
			{
				put("k1", "v1");
				put("k2", "v2");
			}
		};
		expect(request.getParameterMap()).andReturn(map);
		replay(request);
		HttpServletRequestAdapter target = new HttpServletRequestAdapter(request);
		assertEquals("value1", target.getParameter("name1"));
		assertEquals("value2", target.getHeader("name2"));
		Map<String, String> parameterMap = target.getParameterMap();
		assertEquals(2, parameterMap.size());
		assertEquals("v1", parameterMap.get("k1"));
		assertEquals("v2", parameterMap.get("k2"));
		verify(request);
	}

}
