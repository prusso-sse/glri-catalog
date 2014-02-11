package gov.usgs.glri.catalog.factories;

import gov.usgs.glri.catalog.model.params.GLRIFilters;
import gov.usgs.glri.catalog.model.params.GLRIParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SearchFilterFactory {
	public List<GLRIFilters> getScienceBaseSearchFilters() {
		List<GLRIFilters> results = new ArrayList<GLRIFilters>();
		
		List<String> mediums = new ArrayList<String>(Arrays.asList("water", "air"));
		GLRIFilters mediumFilter = new GLRIFilters(GLRIParam.MEDIUM, "Mediums", mediums);
		
		List<String> parameterGroups = new ArrayList<String>(Arrays.asList("Nutrient", "Biological", "Organics", "Inorganics"));
		GLRIFilters parameterGroupsFilter = new GLRIFilters(GLRIParam.PARAM_GROUP, "Parameter Groups", parameterGroups);
		
		List<String> parameters = new ArrayList<String>(Arrays.asList("Nitrogen", "Phosphorus", "Fish", "Birds", "PCB", "Mercury", "Atrazine"));
		GLRIFilters parametersFilter = new GLRIFilters(GLRIParam.PARAM, "Parameters", parameters);
		
		List<String> studyAreas = new ArrayList<String>(Arrays.asList("Lake Michigan Basin", "Lake Erie Basin", "Lake Huron Basin", "Lake Superior Basin", "Lake Ontario Basin"));
		GLRIFilters studyAreasFilter = new GLRIFilters(GLRIParam.AREA, "Study Areas", studyAreas);
		
		List<String> focusAreas = new ArrayList<String>(Arrays.asList("Toxic Substances", "Invasive Species", "Nearshore Health", "Habitat & Wildlife", "Accountability"));
		GLRIFilters focusAreasFilter = new GLRIFilters(GLRIParam.FOCUS, "Focus Areas", focusAreas);
		
		results.add(mediumFilter);
		results.add(parameterGroupsFilter);
		results.add(parametersFilter);
		results.add(studyAreasFilter);
		results.add(focusAreasFilter);
				
		return results;
	}
}
