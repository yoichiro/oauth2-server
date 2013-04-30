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

/**
 * This project is an implementation for OAuth 2.0 Specification.
 * Especially, the protocol of the server area is covered by this project.<br />
 * <br />
 * <b>How to use</b><br />
 * <br />
 * This project is supporting some common processes to issue tokens.
 * To use this framework, you have to provide an implementation of a
 * DataHandler interface. The implementation class connects this framework
 * to your databases or such storages.<br />
 * <br />
 * Also, you have to implement a DataHandlerFactory interface to
 * create your DataHandler instance.<br />
 * <br />
 * Classes you have to implement are only above.<br />
 * <br />
 * A class to handle a request to issue an access token is Token class.
 * But, the Token class needs some helper classes. Therefore, you have to
 * provide their instances to the Token instance. If you're using Spring Framework
 * DI Container, you can inject them to the Token instance. Refer the applicationContext-token-schenario.xml file.<br />
 * <br />
 * The way to use the Token class is simple. You can use it as the following snippet:<br />
 * <br />
 * <code>
 * HttpServletRequest request = ...; // Provided by Servlet Container<br />
 * Token token = ...; // Injected<br />
 * Token.Response response = token.handleRequest(request);<br />
 * int code = response.getCode(); // 200, 400, 401, ...<br />
 * String body = response.getBody(); // {"token_type":"Bearer","access_token":"...", ...}<br />
 * </code><br />
 * An code for an integration test has the request and response contents of each grant type.
 * Refer the test code TokenScenarioTest.
 */
package jp.eisbahn.oauth2.server;