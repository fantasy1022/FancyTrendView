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

package com.fantasy1022.fancytrendapp.common

import android.support.v4.util.ArrayMap
import java.util.*

/**
 * Created by fantasy1022 on 2017/2/8.
 */

object Constant {
    const val GOOGLE_TREND_BASE_URL = "https://trends.google.com/trends/hottrends/visualize/"
    const val SP_DEFAULT_COUNTRY_INDEX_KEY = "SP_DEFAULT_COUNTRY_INDEX_KEY"
    const val SP_DEFAULT_COUNTRY_NAME_KEY = "SP_DEFAULT_COUNTRY_NAME_KEY"
    const val DEFAULT_ROW_NUMBER = 3
    const val DEFAULT_COLUMN_NUMBER = 3
    const val DEFAULT_TREND_ITEM_NUMBER = DEFAULT_ROW_NUMBER * DEFAULT_COLUMN_NUMBER
    private lateinit var countryMap: MutableMap<String, String>

    //TODO:Check test
    fun generateCountryCodeMapping() {
        //TODO:need remove
        countryMap = mutableMapOf()//(name,code)
        countryMap["United States"] = "1"
        countryMap["India"] = "3"
        countryMap["Japan"] = "4"
        countryMap["Singapore"] = "5"
        countryMap["Israel"] = "6"
        countryMap["Australia"] = "8"
        countryMap["United Kingdom"] = "9"
        countryMap["Hong Kong"] = "10"
        countryMap["Taiwan"] = "12"
        countryMap["Canada"] = "13"
        countryMap["Russia"] = "14"
        countryMap["Germany"] = "15"
        countryMap["France"] = "16"
        countryMap["Netherlands"] = "17"
        countryMap["Brazil"] = "18"
        countryMap["Indonesia"] = "19"
        countryMap["Mexico"] = "21"
        countryMap["South Korea"] = "23"
        countryMap["Turkey"] = "24"
        countryMap["Philippines"] = "25"
        countryMap["Spain"] = "26"
        countryMap["Italy"] = "27"
        countryMap["Vietnam"] = "28"
        countryMap["Egypt"] = "29"
        countryMap["Argentina"] = "30"
        countryMap["Poland"] = "31"
        countryMap["Colombia"] = "32"
        countryMap["Thailand"] = "33"
        countryMap["Malaysia"] = "34"
        countryMap["Ukraine"] = "35"
        countryMap["Saudi Arabia"] = "36"
        countryMap["Kenya"] = "37"
        countryMap["Chile"] = "38"
        countryMap["Romania"] = "39"
        countryMap["South Africa"] = "40"
        countryMap["Belgium"] = "41"
        countryMap["Sweden"] = "42"
        countryMap["Czech Republic"] = "43"
        countryMap["Austria"] = "44"
        countryMap["Hungary"] = "45"
        countryMap["Switzerland"] = "46"
        countryMap["Portugal"] = "47"
        countryMap["Greece"] = "48"
        countryMap["Denmark"] = "49"
        countryMap["Finland"] = "50"
        countryMap["Norway"] = "51"
        countryMap["Nigeria"] = "52"
    }

    //TODO:Remove
    fun getCountryCode(countryName: String): String {
        return countryMap[countryName] ?: ""
    }

