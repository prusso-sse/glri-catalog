package gov.usgs.glri.catalog.services;

import gov.usgs.glri.catalog.model.params.Format;
import gov.usgs.glri.catalog.utils.WebUtils;

import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.google.common.io.CharStreams;

@Service
public class ScienceBaseService {
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	public String queryScienceBase(Map<String, String[]> reqMap) throws Exception {
		String stringFromStream = "";
		
		URIBuilder uriBuild = new URIBuilder();
		uriBuild.setScheme("https");
		uriBuild.setHost("www.sciencebase.gov");
		uriBuild.setPath("/catalog/items");
		uriBuild.setParameter("s", "Search");
		uriBuild.setParameter("q", "");
		uriBuild.setParameter("fields", "title,summary");
		
		WebUtils.appendControlParams(reqMap, uriBuild);
		WebUtils.appendGlriParams(reqMap, uriBuild);
		WebUtils.appendSpatialParams(reqMap, uriBuild);		
		Format format = WebUtils.appendStandardParams(reqMap, uriBuild);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpGet httpGet = new HttpGet(uriBuild.build());
		System.out.println(httpGet.getURI());
		httpGet.addHeader("Accept", "application/json,application/xml,text/html");
		CloseableHttpResponse response1 = httpclient.execute(httpGet);

		try {
			System.out.println(response1.getStatusLine());
			HttpEntity entity = response1.getEntity();
			
			String encoding = WebUtils.findEncoding(entity, DEFAULT_ENCODING);
			stringFromStream = CharStreams.toString(new InputStreamReader(entity.getContent(), encoding));
			
			EntityUtils.consume(entity);
		} finally {
			response1.close();
		}
		
		return stringFromStream;
	}
}
