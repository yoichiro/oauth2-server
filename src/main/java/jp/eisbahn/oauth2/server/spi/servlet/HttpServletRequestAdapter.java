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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.eisbahn.oauth2.server.models.Request;

/**
 * This class adapts a HttpServletRequest to a Request interface.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class HttpServletRequestAdapter implements Request {

	private HttpServletRequest request;
	
	/**
	 * Initialize this instance with the HttpServletRequest.
	 * @param request The request object on the Servlet API.
	 */
	public HttpServletRequestAdapter(HttpServletRequest request) {
		super();
		this.request = request;
	}
	
	/*
	 * (non-Javadoc)
	 * @see jp.eisbahn.oauth2.server.models.Request#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String name) {
		return request.getParameter(name);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.eisbahn.oauth2.server.models.Request#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		return request.getHeader(name);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.eisbahn.oauth2.server.models.Request#getParameterMap()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getParameterMap() {
		return request.getParameterMap();
	}

}
