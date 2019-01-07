package com.ecmp;

import com.ecmp.context.ContextUtil;
import com.ecmp.core.controller.CommonController;
import com.ecmp.log.util.LogUtil;
import com.ecmp.util.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * @author Vision.Mac
 * @version 1.0.1 2018/9/19 12:59
 */
@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class ApplicationTests {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private MockHttpSession session;

    @Before
    public void setUp() {
        String sessionId = IdGenerator.uuid2();
        ContextUtil.mockUser(sessionId);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        session = new MockHttpSession();
        session.setAttribute("sessionId", sessionId);

        LogUtil.debug("sessionId: {}", session.getAttribute("sessionId"));
        LogUtil.debug("SessionUser: {}", ContextUtil.getSessionUser());
    }

    @Test
    public void testHealth() throws Exception {
        session.clearAttributes();
        // 模拟发送请求
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/monitor/health")
                        .session(session))
                .andExpect(MockMvcResultMatchers.handler().handlerType(CommonController.class))
                .andExpect(MockMvcResultMatchers.handler().methodName(("health")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
