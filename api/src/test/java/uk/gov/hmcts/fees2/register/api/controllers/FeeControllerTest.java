package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by tarun on 02/11/2017.
 */


public class FeeControllerTest extends BaseTest {

    /**
     *
     * @throws Exception
     */
    @Test
    public void createFee() throws Exception{

        restActions
            .withUser("admin")
            .post("/fee", getRangedFeeDto())
            .andExpect(status().isCreated());

    }
}
