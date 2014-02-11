package gov.usgs.glri.catalog.model.params;

import java.util.List;

public class GLRIFilters {
	private GLRIParam glriParam;
	private String filterName;
	private List<String> values;
	
	public GLRIFilters(GLRIParam param, String name, List<String> values) {
		this.glriParam = param;
		this.filterName = name;
		this.values = values;
	}

	public GLRIParam getGlriParam() {
		return glriParam;
	}

	public void setGlriParam(GLRIParam glriParam) {
		this.glriParam = glriParam;
	}
	
	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
}
