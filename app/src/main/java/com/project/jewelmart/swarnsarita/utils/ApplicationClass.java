package com.project.jewelmart.swarnsarita.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.pojo.Appdata;
import com.project.jewelmart.swarnsarita.pojo.CountryPojo;
import com.project.jewelmart.swarnsarita.pojo.Usertype;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationClass extends Application {

    APIInterface apiInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        Fabric.with(this, new Crashlytics());        // Logging set to help debug issues, remove before releasing your app.
        apiInterface = APIClient.getClient().create(APIInterface.class);
        SingletonSupport.initinstance();
        //getCountriesList();
        //Fresco.initialize(this);
        getAppdata();
        // getClientList();
        // getUserTypeList();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // MultiDex.install(this);
    }


    public void getCountriesList() {
        Call<CountryPojo> countriesCall = apiInterface.doGetCountries();
        countriesCall.enqueue(new Callback<CountryPojo>() {
            @Override
            public void onResponse(Call<CountryPojo> call, Response<CountryPojo> response) {

                Log.d("TAG", response.code() + "");

                CountryPojo resource = response.body();
                List<CountryPojo.Country> ListCountry = resource.getCountries();
                //SingletonSupport.getInstance().countriesList = (ArrayList<CountryPojo.Country>) ListCountry;
                // Toast.makeText(getApplicationContext(),ListCountry.toString(),Toast.LENGTH_LONG).show();
                // responseText.setText(displayResponse);

            }

            @Override
            public void onFailure(Call<CountryPojo> call, Throwable t) {
                call.cancel();
            }
        });

    }

    public void getAppdata() {
        Call<Appdata> countriesCall = apiInterface.doAppDate();
        countriesCall.enqueue(new Callback<Appdata>() {
            @Override
            public void onResponse(Call<Appdata> call, Response<Appdata> response) {
                Log.d("TAG", response.code() + "");
                try {
                    Appdata resource = response.body();
                    if (resource != null) {
                        List<Appdata.Melting> melting = null;
                        if (resource.getMelting() != null) {
                            melting = resource.getMelting();
                        }
                        List<Appdata.CountryDatum> contries = resource.getCountryData();
                        List<Appdata.Color> colors = resource.getColor();
                        List<Appdata.Tone> tone = resource.getTone();
                        List<Appdata.Polish> polishes = resource.getPolish();
                        SingletonSupport.getInstance().meltinglist = (ArrayList<Appdata.Melting>) melting;
                        SingletonSupport.getInstance().countriesList = (ArrayList<Appdata.CountryDatum>) contries;
                        SingletonSupport.getInstance().colors = (ArrayList<Appdata.Color>) colors;
                        SingletonSupport.getInstance().tone = (ArrayList<Appdata.Tone>) tone;
                        SingletonSupport.getInstance().polish = (ArrayList<Appdata.Polish>) polishes;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                // SingletonSupport.getInstance().clientIdList= (ArrayList<ClientId.Client>) ListCountry;
            }

            @Override
            public void onFailure(Call<Appdata> call, Throwable t) {
                call.cancel();
            }
        });
    }


    public void getUserTypeList() {
        Call<Usertype> countriesCall = apiInterface.doGetUserType();
        countriesCall.enqueue(new Callback<Usertype>() {
            @Override
            public void onResponse(Call<Usertype> call, Response<Usertype> response) {
                Log.d("TAG", response.code() + "");
                Usertype resource = response.body();
                List<Usertype.Usertype_> ListCountry = resource.getUsertype();
                // SingletonSupport.getInstance().usertypeList= (ArrayList<Usertype.Usertype_>) ListCountry;
            }

            @Override
            public void onFailure(Call<Usertype> call, Throwable t) {
                call.cancel();
            }

        });

    }


}
