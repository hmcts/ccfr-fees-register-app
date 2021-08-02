package uk.gov.hmcts.fees2.register.util;

import org.owasp.encoder.Encode;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

import java.util.Arrays;
import java.util.Objects;

public class URIUtils {

    private static String getRequestMappingPath(Class<?> clazz) {

        if (clazz.getAnnotation(RequestMapping.class) == null) {
            return "";
        }

        String[] reqMap = clazz.getAnnotation(RequestMapping.class).value();

        if (reqMap.length == 0){
            return "";
        }

        return reqMap[0];

    }

    public static String getUrlForPostMethod(Class<?> clazz, String methodName) {

        String url = getRequestMappingPath(clazz);

        String[] str = Arrays.stream(clazz.getMethods())
            .filter(method -> method.getName().equalsIgnoreCase(methodName))
            .map(method -> method.getAnnotation(PostMapping.class))
            .filter(Objects::nonNull)
            .map(PostMapping::value)
            .findFirst()
            .orElse(null);

        if (str == null || str.length == 0) {
            return url;
        }

        return url + str[0];

    }


    public static String getUrlForGetMethod(Class<?> clazz, String methodName) {

        String url = getRequestMappingPath(clazz);

        String[] str = Arrays.stream(clazz.getMethods())
            .filter(method -> method.getName().equalsIgnoreCase(methodName))
            .map(method -> method.getAnnotation(GetMapping.class))
            .filter(Objects::nonNull)
            .map(GetMapping::value)
            .findFirst()
            .orElse(null);

        if (str == null || str.length == 0) {
            return url;
        }

        return url + str[0];

    }

    public static String getUrlForDeleteMethod(Class<?> clazz, String methodName) {

        String url = getRequestMappingPath(clazz);

        String[] str = Arrays.stream(clazz.getMethods())
            .filter(method -> method.getName().equalsIgnoreCase(methodName))
            .map(method -> method.getAnnotation(DeleteMapping.class))
            .filter(Objects::nonNull)
            .map(DeleteMapping::value)
            .findFirst().orElse(null);

        if (str == null || str.length == 0) {
            return url;
        }

        return url + str[0];

    }

    public static FeeVersionDto encodeVersionDetails(final FeeVersionDto request) {

        request.setDescription(null != request.getDescription() ? Encode.forHtml(request.getDescription()) : "");
        request.setMemoLine(null != request.getMemoLine() ? Encode.forHtml(request.getMemoLine()) : "");
        request.setStatutoryInstrument(null != request.getStatutoryInstrument() ? Encode.forHtml(request.getStatutoryInstrument()) : "");
        request.setSiRefId(null != request.getSiRefId() ? Encode.forHtml(request.getSiRefId()) : "");
        request.setNaturalAccountCode(null != request.getNaturalAccountCode() ? Encode.forHtml(request.getNaturalAccountCode()) : "");
        request.setLastAmendingSi(null != request.getLastAmendingSi() ? Encode.forHtml(request.getLastAmendingSi()) : "");
        request.setConsolidatedFeeOrderName(null != request.getConsolidatedFeeOrderName() ? Encode.forHtml(request.getConsolidatedFeeOrderName()) : "");
        request.setReasonForUpdate(null != request.getReasonForUpdate() ? Encode.forHtml(request.getReasonForUpdate()) : "");

        return request;
    }
}
