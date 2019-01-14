package com.fantasy1022.fancytrendapp.injection;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.fantasy1022.fancytrendapp.common.Constant;
import com.fantasy1022.fancytrendapp.data.TrendRepository;
import com.fantasy1022.fancytrendapp.data.TrendRepositoryImpl;
import com.fantasy1022.fancytrendapp.data.remote.FancyTrendRestService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fantasy_apple on 2017/6/25.
 */

//TODO:Change to Kotlin
@Module
public class NetworkModule {
    private static final String BASE_URL = Constant.INSTANCE.GOOGLE_TREND_BASE_URL;
    private static ArrayMap<String, String> countryMap;

    @Provides
    public static TrendRepository provideTrendRepo(FancyTrendRestService googleTrendRestService) {
        return new TrendRepositoryImpl(googleTrendRestService);
    }

    @Provides
    public static FancyTrendRestService provideGoogleTrendRestService(Retrofit retrofit) {
        FancyTrendRestService googleTrendRestService = retrofit.create(FancyTrendRestService.class);
        generateCountryCodeMapping();
        return googleTrendRestService;
    }

    //TODO:Remove
//    @Provides
//    public static Retrofit getRetrofitInstance(OkHttpClient okHttpClient) {
//        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
//        return retrofit;
//    }

    @Provides
    public static Retrofit getRetrofitInstanceForCoroutine(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    public static OkHttpClient getOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
        return okHttpClient;
    }

    @Provides
    public static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }

    //For test using gson to transform response
    public static void getTrendUsingOkhttp() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.INSTANCE.INSTANCE.GOOGLE_TREND_BASE_URL + "/api/terms/")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Test", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                Type trendMapType = new TypeToken<ArrayMap<String, ArrayList<String>>>() {
                }.getType();
                ArrayMap<String, ArrayList<String>> map = gson.fromJson(response.body().string(), trendMapType);
            }
        });
    }

    //TODO: Move to test file
    public static ArrayMap<String, List<String>> generateTrendMap() {
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

    //TODO:Put it to Const class
    private static void generateCountryCodeMapping() {
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
}
