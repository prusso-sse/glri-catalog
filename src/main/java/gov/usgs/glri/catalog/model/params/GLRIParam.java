package gov.usgs.glri.catalog.model.params;

import org.apache.commons.lang3.StringUtils;

public enum GLRIParam {
	
	MEDIUM("medium", "sample-medium"),
	PARAM_GROUP("param_group", "parameter-group"),
	PARAM("param", "parameter"),
	AREA("area", "study-area"),
	FOCUS("focus", "focus-area"),
	UNKNOWN("", "");
	
	//Same for all grli tags
	private static final String glriSchema = "https://www.sciencebase.gov/vocab/GLRI";
	
	private final String shortName;
	private final String fullNameLocal;
	
	GLRIParam(String shortNameLocal, String fullNameLocal) {
		this.shortName = shortNameLocal;
		this.fullNameLocal = fullNameLocal;
	}
	
	public String getFullName() {
		return glriSchema + "/" + fullNameLocal;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	/**
	 * Finds a tag for a given shortName or null if it cannot be found.
	 * 
	 * @param shortName Case Insensitive
	 * @return 
	 */
	public GLRIParam getForShortName(String shortName) {
		shortName = StringUtils.trimToNull(shortName);
		if (shortName == null) return null;
		shortName = shortName.toLowerCase();
		
		
		for (GLRIParam tag : GLRIParam.values()) {
			if (tag.shortName.equals(shortName)) return tag;
		}
		
		return null;
	}	
}

