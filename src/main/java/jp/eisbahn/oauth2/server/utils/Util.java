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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * This class provides some utility methods.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class Util {

	/**
	 * Decode the URL encoded string.
	 * @param source The URL encoded string.
	 * @return The decoded original string.
	 */
	public static String decodeParam(String source) {
		try {
			return URLDecoder.decode(source, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Decode the BASE64 encoded string.
	 * @param source The BASE64 string.
	 * @return The decoded original string.
	 */
	public static String decodeBase64(String source) {
		try {
			return new String(Base64.decodeBase64(source), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Encode the object to JSON format string.
	 * @param source The object that you want to change to JSON string.
	 * @return The JSON encoded string.
	 * @throws IllegalStateException If the translation failed.
	 */
	public static String toJson(Object source) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(source);
		} catch (JsonGenerationException e) {
			throw new IllegalStateException(e);
		} catch (JsonMappingException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
