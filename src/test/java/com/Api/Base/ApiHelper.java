package com.Api.Base;



import org.apache.commons.collections4.map.HashedMap;

import org.testng.annotations.Test;

import com.Api.requestPOJO.AddDataPojo;
import com.Api.requestPOJO.updateRequestPojo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import io.restassured.specification.RequestSpecification;

public class ApiHelper {


	String token="Bearer ";

	RequestSpecification reqSpec=RestAssured.given().baseUri("https://api.github.com/")
			 .header("Authorization",token);
	
 public Response getAllRepo() {
	
			 Response res=reqSpec.when().get("user/repos");
			 res.then().contentType(ContentType.JSON);
		   //res.prettyPrint();
			 return res;
 }

 public Response getASingleRepo(String repo) {
	
	
	 		reqSpec
			 .pathParam("owner","BvidhyaVipin")
			 .pathParam("repo", repo);
	 
	 Response res=reqSpec.when().get("repos/{owner}/{repo}");
	
	
	 //res.prettyPrint();
	 //res.then().log();
	 return res;
	 
 }

 public Response post(String name,String description,String homepage,String myprivate) {
	 

	 AddDataPojo r= AddDataPojo.builder().name(name).description(description).homepage(homepage).myprivate(myprivate).build(); 
	 
	 
	 reqSpec.contentType(ContentType.JSON)
	 .body(r);
	 
	 Response res=reqSpec.when().post("user/repos");
	 
	 res.prettyPrint();

	 return res;
	 		
	 		
	 
 }

public Response update(String reponame,String description,String myprivate) {
	updateRequestPojo p=updateRequestPojo.builder().name(reponame).description(description).myprivate(myprivate).build();
	
	reqSpec
			.pathParam("owner","BvidhyaVipin")
			 .pathParam("repo", "vidhya");
	reqSpec.contentType(ContentType.JSON)
	.body(p);
	 Response res=reqSpec.when().patch("repos/{owner}/{repo}");
	 res.prettyPrint();
	 return res;
	
}
public Response delete(String reponame) {
	
	 reqSpec
			 .pathParam("owner","BvidhyaVipin")
			 .pathParam("repo", reponame);
	 
	 Response res=reqSpec.when().delete("repos/{owner}/{repo}");
	
	
	 res.prettyPrint();
	 //res.then().log();
	 return res;
}
	 
	 


}
