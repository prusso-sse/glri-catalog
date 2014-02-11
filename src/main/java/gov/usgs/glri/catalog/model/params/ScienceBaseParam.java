package gov.usgs.glri.catalog.model.params;

import org.apache.commons.lang3.StringUtils;

public enum ScienceBaseParam {
	
	TEXT_QUERY("text_query", "q"),
	FORMAT("format", "format"),
	FIELDS("fields", "fields"),
	SPATIAL("spatial", "searchExtent");
	
	
	private final String shortName;
	private final String fullName;
	
	ScienceBaseParam(String shortName, String fullName) {
		this.shortName = shortName;
		this.fullName = fullName;
	}
	
	public String getFullName() {
		return fullName;
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
	public ScienceBaseParam getForShortName(String shortName) {
		shortName = StringUtils.trimToNull(shortName);
		if (shortName == null) return null;
		shortName = shortName.toLowerCase();
		
		
		for (ScienceBaseParam tag : ScienceBaseParam.values()) {
			if (tag.shortName.equals(shortName)) return tag;
		}
		
		return null;
	}
}
