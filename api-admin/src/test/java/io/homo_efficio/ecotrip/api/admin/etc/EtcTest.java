package io.homo_efficio.ecotrip.api.admin.etc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-30
 */
public class EtcTest {

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

    @Test
    public void splitTest() {
        String[] s = "abc".split(" ");
        assertThat(s.length).isEqualTo(1);
        assertThat(s[0]).isEqualTo("abc");
    }

    @Test
    public void spaceSplit() {
        String[] split = ", ,".split(",");
        assertThat(split.length).isEqualTo(2);
        assertThat(split[0]).isEqualTo("");
        assertThat(split[1]).isEqualTo(" ");
    }

    @Test
    public void lineStartsWithNumber() {
        Pattern pattern = Pattern.compile("^[0-9]*,");

        assertThat(pattern.matcher("87,[수학여행]길").find()).isTrue();
        assertThat(pattern.matcher(" 전주는 조선왕").find()).isFalse();


    }

    @Test
    public void mergeLine() {
        List<String> lines = List.of(
                "87,[수학여행]길에서 놀며 배우는 자연학교여행,\"아동·청소년 체험학습,\",\"전라북도 무주군, 전주시, 부안군\",덕유산국립공원 숲속 탐방-금강 레프팅-마이산 역암동굴 탐방-변산반도국립공원 갯벌 탐방,\" 백두대간의 등줄기 덕유산에서 뻗어져 내려온 산줄기와 강줄기는 마이산에서 만나 산태극 수태극을 불러왔습니다. 1억 년 전의 호수였던 신비로운 마이산에서 돌탑과   역고드름 이야기는 직접 보아야만 느낄 수 있는 신비함을 간직한 자연생태체험입니다.   ",
                " 전주는 조선왕조 500년의 발상지이며 음식문화가 발달하고 전통이 살아 숨 쉬는   온고지신의 전통도시여행입니다. 이와 더불어 서해안 갯벌여행은 해양 생태계의 생   성지이고 바닷물과 흙을 빚어 새로운 자원탄생의 소중함을 느끼게 해주는 탐험여행   입니다. \"",
                "88,산과 바다가 함께하는 아름다운 동행,\"문화생태체험,농어촌생태체험,\",전라북도 부안,변산반도 국립공원의 아름다운 산과 바다 그리고 지역주민의 생활문화 체험 프로그램,\" 산과 바다가 아름다운 변산반도국립공원에서 숲체험, 바다체험과 더불어 바다와 들을 주 생활지로 살아가는 지역주민들의 생활문화 등 다양한 체험위주의 프로그램\"",
                "89,내장산과 함께하는 즐거운 여행(숙박형),자연생태체험,전라북도 정읍시 내장동 59-10 내장산국립공원,내장산 자연 속 트레킹과 함께 미션체험도 즐기시고 100년 전 초가마을에서 하룻밤을 보낼 수  있는 숙박형 프로그램입니다.,\" - 내장산 탐방안내소 관람",
                " - 자연놀이 및 나뭇잎티셔츠 만들기 체험",
                " - 숲 트레킹, 미션수행 ",
                " - 초가집에서 보내는 밤",
                "   (여치집만들기 체험 등)",
                " - 강정 만들기 체험\"",
                "90,내장산과 함께하는 즐거운 여행(당일형),건강나누리캠프,전라북도 정읍시 내장동 59-10 내장산국립공원,내장산의 자연을 느낄 수 있는 체험관광, 내장산국립공원의 자연을 느낄 수 있는 매우 유익한 체험관광 프로그램입니다."
        );

        Pattern pattern = Pattern.compile("^[0-9]*,");

        List<String> mergedLines = new ArrayList<>();
        StringBuilder prev = new StringBuilder();
        for (String line : lines) {
            if (!pattern.matcher(line).find()) {
                prev.append(line);
            } else {
                if (!StringUtils.isEmpty(prev.toString())) {
                    mergedLines.add(prev.toString());
                }
                prev = new StringBuilder(line);
            }
        }
        mergedLines.add(prev.toString());
        for (String mergedLine : mergedLines) {
            System.out.println(mergedLine);
        }

        assertThat(mergedLines.size()).isEqualTo(4);
    }


    @Disabled("긴 문자열에서 빈도수 부정확")
    @ParameterizedTest
    @MethodSource("details")
    public void stringTokenizrCount(String detail, int count) {
        StringTokenizer st1 = new StringTokenizer(detail, "문화");
        int finalCount = st1.countTokens();
        if (detail.startsWith("문화")) finalCount++;
        if (!detail.endsWith("문화")) finalCount--;
        assertThat(finalCount).isEqualTo(count);
    }

    @ParameterizedTest
    @MethodSource("details")
    public void splitCount(String detail, int count) {
        String[] split = detail.split("문화");
        int finalCount = split.length;
//        if (detail.startsWith("문화")) finalCount++;
        if (!detail.endsWith("문화")) finalCount--;
        assertThat(finalCount).isEqualTo(count);
    }

    private static Stream<Arguments> details() {
        return Stream.of(
                Arguments.of("문화-문화-문화", 3),
                Arguments.of("문화-문화-문화-", 3),
                Arguments.of("-문화-문화-문화", 3),
                Arguments.of("-문화-문화-문화-", 3),
                Arguments.of("수려한 경관의 치악산 국립공원은 강원권의 교통요지인 원주시에 인접해 있어 수도권과의 거리가 가깝습니다. 자연을 제대로 느낄 수 있는 국립공원 에코투어의 다양한 프로그램을 통해 색다른 수학여행을 즐길 수 있습니다. 문화가 있는 수학여행으로서 박경리 문학관과 고판화 박물관을 통해 다양한 전시물 관람과 판화 체험 등을 할 수 있습니다. 아름다운 자연과 함께 하고 싶다면 야영을, 체험형 숙박을 원한다면 템플스테이를, 지역주민의 따뜻한 마음을 느끼고 싶다면 민박을 선택 할 수 있는 숙박의 다양화가 구축되어 있습니다. 자연의 향기를 제대로 느끼고 색다른 체험을 원한다면 '자연과 문화가 있는 치악산 수학여행'에 참여해 보시길 바랍니다.", 2)
        );
    }
}
