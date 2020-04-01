package io.homo_efficio.ecotrip.api.admin.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-01
 */
@Component
public class EcoProgramParser {

    public static final String COMMA_REPLACER = "=:=:=";
    public static final String DOUBLE_COMMA_REPLACER_COMMA_REMOVED = ":+:+:+:";
    public static final String DOUBLE_COMMA_REPLACER = ",:+:+:+:,";


    List<String> getMergedLines(List<String> lines) {
        Pattern pattern = Pattern.compile("^[0-9]*,");

        List<String> mergedLines = new ArrayList<>();
        StringBuilder prev = new StringBuilder();
        for (String line : lines) {
            if (!pattern.matcher(line).find()) {
                prev.append(line);
            } else {
                if (!StringUtils.isEmpty(prev.toString()) && pattern.matcher(prev).find()) {
                    mergedLines.add(prev.toString());
                }
                prev = new StringBuilder(line);
            }
        }
        if (pattern.matcher(prev).find()) {
            mergedLines.add(prev.toString());
        }
        return mergedLines;
    }

    ParsedEcoProgram parseProgram(String ecoProgramInfo) {
        List<String> cols = new ArrayList<>();

        String doubleCommaReplaced = ecoProgramInfo.replace(",,", DOUBLE_COMMA_REPLACER);
        StringTokenizer st = new StringTokenizer(quotProcessed(doubleCommaReplaced), ",");
        while (st.hasMoreTokens()) {
            cols.add(commaRecovered(st.nextToken()));
        }
        return new ParsedEcoProgram(cols.get(1), cols.get(2), cols.get(3), cols.get(4), cols.size() < 6 ? "" : cols.get(5));
    }

    private String quotProcessed(String ecoProgramInfo) {
        Pattern pattern = Pattern.compile("(\"[^\"]*\")");
        Matcher matcher = pattern.matcher(ecoProgramInfo);

        List<String> olds = new ArrayList<>();
        List<String> news = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group();
            olds.add(group);
            news.add(group
                    .replace("\"", "")
                    .replace(",", COMMA_REPLACER)
                    .trim());
        }

        for (int i = 0; i < olds.size(); i++) {
            ecoProgramInfo = ecoProgramInfo.replace(olds.get(i), news.get(i));
        }
        return ecoProgramInfo;
    }

    private String commaRecovered(String s) {
        return s.replace(COMMA_REPLACER, ",")
                .replace(DOUBLE_COMMA_REPLACER_COMMA_REMOVED, "");
    }

    @Getter
    @AllArgsConstructor
    static class ParsedEcoProgram {
        private String name;
        private String theme;
        private String regionName;
        private String description;
        private String detail;
    }
}
