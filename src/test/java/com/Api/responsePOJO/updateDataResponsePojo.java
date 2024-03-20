package com.Api.responsePOJO;

import java.util.ArrayList;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder

@JsonInclude(JsonInclude.Include.NON_NULL)

public class updateDataResponsePojo {
	public class Error{
		@JsonProperty(value="resource")
	    public String resource;
		@JsonProperty(value="code")
	    public String code;
		@JsonProperty(value="field")
	    public String field;
		@JsonProperty(value="message")
	    public String message;
	}

	public class Root{
		@JsonProperty(value="message")
	    public String message;
		@JsonProperty(value="errors")
	    public ArrayList<Error> errors;
		@JsonProperty(value="documentation_url")
	    public String documentation_url;
	}

}
