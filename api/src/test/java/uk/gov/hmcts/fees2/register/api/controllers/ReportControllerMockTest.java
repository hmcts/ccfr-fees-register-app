package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;
import uk.gov.hmcts.fees2.register.util.UtilityTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class ReportControllerMockTest {

    private MockMvc mockMvc;

    @Mock
    private FeeSearchService feeSearchService;

    @Mock
    private FeeDtoMapper feeDtoMapper;

    @InjectMocks
    private ReportController reportController;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    public void testDownloadReportBlank() throws Exception {

        final ResultActions resultActions = mockMvc.perform(get("/report/download")
                .accept(MediaType.APPLICATION_JSON));

        Assert.assertEquals(200, resultActions.andReturn().getResponse().getStatus());
        Assert.assertEquals("application/vnd.ms-excel", resultActions.andReturn().getResponse().getContentType());
        Assert.assertTrue(resultActions.andReturn().getResponse().getHeaderValue("Content-Disposition").toString()
                .startsWith("attachment; filename=Fee_Register_"));

    }

    @Test
    public void testDownloadReport() throws Exception {

        final List<Fee> reportDataList = new ArrayList<>();

        reportDataList.add(UtilityTest.buildFixedFee());

        when(feeDtoMapper.toFeeDto(any(Fee.class))).thenReturn(UtilityTest.buildFee2Dto());

        when(feeSearchService.search(any(SearchFeeDto.class))).thenReturn(reportDataList);

        final ResultActions resultActions = mockMvc.perform(get("/report/download")
                .accept(MediaType.APPLICATION_JSON));

        Assert.assertEquals(200, resultActions.andReturn().getResponse().getStatus());
        Assert.assertEquals("application/vnd.ms-excel", resultActions.andReturn().getResponse().getContentType());
        Assert.assertTrue(resultActions.andReturn().getResponse().getHeaderValue("Content-Disposition").toString()
                .startsWith("attachment; filename=Fee_Register_"));

        Assert.assertTrue(resultActions.andReturn().getResponse().getOutputStream().isReady());

    }

}
