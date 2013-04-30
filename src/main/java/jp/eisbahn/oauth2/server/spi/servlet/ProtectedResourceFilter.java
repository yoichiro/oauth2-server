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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import jp.eisbahn.oauth2.server.data.DataHandlerFactory;
import jp.eisbahn.oauth2.server.endpoint.ProtectedResource;
import jp.eisbahn.oauth2.server.endpoint.ProtectedResource.Response;
import jp.eisbahn.oauth2.server.exceptions.OAuthError;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.AccessTokenFetcherProvider;
import jp.eisbahn.oauth2.server.fetcher.accesstoken.impl.DefaultAccessTokenFetcherProvider;

/**
 * This servlet filter checks whether a request to access to each protected
 * resource is valid or not as the OAuth 2.0.
 * 
 * This instance needs two helper objects. One is a DataHandlerFactory instance.
 * Other one is a AccessTokenFetcherProvider instance. These implementation
 * class name are specified as the init-param values. For instance, specify
 * the following in your web.xml file:<br />
 * <br />
 * <code>
 * &lt;filter&gt;<br />
 * &nbsp;&nbsp;&lt;filter-name&gt;protectedResourceFilter&lt;filter-name&gt;<br />
 * &nbsp;&nbsp;&lt;filter-class&gt;jp.eisbahn.oauth2.server.spi.servlet.ProtectedResourceFilter&lt;filter-name&gt;<br />
 * &nbsp;&nbsp;&lt;init-param&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-name&gt;dataHandlerFactory&lt;/param-name&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-value&gt;your-class-name&lt;/param-value&gt;<br />
 * &nbsp;&nbsp;&lt;/init-param&gt;<br />
 * &nbsp;&nbsp;&lt;init-param&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-name&gt;accessTokenFetcherProvider&lt;/param-name&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-value&gt;your-class-name&lt;/param-value&gt;<br />
 * &nbsp;&nbsp;&lt;/init-param&gt;<br />
 * &lt;/filter&gt;
 * </code>
 * 
 * @author Yoichiro Tanaka
 *
 */
public class ProtectedResourceFilter implements Filter {

	private static final String DATA_HANDLER_FACTORY_CLASSNAME = "dataHandlerFactory";
	private static final String ACCESS_TOKEN_FETCHER_PROVIDER_CLASSNAME = "accessTokenFetcherProvider";

	private ProtectedResource protectedResource;

	/**
	 * Initialize this filter.
	 * For instance, this method loads two implementation class name and create
	 * these instances. Then, the ProtectedResource instance to process the
	 * OAuth 2.0 validation for the request is created with their helper instances.
	 * 
	 * @param config The FilterConfig object.
	 * @exception ServletException Each helper instance could not be created.
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		try {
			DataHandlerFactory dataHandlerFactory = getDataHandlerFactory(config);
			AccessTokenFetcherProvider accessTokenFetcherProvider = getAccessTokenFetcherProvider(config);
			protectedResource = new ProtectedResource();
			protectedResource.setDataHandlerFactory(dataHandlerFactory);
			protectedResource.setAccessTokenFetcherProvider(accessTokenFetcherProvider);
		} catch (ClassNotFoundException e) {
			throw new ServletException(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new ServletException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ServletException(e.getMessage(), e);
		}
	}

	/**
	 * Check the request for whether can access or not to APIs to access the protected
	 * resource.
	 * 
	 * @param req The request object.
	 * @param resp The response object.
	 * @param chain The chain object to chain some filters.
	 * @exception IOException When the error regarding I/O occurred.
	 * @exception ServletException When the first argument is not a HttpServletRequest
	 * instance.
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		if (req instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest)req;
			HttpServletRequestAdapter adapter = new HttpServletRequestAdapter(httpRequest);
			try {
				Response response = protectedResource.handleRequest(adapter);
				req.setAttribute("client_id", response.getClientId());
				req.setAttribute("remote_user", response.getRemoteUser());
				req.setAttribute("scope", response.getScope());
				chain.doFilter(req, resp);
			} catch (OAuthError e) {
				if (resp instanceof HttpServletResponse) {
					HttpServletResponse httpResponse = (HttpServletResponse)resp;
					httpResponse.setStatus(e.getCode());
					List<String> params = new ArrayList<String>();
					params.add("error=\"" + e.getType() + "\"");
					if (StringUtils.isNotBlank(e.getDescription())) {
						params.add("error_description=\"" + e.getDescription() + "\"");
					}
					String error = StringUtils.join(params, ", ");
					httpResponse.setHeader("WWW-Authenticate", "Bearer " + error);
				} else {
					throw new ServletException("This filter is available under HTTP Servlet container.");
				}
			}
		} else {
			throw new ServletException("This filter is available under HTTP Servlet container.");
		}
	}

	/**
	 * Do nothing.
	 */
	@Override
	public void destroy() {
	}

	private AccessTokenFetcherProvider getAccessTokenFetcherProvider(FilterConfig config)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		AccessTokenFetcherProvider provider = createInstance(ACCESS_TOKEN_FETCHER_PROVIDER_CLASSNAME, config);
		if (provider != null) {
			return provider;
		} else {
			return new DefaultAccessTokenFetcherProvider();
		}
	}

	private DataHandlerFactory getDataHandlerFactory(FilterConfig config)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return createInstance(DATA_HANDLER_FACTORY_CLASSNAME, config);
	}

	@SuppressWarnings("unchecked")
	private <T> T createInstance(String name, FilterConfig config)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String className = config.getInitParameter(name);
		if (className != null) {
			Class<?> clazz = Class.forName(className);
			return (T)clazz.newInstance();
		} else {
			return null;
		}
	}

}
