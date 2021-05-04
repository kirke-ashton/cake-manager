package com.waracle.cakemanager.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cakemanager.model.Cake;

import com.waracle.cakemanager.service.CakeService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.waracle.cakemanager.web.CakeController;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestApplication.class, CakeController.class})
@ExtendWith(SpringExtension.class)
@Import(TestConfiguration.class)
@ActiveProfiles("waracle-integration-test")
public class CakeControllerIntegrationTest {

    private static final String GET_URI = "/cakes";
    private static final String POST_URI = "/cakes";
    private static final String PUT_URI = "/cakes/2";
    private static final String DELETE_URI = "/cakes/2";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CakeService cakeService;

    private MockMvc mockMvc;

    private CakeController cakeController;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        intDatabase();
    }

    @Test
    @WithUserDetails("global.access@waracle.io")
    @DirtiesContext
    public void test_GetCakes() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(GET_URI)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        String mvcResultContentString = mvcResult.getResponse().getContentAsString();
        List<Cake> resultcakes = mapper.readValue(mvcResultContentString, new TypeReference<List<Cake>>(){});
        assertEquals(3, resultcakes.size());
        assertEquals("Black Forest", resultcakes.get(0).getTitle());
        assertEquals("Butter", resultcakes.get(1).getTitle());
        assertEquals("Fruit", resultcakes.get(2).getTitle());
    }

    @Test
    @WithUserDetails("global.access@waracle.io")
    @DirtiesContext
    public void test_GetCakeById() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(GET_URI)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        String mvcResultContentString = mvcResult.getResponse().getContentAsString();
        List<Cake> resultcakes = mapper.readValue(mvcResultContentString, new TypeReference<List<Cake>>(){});
        assertEquals(3, resultcakes.size());
        assertEquals("Black Forest", resultcakes.get(0).getTitle());
        assertEquals("Butter", resultcakes.get(1).getTitle());
        assertEquals("Fruit", resultcakes.get(2).getTitle());

        String getCakeByIdUri = "/cakes/" + resultcakes.get(2).getId();

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(getCakeByIdUri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        mvcResultContentString = mvcResult.getResponse().getContentAsString();

        Cake resultcake = mapper.readValue(mvcResultContentString, Cake.class);
        assertEquals("Fruit", resultcake.getTitle());
    }

    @Test
    @WithUserDetails("global.admin@waracle.io")
    @DirtiesContext
    public void test_SaveCake() throws Exception {

        Cake cake = new Cake(null, "Orange", "Contains Orange", "ImageOfOrangeCake");

        String cakeJson = mapper.writeValueAsString(cake);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(POST_URI).content(cakeJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        String mvcResultContentString = mvcResult.getResponse().getContentAsString();
        Cake resultcake = mapper.readValue(mvcResultContentString, Cake.class);
        assertEquals("Orange", resultcake.getTitle());

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(GET_URI)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        mvcResultContentString = mvcResult.getResponse().getContentAsString();
        List<Cake> resultcakes = mapper.readValue(mvcResultContentString, new TypeReference<List<Cake>>(){});
        assertEquals(4, resultcakes.size());
        assertEquals("Black Forest", resultcakes.get(0).getTitle());
        assertEquals("Butter", resultcakes.get(1).getTitle());
        assertEquals("Fruit", resultcakes.get(2).getTitle());
        assertEquals("Orange", resultcakes.get(3).getTitle());
    }

    @Test
    @WithUserDetails("global.admin@waracle.io")
    @DirtiesContext
    public void test_UpdateExistingCake() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(GET_URI)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        String mvcResultContentString = mvcResult.getResponse().getContentAsString();
        List<Cake> resultcakes = mapper.readValue(mvcResultContentString, new TypeReference<List<Cake>>(){});
        assertEquals(3, resultcakes.size());
        assertEquals("Black Forest", resultcakes.get(0).getTitle());
        assertEquals("Butter", resultcakes.get(1).getTitle());
        assertEquals("Fruit", resultcakes.get(2).getTitle());

        Cake buttercake = resultcakes.get(1);
        buttercake.setTitle("Butterfly");

        String updateCakeJson = mapper.writeValueAsString(buttercake);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(PUT_URI).content(updateCakeJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        mvcResultContentString = mvcResult.getResponse().getContentAsString();
        Cake resultcake = mapper.readValue(mvcResultContentString, Cake.class);
        assertEquals("Butterfly", resultcake.getTitle());
        assertEquals(buttercake.getId(), resultcake.getId());

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(GET_URI)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        mvcResultContentString = mvcResult.getResponse().getContentAsString();
        resultcakes = mapper.readValue(mvcResultContentString, new TypeReference<List<Cake>>(){});
        assertEquals(3, resultcakes.size());
        assertEquals("Black Forest", resultcakes.get(0).getTitle());
        assertEquals("Butterfly", resultcakes.get(1).getTitle());
        assertEquals("Fruit", resultcakes.get(2).getTitle());
    }

    @Test
    @WithUserDetails("global.admin@waracle.io")
    @DirtiesContext
    public void test_DeleteCake() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_URI)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(GET_URI)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        String mvcResultContentString = mvcResult.getResponse().getContentAsString();
        List<Cake> resultcakes = mapper.readValue(mvcResultContentString, new TypeReference<List<Cake>>(){});
        assertEquals(2, resultcakes.size());
        assertEquals("Black Forest", resultcakes.get(0).getTitle());
        assertEquals("Fruit", resultcakes.get(1).getTitle());
    }

    private void intDatabase() throws Exception {

        List<Cake> cakes = cakeService.getAllCakes();

        cakes.stream().forEach(cake -> cakeService.deleteCakeById(cake.getId()));

        cakeService.saveCake(new Cake(null, "Fruit", "Contains Multiple Fruits", "ImageOfFruitCake"));
        cakeService.saveCake(new Cake(null, "Butter", "Contains Butter", "ImageOfButterCake"));
        cakeService.saveCake(new Cake(null,"Black Forest", "Contains Black Forest", "ImageOfBlackForestCake"));
    }
}