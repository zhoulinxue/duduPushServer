/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.czl.chatClient.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import com.czl.chatClient.Constants;
import com.czl.chatClient.utils.IoUtils;

/**
 * Provides retrieving of {@link InputStream} of image by URI from network or
 * file system or app resources.<br />
 * {@link URLConnection} is used to retrieve image stream from network.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.0
 */
public class httpGetter {
	/** {@value} */
	public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5 * 1000; // milliseconds
	/** {@value} */
	public static final int DEFAULT_HTTP_READ_TIMEOUT = 10 * 1000; // milliseconds

	/** {@value} */
	protected static final int BUFFER_SIZE = 32 * 1024; // 32 Kb
	/** {@value} */
	protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

	protected static final int MAX_REDIRECT_COUNT = 5;

	protected final int connectTimeout;
	protected final int readTimeout;

	public httpGetter() {
		this(DEFAULT_HTTP_CONNECT_TIMEOUT, DEFAULT_HTTP_READ_TIMEOUT);
	}

	public httpGetter(int connectTimeout, int readTimeout) {

		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	/**
	 * 获取输入流
	 * 
	 * @param imageUri
	 * @return
	 * @throws IOException
	 */
	public InputStream getStreamFromNetwork(String imageUri) throws IOException {
		HttpURLConnection conn = createConnection(imageUri);

		int redirectCount = 0;
		while (conn.getResponseCode() / 100 == 3 && redirectCount < MAX_REDIRECT_COUNT) {
			conn = createConnection(conn.getHeaderField("Location"));
			redirectCount++;
		}

		InputStream imageStream;
		try {
			imageStream = conn.getInputStream();
		} catch (IOException e) {
			// Read all data to allow reuse connection (http://bit.ly/1ad35PY)
			IoUtils.readAndCloseStream(conn.getErrorStream());
			throw e;
		}
		if (!shouldBeProcessed(conn)) {
			IoUtils.closeSilently(imageStream);
			throw new IOException("Image request failed with response code " + conn.getResponseCode());
		}
		if (!shouldBeProcessed(conn)) {
			return null;
		}

		return new ContentLengthInputStream(new BufferedInputStream(imageStream, BUFFER_SIZE), conn.getContentLength());
	}

	/**
	 * @param conn
	 *            Opened request connection (response code is available)
	 * @return <b>true</b> - if data from connection is correct and should be
	 *         read and processed; <b>false</b> - if response contains
	 *         irrelevant data and shouldn't be processed
	 * @throws IOException
	 */
	protected boolean shouldBeProcessed(HttpURLConnection conn) throws IOException {
		return conn.getResponseCode() == 200;
	}

	protected HttpURLConnection createConnection(String url) throws IOException {
//		 String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);
		return conn;
	}

	/**
	 * 获取MS 服务器 地址
	 * 
	 * @param msUrl
	 * @return
	 */

	public String getMsServer(String msUrl) {
		// TODO Auto-generated method stub
		try {
			InputStream stream = (ContentLengthInputStream) getStreamFromNetwork(msUrl);
			if (stream != null) {
				return IoUtils.read(stream, Constants.CONTENT_CHAR_SET);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return "";

	}

}
