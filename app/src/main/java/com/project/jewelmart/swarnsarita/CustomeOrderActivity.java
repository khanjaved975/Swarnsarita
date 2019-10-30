package com.project.jewelmart.swarnsarita;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.models.Result;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomeOrderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.fab_image)
    FloatingActionButton fab;

    @BindView(R.id.btn_order)
    Button btn_addorder;

    @BindView(R.id.et_grosswt)
    MaterialEditText et_grosswt;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.et_netwt)
    MaterialEditText et_netwt;

    @BindView(R.id.et_size)
    MaterialEditText et_size;

    @BindView(R.id.et_length)
    MaterialEditText et_length;

    @BindView(R.id.et_remarks)
    MaterialEditText et_remarks;

    @BindView(R.id.et_color)
    MaterialEditText et_color;

    @BindView(R.id.et_date)
    MaterialEditText et_date;

    @BindView(R.id.melting_spinner)
    Spinner melting_spinner;

    private final int SELECT_PICTURE = 1;
    private final int REQUEST_CAMERA = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private String imagesstring = "";
    String imageFilePath;
    Uri outputFileUri;
    private static File mediaFile, file = null;
    APIInterface apiInterface;
    Bitmap yourSelectedImage;

    private List<String> meltingList;
    private ArrayAdapter<String> meltingAdapter;
    private String strMelting = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_order);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontBoldTextView textView = (FontBoldTextView) findViewById(R.id.toolbar_title);
        textView.setText("Custom Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btn_addorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation(strMelting, et_grosswt, et_size)) {
                    if (CheckNetwork.isConnected(CustomeOrderActivity.this)) {
                        AddCustomOrder();
                    } else {
                        Tools.showCustomDialog(CustomeOrderActivity.this, "Alert !", "No Internet Connection , Please Try after Connecting");
                    }
                }
            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        if (SingletonSupport.getInstance().meltinglist != null) {
            meltingList = new ArrayList<>();
            meltingList.add("Select Melting");
            for (int i = 0; i < SingletonSupport.getInstance().meltinglist.size(); i++) {
                meltingList.add(SingletonSupport.getInstance().meltinglist.get(i).getMeltingName());
            }
            meltingAdapter = new ArrayAdapter<String>(CustomeOrderActivity.this, android.R.layout.simple_spinner_item, meltingList);
            meltingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params2.setMargins(0, 10, 0, 0);
            melting_spinner.setLayoutParams(params2);
            melting_spinner.setAdapter(meltingAdapter);
            melting_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        strMelting = "";
                    } else {
                        strMelting = SingletonSupport.getInstance().meltinglist.get(position - 1).getId();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            melting_spinner.setVisibility(View.GONE);
        }
        melting_spinner.setVisibility(View.GONE);
    }

    public void AddCustomOrder() {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(CustomeOrderActivity.this);
        File compressedImageFile = null;
        try {
            compressedImageFile = new Compressor(getApplicationContext()).compressToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), compressedImageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
       String color="";
       if (!et_color.getText().toString().isEmpty()){
           color=et_color.getText().toString();
       }
        Call<Result> countriesCall = apiInterface.CustomizedOrder(
                SingletonSupport.getInstance().user_id,
                et_grosswt.getText().toString(),
                "",
                "",
                strMelting,
                et_size.getText().toString(),
                color,
                et_remarks.getText().toString(),
                et_date.getText().toString(),
                body);
        countriesCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call,
                                   Response<Result> response) {
                Log.d("getCollection", response.code() + "");
                pdg.dismiss();
                try {
                    final Result resource = response.body();
                    if (resource.getAck() == 1) {
                        Tools.showCustomDialog(CustomeOrderActivity.this, "Response", resource.getMsg());
                        ResetForm();
                    } else {
                        Tools.showCustomDialog(CustomeOrderActivity.this, "Response", resource.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.d("Add custome order", t.toString() + "");
                Toast.makeText(CustomeOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                pdg.dismiss();
                call.cancel();
            }

        });

    }

    private String getRealPathFromURIPath(Uri contentURI) {
        Cursor cursor = CustomeOrderActivity.this.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void datePicker() {

        Calendar newDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CustomeOrderActivity.this, this,
                newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH),
                newDate.get(Calendar.DAY_OF_MONTH));
        // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (outputFileUri != null) {
                try {
                    String strPath = getRealPathFromURIPath(outputFileUri);
                    file = new File(strPath);
                    InputStream imageStream = null;
                    imageStream = getContentResolver().openInputStream(outputFileUri);
                    yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    image.setImageBitmap(yourSelectedImage);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    if (yourSelectedImage!=null) {
                        yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        byte[] imageBytes = baos.toByteArray();
                        imagesstring = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        mediaFile = new File(imagesstring);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    try {
                        String strPath = getRealPathFromURIPath(selectedImage);
                        file = new File(strPath);
                        InputStream imageStream = null;
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        image.setImageBitmap(yourSelectedImage);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        byte[] imageBytes = baos.toByteArray();
                        imagesstring = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        mediaFile = new File(imagesstring);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private void chooseImage() {
//"Take Photo",
        final CharSequence[] options = {"Choose from Gallery", "Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomeOrderActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    openCameraIntent();
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_PICTURE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openCameraIntent() {
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            outputFileUri = Uri.fromFile(createImageFile());
            i.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(i, REQUEST_CAMERA);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        Log.d("MYIMAGEPATH", imageFilePath);
        return image;
    }

    public boolean validation(String strmelting,
                               MaterialEditText grossWeight, MaterialEditText size) {
       // String strlength = Length.getText().toString();
        String strgrossWeight = grossWeight.getText().toString();
        String strsize = size.getText().toString();

        float fgrosswt=0.0f, fnetwt=0.0f;
        if (!strgrossWeight.isEmpty()) {
            fgrosswt = Float.valueOf(strgrossWeight);
        }
        /*if (!strnetWeight.isEmpty()) {
            fnetwt = Float.valueOf(strnetWeight);
        }*/

       /* if (fnetwt > fgrosswt) {
            netWeight.setError("Net weight can not be greater than gross weight");
            netWeight.requestFocus();
            return false;
        }

        if (strmelting == null || strmelting.equals("")) {
            Toast.makeText(CustomeOrderActivity.this, "Select Melting", Toast.LENGTH_LONG).show();
            return false;
        }*/

        if (strgrossWeight == null || strgrossWeight.equals("")) {
            grossWeight.setError("Enter Gross Weight");
            grossWeight.requestFocus();
            return false;
        }

      /*
        if (strlength == null || strlength.equals("")) {
            Length.setError("Enter Length");
            Length.requestFocus();
            return false;
        }

         if (strsize == null || strsize.equals("")) {
            size.setError("Enter Size");
            size.requestFocus();
            return false;
        }
        */

        /*if (strnetWeight == null || strnetWeight.equals("")) {
            netWeight.setError("Enter Net Weight");
            netWeight.requestFocus();
            return false;
        }*/

        if (file == null) {
            Toast.makeText(CustomeOrderActivity.this, "Please Select Image", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void ResetForm() {
        et_netwt.setText("");
       // et_grosswt.setText("");
       // et_length.setText("");
        et_size.setText("");
        et_color.setText("");
        et_remarks.setText("");
        et_date.setText("");
        melting_spinner.setSelection(0);
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

        if (c < 7) {
            Tools.showCustomDialog(CustomeOrderActivity.this, "Alert !",
                    "Delivery date must be greater than 7 days .");
        } else {
            et_date.setText(sDay + "-" + sMonth + "-" + year);
        }
    }

}
