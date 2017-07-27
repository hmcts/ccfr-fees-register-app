package uk.gov.hmcts.fees.register.api.componenttests.sugar;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomResultMatcher implements ResultMatcher {

    private final ObjectMapper objectMapper;
    private final List<ResultMatcher> matchers = new ArrayList<>();

    public CustomResultMatcher(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CustomResultMatcher isEqualTo(Object expected) {
        matchers.add(result -> {
            Object actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), expected.getClass());
            assertThat(actual).isEqualTo(expected);
        });
        return this;
    }

    public ResultMatcher isEqualTo(Object expected, Class parameterizedType) {
        matchers.add(result -> {
            JavaType javaType = TypeFactory.defaultInstance().constructParametricType(expected.getClass(), parameterizedType);
            Object actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), javaType);
            assertThat(actual).isEqualTo(expected);
        });
        return this;
    }

    public ResultMatcher isListContaining(Class collectionType, Object... expectedItems) {
        matchers.add(result -> {
            JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, collectionType);
            List actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), javaType);
            assertThat(actual).contains(expectedItems);
        });
        return this;
    }

    @Override
    public void match(MvcResult result) throws Exception {
        for (ResultMatcher matcher : matchers) {
            matcher.match(result);
        }
    }
}
