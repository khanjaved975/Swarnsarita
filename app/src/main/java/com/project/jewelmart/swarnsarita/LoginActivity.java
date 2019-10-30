package com.project.jewelmart.swarnsarita;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.Appdata;
import com.project.jewelmart.swarnsarita.pojo.Loginstatus;
import com.project.jewelmart.swarnsarita.R;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    Button btnLogin,forgetpass;
    Button link_signup;
    APIInterface apiInterface;

    TextInputEditText et_phoneNo, et_password;
    String strPass, strphoneNo;
    UserSessionManager userSessionManager;
    Typeface typeface;
    public Spinner sp_usertype;
    private String usertype ="select";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        typeface = Typeface.createFromAsset(getAssets(), "font/Caviar_Dreams_Bold.ttf");

        if (Tools.isTablet(LoginActivity.this)) {
            LoginActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        } else {
            LoginActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        }

        userSessionManager = new UserSessionManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        if (SingletonSupport.getInstance().countriesList==null) {
            getAppdata();
        }
        link_signup = (Button) findViewById(R.id.link_signup);
        forgetpass = (Button) findViewById(R.id.forgetpass);
        et_phoneNo = (TextInputEditText) findViewById(R.id.input_mobile);
        et_password = (TextInputEditText) findViewById(R.id.input_password);
        sp_usertype = (Spinner) findViewById(R.id.spinner_usertype);
        et_phoneNo.setTypeface(typeface);
        et_password.setTypeface(typeface);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strphoneNo = et_phoneNo.getText().toString();
                strPass = et_password.getText().toString();
                if (validationNew(et_phoneNo, et_password)) {
                    if (!CheckNetwork.isConnected(LoginActivity.this)) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Tools.showCustomDialog(LoginActivity.this, "Alert !", "No Internet Connection , Please Try after Connecting");
                            }
                        });
                    } else {
                        if (strPass.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Enter Password !", Toast.LENGTH_LONG).show();
                        } else {
                            CheckLogin();
                        }
                    }
                }
            }
        });


        sp_usertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                } else {
                    usertype = sp_usertype.getSelectedItem().toString().toLowerCase();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, com.project.jewelmart.swarnsarita.RegistrationActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ForgetPasswordDialog();
            }
        });
        forgetpass.setVisibility(View.GONE);
    }

    public void CheckLogin() {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(LoginActivity.this);
        Call<Loginstatus> countriesCall = apiInterface.doCheckLogin(strphoneNo, strPass,usertype);
        countriesCall.enqueue(new Callback<Loginstatus>() {
            @Override
            public void onResponse(Call<Loginstatus> call, Response<Loginstatus> response) {
                Log.d("TAG", response.code() + "");
                pdg.dismiss();
                Loginstatus resource = response.body();
                if (resource.getAck().toString().equals("1")) {
                    Toast.makeText(getApplicationContext(), resource.getMsg(), Toast.LENGTH_LONG).show();
                    Loginstatus.UserData data = resource.getUserData();
                    SingletonSupport.getInstance().isLogin = true;
                    userSessionManager.setUserID(data.getUserId());
                    userSessionManager.setUserPass(strPass);
                    userSessionManager.setIsUserActive(data.getUserStatus());
                    userSessionManager.setUserContact(data.getMobileNumber());
                    userSessionManager.setUserFullName(data.getFullName());
                    userSessionManager.setUserEmail(data.getEmailId());
                    userSessionManager.setUserType(usertype);
                    userSessionManager.setMeting(data.getDefault_melting());
                    startActivity(new Intent(LoginActivity.this, SplashActivity.class));
                    finish();
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    Tools.showCustomDialog(LoginActivity.this,"Failed",resource.getMsg());
                }
            }

            @Override
            public void onFailure(Call<Loginstatus> call, Throwable t) {
                pdg.dismiss();
                call.cancel();
            }
        });
    }

    public boolean validationNew(TextInputEditText mobileField, TextInputEditText passField) {
        String strmobile = mobileField.getText().toString();
        String strpass = passField.getText().toString();

        // mail
        if (strmobile == null || strmobile.equals("")) {
            mobileField.setError("Enter Email Address.");
            mobileField.requestFocus();
            return false;
        }

        // pass
        if (strpass == null || strpass.equals("")) {
            passField.setError("Enter password");
            passField.requestFocus();
            return false;
        }

         if (usertype.toLowerCase().contains("select") || usertype.isEmpty()){
             Tools.showCustomDialog(LoginActivity.this,"Alert !","Please Select User Type");
             return false;
         }

        return true;
    }


    private void setPasswordDialog() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_set_password);
        dialog.setCancelable(true);
        TextView title=(TextView) dialog.findViewById(R.id.title);
        title.setText("Change Password");
        TextInputEditText oldpass=(TextInputEditText) findViewById(R.id.input_otp);
        TextInputEditText newpass=(TextInputEditText) findViewById(R.id.input_new_pass);
        TextInputEditText confirmpass=(TextInputEditText) findViewById(R.id.input_confirm_pass);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((ImageView) dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void ForgetPasswordDialog() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_forget_pass);
        dialog.setCancelable(true);
        TextView title=(TextView) dialog.findViewById(R.id.title);
        title.setText("Forget Password");
        TextInputEditText mobile=(TextInputEditText) findViewById(R.id.input_mobile);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordDialog();
                dialog.dismiss();
            }
        });

        ((ImageView) dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
                        SingletonSupport.getInstance().countriesList = (ArrayList<Appdata.CountryDatum>) contries;
                        SingletonSupport.getInstance().colors = (ArrayList<Appdata.Color>) colors;
                        SingletonSupport.getInstance().meltinglist = (ArrayList<Appdata.Melting>) melting;
                        SingletonSupport.getInstance().tone = (ArrayList<Appdata.Tone>) tone;
                        SingletonSupport.getInstance().polish = (ArrayList<Appdata.Polish>) polishes;
                    }
                } catch (Exception e) {
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






}
