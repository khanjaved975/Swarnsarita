package com.project.jewelmart.swarnsarita;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.pojo.Acknowledge;
import com.project.jewelmart.swarnsarita.utils.AmazonS3Helper;
import com.project.jewelmart.swarnsarita.utils.Constant;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScreenShotService extends Service {

    public FileObserver observer;
   /// NetworkCommunication net;
    private String error, status = "";
    private String ack;
    private String screenname = "", prodId = "", model = "";
    // The TransferUtility is the primary class for managing transfer to S3
    private TransferUtility transferUtility;
    // A List of all transfers
    private List<TransferObserver> observers;
    private File screenshots;
    private ContentObserver cObserver;
    private HandlerThread handlerThread;
    public static boolean isSend;

    public static final String FILE_POSTFIX = "FROM_ASS";
    private static final long DEFAULT_DETECT_WINDOW_SECONDS = 10;


    private static final String EXTERNAL_CONTENT_URI_MATCHER =
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();
    private static final String[] PROJECTION = new String[] {
            MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED,MediaStore.Images.ImageColumns._ID
    };
    private static final String SORT_ORDER = MediaStore.Images.Media.DATE_ADDED + " DESC";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Log.e("Service", "start");
        if (intent != null) {
            screenname = intent.getStringExtra("ScreenName");
            prodId = intent.getStringExtra("ProductId");
            model = intent.getStringExtra("Model");
        }

        File pix = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        screenshots = new File(pix, "Screenshots");

        handlerThread = new HandlerThread("content_observer");
        handlerThread.start();
        final Handler handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        cObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                Log.d("MYSERVICE", "URL : " + uri.toString());
                  //  SingletonSupport.getInstance().screenshotfilename = getFilePath(getApplicationContext(), uri);
                // SingletonSupport.getInstance().screenshotfilename = FilePath.getPath(getApplicationContext(), uri);
                Data result = null;
                try {
                    result = getLatestData(getApplicationContext(), uri);
                    if (result != null) {
                        SingletonSupport.getInstance().screenshotfilename = result.path;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (result!=null) {
                    if (!SingletonSupport.getInstance().user_id.isEmpty() &&
                            !SingletonSupport.getInstance().screenshotfilename.isEmpty()) {
                    long currentTime = System.currentTimeMillis() / 1000;
                    if (matchTime(currentTime, result.dateAdded)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                        String dateTime = sdf.format(new Date());
                        String filename = SingletonSupport.getInstance().user_id +
                                "-" + dateTime + ".jpeg";
                        if (filename.contains(SingletonSupport.getInstance().user_id)) {
                            if (!isSend) {
                                isSend=true;
                                Screenshot(SingletonSupport.getInstance().user_id,filename);
                                //capture(SingletonSupport.getInstance().user_id, prodId, screenname, model, filename);
                            }
                        }
                    }
                }
            }
                super.onChange(selfChange, uri);
            }
        };
        getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, cObserver);
        return START_STICKY;
    }


    private Data getLatestData(Context context,Uri uri) throws Exception {
        Data data = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, PROJECTION, null, null, SORT_ORDER);
            if (cursor != null && cursor.moveToFirst()) {
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                long dateAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));

                if (fileName.contains("Screenshot")) {
                    //if (cursor.moveToNext()) {
                       /* id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                        fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        dateAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));*/
                        data = new Data();
                        data.id = id;
                        data.fileName = fileName;
                        data.path = path;
                        data.dateAdded = dateAdded;
                   /* } else {
                        return null;
                    }*/
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return data;
    }


    private boolean matchTime(long currentTime, long dateAdded) {
        return Math.abs(currentTime - dateAdded) <= DEFAULT_DETECT_WINDOW_SECONDS;
    }

    class Data {
        long id;
        String fileName;
        String path;
        long dateAdded;
    }

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Service", "stop");

       // if ( getContentResolver()!=null) {
//            getContentResolver().unregisterContentObserver(cObserver);
      //  }
//        observer.stopWatching();
        // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }


    public void setFileToUpload(String userId, String filename, File fileToUpload) {
        Log.e("ScreenShot Service", "Uploading Screenshot - " + filename);
        AmazonS3Helper s3Helper = new AmazonS3Helper(getApplicationContext());
        // Initializes TransferUtility, always do this before using it.
        transferUtility = s3Helper.getTransferUtility(this);
        try {
            fileToUpload = AmazonS3Helper.copyContentUriToFile(getApplicationContext(), Uri.fromFile(fileToUpload));
            TransferObserver observer = transferUtility.upload(Constant.BUCKET_NAME,
                    filename, fileToUpload);
        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
            observer.setTransferListener(new UploadListener());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Screenshot(final String user_id,final String image_name) {
        APIInterface apiInterface;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Acknowledge> countriesCall = apiInterface.pushScreenshot(user_id,image_name);
        countriesCall.enqueue(new Callback<Acknowledge>() {
            @Override
            public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                Log.d("TAG", response.code() + "");
                Acknowledge resource = response.body();
                if (resource.getAck().toString().equals("1")) {
                    setFileToUpload(SingletonSupport.getInstance().user_id, image_name, new File(SingletonSupport.getInstance().screenshotfilename));
                    Intent i1 = new Intent(getBaseContext(), ServiceAlert.class);
                    i1.putExtra("ack", "1");
                    i1.putExtra("error",resource.getMsg());
                    i1.putExtra("status", "Blocked");
                    i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i1);
                    // Toast.makeText(getApplicationContext(), resource.getMsg(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), resource.getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Acknowledge> call, Throwable t) {
                call.cancel();
            }
        });
    }



/*
    public void capture(final String userId, final String prodId, final String screenname, final String model, final String fileName) {
        StringRequest postRequest = badgenew StringRequest(Request.Method.POST, net.Server + net.Folder + "Screenshot",
                badgenew Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = badgenew JSONObject(response);
                            Log.d("!!!!", "" + jsonResponse);
                             if (fileName.contains(SingletonClass.getinstance().userId)) {
                            setFileToUpload(SingletonClass.getinstance().userId, fileName, badgenew File(SingletonClass.getinstance().filename));
                             }

                            if (jsonResponse != null) {
                                ack = jsonResponse.getString("ack");
                                error = jsonResponse.getString("error");
                                if (jsonResponse.has("status")) {
                                    status = jsonResponse.getString("status");
                                }
//                                Toast.makeText(getApplicationContext(), error,
//                                        Toast.LENGTH_LONG).show();
                                Intent i1 = badgenew Intent(getBaseContext(), ServiceAlert.class);
                                i1.putExtra("ack", ack);
                                i1.putExtra("error", error);
                                i1.putExtra("status", status);
                                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i1);
                            } else {
                                Toast.makeText(getApplicationContext(), "Server error, Please try after some time",
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                badgenew Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = badgenew HashMap<>();
                // the POST parameters:
                params.put("user_id", userId);
                params.put("item_code", prodId);
                params.put("screen_name", screenname);
                params.put("device_model", model);
                params.put("image_name", fileName);
                Log.wtf("Network", "Link : " + net.Server + net.Folder + "Screenshot " + params);
                return params;
            }
        };
        postRequest.setRetryPolicy(badgenew DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(postRequest);

    }
*/


    private class UploadListener implements TransferListener {

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e("UPLOAD S3", "Error during upload: " + id, e);
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d("UPLOAD S3", String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d("UPLOAD S3", "onStateChanged: " + id + ", " + newState);
        }
    }

}
