package uk.gov.hmcts.fees2.register.util;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

public class URIUtils {

    private static String getRequestMappingPath(Class<?> clazz) {

        if (clazz.getAnnotation(RequestMapping.class) == null) {
            return "";
        }

        String reqMap[] = clazz.getAnnotation(RequestMapping.class).value();

        if (reqMap.length == 0) return "";

        return reqMap[0];

    }

    public static String getUrlForPostMethod(Class<?> clazz, String methodName) {

        String url = getRequestMappingPath(clazz);

        String[] str = Arrays.stream(clazz.getMethods())
            .filter(method -> method.getName().equalsIgnoreCase(methodName))
            .map(method -> method.getAnnotation(PostMapping.class))
            .filter(a -> a != null)
            .map(PostMapping::value)
            .findFirst()
            .get();

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
            .filter(a -> a != null)
            .map(GetMapping::value)
            .findFirst()
            .get();

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
            .filter(a -> a != null)
            .map(DeleteMapping::value)
            .findFirst()
            .get();

        if (str == null || str.length == 0) {
            return url;
        }

        return url + str[0];

    }

}
