/*******************************************************************************
 * Project:		glri-catalog
 * Source:		MainController.java
 * Author:		Philip Russo
 ******************************************************************************/

package gov.usgs.glri.catalog.controllers;

import gov.usgs.glri.catalog.factories.SearchFilterFactory;
import gov.usgs.glri.catalog.model.params.GLRIFilters;
import gov.usgs.glri.catalog.services.ScienceBaseService;
import gov.usgs.glri.catalog.utils.WebUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class MainController {
	private static Logger log = WebUtils.getLogger(MainController.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ScienceBaseService sbService;
	
	@Autowired
	private SearchFilterFactory sfFactory;
		
	@RequestMapping(value="/", method=RequestMethod.GET)
    public ModelAndView entry() {
		log.info("MainController.entry() Called");		
		
		ModelAndView mv = new ModelAndView("/main", "title", "Science Base Sample Query Page");
		mv.addObject("version", env.getProperty("catalog.version"));
		
		/**
		 * Get our filter params
		 * 		Factory Calls
		 */
		List<GLRIFilters> filters = sfFactory.getScienceBaseSearchFilters();
		mv.addObject("filters", filters);
		
		List<String> responseFormats = new ArrayList<String>(Arrays.asList("json", "html", "xml"));
		mv.addObject("responseFormats", responseFormats);
		
		/**
		 * Add the environment to the session so JSPs can grab their own properties
		 */
		mv.addObject("env", env);
		
		return mv;
    }
	
	@RequestMapping(value="/sciencebasequery", method = RequestMethod.POST)
	@ResponseBody
	public String getScienceBaseQueryPost(HttpServletRequest request) {
		log.info("MainController.getScienceBaseQueryPost() Called");
		
		Map<String, String[]> reqMap = request.getParameterMap();
		reqMap = WebUtils.addProjectConstantParams(reqMap);
		
		String response = "";
		try {
			response = sbService.queryScienceBase(reqMap);		
		} catch (Exception ex) {
			log.error(ex.getMessage());
			
			/**
			 * TODO We need an error page
			 */
			/*
			response.setContentType("text/html;charset=UTF-8");
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet ScienceBaseQuery</title>");			
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet ScienceBaseQuery at " + request.getContextPath() + "</h1>");
			out.println("<p>Error</p>");
			out.println("</body>");
			out.println("</html>");
			*/
		}
		
		System.out.println("Sending back: \n[" + response + "]\n");
		
		return response;
	}
	
	@RequestMapping(value="/sciencebasequery", method = RequestMethod.GET)
	@ResponseBody
	public String getScienceBaseQueryGet(HttpServletRequest request) {
		log.info("MainController.getScienceBaseQueryResponse() Called");
		
		Map<String, String[]> reqMap = request.getParameterMap();
		reqMap = WebUtils.addProjectConstantParams(reqMap);
		
		String response = "";
		try {
			response = sbService.queryScienceBase(reqMap);		
		} catch (Exception ex) {
			log.error(ex.getMessage());
			
			/**
			 * TODO We need an error page
			 */
			/*
			response.setContentType("text/html;charset=UTF-8");
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet ScienceBaseQuery</title>");			
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet ScienceBaseQuery at " + request.getContextPath() + "</h1>");
			out.println("<p>Error</p>");
			out.println("</body>");
			out.println("</html>");
			*/
		}
		
		System.out.println("Sending back: \n[" + response + "]\n");
		
		return response;
	}
}
