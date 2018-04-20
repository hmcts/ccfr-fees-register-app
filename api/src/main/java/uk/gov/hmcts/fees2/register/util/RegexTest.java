package uk.gov.hmcts.fees2.register.util;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    public static void main(String[] args) {
        String number = "FEE0001";

        Pattern pattern = Pattern.compile("^(.*)[^\\d](\\d+)(.*?)$");
        Matcher matcher = pattern.matcher(number);

        if (matcher.find()) {
            //System.out.println(matcher.group(1));
            System.out.println(new Integer(matcher.group(2)));
            //System.out.println(matcher.group(3));
        }

        //System.out.println(StringUtils.leftPad(1+"", 4, "0"));
    }
}
