package com.project.jewelmart.swarnsarita.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.pojo.Cities;
import com.project.jewelmart.swarnsarita.pojo.States;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Carlos Vargas on 8/10/16.
 */

public class FragmentAddress extends Fragment {

    private TextInputLayout tilAddress,tilgst,tilshope;
    private TextInputEditText tieAddress,tiegst,tieshope;
    public String address, selectedCountryId, selectedCityId, selectedStateId,gst,shopname;
    APIInterface apiInterface;
    SearchableSpinner sp_counntry, sp_state, sp_city;
    Typeface typeface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/Caviar_Dreams_Bold.ttf");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        tilAddress = (TextInputLayout) view.findViewById(R.id.tilAddress);
        tilgst = (TextInputLayout) view.findViewById( R.id.tilgst);
        tilshope = (TextInputLayout) view.findViewById(R.id.tilshope);
        tieAddress = (TextInputEditText) view.findViewById(R.id.tieAddress);
        tiegst = (TextInputEditText) view.findViewById(R.id.tiegst);
        tieshope = (TextInputEditText) view.findViewById(R.id.tieshope);
        tieAddress.setTypeface(typeface);
        tiegst.setTypeface(typeface);
        tieshope.setTypeface(typeface);
        tieAddress.addTextChangedListener(new CustomTextWatcher(tieAddress));
        tiegst.addTextChangedListener(new CustomTextWatcher(tiegst));
        tieshope.addTextChangedListener(new CustomTextWatcher(tieshope));

        sp_state = (SearchableSpinner) view.findViewById(R.id.sp_state);
        sp_city = (SearchableSpinner) view.findViewById(R.id.sp_city);
        sp_counntry = (SearchableSpinner) view.findViewById(R.id.sp_counntry);
        ArrayList<String> objects = new ArrayList<String>();
        for (int i = 0; i < SingletonSupport.getInstance().countriesList.size(); i++) {
            objects.add("" + SingletonSupport.getInstance().countriesList.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, objects);
        sp_counntry.setAdapter(adapter);
        sp_counntry.setSelection(100);
        selectedCountryId = SingletonSupport.getInstance().countriesList.get(100).getId();
        sp_counntry.setTitle("Select Country");
        sp_counntry.setPositiveButton("OK");
        sp_counntry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountryId = SingletonSupport.getInstance().countriesList.get(position).getId();
                getStateList(selectedCountryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getStateList(selectedCountryId);
        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStateId = SingletonSupport.getInstance().stateList.get(position).getId();
                getCityList(selectedStateId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCityId = SingletonSupport.getInstance().cityList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean validateFields() {
        return isValid();
    }

    private class CustomTextWatcher implements TextWatcher {

        private final View view;

        private CustomTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            if (view.getId() == R.id.tieAddress) {
                //isValid();
            }
        }
    }

    private boolean isValid() {

        String text = tieAddress.getText().toString().trim();
        String txtgst =tiegst.getText().toString().trim();
        String txtshop = tieshope.getText().toString().trim();

        /*if (text.isEmpty()) {
            tilAddress.setErrorEnabled(true);
            tilAddress.setError("Enter Pincode");
            Utils.requestFocus(getActivity(), tieAddress);
            return false;
        }

        if (txtshop.isEmpty()) {
            tilshope.setErrorEnabled(true);
            tilshope.setError("Enter Office Name");
            Utils.requestFocus(getActivity(), tieshope);
            return false;
        }

        if (txtgst.isEmpty()) {
            tilgst.setErrorEnabled(true);
            tilgst.setError("Enter GST Number");
            Utils.requestFocus(getActivity(), tiegst);
            return false;
        }

        if (txtshop.length() < 1) {
            tilAddress.setError("Enter Office Name");
            Utils.requestFocus(getActivity(), tieshope);
            return false;
        } else {
            tilAddress.setErrorEnabled(false);
        }
       if (txtgst.isEmpty()) {
            tilgst.setErrorEnabled(true);
            tilgst.setError("Enter GST Number");
            Utils.requestFocus(getActivity(), tiegst);
            return false;
        }*/
        address = text;
        gst=txtgst;
        shopname=txtshop;

        return true;
    }


    public void getStateList(String country) {
        Call<States> countriesCall = apiInterface.doGetState(country);
        countriesCall.enqueue(new Callback<States>() {
            @Override
            public void onResponse(Call<States> call, Response<States> response) {

                Log.d("TAG State", response.code() + "");
                States resource = response.body();
                List<States.State> ListCountry = resource.getStates();
                SingletonSupport.getInstance().stateList = (ArrayList<States.State>) ListCountry;
                ArrayList<String> objects = new ArrayList<String>();
                for (int i = 0; i < SingletonSupport.getInstance().stateList.size(); i++) {
                    objects.add("" + SingletonSupport.getInstance().stateList.get(i).getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, objects);
                sp_state.setAdapter(adapter);
                sp_state.setTitle("Select State");
                sp_state.setPositiveButton("OK");
                getCityList(SingletonSupport.getInstance().stateList.get(0).getId());

            }

            @Override
            public void onFailure(Call<States> call, Throwable t) {
                call.cancel();
            }
        });

    }


    public void getCityList(String state) {
        Call<Cities> countriesCall = apiInterface.doGetCity(state);
        countriesCall.enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                Log.d("TAG Country", response.code() + "");
                Cities resource = response.body();
                List<Cities.City> ListCountry = resource.getCities();
                SingletonSupport.getInstance().cityList = (ArrayList<Cities.City>) ListCountry;
                ArrayList<String> objects = new ArrayList<String>();
                for (int i = 0; i < SingletonSupport.getInstance().cityList.size(); i++) {
                    objects.add("" + SingletonSupport.getInstance().cityList.get(i).getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, objects);
                sp_city.setAdapter(adapter);
                sp_city.setTitle("Select City");
                sp_city.setPositiveButton("OK");
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {
                call.cancel();
            }
        });

    }

}
