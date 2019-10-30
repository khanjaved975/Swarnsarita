package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 11/23/18.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appdata {

    @SerializedName("company_type_data")
    @Expose
    private List<CompanyTypeDatum> companyTypeData = null;
    @SerializedName("company_name_data")
    @Expose
    private List<CompanyNameDatum> companyNameData = null;
    @SerializedName("melting")
    @Expose
    private List<Melting> melting = null;
    @SerializedName("country_data")
    @Expose
    private List<CountryDatum> countryData = null;

    @SerializedName("tone")
    @Expose
    private List<Tone> tone = null;

    @SerializedName("polish")
    @Expose
    private List<Polish> polish = null;

    @SerializedName("color")
    @Expose
    private List<Color> color = null;



    public List<CompanyTypeDatum> getCompanyTypeData() {
        return companyTypeData;
    }

    public void setCompanyTypeData(List<CompanyTypeDatum> companyTypeData) {
        this.companyTypeData = companyTypeData;
    }

    public List<CompanyNameDatum> getCompanyNameData() {
        return companyNameData;
    }

    public void setCompanyNameData(List<CompanyNameDatum> companyNameData) {
        this.companyNameData = companyNameData;
    }

    public List<Melting> getMelting() {
        return melting;
    }

    public void setMelting(List<Melting> melting) {
        this.melting = melting;
    }

    public List<CountryDatum> getCountryData() {
        return countryData;
    }

    public void setCountryData(List<CountryDatum> countryData) {
        this.countryData = countryData;
    }

    public void setTone(List<Tone> tone) {
        this.tone = tone;
    }

    public List<Polish> getPolish() {
        return polish;
    }

    public List<Tone> getTone() {
        return tone;
    }

    public List<Color> getColor() {
        return color;
    }

    public class CompanyNameDatum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("company_name")
        @Expose
        private String companyName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

    }

    public class CompanyTypeDatum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public class CountryDatum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public class Tone {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("tone_type")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getToneType() {
            return name;
        }

        public void setToneTypee(String name) {
            this.name = name;
        }

    }

    public class Polish {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("polish_type")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPolishType() {
            return name;
        }

        public void setPolishType(String name) {
            this.name = name;
        }

    }

    public class Color {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("color_type")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getColorType() {
            return name;
        }

        public void setcColorType(String name) {
            this.name = name;
        }

    }


    public class Melting {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("melting_name")
        @Expose
        private String meltingName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMeltingName() {
            return meltingName;
        }

        public void setMeltingName(String meltingName) {
            this.meltingName = meltingName;
        }

    }
}
