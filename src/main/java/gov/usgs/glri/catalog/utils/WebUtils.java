/*******************************************************************************
 * Project:		glri-catalog
 * Source:		WebsiteUtils.java
 * Author:		Philip Russo
 ******************************************************************************/

package gov.usgs.glri.catalog.utils;

import gov.usgs.glri.catalog.model.params.ControlParam;
import gov.usgs.glri.catalog.model.params.Format;
import gov.usgs.glri.catalog.model.params.GLRIParam;
import gov.usgs.glri.catalog.model.params.ScienceBaseParam;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class WebUtils {
	public static final Format DEFAULT_FORMAT = Format.HTML;
	
	public static Logger getLogger(Class<?> T) {
		URL logFile = T.getResource("/log4j.properties");
		if(logFile == null) {
			logFile = T.getResource("conf/log4j.properties");
		}
		
		Logger log = Logger.getLogger(T.getName());	
		PropertyConfigurator.configure(logFile);
		
		return log;
	}
	
	/**
	 * Parses an http contentType header string into the encoding, if it exists.
	 * If it cannot find the encoding, the default is returned.
	 * 
	 * @param entity Find the header in this entity
	 * @param defaultEncoding Return this encoding if we cannot find the value in the header.
	 * @return 
	 */
	public static String findEncoding(HttpEntity entity, String defaultEncoding) {
		
		try {
			return findEncoding(entity.getContentType().getValue(), defaultEncoding);
		} catch (RuntimeException e) {
			return defaultEncoding;
		}
	}
	
	/**
	 * Parses an http contentType header string into the encoding, if it exists.
	 * If it cannot find the encoding, the default is returned.
	 * 
	 * @param contentTypeHeaderString The String value of the http contentType header
	 * @param defaultEncoding Return this encoding if we cannot find the value in the header.
	 * @return 
	 */
	public static String findEncoding(String contentTypeHeaderString, String defaultEncoding) {
		
		try {
			//Example contentType:  text/html; charset=utf-8

			String[] parts = contentTypeHeaderString.split(";");
			String encoding = StringUtils.trimToNull(parts[1]);
			parts = encoding.split("=");
			
			if (parts[0].trim().equalsIgnoreCase("charset")) {
				encoding = StringUtils.trimToNull(parts[1]);
				return encoding.toUpperCase();
			} else {
				return defaultEncoding;
			}
			
		} catch (RuntimeException e) {
			return defaultEncoding;
		}
	}
	
	public static void appendControlParams(Map<String, String[]> requestParams, URIBuilder uriBuild) {
		for (ControlParam tag : ControlParam.values()) {
			String[] vals = requestParams.get(tag.getShortName());
			if (vals != null && vals.length > 0) {
				String val = StringUtils.trimToNull(vals[0]);
				if (val != null && ("true".equalsIgnoreCase(val) || "yes".equalsIgnoreCase(val))) {
					uriBuild.addParameter(tag.getFullName(), tag.getValue());
				}	
			}
		}
	}
	
	public static void appendGlriParams(Map<String, String[]> requestParams, URIBuilder uriBuild) {
		for (GLRIParam tag : GLRIParam.values()) {
			String[] vals = requestParams.get(tag.getShortName());
			if (vals != null && vals.length > 0) {
				String val = StringUtils.trimToNull(vals[0]);
				if (val != null) {
					uriBuild.addParameter("filter", "tags={scheme:'" + tag.getFullName() + "',name:'" + val + "'}");
				}	
			}
		}
	}
	
	public static Format appendStandardParams(Map<String, String[]> requestParams, URIBuilder uriBuild) {
		Format format = DEFAULT_FORMAT;
		
		boolean formatFound = false;
		
		for (ScienceBaseParam tag : ScienceBaseParam.values()) {
			String[] vals = requestParams.get(tag.getShortName());
			if (vals != null && vals.length > 0) {
				String val = StringUtils.trimToNull(vals[0]);
				if (val != null) {
					if (tag.equals(ScienceBaseParam.FORMAT)) {
						format = Format.UNKNOWN.getForShortName(val);
						formatFound = true;
					}
					uriBuild.addParameter(tag.getFullName(), val);
				}	
			}
		}
		
		if (!formatFound) {
			uriBuild.addParameter(ScienceBaseParam.FORMAT.getFullName(), DEFAULT_FORMAT.getShortName());
			format = DEFAULT_FORMAT;
		}
		
		return format;
	}
	
	public static void appendSpatialParams(Map<String, String[]> requestParams, URIBuilder uriBuild) {
		String[] spatialQuery = requestParams.get(ScienceBaseParam.SPATIAL);
		
		if((spatialQuery != null) && (spatialQuery.length > 0)) {
			String val = StringUtils.trimToNull(spatialQuery[0]);
			if (val != null) {
				System.out.println("Spatial Param: [" + val + "]");
				uriBuild.addParameter(ScienceBaseParam.SPATIAL.getFullName(), val);
			}
		}
	}
	
	public static Map<String, String[]> addProjectConstantParams(Map<String, String[]> existingParams) {
		HashMap<String, String[]> paramMap = new HashMap<String, String[]>();
		
		//Add the GLRI project collection ID so that all searches take place w/in this collection
		//paramMap.put(ScienceBaseParam.PARENT_ID.getShortName(), new String[]{"52e6a0a0e4b012954a1a238a"});
		
		paramMap.putAll(existingParams);
		return paramMap;
	}
	
	public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
        List<T> r = new ArrayList<T>(c.size());
        for(Object o: c)
          r.add(clazz.cast(o));
        return r;
    }
}
