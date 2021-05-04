package com.waracle.cakemanager.web;

import com.waracle.cakemanager.service.CakeService;

import static org.mockito.ArgumentMatchers.any;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import org.mockito.InjectMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.waracle.cakemanager.model.Cake;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.ArrayList;

@SpringBootTest(classes = CakeController.class)
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
public class CakeControllerTest {

    private static final String GET_URI = "/cakes";
    private static final String GET_ID_URI = "/cakes/2";
    private static final String POST_URI = "/cakes";
    private static final String PUT_EXISTING_URI = "/cakes/1";
    private static final String PUT_NON_EXISTING_URI = "/cakes/2";
    private static final String DELETE_URI = "/cakes/2";

    private MockMvc mockMvc;

    @MockBean
    private CakeService cakeService;

    @InjectMocks
    private CakeController cakeController;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(cakeController).build();
    }

    @Test
    public void test_GetNoCakes() throws Exception {

        when(cakeService.getAllCakes()).thenReturn(new ArrayList<>());

        List<Cake> resultcakes = new ArrayList<>();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(GET_URI)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        String mvcResultContentString = mvcResult.getResponse().getContentAsString();
        resultcakes = mapper.readValue(mvcResultContentString, new TypeReference<List<Cake>>(){});
        assertEquals(0, resultcakes.size());

        verify(cakeService, times(1)).getAllCakes();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(GET_URI).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        String resultContentString = result.andReturn().getResponse().getContentAsString();
        resultcakes = mapper.readValue(resultContentString, new TypeReference<List<Cake>>(){});
        assertEquals(0, resultcakes.size());
    }

    @Test
    public void test_GetCakes() throws Exception {

        List<Cake> cakes = new ArrayList<>();
        Cake cake = new Cake( Long.parseLong("1"), "Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");
        cakes.add(cake);
        cake = new Cake(Long.parseLong("2"),"Butter", "Contains Butter", "ImageOfButterCake");
        cakes.add(cake);

        when(cakeService.getAllCakes()).thenReturn(cakes);

        List<Cake> resultcakes = new ArrayList<>();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(GET_URI)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        String mvcResultContentString = mvcResult.getResponse().getContentAsString();
        resultcakes = mapper.readValue(mvcResultContentString, new TypeReference<List<Cake>>(){});
        assertEquals(2, resultcakes.size());
        assertEquals("Fruit", resultcakes.get(0).getTitle());
        assertEquals("Butter", resultcakes.get(1).getTitle());

        verify(cakeService, times(1)).getAllCakes();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(GET_URI).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        String resultContentString = result.andReturn().getResponse().getContentAsString();
        resultcakes = mapper.readValue(resultContentString, new TypeReference<List<Cake>>(){});
        assertEquals(2, resultcakes.size());
        assertEquals("Fruit", resultcakes.get(0).getTitle());
        assertEquals("Butter", resultcakes.get(1).getTitle());
    }

    @Test
    public void test_FailedGetCakeByIdReturnsBadRequest() throws Exception {

        Cake cake = new Cake( Long.parseLong("1"), "Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");

        when(cakeService.getCakeById(Long.parseLong("1"))).thenReturn(cake);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(GET_ID_URI)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());

        verify(cakeService, times(1)).getCakeById(any(Long.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(GET_ID_URI).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void test_GetCakeById() throws Exception {

        Cake cake = new Cake(Long.parseLong("2"),"Butter", "Contains Butter", "ImageOfButterCake");

        when(cakeService.getCakeById(Long.parseLong("2"))).thenReturn(cake);

        Cake resultcake = null;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(GET_ID_URI)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        String mvcResultContentString = mvcResult.getResponse().getContentAsString();
        resultcake = mapper.readValue(mvcResultContentString, Cake.class);
        assertEquals("Butter", resultcake.getTitle());

        verify(cakeService, times(1)).getCakeById(any(Long.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(GET_ID_URI).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        String resultContentString = result.andReturn().getResponse().getContentAsString();
        resultcake = mapper.readValue(resultContentString, Cake.class);
        assertEquals("Butter", resultcake.getTitle());
    }

    @Test
    public void test_SaveCake() throws Exception {

        Cake cake = new Cake( Long.parseLong("1"), "Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");

        when(cakeService.saveCake(any(Cake.class))).thenReturn(cake);

        Cake resultcake = null;

        String cakeJson = mapper.writeValueAsString(cake);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(POST_URI).content(cakeJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        String mvcResultContentString = mvcResult.getResponse().getContentAsString();
        resultcake = mapper.readValue(mvcResultContentString, Cake.class);
        assertEquals("Fruit", resultcake.getTitle());

        verify(cakeService, times(1)).saveCake(any(Cake.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(POST_URI).content(cakeJson)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        String resultContentString = result.andReturn().getResponse().getContentAsString();
        resultcake = mapper.readValue(resultContentString, Cake.class);
        assertEquals("Fruit", resultcake.getTitle());
    }

    @Test
    public void test_UpdateExistingCake() throws Exception {

        Cake existingCake = new Cake( Long.parseLong("1"), "Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");

        when(cakeService.getCakeById(Long.parseLong("1"))).thenReturn(existingCake);
        when(cakeService.saveCake(any(Cake.class))).thenReturn(existingCake);

        Cake updateCake = new Cake(null,"Butter", "Contains Butter", "ImageOfButterCake");

        String updateCakeJson = mapper.writeValueAsString(updateCake);

        Cake resultcake = null;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(PUT_EXISTING_URI).content(updateCakeJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        String mvcResultContentString = mvcResult.getResponse().getContentAsString();
        resultcake = mapper.readValue(mvcResultContentString, Cake.class);
        assertEquals("Butter", resultcake.getTitle());

        verify(cakeService, times(1)).saveCake(any(Cake.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put(PUT_EXISTING_URI).content(updateCakeJson)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        String resultContentString = result.andReturn().getResponse().getContentAsString();
        resultcake = mapper.readValue(resultContentString, Cake.class);
        assertEquals("Butter", resultcake.getTitle());
    }

    @Test
    public void test_UpdateNonExistingCake() throws Exception {

        Cake existingCake = new Cake( Long.parseLong("1"), "Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");

        when(cakeService.getCakeById(Long.parseLong("1"))).thenReturn(existingCake);
        when(cakeService.saveCake(any(Cake.class))).thenReturn(existingCake);

        Cake updateCake = new Cake(null,"Butter", "Contains Butter", "ImageOfButterCake");

        String updateCakeJson = mapper.writeValueAsString(updateCake);

        Cake resultcake = null;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(PUT_NON_EXISTING_URI).content(updateCakeJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        String mvcResultContentString = mvcResult.getResponse().getContentAsString();
        resultcake = mapper.readValue(mvcResultContentString, Cake.class);
        assertEquals("Fruit", resultcake.getTitle());

        verify(cakeService, times(1)).saveCake(any(Cake.class));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put(PUT_NON_EXISTING_URI).content(updateCakeJson)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        String resultContentString = result.andReturn().getResponse().getContentAsString();
        resultcake = mapper.readValue(resultContentString, Cake.class);
        assertEquals("Fruit", resultcake.getTitle());
    }

    @Test
    public void test_DeleteCake() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_URI)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        verify(cakeService, times(1)).deleteCakeById(Long.parseLong("2"));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_URI));
        result.andExpect(status().isOk());
    }
}
