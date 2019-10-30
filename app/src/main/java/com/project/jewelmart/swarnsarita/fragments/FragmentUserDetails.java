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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.hbb20.CountryCodePicker;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.pojo.Company;
import com.project.jewelmart.swarnsarita.utils.Constant;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Utils;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Carlos Vargas on 8/10/16.
 */

public class FragmentUserDetails extends Fragment {

    private TextInputLayout tilPhone, tilUsername, tilFullrname, tilEmail, tilPass;
    private TextInputEditText tiephone, tieUsername, tiefullname, tieEmail, tiePass;
    public String pnone, username, pass, fullname, gender, company, email;
    RadioGroup radioGroup;
    APIInterface apiInterface;
    Spinner sp_clientid;
    private RadioButton radioButton;
    private boolean userIsInteracting;

    View view;
    Typeface typeface;
    CountryCodePicker cpp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/Caviar_Dreams_Bold.ttf");

        apiInterface = APIClient.getClient().create(APIInterface.class);
        tilPhone = (TextInputLayout) view.findViewById(R.id.tilphone);
        tilUsername = (TextInputLayout) view.findViewById(R.id.tilLastName);
        tilPass = (TextInputLayout) view.findViewById(R.id.tilPass);
        tilFullrname = (TextInputLayout) view.findViewById(R.id.tilFullName);
        tilEmail = (TextInputLayout) view.findViewById(R.id.tilEmail);
        tiephone = (TextInputEditText) view.findViewById(R.id.tiephone);
        tiefullname = (TextInputEditText) view.findViewById(R.id.tieFullName);
        tieUsername = (TextInputEditText) view.findViewById(R.id.tieUserName);
        tiePass = (TextInputEditText) view.findViewById(R.id.tiePass);
        cpp = (CountryCodePicker) view.findViewById(R.id.ccp);
        cpp.setCountryForNameCode("+91");
        tieEmail = (TextInputEditText) view.findViewById(R.id.tieEmail);
        sp_clientid = (Spinner) view.findViewById(R.id.sp_clientid);
        tiephone.addTextChangedListener(new CustomTextWatcher(tiephone));
        tieUsername.addTextChangedListener(new CustomTextWatcher(tieUsername));
        tieEmail.setTypeface(typeface);
        tiePass.setTypeface(typeface);
        tiefullname.setTypeface(typeface);
        tieUsername.setTypeface(typeface);
        tiephone.setTypeface(typeface);
        GetCompany();


        sp_clientid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_clientid != null && sp_clientid.getSelectedItem() != null) {
                    if (SingletonSupport.getInstance().companylist != null) {
                        company = String.valueOf(SingletonSupport.getInstance().companylist.get(position).getId());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                /*if(checkedId == R.id.silent) {
                    Toast.makeText(getApplicationContext(), "choice: Silent",
                            Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.sound) {
                    Toast.makeText(getApplicationContext(), "choice: Sound",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "choice: Vibration",
                            Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }

    public boolean validateFields() {
        int selectedid = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) view.findViewById(selectedid);
        gender = radioButton.getText().toString();

        return isValid(tiephone, tilPhone, Constant.VAR_ONE) &&
                isValid(tieUsername, tilUsername, Constant.VAR_TWO) &&
                isValid(tiePass, tilPass, Constant.VAR_three) &&
                isValid(tiefullname, tilFullrname, Constant.VAR_four) &&
                isValid(tieEmail, tilEmail, Constant.VAR_five);
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
            switch (view.getId()) {
                case R.id.tiephone:
                    isValid(tiephone, tilPhone, Constant.VAR_ONE);
                    break;
                case R.id.tieUserName:
                    isValid(tieUsername, tilUsername, Constant.VAR_TWO);
                    break;

                case R.id.tiePass:
                    isValid(tiePass, tilPass, Constant.VAR_three);
                    break;

                case R.id.tieFullName:
                    isValid(tiefullname, tilFullrname, Constant.VAR_four);
                    break;

                case R.id.tieEmail:
                    isValid(tieEmail, tilEmail, Constant.VAR_five);
                    break;
                default:
                    break;
            }
        }
    }

    private boolean isValid(TextInputEditText tieText, TextInputLayout tilText, int var) {
        String text = tieText.getText().toString().trim();
        if (text.isEmpty()) {
            tilText.setErrorEnabled(true);
            tilText.setError("Field is empty");
            if (var == Constant.VAR_TWO) {
                tilText.setError("Enter Username");
            } else if (var == Constant.VAR_ONE) {
                tilText.setError("Enter Phone No.");
            } else if (var == Constant.VAR_three) {
                tilText.setError("Enter Password.");
            } else if (var == Constant.VAR_four) {
                tilText.setError("Enter FullName.");
            } else if (var == Constant.VAR_five) {
                tilText.setError("Enter Email.");
            }
            Utils.requestFocus(getActivity(), tieText);
            return false;
        }

        /*if (text.length() < 1) {
            tilText.setError(getString(R.string.error_name));

            if (var == Constant.VAR_TWO) {
                tilText.setError(getString(R.string.error_lastname));
            }
            Utils.requestFocus(getActivity(), tieText);
            return false;
        } else {
            tilText.setErrorEnabled(false);
        }*/

        if (var == Constant.VAR_ONE) {
            pnone = text;
        } else if (var == Constant.VAR_TWO) {
            username = text;
        } else if (var == Constant.VAR_three) {
            pass = text;
        } else if (var == Constant.VAR_four) {
            fullname = text;
        } else if (var == Constant.VAR_five) {
            email = text;
        }

        return true;

    }

    public void GetCompany() {
        Call<Company> countriesCall = apiInterface.doGetCompany();
        countriesCall.enqueue(new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {
                Log.d("TAG Country", response.code() + "");
                Company resource = response.body();
                List<Company.Company_> ListCountry = resource.getCompany();
                SingletonSupport.getInstance().companylist = (ArrayList<Company.Company_>) ListCountry;
                ArrayList<String> objects = new ArrayList<String>();
                for (int i = 0; i < SingletonSupport.getInstance().companylist.size(); i++) {
                    objects.add("" + SingletonSupport.getInstance().companylist.get(i).getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, objects);
                sp_clientid.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                call.cancel();
            }
        });

    }


}
