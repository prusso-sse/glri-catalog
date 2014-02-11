<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="gov.usgs.cida.config.DynamicReadOnlyProperties"%>
<!DOCTYPE HTML>

<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<html>
	<head>
		<tiles:insertAttribute name="meta" />
                <script type="text/javascript">
                    (function(){                       
                    }());
                </script>
                        
	</head>
	<body>
		<div class="container site_body_content">
			<div class="row">
				<div id="site_header" class="col-xs-12">
					<tiles:insertAttribute name="header" />
				</div>
			</div>
			<div class="row">
				<div id="site_nav" class="col-xs-12">
					<tiles:insertAttribute name="nav" />
				</div>
			</div>
			<div class="row site_body_content">
				<div id="site_content" class="col-xs-12">
					<tiles:insertAttribute name="body" />
				</div>
			</div>
		</div>
		
		<!--	<div id="site_footer" class="navbar-fixed-bottom"> -->
		<div id="site_footer" class="">
			<tiles:insertAttribute name="footer" />
 		</div>
	</body>
</html>
