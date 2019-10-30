package com.project.jewelmart.swarnsarita;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.jewelmart.swarnsarita.R;
import com.project.jewelmart.swarnsarita.adapters.ViewPagerAdapter;
import com.project.jewelmart.swarnsarita.components.CirclePageIndicator;
import com.project.jewelmart.swarnsarita.components.CustomViewPager;
import com.project.jewelmart.swarnsarita.fragments.FragmentFour;
import com.project.jewelmart.swarnsarita.fragments.FragmentUserDetails;
import com.project.jewelmart.swarnsarita.fragments.FragmentAddress;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.models.StepperEntity;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.pojo.OtpResponse;
import com.project.jewelmart.swarnsarita.pojo.Status;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        View.OnClickListener {

    private AppCompatTextView lblTitle;
    APIInterface apiInterface;

    private CustomViewPager customViewPager;
    private final List<Fragment> listFragment = new ArrayList<>();
    private final List<String> listFragmentTitle = new ArrayList<>();

    private FragmentUserDetails fragmentOne;
    private FragmentAddress fragmentTwo;
    private FragmentFour fragmentFour;

    private AppCompatButton btnBack;
    private AppCompatButton btnNext;
    private boolean doubleBackToExit = false;
    StepperEntity stepperEntity;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        stepperEntity = new StepperEntity();
        btnBack = (AppCompatButton) findViewById(R.id.btnBack);
        btnNext = (AppCompatButton) findViewById(R.id.btnNext);
       /* AppCompatImageView imgBack = (AppCompatImageView) findViewById(R.id.imgBack);
        lblTitle = (AppCompatTextView) findViewById(R.id.lblTitle);*/
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        customViewPager = (CustomViewPager) findViewById(R.id.viewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        stepperEntity = new StepperEntity();
        if (Tools.isTablet(RegistrationActivity.this)) {
            RegistrationActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        } else {
            RegistrationActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), listFragment, listFragmentTitle);

        customViewPager.setOffscreenPageLimit(4);
        customViewPager.setAdapter(adapter);
        fragmentOne = new FragmentUserDetails();
        fragmentTwo = new FragmentAddress();
        // fragmentFour = badgenew FragmentFour();
        listFragment.add(fragmentOne);
        listFragment.add(fragmentTwo);
        //listFragment.add(fragmentFour);
        adapter.notifyDataSetChanged();
        indicator.setViewPager(customViewPager);
        customViewPager.addOnPageChangeListener(this);
        assert btnBack != null;
        btnBack.setOnClickListener(this);
        assert btnNext != null;
        btnNext.setOnClickListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        switch (position) {
            case 0:
                btnBack.setEnabled(false);
                btnNext.setText("Next");

                break;
            case 1:
                //lblTitle.setText(getString(R.string.shipping));
                btnBack.setEnabled(true);
                btnNext.setText("Finish");

                break;
            case 2:
                // lblTitle.setText(getString(R.string.payment));
                btnBack.setEnabled(true);
                break;
    /*        case 3:
                //lblTitle.setText(getString(R.string.confirmation));
                btnBack.setEnabled(false);
                btnNext.setEnabled(false);
                Log.i("x- pos", ""+currentPosition);
                Log.i("x- data", badgenew Gson().toJson(stepperEntity));

                fragmentFour.lblMessage.setText(getString(android.R.string.cancel,
                        stepperEntity.getName(), stepperEntity.getLastname(),
                        stepperEntity.getAddress(), stepperEntity.getCard()));

                break;*/
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnBack:
                if (currentPosition > 0) {
                    customViewPager.setCurrentItem(currentPosition - 1, true);
                }
                break;
            case R.id.btnNext:
                switch (currentPosition) {
                    case 0:
                        if (fragmentOne.validateFields()) {
                            stepperEntity.setgetPhoneno(fragmentOne.pnone);
                            stepperEntity.setUsername(fragmentOne.username);
                            stepperEntity.setPassword(fragmentOne.pass);
                            stepperEntity.setName(fragmentOne.fullname);
                            stepperEntity.setGender(fragmentOne.gender);
                            stepperEntity.setCompany(fragmentOne.company);
                            stepperEntity.setEmail(fragmentOne.email);
                            customViewPager.setCurrentItem(1, true);
                        }
                        break;
                    case 1:
                        if (fragmentTwo.validateFields()) {
                            stepperEntity.setCity(fragmentTwo.selectedCityId);
                            stepperEntity.setCountry(fragmentTwo.selectedCountryId);
                            stepperEntity.setState(fragmentTwo.selectedStateId);
                            stepperEntity.setPincode(fragmentTwo.address);
                            stepperEntity.setGst(fragmentTwo.gst);
                            stepperEntity.setShope(fragmentTwo.shopname);
                            customViewPager.setCurrentItem(2, true);
                            GenerateOtp(stepperEntity.getPhoneno());
                            // Register();
                        }
                        break;
                  /*  case 2:
                        if(fragmentFour.validateFields()){
                            stepperEntity.setCard(fragmentThree.card);
                            customViewPager.setCurrentItem(3, true);
                        }
                        break;*/
                    default:
                        break;
                }
                break;
           /* case R.id.imgBack:
                onBackPressed();
                break;*/
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {

        //   if (doubleBackToExit) {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        return;
        //  }

        //this.doubleBackToExit = true;
        // Toast.makeText(this,"message_back", Toast.LENGTH_SHORT).show();

      /*  badgenew Handler().postDelayed(badgenew Runnable() {
            @Override
            public void run() {
                doubleBackToExit=false;
            }
        }, 2000);*/
    }


    public void Register() {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(RegistrationActivity.this);
        Call<Status> countriesCall = apiInterface.doRegistration(
                stepperEntity.getPhoneno(),
                stepperEntity.getUsername(),
                stepperEntity.getPassword(),
                stepperEntity.getEmail(),
                stepperEntity.getName(),
                stepperEntity.getGender(),
                stepperEntity.getCountry(),
                stepperEntity.getState(),
                stepperEntity.getCity(),
                stepperEntity.getPincode(),
                stepperEntity.getCompany(),
                "active",
                "25-01-2030",
                "android", stepperEntity.getGst(), stepperEntity.getShope());
        countriesCall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Log.d("TAG", response.code() + "");
                pdg.dismiss();
                Status resource = response.body();
                if (resource.getAck().toString().equals("1")) {
                    Toast.makeText(getApplicationContext(), resource.getError(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                } else {
                    Tools.showCustomDialog(getApplicationContext(), "Alert !",resource.getError());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                call.cancel();
                pdg.dismiss();

            }
        });
    }


    public void GenerateOtp(String mobile) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(RegistrationActivity.this);

        Call<OtpResponse> countriesCall = apiInterface.getOTP(mobile);
        countriesCall.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                Log.d("TAG", response.code() + "");
                pdg.dismiss();
                OtpResponse resource = response.body();
                if (resource.getAck().equalsIgnoreCase("1")) {
                    Toast.makeText(getApplicationContext(), resource.getMsg(), Toast.LENGTH_LONG).show();
                    //  startActivity(badgenew Intent(RegistrationActivity.this, MainActivity.class));
                    showVerifyMobile(String.valueOf(resource.getData().getOtp()));
                } else {
                    Toast.makeText(getApplicationContext(), resource.getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                call.cancel();
                pdg.dismiss();

            }
        });
    }


    public void showVerifyMobile(final String otp) {
        LayoutInflater inflater = LayoutInflater.from(RegistrationActivity.this);
        View subView = inflater.inflate(R.layout.dialog_info_otp, null);
        final EditText subEditText = (EditText) subView.findViewById(R.id.et_code);
        final Button tvResend = (Button) subView.findViewById(R.id.resend);
        final Button btn_continue = (Button) subView.findViewById(R.id.bt_continue);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Verify Mobile Number");
        builder.setView(subView);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenerateOtp(stepperEntity.getPhoneno());
            }
        });


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = subEditText.getText().toString();
                if (!code.isEmpty()) {
                    if (code.equals(otp)) {
                        Register();
                        alertDialog.dismiss();
                    } else {
                        subEditText.setError("Code is invalid");
                    }
                } else {
                    subEditText.setError("Please Enter Code");
                }

            }
        });

        /*builder.setPositiveButton("DONE", badgenew DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = subEditText.getText().toString();
                if (!code.isEmpty()) {
                    if (code.equals(otp)) {
                       Register();
                    } else {
                        subEditText.setError("Code is invalid");
                    }
                } else {
                    subEditText.setError("Please Enter Code");
                }

            }
        });

        builder.setNegativeButton("Cancel", badgenew DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });*/

        builder.show();
    }


}
