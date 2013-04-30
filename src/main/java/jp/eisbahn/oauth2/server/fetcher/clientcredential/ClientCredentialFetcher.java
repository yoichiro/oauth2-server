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

import jp.eisbahn.oauth2.server.models.ClientCredential;
import jp.eisbahn.oauth2.server.models.Request;

/**
 * This interface defines a method to fetch a client credential information
 * from a request.
 * 
 * @author Yoichiro Tanaka
 *
 */
public interface ClientCredentialFetcher {

	/**
	 * Fetch a client credential from a request passed as the argument and
	 * return it.
	 * @param request The request object.
	 * @return The fetched client credential information.
	 */
	public ClientCredential fetch(Request request);

}
