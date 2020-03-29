package io.homo_efficio.ecotrip.api.admin.etc;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-30
 */
public class RegExpTest {

    @Test
    public void doubleQuotedStrings() {
        String ecoProgramInfo = "1,자연과 문화를 함께 즐기는 설악산 기행,\"문화생태체험,자연생태체험,\",강원도 속초,\"설악산 탐방안내소, 신흥사, 권금성, 비룡폭포\",\" 설악산은 왜 설악산이고, 신흥사는 왜 신흥사일까요? 설악산에 대해 정확히 알고, 배우고, 느낄 수 있는 당일형 생태관광입니다.\"";

        Pattern pattern = Pattern.compile("(\"[^\"]*\")");
        Matcher matcher = pattern.matcher(ecoProgramInfo);

        while (matcher.find()) {
            System.out.println(matcher.group());
            System.out.println(matcher.group().replace("\"", "").replace(",", "=:=:=").trim());
        }
    }

    @Test
    public void replaceDoubleQuoted() {
        String ecoProgramInfo = "1,자연과 문화를 함께 즐기는 설악산 기행,\"문화생태체험,자연생태체험,\",강원도 속초,\"설악산 탐방안내소, 신흥사, 권금성, 비룡폭포\",\" 설악산은 왜 설악산이고, 신흥사는 왜 신흥사일까요? 설악산에 대해 정확히 알고, 배우고, 느낄 수 있는 당일형 생태관광입니다.\"";

        Pattern pattern = Pattern.compile("(\"[^\"]*\")");
        Matcher matcher = pattern.matcher(ecoProgramInfo);

        List<String> olds = new ArrayList<>();
        List<String> news = new ArrayList<>();
        while (matcher.find()) {
            olds.add(matcher.group());
            news.add(matcher.group().replace("\"", "").replace(",", "=:=:=").trim());
        }

        for (int i = 0; i < olds.size(); i++) {
            ecoProgramInfo = ecoProgramInfo.replace(olds.get(i), news.get(i));
        }

        System.out.println(ecoProgramInfo);

        String[] split = ecoProgramInfo.split(",");
        assertThat(split.length).isEqualTo(6);
    }
}
