<#if result??>
	<div id="graph"></div>
	
	<script type="text/javascript">
		var width = $(window).width()*.60;
		var height = 325;
		var padding = 80;
		var jsonStatsStr = ${result};
		var jsonStatsData = jQuery.parseJSON(jsonStatsStr);
		
		var serviceStats = jsonStatsData.results[0].${gn};
		
		if(serviceStats != null){			
			var data = serviceStats.${statName};
			
			var methods = ${methods};
	
			var colors = ["steelblue","blue","cornflowerblue"];
	//add more		              "red","firebrick","maroon"];
			
			var fills = ["steelblue","blue", "cyan"];
	//add more		             "orangered","tomato","crimson"];
			
			var times = new Array();
			for(i=0; i<data.length; i++){
			    times.push(data[i].ts.max);
			}
			
			var values = new Array();
			var str = "";
			for(var i in methods){
			    var m = methods[i];
			    var tempArray = new Array();
			    str = str + "[";
			    for(var j in data){
			        tempArray.push(data[j][m]);
			        str = str + "," + data[j][m];
			    }
			    str = str + "]\n";
			values.push(tempArray);
			}
			
			function getBiggest(d){
				var max = 0;
				for(var i in methods){
					var method = methods[i];
					if(max < d[method]){
						max = d[method];
					}
				}
				
				if(max == 0){
					return 1;
				}
				
				return max;
			}
			
			var y = d3.scale.linear().domain([0, d3.max(data, function(d) { return getBiggest(d); })]).range([height, 0]);
			var x = d3.time.scale().domain([d3.min(times,  function(d) {  return getDate(d); }), d3.max(times, function(d) {  return  getDate(d);  })]).range([1, width]);
			var r = d3.scale.linear().domain([0, 1250]).range([1, 4]);
			var rec = d3.scale.linear().domain([0, 1250]).range([5, 15]);
			        
			function getDate(d){    
			   return new Date(d);
			}
			
			var graphic = d3.select("#graph").
			    append("svg:svg").
			    attr("width", width + padding * 2).
			    attr("height", height + padding * 3);
			    
			//graph		
			var graphGroup = graphic.append("svg:g").
			    attr("transform", "translate("+padding*1.5+","+padding/4+")");
			    
		    var line = d3.svg.area()
			    .x(function(d, i) { return x(getDate(times[i])); })
			    .y0(height-1)
			    .y1(y)
			    .interpolate("basis");
	
			graphGroup.selectAll(".line")
			    .data(values)
			    .enter().append("path")
			    .attr("class", "line")
			    .attr("d", 	line)
			    .style('stroke', function(d, i) { return colors[i]; })
			    .style('stroke-width', 1)
			    .style('fill', function(d, i) { return fills[i]; })
			    .style('fill-opacity',".06");
	
			var axisGroup = graphic.append("svg:g").
			  attr("transform", "translate("+(padding*1.5)+","+(padding/4)+")");
			
			var xAxis = d3.svg.axis()
			    .scale(x)
			    .orient("bottom")
			    .ticks(8);    
			
			var yAxis = d3.svg.axis()
			    .scale(y)
			    .orient("left")
			    .ticks(5);
			
			axisGroup.append("g")
			  .attr("class", "y axis")
			  .call(yAxis);
			    
			axisGroup.append("g")
			  .attr("class", "x axis")
			  .attr("transform", "translate(0," + (height) + ")")
			  .call(xAxis);
			var legendpadding = 20;
	        var legendOffset = height+50; 
			for(var methodIndex in methods){
			    /*
			    var cirData = values[methodIndex];
			    graphGroup.selectAll(methods[methodIndex] + ".circles")
			              .data(cirData)
			              .enter()
			              .append("svg:circle")
			              .attr("cx", function(d, i) { return x(getDate(times[i])); })
			              .attr("cy", y)
			              .attr("r", r(width))
			              .attr("opacity", 0)
			              .attr("stroke",colors[methodIndex])
			              .attr("fill",fills[methodIndex])
					      .on("mouseover", function(d, i) {
					     		d3.select("#GraphTitle span").text("value: " + d + " Date: " + getDate(times[i]));
				//	     		d3.select(this).attr("fill","aliceblue");
					      })
					      .on("mouseout", function() {
					     		d3.select("#GraphTitle span").text("Statistics Graph");
				//	     		d3.select(this).attr("fill",fills[methodIndex]);
					      });
			    */
			    var recSize = rec(width);
	            axisGroup.append("svg:rect")
	               .attr("fill", colors[methodIndex] )
	               .attr("x", legendpadding / 2)
	               .attr("y", legendOffset+(recSize+10)*methodIndex)
	               .attr("width", recSize)
	               .attr("height", recSize);
	            axisGroup.append("svg:text")
	               .attr("x", 20 + legendpadding/2)
	               .attr("y", legendOffset+10+(recSize+10)*methodIndex)
	               .text(methods[methodIndex]);
			}
			
			axisGroup.append("defs")
			.append("path")
			.attr("id", "yAxisLabel")
			.attr("d", "M -"+(padding*1.3)+",0 V "+height);
			
			var yAxisText = $("#statField option:selected").text()
			if(getUnits() != null){
				yAxisText = yAxisText+" ("+getUnits()+")";
			}
			
			axisGroup.append("svg:g")
			.attr("id", "thing")
			.attr("fill", "black")
			.append("text")
			.attr("font-size", "20")
			.append("textPath")
			.attr("xlink:href", "#yAxisLabel")
			.attr("text-anchor","middle")
			.attr("startOffset","50%")
			.text(yAxisText);
			    
			axisGroup.append("defs")
			.append("path")
			.attr("id", "xAxisLabel")
			.attr("d", "M 0,"+(height+padding)+" H "+width);
			
			axisGroup.append("svg:g")
			.attr("id", "thing")
			.attr("fill", "black")
			.append("text")
			.attr("font-size", "20")
			.append("textPath")
			.attr("xlink:href", "#xAxisLabel")
			.attr("text-anchor","middle")
			.attr("startOffset","50%")
			.text("Time");
		} else {
			$("#graph").html("<p><b>Requested data is unavailable</b></p>");
		}
	</script>
<#else>
	<p>
		<b>There was an error retrieving graph data.</b><br />
	</p>
</#if>