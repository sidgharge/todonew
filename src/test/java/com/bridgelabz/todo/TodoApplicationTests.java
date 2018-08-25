//package com.bridgelabz.todo;
//
//import static org.junit.Assert.assertEquals;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(classes= { TodoApplication.class})
//public class TodoApplicationTests {
//	
//	private MockMvc mockMvc;
//
//	@Autowired
//	private WebApplicationContext wac;
//
//	private ObjectMapper mapper = new ObjectMapper();
//
//	private Resource casesFile;
//
//	private List<TestCase> cases;
//
//	@Before
//	public void setup() throws JsonParseException, JsonMappingException, IOException {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//		casesFile = new ClassPathResource("cases.json");
//
//		cases = mapper.readValue(casesFile.getInputStream(), new TypeReference<List<TestCase>>() {
//		});
//	}
//
//	@Test
//	public void contextLoads() throws JsonProcessingException, Exception {
//		for (TestCase testCase : cases) {
//			ResultActions actions = mockMvc.perform(
//					getMethod(testCase).headers(testCase.getRequest().getHeaders()).contentType(MediaType.APPLICATION_JSON)
//							.content(getRequestBody(testCase)).accept(MediaType.APPLICATION_JSON));
//
//			actions.andExpect(status().is(testCase.getResponse().getStatus().value()));
//
//			MockHttpServletResponse response = actions.andReturn().getResponse();
//
//			for (String key : testCase.getResponse().getHeaders().keySet()) {
//				assertEquals(testCase.getResponse().getHeaders().get(key), response.getHeader(key));
//			}
//			assertEquals(getResponseBody(testCase), response.getContentAsString());
//		}
//	}
//
//	private MockHttpServletRequestBuilder getMethod(TestCase testCase) {
//		return MockMvcRequestBuilders.request(HttpMethod.resolve(testCase.getRequest().getMethod()),
//				testCase.getRequest().getUrl());
//	}
//
//	private String getRequestBody(TestCase testCase) throws JsonProcessingException {
//		return mapper.writeValueAsString(testCase.getRequest().getBody());
//	}
//
//	private String getResponseBody(TestCase testCase) throws JsonProcessingException {
//		return mapper.writeValueAsString(testCase.getResponse().getBody());
//	}
//
//}
