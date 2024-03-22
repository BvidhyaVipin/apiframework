package com.Api.Tests;


import static org.testng.Assert.assertEquals;

import java.util.List;

import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.Api.Base.ApiHelper;
import com.Api.responsePOJO.GetResponsePOJO;
import com.Api.responsePOJO.NonExistingRepoResponsePojo;
import com.Api.utils.ExtentReportsUtility;
import com.Api.utils.JsonSchemaValidate;
import com.github.javafaker.Faker;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;


@Listeners(com.Api.listener.TestEventListenersUtility.class)
public class TestCases {
	ApiHelper API;
	Faker faker;
	String repo="cucumber";
	String repocreate;
	String newReponame;
	
	
	ExtentReportsUtility report=ExtentReportsUtility.getInstance();
	@BeforeClass
	public void beforeClass() {
		API=new ApiHelper();
		faker= new Faker();
		repocreate="created"+faker.name().lastName();
		newReponame=repocreate+"addded";
		
		 
	 }
	
	@Test(priority = 0, description="get a single repo details from github")
	public void getASingleRepo() {
		Response res=API.getASingleRepo(repo);
		res.prettyPrint();
		GetResponsePOJO getresponse=res.getBody().as(new TypeRef<GetResponsePOJO>() {});
		report.logTestInfo("repo details extracted");
		assertEquals(res.getStatusCode(),HttpStatus.SC_OK,"response code is not matching");
		report.logTestInfo("response code verified");
		//String s=res.body().jsonPath().get("full_name");
		//System.out.println(s);
		assertEquals(getresponse.getName(), repo,"given repo is not in github");
		report.logTestInfo("full name of repo verified");
		JsonSchemaValidate.validateSchemaInClassPath(res, "expectedSchemas/StatusResponseSchema.json");
		Assert.assertEquals(res.header("Content-Type"), "application/json; charset=utf-8");
		report.logTestInfo("conent type verified");
	
		
	}
	
	@Test(priority=1,description="get a single repo negative case non existing repo ")
	public void getASingleRepoWithNonExistingName() {
		Response res=API.getASingleRepo(repo+"r");
		NonExistingRepoResponsePojo resp=res.as(new TypeRef<NonExistingRepoResponsePojo>() {});
		assertEquals(res.statusCode(),HttpStatus.SC_NOT_FOUND,"response code is not matching");
		//String s=res.getStatusLine();
		report.logTestInfo("response code verified");
		//res.prettyPrint();
		assertEquals(resp.getMessage(), "Not Found","didnt display the not found message");
		String s=res.body().jsonPath().get("message");
		assertEquals(s, "Not Found");
		report.logTestInfo("not found message verified");

		
	}
	
	@Test(priority=2,description="get all repo")
	public void getAllRepo() {
		
		Response response=API.getAllRepo();
		response.prettyPrint();
		
		//validate schema
		//JsonSchemaValidate.validateSchema(response.asPrettyString(), "expectedSchemas/StatusResponseSchema.json");
	
		//response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("expectedSchemas/StatusResponseSchema.json"));
		//status code check
		Assert.assertEquals(response.statusCode(),200);
		report.logTestInfo("status code verified");
		//total no of repositories
		int no=response.body().jsonPath().getInt("size()");
		System.out.println("total no of repositories:"+no);
		report.logTestInfo("no of repositories found");
		//Print only the repositories names which are public
		System.out.println("public repositories");
		List<String> repos=response.body().jsonPath().getList("findAll{it->it.private!=true}.name");
		for(String rep:repos) {
			System.out.println(rep);
		}
		report.logTestInfo("names of repositories extracted");
		
		//Validate response header has content-Type = application/json; charset=utf-8
		String contentType = response.header("Content-Type"); 
		System.out.println(contentType);
		
		Assert.assertEquals(response.header("Content-Type"), "application/json; charset=utf-8");
		report.logTestInfo("content type verified");
		
		
	}
	
	@Test(priority=3,description="create a repo")
	public void createARepo() {
		Response res=API.post("vidhya","restassured add data", "https://github.com", "true");
		System.out.println("new repo created");
		//assertEquals(res.getStatusCode(), 201);
		report.logTestInfo("status code verified");
		assertEquals(res.body().jsonPath().get("name"),"vidhya");
		report.logTestInfo("repo name updated");
		assertEquals(res.body().jsonPath().get("owner.login"), "BvidhyaVipin");
		report.logTestInfo("owner name verified");
		
		
		
	}
	
	@Test(priority=4,description="create a repo with existing name negative scenario")
	public void createARepoWithExixtingName() {
		Response res=API.post("vidhya","restassured add data", "https://github.com", "true");
		int code=res.getStatusCode();
		System.out.println(code);
		assertEquals(res.statusCode(), 422);
		report.logTestInfo("status code verified");
		//res.prettyPrint();
		String message=res.body().jsonPath().get("errors[0].message");
		//System.out.println("message"+message);
		assertEquals(message, "name already exists on this account");
		report.logTestInfo("message verified");
		
		
		
	}
	
	@Test(priority=5,description="update the repo")
	public void updateRepo(){
		Response response=API.update("vidhyaB", "After update","true");

		assertEquals(response.statusCode(), 200);
		report.logTestInfo("status code verified");
		String name=response.body().jsonPath().getString("name");
		assertEquals(name,"vidhyaB");
		report.logTestInfo("name verified");
		
	}
	
	@Test(priority=6,description="delete the repo")
	public void deleteRepo() {
		Response res=API.delete("vidhyaB");
		int e=res.getStatusCode();
		System.out.println(e);
		assertEquals(res.statusCode(), 204);
		report.logTestInfo("status code verified");
		System.out.println("deleted");
		
	}
	
	@Test(priority=7,description="delete the non existing repo negative scenario")
	public void deleteRepoWithNonExixtingName() {
		Response res=API.delete("vidhyaB");
		int e=res.getStatusCode();
		System.out.println(e);
		assertEquals(res.statusCode(), 404);
		report.logTestInfo("status code verified");
		//res.prettyPrint();
		
		
		
	}

}
