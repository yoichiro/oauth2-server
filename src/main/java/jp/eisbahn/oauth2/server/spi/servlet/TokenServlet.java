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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import jp.eisbahn.oauth2.server.data.DataHandlerFactory;
import jp.eisbahn.oauth2.server.endpoint.Token;
import jp.eisbahn.oauth2.server.endpoint.Token.Response;
import jp.eisbahn.oauth2.server.fetcher.clientcredential.ClientCredentialFetcher;
import jp.eisbahn.oauth2.server.fetcher.clientcredential.ClientCredentialFetcherImpl;
import jp.eisbahn.oauth2.server.granttype.GrantHandlerProvider;
import jp.eisbahn.oauth2.server.granttype.impl.DefaultGrantHandlerProvider;

/**
 * This class is an HttpServlet implementation of the Token issuing endpoint.
 * 
 * This instance needs Three helper objects. One is a DataHandlerFactory instance.
 * Other one is a GrantHandlerProvider instance. Last one is
 * ClientCredentialFetcher instance. These implementation
 * class name are specified as the init-param values. For instance, specify
 * the following in your web.xml file:<br />
 * <br />
 * <code>
 * &lt;servlet&gt;<br />
 * &nbsp;&nbsp;&lt;servlet-name&gt;token&lt;servlet-name&gt;<br />
 * &nbsp;&nbsp;&lt;servlet-class&gt;jp.eisbahn.oauth2.server.spi.servlet.TokenServlet&lt;servlet-name&gt;<br />
 * &nbsp;&nbsp;&lt;init-param&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-name&gt;dataHandlerFactory&lt;/param-name&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-value&gt;your-class-name&lt;/param-value&gt;<br />
 * &nbsp;&nbsp;&lt;/init-param&gt;<br />
 * &nbsp;&nbsp;&lt;init-param&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-name&gt;grantHandlerProvider&lt;/param-name&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-value&gt;your-class-name&lt;/param-value&gt;<br />
 * &nbsp;&nbsp;&lt;/init-param&gt;<br />
 * &nbsp;&nbsp;&lt;init-param&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-name&gt;clientCredentialFetcher&lt;/param-name&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-value&gt;your-class-name&lt;/param-value&gt;<br />
 * &nbsp;&nbsp;&lt;/init-param&gt;<br />
 * &lt;/servlet&gt;
 * </code>
 * 
 * @author Yoichiro Tanaka
 *
 */
@SuppressWarnings("serial")
public class TokenServlet extends HttpServlet {

	private static final String DATA_HANDLER_FACTORY_CLASSNAME = "dataHandlerFactory";
	private static final String GRANT_HANDLER_PROVIDER_CLASSNAME = "grantHandlerProvider";
	private static final String CLIENT_CREDENTIAL_FETCHER_CLASSNAME = "clientCredentialFetcher";

	private Token token;

	/**
	 * Initialize this servlet.
	 * For instance, this method loads three implementation class name and create
	 * these instances. Then, the Token instance to process the
	 * OAuth 2.0 grant execution for the request is created with their helper instances.
	 * 
	 * @param config The ServletConfig object.
	 * @exception ServletException Each helper instance could not be created.
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			DataHandlerFactory dataHandlerFactory = getDataHandlerFactory(config);
			GrantHandlerProvider grantHandlerProvider = getGrantHandlerProvider(config);
			ClientCredentialFetcher clientCredentialFetcher = getClientCredentialFetcher(config);
			token = new Token();
			token.setDataHandlerFactory(dataHandlerFactory);
			token.setGrantHandlerProvider(grantHandlerProvider);
			token.setClientCredentialFetcher(clientCredentialFetcher);
		} catch (ClassNotFoundException e) {
			throw new ServletException(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new ServletException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ServletException(e.getMessage(), e);
		}
	}

	private ClientCredentialFetcher getClientCredentialFetcher(ServletConfig config)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		ClientCredentialFetcher fetcher = createInstance(CLIENT_CREDENTIAL_FETCHER_CLASSNAME, config);
		if (fetcher != null) {
			return fetcher;
		} else {
			return new ClientCredentialFetcherImpl();
		}
	}

	private GrantHandlerProvider getGrantHandlerProvider(ServletConfig config)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		GrantHandlerProvider provider = createInstance(GRANT_HANDLER_PROVIDER_CLASSNAME, config);
		if (provider != null) {
			return provider;
		} else {
			return new DefaultGrantHandlerProvider();
		}
	}

	private DataHandlerFactory getDataHandlerFactory(ServletConfig config)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return createInstance(DATA_HANDLER_FACTORY_CLASSNAME, config);
	}

	@SuppressWarnings("unchecked")
	private <T> T createInstance(String name, ServletConfig config)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String className = config.getInitParameter(name);
		if (className != null) {
			Class<?> clazz = Class.forName(className);
			return (T)clazz.newInstance();
		} else {
			return null;
		}
	}

	/**
	 * Issue the token against the request based on OAuth 2.0.
	 * 
	 * @param req The request object.
	 * @param resp The response object.
	 * @exception IOException When the error regarding I/O occurred.
	 * @exception ServletException When other error occurred.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpServletRequestAdapter request = new HttpServletRequestAdapter(req);
		Response response = token.handleRequest(request);
		resp.setStatus(response.getCode());
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter writer = resp.getWriter();
		IOUtils.write(response.getBody(), writer);
		writer.flush();
	}

}
