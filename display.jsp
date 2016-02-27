<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>D3 clustering</title>
<style>
body {
	margin: 0px;
}

.h, .v {
	stroke: black;
	stroke-dasharray: 5 5;
	stroke-width: 1;
	stroke-opacity: .5;
}

.axis path, .axis line {
	fill: none;
	stroke: black;
	shape-rendering: crispEdges;
}

.axis text {
	font-family: sans-serif;
	font-size: 11px;
}
</style>
</head>
<body>
	<script src="http://d3js.org/d3.v2.min.js"></script>
	<script>
		var color = [ "red", "green", "blue", "orange", "yellow", "voilet",
				"black", "indigo", "pink" ];
		var width = 500, height = 500, margin = 50;
		var svg = d3.select("body").append("svg").attr("width", width).attr(
				"height", height);
		var x = d3.scale.linear().domain([ 130, 400 ]).range(
				[ margin, width - margin ]);
		var y = d3.scale.linear().domain([ 1700, 3000 ]).range(
				[ height - margin, margin ]);

		var xAxis = d3.svg.axis().scale(x).orient("bottom");

		var yAxis = d3.svg.axis().scale(y).orient("left");

		svg.append("g").attr("class", "axis").attr("transform",
				"translate(0," + (height - margin) + ")").call(xAxis);

		svg.append("g").attr("class", "axis").attr("transform",
				"translate(" + margin + ",0)").call(yAxis);

		var D3data =<%=request.getAttribute("out_data")%>
		
		svg.selectAll("circle").data(D3data).enter().append("circle")
			.attr("r",4)
			.attr("y", function(d, i) {return i * 5;})
			.attr("cx", function(d) {return x(+d.Prcp);})
			.attr("cy", function(d) {return y(+d.Wind);})
			.attr("fill", function(d) {return color[d.cluster];})
	</script>
</body>
</html>