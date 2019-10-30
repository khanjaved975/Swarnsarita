package com.project.jewelmart.swarnsarita;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;

import com.google.firebase.messaging.FirebaseMessaging;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.Acknowledge;
import com.project.jewelmart.swarnsarita.pojo.CartCount;
import com.project.jewelmart.swarnsarita.utils.Constant;
import com.project.jewelmart.swarnsarita.utils.NotificationUtils;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;
import com.project.jewelmart.swarnsarita.widgets.FontTextView;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.worker.WorkerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2900;
    ProgressBar progressBar;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    UserSessionManager userSessionManager;
    @BindView(R.id.version)
    FontTextView version;
    boolean isregister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        userSessionManager = new UserSessionManager(this);
        PackageInfo pInfo = null;
        try {
            pInfo = SplashActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String ver = pInfo.versionName;
            version.setText("Version : " + ver);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Tools.isTablet(SplashActivity.this)) {
            SplashActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        } else {
            SplashActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Constant.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {
//                    String message = intent.getStringExtra("title");
//                    String order_id = intent.getStringExtra("order_id");
//                    String subtitle = intent.getStringExtra("subtitle");
//                    Tools.showCustomDialog(getApplicationContext(), message, subtitle);
                }
            }
        };

        if (!isregister) {
            displayFirebaseRegId();
        }

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                Intent i = null;
                if (CheckNetwork.isConnected(SplashActivity.this)) {
                  /*  if (isDeviceRooted()) {
                        Toast.makeText(SplashActivity.this, "You are not allowed to use the app on a rooted device", Toast.LENGTH_LONG).show();
                    } else {*/
                    SingletonSupport.getInstance().usertype = userSessionManager.getUserType();
                    SingletonSupport.getInstance().user_id = userSessionManager.getUserID();
                    if (userSessionManager.getIsUserActive().toLowerCase().equalsIgnoreCase("available")) {
                        //initFreshChat();
                        SingletonSupport.getInstance().isLogin = true;
                        if (SingletonSupport.getInstance().user_id.isEmpty()) {
                            i = new Intent(getApplicationContext(), LoginActivity.class);
                        } else {
                            if (SingletonSupport.getInstance().usertype.equalsIgnoreCase("worker")) {
                                i = new Intent(getApplicationContext(), WorkerActivity.class);
                            } else {
                                i = new Intent(getApplicationContext(), MainActivity.class);
                            }
                        }
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    } else {
                        if (SingletonSupport.getInstance().user_id.isEmpty()) {
                            i = new Intent(getApplicationContext(), LoginActivity.class);
                        } else {
                            if (SingletonSupport.getInstance().usertype.equalsIgnoreCase("worker")) {
                                SingletonSupport.getInstance().isLogin = true;
                                i = new Intent(getApplicationContext(), WorkerActivity.class);
                            } else {
                                SingletonSupport.getInstance().isLogin = false;
                                i = new Intent(getApplicationContext(), MainActivity.class);
                            }
                        }
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                    //  }
                } else {
                    Tools.showCustomDialog(SplashActivity.this, "Alert !", getResources().getString(R.string.no_internet));
                }
            }
        }, SPLASH_TIME_OUT);

    }

    private void displayFirebaseRegId() {
        isregister = true;
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constant.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e(TAG, "Firebase reg id: " + regId);
        SingletonSupport.getInstance().user_id = userSessionManager.getUserID();
        if (!SingletonSupport.getInstance().user_id.isEmpty()) {
            RegisterToken(userSessionManager.getUserID(), regId);
            sendEmail(userSessionManager.getUserID());
        }

    }

    public void initFreshChat() {
//        FreshchatConfig freshConfig = new2 FreshchatConfig(Constant.FRESHCHAT_APP_ID, Constant.FRESHCHAT_APP_KEY);
//        freshConfig.setCameraCaptureEnabled(true);
//        freshConfig.setGallerySelectionEnabled(true);
//        Freshchat.getInstance(getApplicationContext()).init(freshConfig);
//        FreshchatUser freshUser = Freshchat.getInstance(getApplicationContext()).getUser();
//        freshUser.setFirstName(userSessionManager.getUserFullName());
//        freshUser.setLastName(" ");
//        freshUser.setEmail(userSessionManager.getUserEmail());
//        freshUser.setPhone("+91", userSessionManager.getUserContact());
//        Freshchat.getInstance(getApplicationContext()).setUser(freshUser);
    }

    public static void getCartCount(Context con, String user_id, final String table) {
        APIInterface apiInterface;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CartCount> countriesCall = apiInterface.CartCount(user_id, table);
        countriesCall.enqueue(new Callback<CartCount>() {
            @Override
            public void onResponse(Call<CartCount> call, Response<CartCount> response) {
                Log.d("doGetProductGrid", response.code() + "");
                final CartCount resource = response.body();
                if (resource != null) {
                    if (resource.getCount() != null) {
                        SingletonSupport.getInstance().cartCount = resource.getCount();
                    }
                }
            }

            @Override
            public void onFailure(Call<CartCount> call, Throwable t) {
                Log.d("doGetProductGrid", t.toString() + "");
                call.cancel();
            }
        });

    }

    public void RegisterToken(String user_id, final String toke) {
        APIInterface apiInterface;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Acknowledge> countriesCall = apiInterface.RegisterToken(user_id, toke, userSessionManager.getUserType());
        countriesCall.enqueue(new Callback<Acknowledge>() {
            @Override
            public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                Log.d("doGetProductGrid", response.code() + "");
                final Acknowledge resource = response.body();
            }

            @Override
            public void onFailure(Call<Acknowledge> call, Throwable t) {
                Log.d("doGetProductGrid", t.toString() + "");
                call.cancel();
            }
        });

    }


    public void sendEmail(String user_id) {
        APIInterface apiInterface;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Acknowledge> countriesCall = apiInterface.getSendEmail(user_id);
        countriesCall.enqueue(new Callback<Acknowledge>() {
            @Override
            public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                Log.d("sendEmail", response.code() + "");
                final Acknowledge resource = response.body();
            }

            @Override
            public void onFailure(Call<Acknowledge> call, Throwable t) {
                Log.d("sendEmail", t.toString() + "");
                call.cancel();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());
        isregister = false;
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}
