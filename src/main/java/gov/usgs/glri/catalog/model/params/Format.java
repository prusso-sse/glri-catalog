package gov.usgs.glri.catalog.model.params;

import org.apache.commons.lang3.StringUtils;

public enum Format {
	
	UNKNOWN("", ""),
	HTML("html", "text/html"),
	XML("xml", "application/xml"),
	JSON("json", "application/json");
	
	private final String shortName;
	private final String fullName;
	
	Format(String shortName, String fullName) {
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
	public Format getForShortName(String shortName) {
		shortName = StringUtils.trimToNull(shortName);
		if (shortName == null) return null;
		shortName = shortName.toLowerCase();
		
		
		for (Format tag : Format.values()) {
			if (tag.shortName.equals(shortName)) return tag;
		}
		
		return null;
	}	
}
