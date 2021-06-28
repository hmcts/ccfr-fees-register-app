package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"local", "test"})
public class ReportControllerTest {

    MockMvc mockMvc;

    @MockBean
    private FeeSearchService feeSearchService;

    @MockBean
    private FeeDtoMapper feeDtoMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    /*@Test(expected = FeesException.class)
    public void testDownloadReport_FeesException() throws Exception {

        //when(feeSearchService.search(any(SearchFeeDto.class)))
        //      .thenThrow(new FeesException(any(Exception.class)));

        final ResultActions resultActions = mockMvc.perform(get("/report/download")
                .header("Authorization", "user")
                .accept(MediaType.APPLICATION_JSON));

        //Assert.assertEquals(400, resultActions.andReturn().getResponse().getStatus());
    }*/

    @Test
    public void testDownloadReport() throws Exception {

        final List<Fee> reportDataList = new ArrayList<>();

        when(feeSearchService.search(any(SearchFeeDto.class)))
                .thenReturn(reportDataList);


        final ResultActions resultActions = mockMvc.perform(get("/report/download")
                .header("Authorization", "user")
                .accept(MediaType.APPLICATION_JSON));

        Assert.assertEquals(200, resultActions.andReturn().getResponse().getStatus());
        Assert.assertEquals("application/vnd.ms-excel", resultActions.andReturn().getResponse().getContentType());
        Assert.assertTrue(resultActions.andReturn().getResponse().getHeaderValue("Content-Disposition").toString()
                .startsWith("attachment; filename=Fee_Register_"));

    }

}
