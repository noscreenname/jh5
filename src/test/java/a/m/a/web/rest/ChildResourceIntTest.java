package a.m.a.web.rest;

import a.m.a.Application;
import a.m.a.domain.Child;
import a.m.a.repository.ChildRepository;
import a.m.a.web.rest.dto.ChildDTO;
import a.m.a.web.rest.mapper.ChildMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ChildResource REST controller.
 *
 * @see ChildResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ChildResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Long DEFAULT_VALUE = 1L;
    private static final Long UPDATED_VALUE = 2L;

    @Inject
    private ChildRepository childRepository;

    @Inject
    private ChildMapper childMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restChildMockMvc;

    private Child child;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChildResource childResource = new ChildResource();
        ReflectionTestUtils.setField(childResource, "childRepository", childRepository);
        ReflectionTestUtils.setField(childResource, "childMapper", childMapper);
        this.restChildMockMvc = MockMvcBuilders.standaloneSetup(childResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        child = new Child();
        child.setName(DEFAULT_NAME);
        child.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createChild() throws Exception {
        int databaseSizeBeforeCreate = childRepository.findAll().size();

        // Create the Child
        ChildDTO childDTO = childMapper.childToChildDTO(child);

        restChildMockMvc.perform(post("/api/childs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(childDTO)))
                .andExpect(status().isCreated());

        // Validate the Child in the database
        List<Child> childs = childRepository.findAll();
        assertThat(childs).hasSize(databaseSizeBeforeCreate + 1);
        Child testChild = childs.get(childs.size() - 1);
        assertThat(testChild.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChild.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllChilds() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get all the childs
        restChildMockMvc.perform(get("/api/childs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(child.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())));
    }

    @Test
    @Transactional
    public void getChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

        // Get the child
        restChildMockMvc.perform(get("/api/childs/{id}", child.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(child.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingChild() throws Exception {
        // Get the child
        restChildMockMvc.perform(get("/api/childs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

		int databaseSizeBeforeUpdate = childRepository.findAll().size();

        // Update the child
        child.setName(UPDATED_NAME);
        child.setValue(UPDATED_VALUE);
        ChildDTO childDTO = childMapper.childToChildDTO(child);

        restChildMockMvc.perform(put("/api/childs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(childDTO)))
                .andExpect(status().isOk());

        // Validate the Child in the database
        List<Child> childs = childRepository.findAll();
        assertThat(childs).hasSize(databaseSizeBeforeUpdate);
        Child testChild = childs.get(childs.size() - 1);
        assertThat(testChild.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChild.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteChild() throws Exception {
        // Initialize the database
        childRepository.saveAndFlush(child);

		int databaseSizeBeforeDelete = childRepository.findAll().size();

        // Get the child
        restChildMockMvc.perform(delete("/api/childs/{id}", child.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Child> childs = childRepository.findAll();
        assertThat(childs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
