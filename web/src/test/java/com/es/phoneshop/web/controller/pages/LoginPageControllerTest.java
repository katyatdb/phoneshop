package com.es.phoneshop.web.controller.pages;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class LoginPageControllerTest {
    private static final String loginUrl = "/login";
    private MockMvc mockMvc;

    @InjectMocks
    private LoginPageController loginPageController;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginPageController)
                .setSingleView(new InternalResourceView("/WEB-INF/pages/login.jsp"))
                .build();
    }

    @Test
    public void testShowLoginPage() throws Exception {
        mockMvc.perform(get(loginUrl))
                .andExpect(view().name("login"));
    }
}
