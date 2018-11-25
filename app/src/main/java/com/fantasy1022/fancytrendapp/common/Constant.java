/*
 * Copyright 2017 Fantasy Fang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fantasy1022.fancytrendapp.common;

import android.support.v4.util.ArrayMap;

import java.util.Arrays;
import java.util.List;

/**
 * Created by fantasy1022 on 2017/2/8.
 */

public class Constant {
    public final static String GOOGLE_TREND_BASE_URL = "https://trends.google.com/trends/hottrends/visualize/";
    public final static String DEFAULT_COUNTRY_CODE = "12";//Taiwan //TODO: remove
    public final static String SP_DEFAULT_COUNTRY_KEY = "SP_DEFAULT_COUNTRY_KEY";
    public final static String SP_DEFAULT_COUNTRY_INDEX_KEY = "SP_DEFAULT_COUNTRY_INDEX_KEY";
    public final static String SP_DEFAULT_CLICK_BEHAVIOR_KEY = "SP_DEFAULT_CLICK_BEHAVIOR_KEY";
    public final static int DEFAULT_ROW_NUMBER = 3;
    public final static int DEFAULT_COLUMN_NUMBER = 3;
    public final static int DEFAULT_TREND_ITEM_NUMBER = DEFAULT_ROW_NUMBER * DEFAULT_COLUMN_NUMBER;
    private static ArrayMap<String, String> countryMap;


    public static void generateCountryCodeMapping() {
        countryMap = new ArrayMap<>();//(name,code)
        countryMap.put("United States", "1");
        countryMap.put("India", "3");
        countryMap.put("Japan", "4");
        countryMap.put("Singapore", "5");
        countryMap.put("Israel", "6");
        countryMap.put("Australia", "8");
        countryMap.put("United Kingdom", "9");
        countryMap.put("Hong Kong", "10");
        countryMap.put("Taiwan", "12");
        countryMap.put("Canada", "13");
        countryMap.put("Russia", "14");
        countryMap.put("Germany", "15");
        countryMap.put("France", "16");
        countryMap.put("Netherlands", "17");
        countryMap.put("Brazil", "18");
        countryMap.put("Indonesia", "19");
        countryMap.put("Mexico", "21");
        countryMap.put("South Korea", "23");
        countryMap.put("Turkey", "24");
        countryMap.put("Philippines", "25");
        countryMap.put("Spain", "26");
        countryMap.put("Italy", "27");
        countryMap.put("Vietnam", "28");
        countryMap.put("Egypt", "29");
        countryMap.put("Argentina", "30");
        countryMap.put("Poland", "31");
        countryMap.put("Colombia", "32");
        countryMap.put("Thailand", "33");
        countryMap.put("Malaysia", "34");
        countryMap.put("Ukraine", "35");
        countryMap.put("Saudi Arabia", "36");
        countryMap.put("Kenya", "37");
        countryMap.put("Chile", "38");
        countryMap.put("Romania", "39");
        countryMap.put("South Africa", "40");
        countryMap.put("Belgium", "41");
        countryMap.put("Sweden", "42");
        countryMap.put("Czech Republic", "43");
        countryMap.put("Austria", "44");
        countryMap.put("Hungary", "45");
        countryMap.put("Switzerland", "46");
        countryMap.put("Portugal", "47");
        countryMap.put("Greece", "48");
        countryMap.put("Denmark", "49");
        countryMap.put("Finland", "50");
        countryMap.put("Norway", "51");
        countryMap.put("Nigeria", "52");
    }

    public static String getCountryCode(String countryName) {
        return countryMap.get(countryName);
    }

