package com.project.jewelmart.swarnsarita;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.pojo.Acknowledge;
import com.project.jewelmart.swarnsarita.pojo.CheckOut;
import com.project.jewelmart.swarnsarita.utils.DateUtils;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    APIInterface apiInterface;
    List<CheckOut> listCheckOut;
    LinearLayout lay_checkout;
    UserSessionManager userSessionManager;
    private ArrayList<String> controlValue;
    private ArrayList<String> keys;
    private ArrayList<String> values;
    private HashMap<String, MaterialEditText> textFieldList = new HashMap<String, MaterialEditText>();
    private HashMap<String, Spinner> dropDownList = new HashMap<String, Spinner>();
    private HashMap<String, MaterialEditText> textAreaList = new HashMap<String, MaterialEditText>();
    HashMap<String, Object> tags;
    Typeface typeface;
    String gift_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        initToolbar();
        userSessionManager = new UserSessionManager(this);
        typeface = Typeface.createFromAsset(getAssets(), "font/Caviar_Dreams_Bold.ttf");
        lay_checkout = (LinearLayout) findViewById(R.id.lay_checkout);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        if (getIntent() != null) {
            if (!getIntent().getStringExtra("gift_id").equalsIgnoreCase("0")) {
                gift_id = getIntent().getStringExtra("gift_id");
            }
        }
        if (Tools.isTablet(CheckoutActivity.this)) {
            CheckoutActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        } else {
            CheckoutActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        }
        GetOrderDetail();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Check Out");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void GetOrderDetail() {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(CheckoutActivity.this);
        Call<List<CheckOut>> countriesCall = apiInterface.getOrderField();
        countriesCall.enqueue(new Callback<List<CheckOut>>() {
            @Override
            public void onResponse(Call<List<CheckOut>> call, Response<List<CheckOut>> response) {
                pdg.dismiss();
                Log.d("GetOrderDetail", response.code() + "");
                final List<CheckOut> resource = response.body();
                listCheckOut = resource;
                generateField();
            }

            @Override
            public void onFailure(Call<List<CheckOut>> call, Throwable t) {
                pdg.dismiss();
                Log.d("GetOrderDetail", t.toString() + "");
                call.cancel();
            }
        });
    }


    private void generateField() {
        tags = new HashMap<String, Object>();
        MaterialEditText myEditText;
        MaterialEditText myEditTextArea;
        Spinner spinner;

        for (int i = 0; i < listCheckOut.size(); i++) {
            CheckOut checkOut = (CheckOut) listCheckOut.get(i);
            if (checkOut.getFieldType().equalsIgnoreCase("Textfield")) {
                myEditText = new MaterialEditText(this);
                myEditText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                myEditText.setHint(checkOut.getFieldName());
                myEditText.setTag(checkOut.getFieldCode() + i + "");
                myEditText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
                myEditText.setMetHintTextColor(getResources().getColor(R.color.grey_60));
                myEditText.setTypeface(typeface);
                if (checkOut.getFieldCode().equalsIgnoreCase("mobile_no")) {
                    myEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                    myEditText.setText(userSessionManager.getUserContact());
                } else if (checkOut.getFieldCode().equalsIgnoreCase("email_id")) {
                    myEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    myEditText.setText(userSessionManager.getUserEmail());
                } else if (checkOut.getFieldCode().equalsIgnoreCase("pincode")) {
                    myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (checkOut.getFieldCode().equalsIgnoreCase("delivery_date")) {
                    myEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                    myEditText.setFocusable(false);
                    // myEditText.setFocusable(false);
                    myEditText.setHint(checkOut.getFieldName());
                    myEditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            datePicker();
                        }
                    });
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);
                    Date c2 = DateUtils.addDay(c, 7);
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = df.format(c2);
                    myEditText.setText(formattedDate);
                } else if (checkOut.getFieldCode().equalsIgnoreCase("full_name")) {
                    myEditText.setText(userSessionManager.getUserFullName());
                }else if (checkOut.getFieldCode().equalsIgnoreCase("stamp")) {
                   // myEditText.setText(userSessionManager.getStamp());
                } else if (checkOut.getFieldCode().equalsIgnoreCase("melting_name")) {
                    myEditText.setText(userSessionManager.getMelting());
                }
                myEditText.setSingleLine();
                myEditText.setFloatingLabelTextColor(R.color.colorAccent);
                myEditText.setPrimaryColor(R.color.white);
              /*if (from != null && from.equalsIgnoreCase("orders")) {
                    myEditText.setText("" + SingletonClass.getinstance().selectedOrder.finalizeDataList.get(i).value);
                    myEditText.setFocusable(false);
                    myEditText.setEnabled(false);
                }*/
                tags.put("Textfield" + i, myEditText.getTag());
                textFieldList.put(checkOut.getFieldCode(), myEditText);
                lay_checkout.addView(myEditText);
            } else if (checkOut.getFieldType().equalsIgnoreCase("Dropdown")) {
                TextView label = new TextView(this);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        100
                );
                params1.setMargins(0, 50, 0, 0);
                label.setText(checkOut.getFieldName());
