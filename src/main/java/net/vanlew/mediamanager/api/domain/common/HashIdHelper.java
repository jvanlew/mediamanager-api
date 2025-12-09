package net.vanlew.mediamanager.api.domain.common;

import org.sqids.Sqids;

import java.util.ArrayList;
import java.util.List;

public class HashIdHelper {
    private static final Integer hashLength = 5;

    public static String hashValidAlphabet = "abcdefghijklmnopqrstuvwxyz1234567890";

    private HashIdHelper() {
        // private constructor to prevent instantiation
    }

    public static String encode(Integer hashCode) {

        hashCode = Math.abs(hashCode);

        var sqid = new Sqids.Builder()
                .minLength(hashLength)
                .alphabet(hashValidAlphabet)
                .build();

        return sqid.encode(List.of(Long.valueOf(hashCode)));
    }

    public static String encode(List<Integer> hashCodes) {
        var sqid = new Sqids.Builder()
                .minLength(hashLength)
                .alphabet(hashValidAlphabet)
                .build();

        List<Long> longList = new ArrayList<>();
        for (Integer num : hashCodes) {
            longList.add(Math.abs(num.longValue()));
        }

        return sqid.encode(longList);
    }


    public static List<Long> decode(String hash) {
        var sqid = new Sqids.Builder()
                .minLength(hashLength)
                .alphabet(hashValidAlphabet)
                .build();

        return sqid.decode(hash);

    }
}