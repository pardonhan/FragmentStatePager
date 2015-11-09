package com.qdjxd.examination.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
import org.xmlpull.v1.XmlPullParserException;

public class MyAndroidHttpTransport extends HttpTransportSE {

	private static final int outTime = 20000;//���ӳ�ʱʱ��
	
	public MyAndroidHttpTransport(String url) {
		super(url);
	}

	protected ServiceConnection getServiceConnection() throws IOException {
		return new ServiceConnectionSE(this.url);
	}

	public void callWithOutResult(String soapAction, SoapEnvelope envelope)
			throws IOException, XmlPullParserException {
		if (soapAction == null)
			soapAction = "\"\"";
		byte[] requestData = createRequestData(envelope);
		this.requestDump = ((this.debug) ? new String(requestData) : null);
		this.responseDump = null;
		ServiceConnection connection = getServiceConnection();
		connection.setRequestProperty("User-Agent", "kSOAP/2.0");
		connection.setRequestProperty("SOAPAction", soapAction);
		connection.setRequestProperty("Content-Type", "text/xml");
		connection.setRequestProperty("Connection", "close");
		connection.setRequestProperty("Content-Length", "" + requestData.length);
		connection.setRequestMethod("POST");

		OutputStream os = connection.openOutputStream();
		os.write(requestData, 0, requestData.length);
		os.flush();
		os.close();
		requestData = null;
		try {
			connection.connect();
			connection.disconnect();
		} catch (IOException e) {
			connection.disconnect();
		}
	}

	class ServiceConnectionSE implements ServiceConnection {
		private HttpURLConnection connection;

		public ServiceConnectionSE(String url) throws IOException {
			this.connection = ((HttpURLConnection) new URL(url)
					.openConnection());
			this.connection.setUseCaches(false);
			this.connection.setDoOutput(true);
			this.connection.setDoInput(true);
			this.connection.setConnectTimeout(outTime);
		}

		public void connect() throws IOException {
			this.connection.connect();
		}

		public void disconnect() {
			this.connection.disconnect();
		}

		public void setRequestProperty(String string, String soapAction) {
			this.connection.setRequestProperty(string, soapAction);
		}

		public void setRequestMethod(String requestMethod) throws IOException {
			this.connection.setRequestMethod(requestMethod);
		}

		public OutputStream openOutputStream() throws IOException {
			return this.connection.getOutputStream();
		}

		public InputStream openInputStream() throws IOException {
			return this.connection.getInputStream();
		}

		public InputStream getErrorStream() {
			return this.connection.getErrorStream();
		}
	}

}
