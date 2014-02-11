<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@include file="/WEB-INF/base.jsp"%>

<div id="main_page" class="page_body_content">
	<div class="row">
		<div class="col-xs-12">
			<div class="well well-sm">
				<h1>GLRI to ScienceBase Query API Example</h1>
				<p>All query are being submitted to the ScienceBase REST API.</p>
				<p>
					Choosing the <b>GLRI Results Only</b> option limits the results to the
					<a href="https://www.sciencebase.gov/catalog/item/52e6a0a0e4b012954a1a238a">Great Lakes Restoration Initiative</a>
					community of datasets.
				</p>
			</div>
		</div>
	</div>
	<form role="form" id="sb-query-form" action="${context}/sciencebasequery" method="POST">
		<div class="row">
			<div class="col-xs-12">
				<div class="well">
					<div class="row">
						<div class="col-xs-6">		
							<div class="row" style="padding-bottom: 10px;">
								<div class="col-xs-4">
									<label class="filter_label pull-right">Text Search</label>
								</div>
								<div class="col-xs-8">
									<input type="text" class="form-control" id="text_query" name="text_query" style="width: 90%;">
								</div>
							</div>
							<c:forEach var="filter" items="${filters}" varStatus="index">
								<div class="row">
									<div class="col-xs-4">
										<label class="filter_label pull-right">${filter.filterName}</label>
									</div>
									<div class="col-xs-8">
									<select class="selectpicker pull-left" name="${filter.glriParam.getShortName()}" id="${filter.glriParam.getShortName()}" multiple title="Any" data-width="90%">
										<c:forEach var="filteritem" items="${filter.values}" varStatus="index">
											<option value="${filteritem}">${filteritem}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							</c:forEach>
							<div class="row">
								<div class="col-xs-12">
									<select class="selectpicker pull-left adjustDropDown" name="format" id="format" title="Result Format" data-style="btn-xs" data-width="auto">
										<c:forEach var="responseFormat" items="${responseFormats}" varStatus="index">
											<option value="${responseFormat}">${responseFormat}</option>
										</c:forEach>
									</select>
									<div class="checkbox pull-left" style="margin-left: 20px;">
										<label style="white-space: nowrap;">
											<input type="checkbox" name="glri_only" id="glri_only" value="true" checked> GLRI Results Only?
										</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-12">
									<input class="btn btn-primary btn-sm" id="query-submit" type="submit" value="Search" style="margin-left: 30px"/>
								</div>
							</div>
						</div>
						<div class="col-xs-6">
							<div class="row">
								<div class="col-xs-12">
									<div id="map" class="boundingMap"></div>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-4">
									<div class="checkbox" style="margin-left: 6px;">
										<label>
											<input type="checkbox" name="drawBox" id="drawBox" value="box"> Draw Bounding Box
										</label>
									</div>
								</div>
								<div class="col-xs-7">
									<div class="row">
										<div class="col-xs-3 boundsGroup">
											<div class="input-group input-group-sm">
												<span class="input-group-addon boundsValue">xMin</span>
												<input id="xmin_label" type="text" class="form-control boundsValue" placeholder="-" disabled>
											</div>
										</div>
										<div class="col-xs-3 boundsGroup">
											<div class="input-group input-group-sm">
												<span class="input-group-addon boundsValue">yMin</span>
												<input id="ymin_label" type="text" class="form-control boundsValue" placeholder="-" disabled>
											</div>
										</div>
										<div class="col-xs-3 boundsGroup">
											<div class="input-group input-group-sm">
												<span class="input-group-addon boundsValue">xMax</span>
												<input id="xmax_label" type="text" class="form-control boundsValue" placeholder="-" disabled>
											</div>
										</div>
										<div class="col-xs-3 boundsGroup">
											<div class="input-group input-group-sm">
												<span class="input-group-addon boundsValue">yMax</span>
												<input id="ymax_label" type="text" class="form-control boundsValue" placeholder="-" disabled>
											</div>
										</div>
										<input type="hidden" id="spatial" name="spatial" value="">
									</div>
								</div>
								<div class="col-xs-1"></div>
							</div>
							<div class="row">
								<div class="col-xs-12">
									<button id="clearMapButton" type="button" class="btn btn-default btn-xs" style="margin-left: 6px;">Clear Map Filter</button>
								</div>
							</div>							
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
	<div class="row">
		<div class="col-xs-12">
			<div class="well">
				<div class="row">
					<div class="col-xs-12">
						<table id="query-results-table">
							<thead>
								<tr>
									<th>id</th>
									<th>title</th>
									<th>summary</th>
								</tr>
							</thead>
							<tbody>
	
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(
		function() {
			/* Kick off the fancy selects */
			$('.selectpicker').selectpicker();

			/**
			 * Build the OpenLayers map
			 */
			var boxControl;

			var lon = -85.47;
			var lat = 45.35;
			var zoom = 5.25;
			var map, wmsLayer, boxLayer;

			function init() {
				map = new OpenLayers.Map('map');

				wmsLayer = new OpenLayers.Layer.WMS("OpenLayers WMS",
						"http://vmap0.tiles.osgeo.org/wms/vmap0?", {
							layers : 'basic'
						});

				boxLayer = new OpenLayers.Layer.Vector("Box layer");

				map.addLayers([ wmsLayer, boxLayer ]);
				map.addControl(new OpenLayers.Control.LayerSwitcher());
				map.addControl(new OpenLayers.Control.MousePosition());

				boxControl = new OpenLayers.Control.DrawFeature(boxLayer,
						OpenLayers.Handler.RegularPolygon, {
							handlerOptions : {
								sides : 4,
								irregular : true
							}
						});

				// register a listener for removing any boxes already drawn
				boxControl.handler.callbacks.create = function(data) {
					if (boxLayer.features.length > 0) {
						boxLayer.removeAllFeatures();
					}
				}

				// register a listener for drawing a box
				boxControl.events.register('featureadded', boxControl,
						function(f) {

							// Variables for the geometry are: bottom/left/right/top
							// Sciencebase requires bounds to look like: [xmin,ymin,xmax,ymax]
							var extent = "["
									+ f.feature.geometry.bounds.left + ","
									+ f.feature.geometry.bounds.bottom
									+ "," + f.feature.geometry.bounds.right
									+ "," + f.feature.geometry.bounds.top
									+ "]";
							
							$('#xmin_label').val(f.feature.geometry.bounds.left);
							$('#ymin_label').val(f.feature.geometry.bounds.bottom);
							$('#xmax_label').val(f.feature.geometry.bounds.right);
							$('#ymax_label').val(f.feature.geometry.bounds.top);

							$('#spatial').val(extent);
						});

				map.addControl(boxControl);

				map.setCenter(new OpenLayers.LonLat(lon, lat), 5);
			}

			init();

			$('#drawBox').click(function() {
				if ($(this).is(':checked')) {
					boxControl.handler.stopDown = true;
					boxControl.handler.stopUp = true;
					boxControl.activate();
				} else {
					boxControl.handler.stopDown = false;
					boxControl.handler.stopUp = false;
					boxControl.deactivate();
				}
			});
			
			$('#clearMapButton').click(function() {
				$('#spatial').val('');
				$('#xmin_label').val('-');
				$('#ymin_label').val('-');
				$('#xmax_label').val('-');
				$('#ymax_label').val('-');
				
				boxLayer.removeAllFeatures();
				map.setCenter(new OpenLayers.LonLat(lon, lat), 5);				
			});
			
			/**
			 * Dynatable
			 */
			// Sets up click behavior on all button elements with the alert class
		    // that exist in the DOM when the instruction was executed
			$.dynatableSetup({
		features: {
		  paginate: false,
		  sort: true,
		  pushState: true,
		  search: false,
		  recordCount: true,
		  perPageSelect: false
		},
		params: {
			records: "_root"
		}
	});
    $("#query-submit").on( "click", function(event) {
		event.preventDefault();
		event.stopPropagation();
		updateTable();
    });
		});
	
	var dynamicTable = null;

	function updateTable() {
		var url = buildDataUrl();
		console.log( "Submitting the AJAX Request as: " + url);
				
		$.ajax({
			dataType: "json",
			url: url,
			success: tableDataReady
		});

		
	}

	var tableDataReady = function(data) {
		
		var records = data.items;
		
		if (dynamicTable == null) {
			$("#query-results-table").dynatable({
				dataset: {
					records: records
				}
			});
			
			dynamicTable = $("#query-results-table").data('dynatable');
		} else {
			dynamicTable.records.updateFromJson(records);
			dynamicTable.process();
		}
		
	};
	
	function buildDataUrl() {
    	var url = $("#sb-query-form").attr("action");
    	url += "?text_query=" + (($("#text_query").val() === null) ? "" : $("#text_query").val());
    	url += "&medium=" + (($("#medium").val() === null) ? "" : $("#medium").val());
    	url += "&param_group=" + (($("#param_group").val() === null) ? "" : $("#param_group").val());
    	url += "&param=" + (($("#param").val() === null) ? "" : $("#param").val());
    	url += "&area=" + (($("#area").val() === null) ? "" : $("#area").val());
    	url += "&focus=" + (($("#focus").val() === null) ? "" : $("#focus").val());
    	url += "&spatial=" + (($("#spatial").val() === null) ? "" : $("#spatial").val());
    	url += "&format=" + (($("#format").val() === null) ? "" : $("#format").val());
    	url += "&glri_only=" + (($("#glri_only").val() === null) ? "" : $("#glri_only").val());
    	return url;
    };
</script>

