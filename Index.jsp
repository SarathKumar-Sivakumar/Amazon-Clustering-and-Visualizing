<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>D3 clustering</title>
</head>
<body>
	<form id="home" name="home" method="post" action="clust"
		enctype="multipart/form-data">
		<table>
			<tr>
				<td>Upload the file</td>
				<td><input type="file" id="upload" name="upload" accept=".csv"></td>
			</tr>
			<tr>
				<td>Enter the no of cluster</td>
				<td><input type="text" id="cluster" name="cluster"></td>
			</tr>
		</table>
		<input type="submit" value="submit">
	</form>

</body>
</html>