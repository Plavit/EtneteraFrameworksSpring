package com.etnetera.hr;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Class used for Spring Boot/MVC based tests.
 *
 * @author Etnetera
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
//@TestMethodOrder
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JavaScriptFrameworkTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private JavaScriptFrameworkRepository repository;

    public void prepareData() throws Exception {
        JavaScriptFramework react = new JavaScriptFramework("ReactJS");
        JavaScriptFramework vue = new JavaScriptFramework("Vue.js");

        repository.save(react);
        repository.save(vue);

        //print contents after test setup to verify status
        System.out.println("Repository contents after preparation:\n" + repository.findAll() + "\nEOL");
    }

    @Test
    public void test1_frameworksTest() throws Exception {
        prepareData();

        mockMvc.perform(get("/frameworks")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("ReactJS")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Vue.js")));
    }

    @Test
    public void test2_addFrameworkInvalid() throws JsonProcessingException, Exception {
        JavaScriptFramework framework = new JavaScriptFramework();
        mockMvc.perform(post("/add").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].message", is("NotEmpty")));

        framework.setName("verylongnameofthejavascriptframeworkjavaisthebest");
        mockMvc.perform(post("/add").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].message", is("Size")));

    }

    /**
     * Newly added tests
     */

    //Tests functionality - delete Framework
    @Test
    public void test3_deleteTest() throws Exception {
        JavaScriptFramework raphael = new JavaScriptFramework("RaphaelJS");
        repository.save(raphael);
        System.out.println("Repository contents after delprep:\n" + repository.findAll() + "\nEOL");

        mockMvc.perform(get("/frameworks")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("RaphaelJS")));

        mockMvc.perform(delete("/delete/3")).andExpect(status().isOk());

        mockMvc.perform(get("/frameworks")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("ReactJS")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Vue.js")));


    }

    //Tests functionality - search by name
    @Test
    public void test4_searchByName() throws Exception {
        System.out.println("Repository contents at searchByName: \n" + repository.findAll() + "\nEOL");

        mockMvc.perform(get("/findByName/ReactJS").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("ReactJS")));

    }

    //Tests functionality - update
    @Test
    public void test5_update() throws Exception {
        System.out.println("Repository contents at update: \n" + repository.findAll() + "\nEOL");

        Optional<JavaScriptFramework> frameworkOptional = repository.findById(1L);
        assert (frameworkOptional.isPresent());

        JavaScriptFramework newFW = frameworkOptional.get();
        newFW.setName("Angular");

        mockMvc.perform(post("/update")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(newFW)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/findByName/Angular").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Angular")))
                .andExpect(jsonPath("$.id", is(1)));

        System.out.println("Repository contents after update: \n" + repository.findAll() + "\nEOL");
    }

    //Tests functionality - add version
    @Test
    public void test6_addVersion() throws Exception {


        System.out.println("Repository contents before addVersion:\n" + repository.findAll() + "\nEOL");

        JavaScriptFrameworkVersion version = new JavaScriptFrameworkVersion("2.0");
        version.setHype(100);
        //todo fix versions passing - probably needs context resolver
//        version.setDeprecationDate(LocalDate.now());

        System.out.println("Version to be written:" + version);

        mockMvc.perform(post("/addVersion/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsBytes(version)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/frameworks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].versions", hasSize(1)))
                .andExpect(jsonPath("$[0].versions[0].version").value("2.0"))
//                .andExpect(jsonPath("$[0].versions[0].deprecationDate").value(LocalDate.now()))
                .andExpect(jsonPath("$[0].versions[0].hype").value(100));
    }

    //Tests functionality - invalid version addition
    @Test(expected = org.springframework.web.util.NestedServletException.class)
    public void test7_addVersionInvalid() throws Exception {
        JavaScriptFrameworkVersion version = new JavaScriptFrameworkVersion();
        System.out.println(version);
//		version.setDeprecationDate(LocalDate.now());

        //invalid name null

        mockMvc.perform(post("/addVersion/1")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(version)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("versionName")))
                .andExpect(jsonPath("$.errors[0].message", is("NotEmpty")));

        version.setVersion("verylongnameohlalathisshouldprobablynotbepermittednowshouldit");

        //invalid long name
        mockMvc.perform(post("/addVersion/1")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(version)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("versionName")))
                .andExpect(jsonPath("$.errors[0].message", is("Size")))
                .andExpect(status().isBadRequest());


        //no such FW found
        version.setVersion("180.20");
        mockMvc.perform(post("/addVersion/110")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(version)))
                .andExpect(status().isBadRequest());


    }

}