//                label.setTextColor(
//                        Color.rgb(Integer.parseInt((SingletonClass.getinstance().fetchBtnPrimaryColor())[0].trim()),
//                                Integer.parseInt((SingletonClass.getinstance().fetchBtnPrimaryColor())[1].trim()),
//                                Integer.parseInt((SingletonClass.getinstance().fetchBtnPrimaryColor())[2].trim())));
                lay_checkout.addView(label);
                spinner = new Spinner(this);
               /* ArrayAdapter<String> spinnerArrayAdapter = badgenew ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, checkOut.getFieldName());
                LinearLayout.LayoutParams params = badgenew LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                //params.setMargins(0, 10, 0, 0);
                spinner.setLayoutParams(params);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setTag(checkOut.shortName + i + "");
                tags.put("Dropdown" + i, spinner.getTag());

                dropDownList.put(checkOut.shortName, spinner);
                lay_checkout.addView(spinner);*/
            } else if (checkOut.getFieldType().equalsIgnoreCase("TextArea")) {
                myEditTextArea = new MaterialEditText(this);
                myEditTextArea.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                myEditTextArea.setHint(checkOut.getFieldName());
                myEditTextArea.setTag(checkOut.getFieldCode() + i + "");
                myEditTextArea.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
                myEditTextArea.setSingleLine();
                tags.put("TextArea" + i, myEditTextArea.getTag());
                textAreaList.put(checkOut.getFieldCode(), myEditTextArea);
                lay_checkout.addView(myEditTextArea);
            } else if (checkOut.getFieldType().equalsIgnoreCase("Label")) {
                TextView textview = new TextView(this);
                textview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textview.setText(controlValue.get(0));
                textview.setTextSize(14);
                //textview.setTextColor(ContextCompat.getColor(this, R.color.textColorPrimary));
                lay_checkout.addView(textview);
            }
        }
        final Button finalize = new Button(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);
        finalize.setLayoutParams(params);
        finalize.setText("Confirm Order");
        finalize.setAllCaps(true);
        finalize.setTypeface(typeface);
        finalize.setTextColor(getResources().getColor(R.color.white));
        finalize.setBackgroundColor(getResources().getColor(R.color.colorAccent));

      /*  finalize.setTextColor(
                Color.rgb(Integer.parseInt((SingletonClass.getinstance().fetchBtnForePrimaryColor())[0].trim()),
                        Integer.parseInt((SingletonClass.getinstance().fetchBtnForePrimaryColor())[1].trim()),
                        Integer.parseInt((SingletonClass.getinstance().fetchBtnForePrimaryColor())[2].trim())));
        finalize.setBackgroundResource(R.drawable.button_background);*/
        /*GradientDrawable drawable = (GradientDrawable) finalize.getBackground();
        drawable.setColor(Color.rgb(Integer.parseInt((SingletonClass.getinstance().fetchBtnPrimaryColor())[0].trim()),
                        Integer.parseInt((SingletonClass.getinstance().fetchBtnPrimaryColor())[1].trim()),
                        Integer.parseInt((SingletonClass.getinstance().fetchBtnPrimaryColor())[2].trim())));*/

        lay_checkout.addView(finalize);

        finalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keys = new ArrayList<String>();
                values = new ArrayList<String>();
                for (int i = 0; i < listCheckOut.size(); i++) {
                    CheckOut fields = (CheckOut) listCheckOut.get(i);
                    keys.add(fields.getFieldCode());
                    if (fields.getFieldCode().equalsIgnoreCase("melting_name")) {
                        userSessionManager.setMeting(textFieldList.get(keys.get(i)).getText().toString());
                    }
                }
                if (validation()) {
                    // placeOrder("1", "android", "", "", "", "");

                    /*for (int i = 0; i < keys.size(); i++) {
                        if (textFieldList.containsKey(keys.get(i)))
                             textFieldList.get(keys.get(i)).getText().toString());
                            //Log.wtf("!!Keys", keys.get(i) + "=" + textFieldList.get(keys.get(j)).getText().toString());
                       *//* else if (dropDownList.containsKey(keys.get(i)))
                            params.put(keys.get(i), dropDownList.get(keys.get(i)).getSelectedItem().toString());
                            //Log.wtf("!!Keys", keys.get(j) + "=" + dropDownList.get(keys.get(j)).getSelectedItem().toString());
                        else if (textAreaList.containsKey(keys.get(i)))
                            params.put(keys.get(i), textAreaList.get(keys.get(i)).getText().toString());
                        //Log.wtf("!!Keys", keys.get(j) + "=" + textAreaList.get(keys.get(j)).getText().toString());
*//*
                    }*/
                    placeOrder(SingletonSupport.getInstance().user_id, "android", textFieldList.get(keys.get(0)).getText().toString(),
                            textFieldList.get(keys.get(1)).getText().toString(), textFieldList.get(keys.get(2)).getText().toString(),
                            textFieldList.get(keys.get(3)).getText().toString(), textFieldList.get(keys.get(4)).getText().toString());
                }


            }
        });
       /* if (from != null && from.equalsIgnoreCase("orders")) {
            finalize.setVisibility(View.GONE);
        }
*/
    }


    public boolean validation() {
        boolean isValid = false;
        for (int i = 0; i < listCheckOut.size(); i++) {
            if ((listCheckOut.get(i).getFieldType().equalsIgnoreCase("Textfield")) && listCheckOut.get(i).getRequired().equalsIgnoreCase("1")) {
                if (textFieldList.get(listCheckOut.get(i).getFieldCode()).getText().toString().isEmpty()) {
                    textFieldList.get(listCheckOut.get(i).getFieldCode()).setError("Please enter " +
                            textFieldList.get(listCheckOut.get(i).getFieldCode()).getHint().toString());
                    textFieldList.get(listCheckOut.get(i).getFieldCode()).requestFocus();
                    isValid = false;
                    return isValid;
                } else {
                    isValid = true;

                }
            } else if ((listCheckOut.get(i).getFieldType().equalsIgnoreCase("Text_Area")) &&
                    listCheckOut.get(i).getRequired().equalsIgnoreCase("1")) {
                if (textAreaList.get(listCheckOut.get(i).getFieldCode()).getText().toString().isEmpty()) {
                    textAreaList.get(listCheckOut.get(i).getFieldCode()).setError("Please enter " +
                            textAreaList.get(listCheckOut.get(i).getFieldCode()).getHint().toString());
                    textAreaList.get(listCheckOut.get(i).getFieldCode()
                    ).requestFocus();
                    isValid = false;
                    return isValid;
                } else {
                    isValid = true;
                }
            } else {
                if (listCheckOut.get(i).getRequired().equalsIgnoreCase("0")) {
                    isValid = true;
                }
            }
        }
        return isValid;
    }


    public void placeOrder(String user_id, final String device, final String fullname,
                           final String emailid, final String mobile,
                           final String delivery_date, String remarks) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(CheckoutActivity.this);
        Call<Acknowledge> countriesCall = apiInterface.placeOrder(user_id, device, fullname, emailid, mobile, gift_id, delivery_date, "", "", remarks);
        countriesCall.enqueue(new Callback<Acknowledge>() {
            @Override
            public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                pdg.dismiss();
                Log.d("placeOrder", response.code() + "");
                final Acknowledge resource = response.body();
                if (resource.getAck().equals("1")) {
                    Toast.makeText(CheckoutActivity.this, resource.getMsg(), Toast.LENGTH_SHORT).show();
                    SingletonSupport.getInstance().cartCount = 0;
                    Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CheckoutActivity.this, resource.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Acknowledge> call, Throwable t) {
                pdg.dismiss();
                Log.d("placeOrder", t.toString() + "");
                call.cancel();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void datePicker() {

        Calendar newDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CheckoutActivity.this, this,
                newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH),
                newDate.get(Calendar.DAY_OF_MONTH));
        // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();

    }

    private int dateDifference(String dateStr, String enddate) {
        int count = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date sdate = sdf.parse(dateStr);
            Date edate = sdf.parse(enddate);
            long diff = edate.getTime() - sdate.getTime();
            float days = (diff / (1000 * 60 * 60 * 24));
            count = (int) days;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return count;
        //Toast.makeText(this, "Days "+ days, Toast.LENGTH_SHORT).show();        return days;
    }

    public String giveDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(cal.getTime());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String sMonth = ((month + 1) < 10) ? "0" + (month + 1) : (month + 1) + "";
        String sDay = (day < 10) ? "0" + day : day + "";
        int c = 0;

        String startdate = giveDate();
        String enddate = sDay + "/" + sMonth + "/" + year;
        c = dateDifference(startdate, enddate);

        if (c < 1) {
            Tools.showCustomDialog(CheckoutActivity.this, "Alert !",
                    "Delivery date must be greater than 1 days .");
        } else {
            for (int i = 0; i < listCheckOut.size(); i++) {
                CheckOut fields = (CheckOut) listCheckOut.get(i);
                if (fields.getFieldCode().equalsIgnoreCase("delivery_date")) {
                    textFieldList.get(fields.getFieldCode()).setText(sDay + "-" + sMonth + "-" + year);
                }
            }
        }
    }

}
