package uk.gov.hmcts.fees.register.api.configuration.security.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AudienceValidatorTest {

    private AudienceValidator validator;

    @Mock
    Jwt jwt;

    List<String> allowedAudience = new ArrayList<>();

    @Before
    public void setUp() {

        allowedAudience.add("fees");
        validator = new AudienceValidator(allowedAudience);
    }

    @Test
    public void shouldReturnSuccessWhenAudienceMatches() throws Exception {

        List<String> audienceList = new ArrayList<>();
        audienceList.add("paybubble");
        when(jwt.getAudience()).thenReturn(audienceList);
        OAuth2TokenValidatorResult validate = validator.validate(jwt);
        Assert.assertTrue(validate.hasErrors());

    }

    @Test
    public void shouldReturnFailureWhenAudienceNotMatches() throws Exception {

        List<String> audienceList = new ArrayList<>();
        audienceList.add("fees");
        when(jwt.getAudience()).thenReturn(audienceList);

        OAuth2TokenValidatorResult validate = validator.validate(jwt);
        Assert.assertFalse(validate.hasErrors());
    }

}

