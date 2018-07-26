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

	  <div class="container" id="results">

		<c:if test="${bufr.present}">
		      <c:out value="Filename: ${bufr.fileName}"/><br>
		      <c:out value="File Size: ${bufr.fileSize}"/><br>
		      <c:out value="MD5 ChkSum: ${bufr.md5CheckSum}"/><br>
		      <c:out value="Messages: ${bufr.messages}"/><br>
		      <c:out value="Total Response Time: ${bufr.overallResponseTime}"/><br>
		      <p>
		      <table class="table table-bordered table-striped">
		      <thead>
			<tr>
				<th scope="col">Decoder</th>
				<th scope="col">Response Time (ms)</th>
		      		<th scope="col">Status</th>
		      		<th scope="col">Error Text</th>
			</tr>
		      </thead>
		      <tbody>
		      <c:forEach items="${bufr.decoderResults}"	var="decoderResult">
		      	<tr>
			<th scope="row"> 			   
			    <a href="${decoderResult.url}" target="_blank"><c:out value="${decoderResult.decoder}"/></a>
			</th>
			<th scope="row">
			   <c:out value="${decoderResult.responseTime}"/>
			</th>
			<th scope="row">
			   <c:out value="${decoderResult.status}"/>
			</th>
			<th scope="row">
			   <c:out value="${decoderResult.error}"/>
			</th>
		      </c:forEach>
		      </tbody>
		      </table>
		 </c:if>

	</div>
<hr>
 <footer>
        <div class="row">
            <div class="col-md-4">
                <a href="http://www.wmo.int/pages/prog/www/WMOCodes.html">WMO FM 94 BUFR</a><br/>
                <a href="http://www.wmo.int/pages/prog/www/WMOCodes/WMO306_vI2/VolumeI.2.html">
                    WMO Manual on Codes
                </a>
            </div>
            <div class="col-md-4">
                Powered by<br/>
                <a href="https://github.com/ywangd/pybufrkit">PyBufrKit</a><br/>
                <a href="https://software.ecmwf.int/wiki/display/ECC/ecCodes+Home">ecCodes</a><br/>
		<a href="https://github.com/alexmaul/trollbufr">trollbufr</a><br/>
		<a href="https://launchpad.net/libecbufr">libECBUFR</a><br/>
		<a href="https://metacpan.org/pod/Geo::BUFR">Geo::BUFR</a><br/>
		<a href="https://confluence.ecmwf.int/display/BUFR/BUFRDC+Home">BUFRDC</a><br/>
            </div>
            <div class="col-md-4"> 
               <a href="https://github.com/mheene/bufrValidationDashboard">Source
        code</a><br/>
                <a href="mailto:markus.heene@gmail.com">Contact me</a>
            </div>
        </div>
    </footer>

    
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
