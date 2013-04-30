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

package jp.eisbahn.oauth2.server.data;

import jp.eisbahn.oauth2.server.models.Request;

/**
 * This class provides the ability to create a concrete instance of DataHandler.
 * When you use the implementation class of DataHandler, you must also
 * implement this sub class to create the instance of your concrete
 * Data Handler class.
 * 
 * DataHandler instance should be create per request. Therefore, the request
 * instance is passed to this create() method. Or, you might be able to keep
 * the DataHandler instance(s) in this factory instance to cache for performance.
 * 
 * @author Yoichiro Tanaka
 *
 */
public interface DataHandlerFactory {

	/**
	 * Create a DataHandler instance and return it.
	 * This implementation method must create the instance based on a concrete
	 * class of the DataHandler abstract class.
	 * @param request The request object to provide some information passed from
	 * client.
	 * @return The DataHandler instance.
	 */
	public DataHandler create(Request request);

}
