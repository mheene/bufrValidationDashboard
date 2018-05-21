<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1,
    shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet"
    href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css"
    integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4"
    crossorigin="anonymous">
    
    <title>BUFR Validation Dashboard</title>
  </head>
  <body>
	<div class="container" id="main">
	     <div>
		<h3>BUFR  Validation Dashboard</h3>
    	     </div>

	  <div class="container" id="upload">
    	  	<form method="post" action="uploadFile" enctype="multipart/form-data">
            	      <p>Select file to upload:</p>
            	      <p>
			<input type="file" name="uploadFile" class="file btn btn-primary" style="display:inline-block;"/>
            	      	<input type="submit" class="btn btn-primary" value="Upload" />
  		      </p>
          	</form>
	   </div>
<!--    
	  <div class="accordion" id="accordion">
	     <c:catch var="exception">${bufr}</c:catch>
	     <c:if test="!${exception}">${bufr}</c:if>	
	     <c:if test="${exception}">${exception}</c:if>	
    	  </div>
	  -->

	  <div>

		      <c:out value="Filename: ${bufr.fileName}"/><br>
		      <c:out value="File Size: ${bufr.fileSize}"/><br>
		      <c:out value="Messages: ${bufr.messages}"/><br>
		      <p>
		      <c:forEach items="${bufr.decoderResults}" var="decoderResult">
    			   <c:out value="Decoder: ${decoderResult.decoder}"/>
			   <c:out value="Status: ${decoderResult.status}"/>
			   <c:out value="Error: ${decoderResult.error}"/>
			   <br/>
		      </c:forEach>

	</div>
    

    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
    integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
    crossorigin="anonymous"></script>
    <script
    src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"
    integrity="sha384-cs/chFZiN24E4KMATLdqdvsezGxaGsi4hLGOzlXwp5UZB1LY//20VyM2taTB4QvJ"
    crossorigin="anonymous"></script>
    <script
    src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"
    integrity="sha384-uefMccjFJAIv6A+rW+L4AHf99KvxDjWSu1z9VI8SKNVmz4sk7buKt/6v9KI65qnm"
    crossorigin="anonymous"></script>

 
  </body>
</html>
