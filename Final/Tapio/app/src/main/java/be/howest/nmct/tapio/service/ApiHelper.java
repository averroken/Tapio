package be.howest.nmct.tapio.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiHelper {

    private static Retrofit RETROFIT_INSTANCE;
    private static final String BASE_URL = "https://back-end-tapio.herokuapp.com";
    private static IBackEnd API_SERVICE;

    private static Object lock1 = new Object();
    private static Object lock2 = new Object();

    public static IBackEnd getApiService() {
        if(API_SERVICE == null) {
            synchronized (lock2) {
                if(API_SERVICE == null) {
                    API_SERVICE = ApiHelper
                            .getRetrofitInstance()
                            .create(IBackEnd.class);
                }
            }
        }
        return API_SERVICE;
    }

    private static Retrofit getRetrofitInstance() {
        if(RETROFIT_INSTANCE == null) {

            synchronized (lock1) {
                if(RETROFIT_INSTANCE == null) {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();

                    RETROFIT_INSTANCE = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }

        }
        return RETROFIT_INSTANCE;
    }




}
