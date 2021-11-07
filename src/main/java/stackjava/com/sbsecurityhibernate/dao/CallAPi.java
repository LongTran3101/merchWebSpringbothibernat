package stackjava.com.sbsecurityhibernate.dao;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class CallAPi {

	//private static CallAPI2 instance = null;
	private static final int CONNECTION_TIMEOUT_MS = 600000;
	private static final int LATENT_CONNECTION_TIMEOUT_MS = 60000000;
	public static String FINALTOKEN = "";
	public String callAPIPost(String completeUrl, String body)throws ClientProtocolException, IOException  {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		RequestConfig config = RequestConfig.custom().setConnectTimeout(CONNECTION_TIMEOUT_MS)
				.setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS).setSocketTimeout(CONNECTION_TIMEOUT_MS).build();
		HttpPost httppost = new HttpPost(completeUrl);
		
			httppost.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
			httppost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + FINALTOKEN);
			httppost.setConfig(config);
			StringEntity stringEntity = new StringEntity(body, "UTF-8");
			httppost.getRequestLine();
			httppost.setEntity(stringEntity);
	
		CloseableHttpResponse response = client.execute(httppost);
		if (response.getStatusLine().getStatusCode() >= 300) {
			throw new IOException(String.format("failure - received a %d for %s.",
					response.getStatusLine().getStatusCode(), httppost.getURI().toString()));
		}
		HttpEntity entity = response.getEntity();

		// StringWriter writer = new StringWriter();
		// IOUtils.copy(entity.getContent(), writer);
		return EntityUtils.toString(entity, "UTF-8");

	}

}
