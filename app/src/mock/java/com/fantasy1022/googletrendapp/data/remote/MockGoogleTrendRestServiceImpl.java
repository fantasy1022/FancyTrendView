package com.fantasy1022.googletrendapp.data.remote;

import android.support.v4.util.ArrayMap;

import com.fantasy1022.googletrendapp.injection.Injection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by fantasy1022 on 2017/2/22.
 */

public class MockGoogleTrendRestServiceImpl implements GoogleTrendRestService {
    private static ArrayMap<String, List<String>> countryMap;
    private static Single dummyGoogleTrendResult = null;

    public MockGoogleTrendRestServiceImpl() {
        countryMap = Injection.generateTrendMap();
    }

    @Override
    public Single<ArrayMap<String, List<String>>> getGoogleTrend() {
        if (dummyGoogleTrendResult != null) {
            return dummyGoogleTrendResult;
        }
        return Single.just(countryMap);
    }

    public static void setDummyGoogleTrendResult(Single result) {
        dummyGoogleTrendResult = result;
    }

    public static ArrayList<List<String>> getMockTrendList() {
        ArrayList<List<String>> re = new ArrayList<>();
        re.add(Arrays.asList(new String[]{"一二三四五六七八九十你好嗎就四棒耶啦出去玩阿里山六七八九十你好嗎就四棒耶啦出去玩阿里山"}));////"kobe", "周杰倫", "kkbox", "籃球", "棒球", "小小兵", "魯夫", "apple", "雞年"
        return re;
    }
}
