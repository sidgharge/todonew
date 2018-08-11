package com.bridgelabz.todo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.user.controllers.UserController;
import com.bridgelabz.todo.user.repositories.UserRepository;
import com.bridgelabz.todo.user.services.UserService;
import com.bridgelabz.todo.user.services.UserServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes= { TodoApplication.class})
public class TodoApplicationTests {
	
	private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext wac;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private Resource casesFile;
	
	private List<Json> cases;

	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        casesFile = new ClassPathResource("cases.json");
        
        cases = mapper.readValue(casesFile.getInputStream(), new TypeReference<List<Json>>() {});
	}

	@Test
	public void contextLoads() throws JsonProcessingException, Exception {
		for (Json json : cases) {
			ResultActions actions =mockMvc.perform(getTestMethod(json).contentType(MediaType.APPLICATION_JSON).content(getRequestBody(json))
					.accept(MediaType.APPLICATION_JSON));
			actions.andExpect(resultMatcher(json));
		}
	}
	
	private MockHttpServletRequestBuilder getTestMethod(Json json) {
		switch (json.getRequest().getMethod()) {
		case GET:
			return MockMvcRequestBuilders.get(json.getRequest().getUrl());
			
		case PUT:
			return MockMvcRequestBuilders.put(json.getRequest().getUrl());

		default:
			return MockMvcRequestBuilders.get(json.getRequest().getUrl());
		}
	}
	
	private String getRequestBody(Json json) throws JsonProcessingException {
		return mapper.writeValueAsString(json.getRequest().getBody());
	}
	
	private ResultMatcher resultMatcher(Json json) {
		switch (json.getResponse().getStatus()) {
		case OK:
			return status().isOk();

		default:
			return status().isOk();
		}
	}

}