    //TODO:Modify test data
    fun generateTrendMap(): ArrayMap<String, List<String>> {//For test and mock using
        val countryTrendMap = ArrayMap<String, List<String>>()
        countryTrendMap["1"] = Arrays.asList(*arrayOf("Powerball", "This Is Us", "Paul George"))
        countryTrendMap["3"] = Arrays.asList(*arrayOf("Exoplanet discovery", "Cricbuzz", "BMC election 2017"))
        countryTrendMap["4"] = Arrays.asList(*arrayOf("愛子さま", "明日の天気", "ほのかりん"))
        countryTrendMap["5"] = Arrays.asList(*arrayOf("Exoplanet discovery", "Tuas fire", "Europa League"))
        countryTrendMap["6"] = Arrays.asList(*arrayOf("רשת", "יעל אלמוג", "Kim Kardashian"))
        countryTrendMap["8"] = Arrays.asList(*arrayOf("penalty rates", "Bride and Prejudice", "AMD Ryzen"))
        countryTrendMap["9"] = Arrays.asList(*arrayOf("Cheryl Cole", "Lottery Result", "George Michael Funeral"))
        countryTrendMap["10"] = Arrays.asList(*arrayOf("发现系外行星", "Nasa", "張秀文"))
        countryTrendMap["12"] = Arrays.asList(*arrayOf("羅惠美", "泰雅渡假村", "成語蕎"))
        countryTrendMap["13"] = Arrays.asList(*arrayOf("Découverte D’exoplanètes", "Rockfest", "AMD Ryzen"))
        countryTrendMap["14"] = Arrays.asList(*arrayOf("Алексей Петренко", "Аида Гарифуллина", "Порту Ювентус"))
        countryTrendMap["15"] = Arrays.asList(*arrayOf("Franziska Wiese", "Jürgen Drews", "Unwetterwarnung"))
        countryTrendMap["16"] = Arrays.asList(*arrayOf("Catherine Griset", "Crif", "Saint Etienne Manchester"))
        countryTrendMap["17"] = Arrays.asList(*arrayOf("VID", "Range Rover Velar", "Nicolette Kluijver"))
        countryTrendMap["18"] = Arrays.asList(*arrayOf("Brit Awards 2017", "Exoplaneta descoberto", "Corinthians X Palmeiras Ao Vivo"))
        countryTrendMap["19"] = Arrays.asList(*arrayOf("Liga Spanyol", "Manchester United F.c.", "Na Hye Mi"))
        countryTrendMap["21"] = Arrays.asList(*arrayOf("Descubrimiento Exoplanetas", "Tigres Vs Pumas Concachampions", "Porto vs Juventus"))
        countryTrendMap["23"] = Arrays.asList(*arrayOf("-행성 발견", "23 아이덴티티", "나혜미"))
        countryTrendMap["24"] = Arrays.asList(*arrayOf("Ehliyet sınav sonuçları", "Diriliş Ertuğrul 79 Bölüm Fragmanı", "Fenerbahçe Krasnodar Özet"))
        countryTrendMap["25"] = Arrays.asList(*arrayOf("Exoplanet discovery", "Earthquake Today", "earthquake"))
        countryTrendMap["26"] = Arrays.asList(*arrayOf("Descubrimiento Exoplanetas", "Sevilla Leicester", "nueva actualización de Whatsapp"))
        countryTrendMap["27"] = Arrays.asList(*arrayOf("La Porta Rossa", "Siviglia Leicester", "Aggiornamento WhatsApp"))
        countryTrendMap["28"] = Arrays.asList(*arrayOf("Tuoi Thanh Xuan 2 Tap 30", "Giang Kim Đạt", "Na Hye Mi"))
        countryTrendMap["29"] = Arrays.asList(*arrayOf("ترتيب الدوري الاسباني", "عمر عبدالرحمن", "الدوري الاسباني"))
        countryTrendMap["30"] = Arrays.asList(*arrayOf("Camila", "Descubrimiento Exoplanetas", "Atanor"))
        countryTrendMap["31"] = Arrays.asList(*arrayOf("Brit Awards 2017", "Sevilla", "faworki"))
        countryTrendMap["32"] = Arrays.asList(*arrayOf("Descubrimiento Exoplanetas", "Valencia vs Real Madrid", "Juventus"))
        countryTrendMap["33"] = Arrays.asList(*arrayOf("สายป่าน", "วันอังคาร", "เมืองทอง"))
        countryTrendMap["34"] = Arrays.asList(*arrayOf("Exoplanet discovery", "Manchester United F.c.", "La Liga"))
        countryTrendMap["35"] = Arrays.asList(*arrayOf("23 Февраля Праздник", "Порту Ювентус", "Алексей Петренко"))
        countryTrendMap["36"] = Arrays.asList(*arrayOf("ناس", "تحديث الواتس اب", "يوفنتوس"))
        countryTrendMap["37"] = Arrays.asList(*arrayOf("Juventus", "Exoplanet discovery", "Manchester United"))
        countryTrendMap["38"] = Arrays.asList(*arrayOf("Lali Esposito", "Gaviota de Platino", "Peter Cetera"))
        countryTrendMap["39"] = Arrays.asList(*arrayOf("Europa League", "HOROSCOP 23 februarie 2017", "Iohannis"))
        countryTrendMap["40"] = Arrays.asList(*arrayOf("Exoplanet discovery", "Budget Speech 2017", "Cricket live scores"))
        countryTrendMap["41"] = Arrays.asList(*arrayOf("aarde-achtige planeten", "Ben Weyts", "Juventus"))
        countryTrendMap["42"] = Arrays.asList(*arrayOf("Nya Tider", "Klara Svensson", "Emmanuel Frimpong"))
        countryTrendMap["43"] = Arrays.asList(*arrayOf("Jawa", "AMD Ryzen", "Cssd"))
        countryTrendMap["44"] = Arrays.asList(*arrayOf("Münze Österreich", "Weiberfastnacht", "Ho Chi Minh"))
        countryTrendMap["45"] = Arrays.asList(*arrayOf("Porto Juventus", "Buffon", "Nasa Bejelentés"))
        countryTrendMap["46"] = Arrays.asList(*arrayOf("erdähnliche Planeten", "Iker Casillas", "Juventus"))
        countryTrendMap["47"] = Arrays.asList(*arrayOf("Herrera", "Brit Awards 2017", "Liga Espanhola"))
        countryTrendMap["48"] = Arrays.asList(*arrayOf("Νικοσ Γκαλησ", "Θεμησ Μανεσησ", "Κουνδουροσ"))
        countryTrendMap["49"] = Arrays.asList(*arrayOf("Viaplay", "Exoplanet Discovery", "Viafree"))
        countryTrendMap["50"] = Arrays.asList(*arrayOf("Kelly Brook", "Temptation Island", "Saara Aalto"))
        countryTrendMap["51"] = Arrays.asList(*arrayOf("Cecilia Brækhus", "Brit Awards 2017", "Nina Hjerpset-Østlie"))
        countryTrendMap["52"] = Arrays.asList(*arrayOf("Exoplanet discovery", "Man City vs Monaco", "Laliga"))
        return countryTrendMap
    }
}
