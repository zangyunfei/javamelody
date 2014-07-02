package com.zyf.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * http client 工具类
 * 
 * @author yuhui.tang 2013-11-15 下午7:01:12
 */
public class HttpClientUtil {

	private static PoolingHttpClientConnectionManager connManager = null;
	private static Logger log = Logger.getLogger(HttpClientUtil.class);
	private static CloseableHttpClient httpclient = null;

	static {
		try {
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					builder.build());

			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
					.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", sslsf).build();

			connManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);

			httpclient = HttpClients.custom().setConnectionManager(connManager)
					.build();
			// Create socket configuration
			SocketConfig socketConfig = SocketConfig.custom()
					.setTcpNoDelay(true).build();

			connManager.setDefaultSocketConfig(socketConfig);
			// Create message constraints

			MessageConstraints messageConstraints = MessageConstraints.custom()
					.setMaxHeaderCount(200).setMaxLineLength(2000).build();

			// Create connection configuration
			ConnectionConfig connectionConfig = ConnectionConfig.custom()
					.setMalformedInputAction(CodingErrorAction.IGNORE)
					.setUnmappableInputAction(CodingErrorAction.IGNORE)
					.setCharset(Consts.UTF_8)
					.setMessageConstraints(messageConstraints).build();
			connManager.setDefaultConnectionConfig(connectionConfig);
			connManager.setMaxTotal(200);
			connManager.setDefaultMaxPerRoute(20);
		} catch (KeyManagementException e) {
			log.error("KeyManagementException");
		} catch (NoSuchAlgorithmException e) {
			log.error("NoSuchAlgorithmException");
		} catch (KeyStoreException e) {
			log.error("KeyStoreException");
		}
	}

	public static void main(String[] args) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mobileno", "13910296793");
		String str1 = JSONObject.toJSONString(map).replace("\\", "");
		RequestParameter parameter = new RequestParameter();
		parameter
				.setUrl("http://127.0.0.1:8084/user/findpasswd/getvalidcode_bymobile");
		parameter.setEncoding("utf-8");
		parameter.setDecoding("utf-8");
		parameter.setContentType("application/json");
		post(parameter, 10 * 1000, str1);
	}

	/**
	 * 发起post请求
	 * 
	 * @param parameter
	 * @param timeout
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String post(RequestParameter parameter, int timeout,
			String content) throws Exception {

		HttpPost post = new HttpPost(parameter.getUrl());
		String responseString = null;
		try {
			log.error("-------post---------------------------------------------------------");
			log.error(parameter.getUrl() + "\t" + timeout + "\t"
					+ parameter.getDecoding() + "\t" + parameter.getEncoding()
					+ "\t" + parameter.getContentType());
			// log.error(content);
			if (!"".equals(parameter.getContentType())) {
				post.setHeader("Content-type", parameter.getContentType());
			}
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(timeout).setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout)
					.setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);
			if (!"".equals(parameter.getEncoding())) {
				post.setEntity(new StringEntity(content, parameter
						.getEncoding()));
			}
			CloseableHttpResponse response = httpclient.execute(post);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						if ("".equals(parameter.getDecoding())) {
							responseString = EntityUtils.toString(entity);
						} else {
							responseString = EntityUtils.toString(entity,
									parameter.getDecoding());
						}
						log.error(responseString);

					}

				} finally {
					if (entity != null) {
						entity.getContent().close();
					}

				}
			} finally {
				if (response != null) {
					response.close();
				}
			}

		} catch (UnsupportedEncodingException e) {
			throw new Exception(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			post.releaseConnection();
		}
		return responseString;

	}

	/**
	 * 页面发起http请求
	 * 
	 * @param url
	 * @param map
	 * @param charset
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(RequestParameter parameter,
			Map<String, String> map, int timeout) throws Exception {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return httpPost(parameter, list, timeout);
	}

	/**
	 * 页面发起http请求 不包含附件
	 * 
	 * @param list
	 * @param charset
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(RequestParameter parameter,
			List<NameValuePair> list, int timeout) throws Exception {
		HttpPost post = new HttpPost(parameter.getUrl());
		String responseString = null;
		try {
			if (parameter.getContentType() != null)
				post.setHeader("Content-type", parameter.getContentType());
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(timeout).setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout)
					.setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);
			if (timeout <= 0) {
				timeout = 10;
			}
			post.setEntity(new UrlEncodedFormEntity(list, parameter
					.getEncoding())); // 将参数传入post方法中
			CloseableHttpResponse response = httpclient.execute(post);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						responseString = EntityUtils.toString(entity,
								parameter.getDecoding());
						log.error(responseString);
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new Exception(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			post.releaseConnection();
		}
		return responseString;
	}

	/**
	 * get
	 * 
	 * @param url
	 * @param decoding
	 * @return
	 * @throws Exception
	 */
	public static String get(String url, String decoding) throws Exception {
		RequestParameter parameter = new RequestParameter();
		parameter.setUrl(url);
		parameter.setDecoding(decoding);
		return get(parameter, 10 * 1000, 10 * 1000);
	}

	/**
	 * 
	 * @param url
	 * @param encode
	 * @param connectTimeout
	 * @param soTimeout
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String get(RequestParameter parameter, int connectTimeout,
			int soTimeout) throws Exception {

		String responseString = null;
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();
		StringBuilder sb = new StringBuilder();
		sb.append(parameter.getUrl());
		HttpGet get = new HttpGet(sb.toString());
		get.setConfig(requestConfig);
		log.error("-------get---------------------------------------------------------");
		log.error(parameter.getUrl() + "\t" + connectTimeout + "\t"
				+ parameter.getDecoding() + "\t" + parameter.getEncoding()
				+ "\t" + parameter.getContentType());
		try {
			CloseableHttpResponse response = httpclient.execute(get);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						if ("".equals(parameter.getDecoding())) {
							responseString = EntityUtils.toString(entity);
						} else {
							responseString = EntityUtils.toString(entity,
									parameter.getDecoding());
						}
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
				log.error(responseString);
			} catch (Exception e) {
				log.error(HttpClientUtil.class.getName());
				return responseString;
			} finally {
				if (response != null) {
					response.close();
				}

			}

		} catch (SocketTimeoutException e) {
			throw new Exception(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			get.releaseConnection();
		}
		return responseString;

	}

	public final static int connectTimeout = 5000;

	/**
	 * 
	 * HTTPS请求，默认超时为5S
	 * 
	 * @param reqURL
	 * 
	 * @param params
	 * 
	 * @return
	 */

	public static String connectPostHttps(String reqURL,
			Map<String, String> params) {

		String responseContent = null;
		HttpPost httpPost = new HttpPost(reqURL);
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(connectTimeout)
					.setConnectTimeout(connectTimeout)
					.setConnectionRequestTimeout(connectTimeout).build();

			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			httpPost.setEntity(new UrlEncodedFormEntity(formParams,
					Consts.UTF_8));
			httpPost.setConfig(requestConfig);

			// 绑定到请求 Entry
			for (Map.Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}

			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				// 执行POST请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity,
								Consts.UTF_8);
					}

				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}

			} finally {
				if (response != null) {
					response.close();
				}
			}

		} catch (ClientProtocolException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			httpPost.releaseConnection();
		}
		return responseContent;

	}

}
