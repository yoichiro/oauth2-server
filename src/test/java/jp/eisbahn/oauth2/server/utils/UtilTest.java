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

package jp.eisbahn.oauth2.server.utils;

import static org.junit.Assert.assertEquals;
import jp.eisbahn.oauth2.server.utils.Util;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.junit.Test;

public class UtilTest {

	@Test
	public void testDecodeParam() throws Exception {
		String source = "wOJIO9A2W5mFwDgiDvZbTSMK%2FPY%3D";
		String result = Util.decodeParam(source);
		assertEquals("wOJIO9A2W5mFwDgiDvZbTSMK/PY=", result);
	}

	@Test
	public void testToJson() throws Exception {
		JsonTarget target = new JsonTarget();
		target.foo = "foo1";
		target.bar = "bar1";
		String json = Util.toJson(target);
		assertEquals("{\"foo\":\"foo1\",\"bar\":\"bar1\"}", json);
	}

	@JsonPropertyOrder({"foo", "bar"})
	private static class JsonTarget {
		@SuppressWarnings("unused")
		public String foo;
		@SuppressWarnings("unused")
		public String bar;
	}

}
