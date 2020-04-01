package io.homo_efficio.ecotrip.api.admin.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-01
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KeywordAndFrquencyByKeywordDto {

    private String keyword;
    private int count;


    public static KeywordAndFrquencyByKeywordDto of(String keyword, int count) {
        return new KeywordAndFrquencyByKeywordDto(keyword, count);
    }
}
