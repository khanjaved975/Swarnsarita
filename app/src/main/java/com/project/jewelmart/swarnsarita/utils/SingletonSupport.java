package com.project.jewelmart.swarnsarita.utils;
import com.project.jewelmart.swarnsarita.pojo.Collection;
import com.project.jewelmart.swarnsarita.pojo.Appdata;
import com.project.jewelmart.swarnsarita.pojo.Cities;
import com.project.jewelmart.swarnsarita.pojo.Company;
import com.project.jewelmart.swarnsarita.pojo.SortList;
import com.project.jewelmart.swarnsarita.pojo.States;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by javedkhan on 11/27/17.
 */

public class SingletonSupport {

    public ArrayList<Appdata.CountryDatum> countriesList=null;
    public ArrayList<Appdata.Melting> meltinglist;
    public ArrayList<Appdata.Color> colors;
    public ArrayList<Appdata.Polish> polish;
    public ArrayList<Appdata.Tone> tone;
    public ArrayList<States.State> stateList;
    public ArrayList<Cities.City> cityList;
    public ArrayList<Company.Company_> companylist;
    public List<Collection> designcollectionlist;
    public List<SortList> sortLists=null;
    public int backStackCount;
    public ArrayList<String> multiSelectedIds=new ArrayList<>();
    public ArrayList<Integer> multiSelectedPos=new ArrayList<>();
    public ArrayList<String> multiSelectedQty=new ArrayList<>();
    public boolean isfilter,isLogin;
    public String user_id="",usertype="",sort="2";
    public static int cartCount=0;
    public String screenshotfilename;

    private static SingletonSupport ourInstance = new SingletonSupport();

    public static SingletonSupport getInstance() {
        return ourInstance;
    }

    private SingletonSupport() {
    }


    public static void initinstance() {
        if (ourInstance == null) {
            ourInstance = new SingletonSupport();
        }
    }

}