    public static ArrayMap<String, List<String>> generateTrendMap() {//For test and mock using
        ArrayMap<String, List<String>> countryTrendMap = new ArrayMap<>();
        countryTrendMap.put("1", Arrays.asList(new String[]{"Powerball", "This Is Us", "Paul George"}));
        countryTrendMap.put("3", Arrays.asList(new String[]{"Exoplanet discovery", "Cricbuzz", "BMC election 2017"}));
        countryTrendMap.put("4", Arrays.asList(new String[]{"愛子さま", "明日の天気", "ほのかりん"}));
        countryTrendMap.put("5", Arrays.asList(new String[]{"Exoplanet discovery", "Tuas fire", "Europa League"}));
        countryTrendMap.put("6", Arrays.asList(new String[]{"רשת", "יעל אלמוג", "Kim Kardashian"}));
        countryTrendMap.put("8", Arrays.asList(new String[]{"penalty rates", "Bride and Prejudice", "AMD Ryzen"}));
        countryTrendMap.put("9", Arrays.asList(new String[]{"Cheryl Cole", "Lottery Result", "George Michael Funeral"}));
        countryTrendMap.put("10", Arrays.asList(new String[]{"发现系外行星", "Nasa", "張秀文"}));
        countryTrendMap.put("12", Arrays.asList(new String[]{"羅惠美", "泰雅渡假村", "成語蕎"}));
        countryTrendMap.put("13", Arrays.asList(new String[]{"Découverte D’exoplanètes", "Rockfest", "AMD Ryzen"}));
        countryTrendMap.put("14", Arrays.asList(new String[]{"Алексей Петренко", "Аида Гарифуллина", "Порту Ювентус"}));
        countryTrendMap.put("15", Arrays.asList(new String[]{"Franziska Wiese", "Jürgen Drews", "Unwetterwarnung"}));
        countryTrendMap.put("16", Arrays.asList(new String[]{"Catherine Griset", "Crif", "Saint Etienne Manchester"}));
        countryTrendMap.put("17", Arrays.asList(new String[]{"VID", "Range Rover Velar", "Nicolette Kluijver"}));
        countryTrendMap.put("18", Arrays.asList(new String[]{"Brit Awards 2017", "Exoplaneta descoberto", "Corinthians X Palmeiras Ao Vivo"}));
        countryTrendMap.put("19", Arrays.asList(new String[]{"Liga Spanyol", "Manchester United F.c.", "Na Hye Mi"}));
        countryTrendMap.put("21", Arrays.asList(new String[]{"Descubrimiento Exoplanetas", "Tigres Vs Pumas Concachampions", "Porto vs Juventus"}));
        countryTrendMap.put("23", Arrays.asList(new String[]{"-행성 발견", "23 아이덴티티", "나혜미"}));
        countryTrendMap.put("24", Arrays.asList(new String[]{"Ehliyet sınav sonuçları", "Diriliş Ertuğrul 79 Bölüm Fragmanı", "Fenerbahçe Krasnodar Özet"}));
        countryTrendMap.put("25", Arrays.asList(new String[]{"Exoplanet discovery", "Earthquake Today", "earthquake"}));
        countryTrendMap.put("26", Arrays.asList(new String[]{"Descubrimiento Exoplanetas", "Sevilla Leicester", "nueva actualización de Whatsapp"}));
        countryTrendMap.put("27", Arrays.asList(new String[]{"La Porta Rossa", "Siviglia Leicester", "Aggiornamento WhatsApp"}));
        countryTrendMap.put("28", Arrays.asList(new String[]{"Tuoi Thanh Xuan 2 Tap 30", "Giang Kim Đạt", "Na Hye Mi"}));
        countryTrendMap.put("29", Arrays.asList(new String[]{"ترتيب الدوري الاسباني", "عمر عبدالرحمن", "الدوري الاسباني"}));
        countryTrendMap.put("30", Arrays.asList(new String[]{"Camila", "Descubrimiento Exoplanetas", "Atanor"}));
        countryTrendMap.put("31", Arrays.asList(new String[]{"Brit Awards 2017", "Sevilla", "faworki"}));
        countryTrendMap.put("32", Arrays.asList(new String[]{"Descubrimiento Exoplanetas", "Valencia vs Real Madrid", "Juventus"}));
        countryTrendMap.put("33", Arrays.asList(new String[]{"สายป่าน", "วันอังคาร", "เมืองทอง"}));
        countryTrendMap.put("34", Arrays.asList(new String[]{"Exoplanet discovery", "Manchester United F.c.", "La Liga"}));
        countryTrendMap.put("35", Arrays.asList(new String[]{"23 Февраля Праздник", "Порту Ювентус", "Алексей Петренко"}));
        countryTrendMap.put("36", Arrays.asList(new String[]{"ناس", "تحديث الواتس اب", "يوفنتوس"}));
        countryTrendMap.put("37", Arrays.asList(new String[]{"Juventus", "Exoplanet discovery", "Manchester United"}));
        countryTrendMap.put("38", Arrays.asList(new String[]{"Lali Esposito", "Gaviota de Platino", "Peter Cetera"}));
        countryTrendMap.put("39", Arrays.asList(new String[]{"Europa League", "HOROSCOP 23 februarie 2017", "Iohannis"}));
        countryTrendMap.put("40", Arrays.asList(new String[]{"Exoplanet discovery", "Budget Speech 2017", "Cricket live scores"}));
        countryTrendMap.put("41", Arrays.asList(new String[]{"aarde-achtige planeten", "Ben Weyts", "Juventus"}));
        countryTrendMap.put("42", Arrays.asList(new String[]{"Nya Tider", "Klara Svensson", "Emmanuel Frimpong"}));
        countryTrendMap.put("43", Arrays.asList(new String[]{"Jawa", "AMD Ryzen", "Cssd"}));
        countryTrendMap.put("44", Arrays.asList(new String[]{"Münze Österreich", "Weiberfastnacht", "Ho Chi Minh"}));
        countryTrendMap.put("45", Arrays.asList(new String[]{"Porto Juventus", "Buffon", "Nasa Bejelentés"}));
        countryTrendMap.put("46", Arrays.asList(new String[]{"erdähnliche Planeten", "Iker Casillas", "Juventus"}));
        countryTrendMap.put("47", Arrays.asList(new String[]{"Herrera", "Brit Awards 2017", "Liga Espanhola"}));
        countryTrendMap.put("48", Arrays.asList(new String[]{"Νικοσ Γκαλησ", "Θεμησ Μανεσησ", "Κουνδουροσ"}));
        countryTrendMap.put("49", Arrays.asList(new String[]{"Viaplay", "Exoplanet Discovery", "Viafree"}));
        countryTrendMap.put("50", Arrays.asList(new String[]{"Kelly Brook", "Temptation Island", "Saara Aalto"}));
        countryTrendMap.put("51", Arrays.asList(new String[]{"Cecilia Brækhus", "Brit Awards 2017", "Nina Hjerpset-Østlie"}));
        countryTrendMap.put("52", Arrays.asList(new String[]{"Exoplanet discovery", "Man City vs Monaco", "Laliga"}));
        return countryTrendMap;
    }
}
