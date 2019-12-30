package com.project.jewelmart.swarnsarita;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.freshchat.consumer.sdk.Freshchat;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.jewelmart.swarnsarita.adapters.AdapterAccountDetailsList;
import com.project.jewelmart.swarnsarita.adapters.AdapterCallEmailList;
import com.project.jewelmart.swarnsarita.adapters.LeftDrawerAdapter;
import com.project.jewelmart.swarnsarita.fragments.HomeFragment;
import com.project.jewelmart.swarnsarita.fragments.SubcatFragment;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.pojo.Collection;
import com.project.jewelmart.swarnsarita.models.LeftDrawer;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.AccountDetails;
import com.project.jewelmart.swarnsarita.pojo.Appdata;
import com.project.jewelmart.swarnsarita.pojo.CallEmail;
import com.project.jewelmart.swarnsarita.pojo.CartCount;
import com.project.jewelmart.swarnsarita.pojo.CatalogueModel;
import com.project.jewelmart.swarnsarita.pojo.SortList;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.utils.MenuItemBadge;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;
import com.project.jewelmart.swarnsarita.utils.Utils;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawer;
    private AHBottomNavigation bottomNavigation;
    NavigationView leftNavigationView;
    private static LeftDrawerAdapter adapterlist;
    private static List<LeftDrawer> dataList;
    static RecyclerView list_nav;
    List<Collection> parentlist;
    public APIInterface apiInterface;
    FragmentManager frgManager;
    FragmentTransaction frgTransaction;
    private SubcatFragment fragment1;
    TextView user_name, user_email, user_login;
    String catId = "";
    UserSessionManager userSessionManager;
    Fragment fragment;
    private List<com.project.jewelmart.swarnsarita.pojo.Collection.Subcategory> subCatList;
    LinearLayout chat, mycollection, logout, callus,
            ll_customer_order, catalogue, aboutus, contactus,
            notification, offers, account;
    FontBoldTextView txtlogout, txtcontactus;
    CircularImageView avatar;
    boolean isrefresh = false;
    MenuItem menuItemNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FontBoldTextView textView = (FontBoldTextView) findViewById(R.id.toolbar_title);
        textView.setText(getResources().getString(R.string.app_name));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        avatar = (CircularImageView) findViewById(R.id.avatar);
        Glide.with(this).load(R.mipmap.ic_launcher)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(avatar);
        Intent intent = new Intent(this, ScreenShotService.class);
        if (Utils.isMyServiceRunning(ScreenShotService.class, this)) {
            stopService(intent);
            intent.putExtra("ScreenName", "Homescreen");
            intent.putExtra("ProductId", "");
            intent.putExtra("Model", "");
            startService(intent);
        } else {
            intent.putExtra("ScreenName", "Homescreen");
            intent.putExtra("ProductId", "");
            intent.putExtra("Model", "");
//            startService(intent);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        user_name = (TextView) findViewById(R.id.user_name);
        user_login = (TextView) findViewById(R.id.user_login);
        txtlogout = (FontBoldTextView) findViewById(R.id.txt_logout);
        txtcontactus = (FontBoldTextView) findViewById(R.id.txtcontactus);
        user_email = (TextView) findViewById(R.id.email);
        userSessionManager = new UserSessionManager(this);
        leftNavigationView = (NavigationView) findViewById(R.id.nav_left_view);
        list_nav = (RecyclerView) findViewById(R.id.list_leftview_nav);
        list_nav.setLayoutManager(new LinearLayoutManager(this));
        list_nav.setHasFixedSize(true);
        if (SingletonSupport.getInstance().isLogin) {
            if (!CheckNetwork.isConnected(getApplicationContext())) {
                Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
            } else {
                getCartCount(SingletonSupport.getInstance().user_id, "cart");
            }
        }

        // OverScrollDecoratorHelper.setUpOverScroll(list_nav, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        //getCartCount(userSessionManager.getUserID(),"cart");

        if (!CheckNetwork.isConnected(getApplicationContext())) {
            Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
        } else {
            getDrawerList(userSessionManager.getUserID());
        }

        initUI();
        if (SingletonSupport.getInstance().sortLists == null) {
            if (!CheckNetwork.isConnected(getApplicationContext())) {
                Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
            } else {
                getSortList();
            }
        }

    }

    public void setDrawer(List<com.project.jewelmart.swarnsarita.pojo.Collection> List, Context context) {
        dataList = new ArrayList<LeftDrawer>();
        dataList.clear();
        if (List != null) {
            for (int i = 0; i < List.size(); i++) {
                com.project.jewelmart.swarnsarita.pojo.Collection col = List.get(i);
                dataList.add(new LeftDrawer(col.getColName(), "25"));
            }
            adapterlist = new LeftDrawerAdapter(context, dataList, ItemAnimation.LEFT_RIGHT);
            list_nav.setAdapter(adapterlist);
            adapterlist.setOnItemClickListener(new LeftDrawerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, LeftDrawer obj, int position) {
                    selectItem(position);
                }
            });
        }
        if (SingletonSupport.getInstance().designcollectionlist == null) {
            SingletonSupport.getInstance().designcollectionlist = List;
        } else if (SingletonSupport.getInstance().designcollectionlist.isEmpty()) {
            SingletonSupport.getInstance().designcollectionlist = List;
        }
    }


    public void getDrawerList(String user_id) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<com.project.jewelmart.swarnsarita.pojo.Collection>> countriesCall;
        if (SingletonSupport.getInstance().isLogin) {
            countriesCall = apiInterface.doGetDrawerList(user_id);
        } else {
            countriesCall = apiInterface.doGetDrawerList();
        }
        countriesCall.enqueue(new Callback<List<com.project.jewelmart.swarnsarita.pojo.Collection>>() {
            @Override
            public void onResponse(Call<List<com.project.jewelmart.swarnsarita.pojo.Collection>> call,
                                   Response<List<com.project.jewelmart.swarnsarita.pojo.Collection>> response) {
                try {
                    Log.d("Main Activity : ", response.code() + "");
                    //ArrayList<Collection> colList = badgenew ArrayList<Collection>();
                    List<Collection> list = response.body();
                    parentlist = list;
                    SingletonSupport.getInstance().designcollectionlist = parentlist;
                    setDrawer(list, MainActivity.this);
                    if (isrefresh) {
                        HomeFragment homeFragment = new HomeFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frams, homeFragment)
                                .addToBackStack(null)
                                .commit();
                    }
               /* try {
                    jsonResponse = badgenew JSONArray(str);
                    //jsonResponse = response.body();
                    Log.wtf("Response", "Result : " + jsonResponse);
                    for (int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject job = jsonResponse.getJSONObject(i);
                        Collection col = badgenew Collection();
                        col.id = job.getString("id");
                        col.col_name = job.getString("col_name").toLowerCase();
                        col.short_code = job.getString("short_code");
                        col.position = job.getString("position");
                        // cat.count = job.getString("product_count");
                        JSONArray subcol = job.getJSONArray("subcategory");
                        col.subcol = badgenew ArrayList<Collection>();
                        if (subcol.length() > 0) {
                            Collection catAll = badgenew Collection();
                            catAll.id = col.id;
                            catAll.col_name = "All";
                            catAll.short_code = "All";
                            catAll.position = "-1";
                            catAll.count = col.count;
                            catAll.subcol = badgenew ArrayList<Collection>();
                            col.subcol.add(catAll);
                            for (int j = 0; j < subcol.length(); j++) {
                                JSONObject job1 = subcol.getJSONObject(j);
                                createSubCollection(job1, col.subcol);
                            }
                        }
                        colList.add(col);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                collectionList = colList;
                SingletonSupport.getInstance().designcollectionlist = colList;
                setDrawer(colList, MainActivity.this);
                */
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<com.project.jewelmart.swarnsarita.pojo.Collection>> call, Throwable t) {
                Log.d("Main Activity : ", t.toString() + "");
                call.cancel();
            }

        });

    }


    public void selectItem(int position) {
        fragment = null;
        //—args = badgenew Bundle();
        Log.d("In MainActivity", "Position :: " + position);
        String category_names[] = null;
        String[] image_type = null;
        if (position < dataList.size()) {
            String selected = dataList.get(position).getName();
            subCatList = parentlist.get(position).getSubcategory();
            catId = parentlist.get(position).getId();
            //catProductCount = "" + parentlist.get(position).count;
            //imagetype = "" + catMainList.get(position).image_type;
            // sortPosition = "" + catMainList.get(position).default_sort;
            if (subCatList != null && !subCatList.isEmpty()) {
                addFragWithArray(subCatList, selected);
            } else {
                if (!CheckNetwork.isConnected(getApplicationContext())) {
                    Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
                } else {
                    Intent intent = new Intent(MainActivity.this, ProductGridActivity.class);
                    intent.putExtra("collection_id", catId);
                    intent.putExtra("name", parentlist.get(position).getColName());
                    intent.putExtra("mode_type", "normal");
                    intent.putExtra("table", "product_master");
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
            invalidateOptionsMenu();
        }
    }

    private void addFragWithArray(List<com.project.jewelmart.swarnsarita.pojo.Collection.Subcategory> subCatList,
                                  String selected) {
        frgManager = getSupportFragmentManager();
        fragment1 = new SubcatFragment();
        frgTransaction = frgManager.beginTransaction();
        //frgManager.popBackStack();
        Bundle bundle = new Bundle();
        bundle.putSerializable("subcat", (Serializable) subCatList);
        bundle.putString("selectedCatName", selected);
        fragment1.setArguments(bundle);
        frgTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        frgTransaction.replace(R.id.mainListFrame, fragment1);
        frgTransaction.addToBackStack("subcat1");
        frgTransaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void initUI() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }

        if (SingletonSupport.getInstance().isLogin) {
            user_login.setVisibility(View.GONE);
            user_name.setText(userSessionManager.getUserFullName());
            user_email.setText(userSessionManager.getUserEmail());
        } else {
            user_login.setVisibility(View.VISIBLE);
            user_name.setText("");
            user_email.setText("");
            user_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            });
        }

        if (!SingletonSupport.getInstance().isLogin) {
            txtlogout.setText("Login");
        } else {
            txtlogout.setText("Logout");
        }
        txtcontactus.setText("Office Address");
        chat = (LinearLayout) findViewById(R.id.chat);
        offers = (LinearLayout) findViewById(R.id.ll_offers);
        account = (LinearLayout) findViewById(R.id.ll_account);
        logout = (LinearLayout) findViewById(R.id.ll_logout);
        callus = (LinearLayout) findViewById(R.id.callus);
        mycollection = (LinearLayout) findViewById(R.id.mycollection);
        contactus = (LinearLayout) findViewById(R.id.contactus);
        aboutus = (LinearLayout) findViewById(R.id.aboutus);
        catalogue = (LinearLayout) findViewById(R.id.collection);
        ll_customer_order = (LinearLayout) findViewById(R.id.ll_custom_order);
        notification = (LinearLayout) findViewById(R.id.notification);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingletonSupport.getInstance().isLogin) {
                    Freshchat.showConversations(MainActivity.this);
                } else {
                    // Tools.setSnackBar(drawer, "Please Login");
                    snake();
                }
                hadleDrawer();

            }
        });


        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingletonSupport.getInstance().isLogin) {
                    startActivity(new Intent(MainActivity.this, OffersActivity.class));
                } else {
                    //Tools.setSnackBar(drawer, "Please Login");
                    snake();
                }
                hadleDrawer();

            }
        });

        offers.setVisibility(View.GONE);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingletonSupport.getInstance().isLogin) {
                    if (!CheckNetwork.isConnected(getApplicationContext())) {
                        Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
                    } else {
                        getAccountDetails();
                    }
                } else {
                    //Tools.setSnackBar(drawer, "Please Login");
                    snake();
                }
                hadleDrawer();

            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckNetwork.isConnected(getApplicationContext())) {
                    Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
                } else {
                    Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                    startActivity(intent);
                }
                hadleDrawer();

            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.showCustomDialog(MainActivity.this, "Contact Us", getResources().getString(R.string.contact_us));
                hadleDrawer();
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                //Tools.showCustomDialog(MainActivity.this, "About Us", getResources().getString(R.string.aboutus));
                hadleDrawer();
            }
        });

        callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckNetwork.isConnected(getApplicationContext())) {
                    Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
                } else {
                    getCallEmail();
                }
                hadleDrawer();
            }
        });

        catalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckNetwork.isConnected(getApplicationContext())) {
                    Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
                } else {
                    getCatalogue();
                }
                hadleDrawer();
            }
        });

        catalogue.setVisibility(View.GONE);


        ll_customer_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingletonSupport.getInstance().isLogin) {
                    Intent intent = new Intent(MainActivity.this, CustomeOrderActivity.class);
                    startActivity(intent);
                } else {
                    //Tools.setSnackBar(drawer, "Please Login");
                    snake();
                }

            }
        });

        mycollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingletonSupport.getInstance().isLogin) {
                    Intent i = new Intent(MainActivity.this, MyCollectionActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    // Tools.setSnackBar(drawer, "Please Login");
                    snake();
                }
                hadleDrawer();
            }
        });

        mycollection.setVisibility(View.GONE);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //userSessionManager.Logout(MainActivity.this);
                if (!SingletonSupport.getInstance().isLogin) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    Tools.showDialogLogout(MainActivity.this, "Alert !", "Are you sure you want to logout ?");
                }
                /*Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();*/
                hadleDrawer();
            }
        });
        setBottomBar();
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frams, homeFragment)
                .addToBackStack(null)
                .commit();

        if (SingletonSupport.getInstance().meltinglist == null ||
                SingletonSupport.getInstance().colors == null ||
                SingletonSupport.getInstance().tone == null ||
                SingletonSupport.getInstance().polish == null) {
            getAppdata();
        } else if (SingletonSupport.getInstance().meltinglist.isEmpty() ||
                SingletonSupport.getInstance().colors.isEmpty() ||
                SingletonSupport.getInstance().tone.isEmpty() ||
                SingletonSupport.getInstance().polish.isEmpty()) {
            getAppdata();
        }

        if (userSessionManager.getISNotification()) {
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
            userSessionManager.setISNotifiacation(false);
        }
    }

    public void setBottomBar() {
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation1);
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_home);
        bottomNavigation.addItem(item1);

        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Cart", R.drawable.ic_action_cart);
        bottomNavigation.addItem(item2);

        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Custom Order", R.drawable.ic_menu_send);
        bottomNavigation.addItem(item4);

     /*   AHBottomNavigationItem item5 = badgenew AHBottomNavigationItem("Chat", R.drawable.ic_action_message);
        bottomNavigation.addItem(item5);*/

        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Account", R.drawable.ic_action_account);
        bottomNavigation.addItem(item3);
        bottomNavigation.setDefaultBackgroundColor(Color.WHITE);
        bottomNavigation.setAccentColor(Tools.fetchColor(MainActivity.this, R.color.colorAccent));
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                final Intent intent;
                switch (position) {
                    case 0:
                        break;

                    case 1:
                        if (SingletonSupport.getInstance().isLogin) {
                            Intent i = new Intent(MainActivity.this, com.project.jewelmart.swarnsarita.CartActivity.class);
                            startActivity(i);
                        } else {
                            //Tools.setSnackBar(drawer, "Please Login");
                            snake();
                        }
                        break;

                    case 2:
                        if (SingletonSupport.getInstance().isLogin) {
                            intent = new Intent(MainActivity.this, CustomeOrderActivity.class);
                            startActivity(intent);
                        } else {
                            snake();
                        }
                        break;

                   /* case 3:

                        if (SingletonSupport.getInstance().isLogin) {
                            Freshchat.showConversations(MainActivity.this);
                        } else {
                            Tools.setSnackBar(drawer, "Please Login");
                        }
                        break;*/

                    case 3:
                        intent = new Intent(getApplicationContext(), MyAccountActivity.class);
                        startActivity(intent);

                        break;

                    default:
                        break;
                }
                return wasSelected;
            }
        });
        if (SingletonSupport.getInstance().isLogin && SingletonSupport.getInstance().cartCount != 0) {
            setUpBadge(String.valueOf(SingletonSupport.getInstance().cartCount));
        } else {
            setUpBadge("0");
        }

    }

    private void setUpBadge(final String count) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AHNotification notification = new AHNotification.Builder()
                        .setText(count)
                        .setBackgroundColor(Tools.fetchColor(MainActivity.this, R.color.colorAccent))
                        .setTextColor(Color.WHITE)
                        .build();
                bottomNavigation.setNotification(notification, 1);
                //  pref.setNotificationVisible(true);
            }
        }, 1000);


    }

    public void getSortList() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<SortList>> countriesCall = apiInterface.getSortList();
        countriesCall.enqueue(new Callback<List<SortList>>() {
            @Override
            public void onResponse(Call<List<SortList>> call,
                                   Response<List<SortList>> response) {

                Log.d("Main Activity : ", response.code() + "");
                //ArrayList<Collection> colList = badgenew ArrayList<Collection>();
                List<SortList> list = response.body();
                SingletonSupport.getInstance().sortLists = list;
                SingletonSupport.getInstance().sort = list.get(0).getValue().toString();
            }

            @Override
            public void onFailure(Call<List<SortList>> call, Throwable t) {
                Log.d("Main Activity : ", t.toString() + "");
                call.cancel();
            }
        });

    }

    public void getCatalogue() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(MainActivity.this);

        Call<CatalogueModel> countriesCall = apiInterface.GetCatalogue();
        countriesCall.enqueue(new Callback<CatalogueModel>() {
            @Override
            public void onResponse(Call<CatalogueModel> call,
                                   Response<CatalogueModel> response) {
                Log.d("CallEmail Activity : ", response.code() + "");
                pdg.dismiss();
                CatalogueModel catalogueModel = response.body();
                List<String> list = new ArrayList<>();
                if (catalogueModel.getCatalogueSlider() != null) {
                    if (!catalogueModel.getCatalogueSlider().isEmpty()) {
                        for (int i = 0; i < catalogueModel.getCatalogueSlider().size(); i++) {
                            list.add(catalogueModel.getCatalogueSlider().get(i).getImageName());
                        }
                        if (list != null) {
                            Intent intent = new Intent(MainActivity.this, LargeViewActivity.class);
                            intent.putExtra("image_list", (Serializable) list);
                            intent.putExtra("path", catalogueModel.getImagePath());
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "No Data found", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No Data found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CatalogueModel> call, Throwable t) {
                Log.d("CallEmail Activity : ", t.toString() + "");
                call.cancel();
                pdg.dismiss();
            }
        });

    }

    public void hadleDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {  /*Closes the Appropriate Drawer*/
            drawer.closeDrawer(GravityCompat.END);
        }
    }

    public void getCallEmail() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(MainActivity.this);

        Call<List<CallEmail>> countriesCall = apiInterface.getCallEmail();
        countriesCall.enqueue(new Callback<List<CallEmail>>() {
            @Override
            public void onResponse(Call<List<CallEmail>> call,
                                   Response<List<CallEmail>> response) {
                try {
                    Log.d("CallEmail Activity : ", response.code() + "");
                    pdg.dismiss();
                    //ArrayList<Collection> colList = badgenew ArrayList<Collection>();
                    List<CallEmail> list = response.body();
                    showCustomDialog(list);
                    //SingletonSupport.getInstance().sortLists = list;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<CallEmail>> call, Throwable t) {
                Log.d("CallEmail Activity : ", t.toString() + "");
                call.cancel();
                pdg.dismiss();

            }
        });

    }

    public void showCustomDialog(List<CallEmail> list) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        // View view = getLayoutInflater().inflate(R.layout.dialog_call_email, null);

        dialog.setContentView(R.layout.dialog_call_email);
        dialog.setCancelable(true);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        int animation_type = ItemAnimation.BOTTOM_UP;
        AdapterCallEmailList adapterCallEmailList = new
                AdapterCallEmailList(MainActivity.this, list, animation_type);
        recyclerView.setAdapter(adapterCallEmailList);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void snake() {
        Snackbar snackbar = Snackbar
                .make(drawer, "Please Login", Snackbar.LENGTH_LONG)
                .setAction("LOGIC", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public void showAccountDialog(List<AccountDetails.Datum> list) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        // View view = getLayoutInflater().inflate(R.layout.dialog_call_email, null);
        dialog.setContentView(R.layout.dialog_call_email);
        dialog.setCancelable(true);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        FontBoldTextView fontBoldTextView = (FontBoldTextView) dialog.findViewById(R.id.cancel);
        fontBoldTextView.setText("Account Details");
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        int animation_type = ItemAnimation.BOTTOM_UP;
        AdapterAccountDetailsList adapterCallEmailList = new
                AdapterAccountDetailsList(MainActivity.this, list, animation_type);
        recyclerView.setAdapter(adapterCallEmailList);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void getAccountDetails() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(MainActivity.this);

        Call<AccountDetails> countriesCall = apiInterface.getAccountDetails();
        countriesCall.enqueue(new Callback<AccountDetails>() {
            @Override
            public void onResponse(Call<AccountDetails> call,
                                   Response<AccountDetails> response) {
                try {
                    Log.d("CallEmail Activities: ", response.code() + "");
                    pdg.dismiss();
                    //ArrayList<Collection> colList = badgenew ArrayList<Collection>();
                    AccountDetails accountDetails = response.body();
                    if (accountDetails.getAck().toString().equalsIgnoreCase("1")) {
                        List<AccountDetails.Datum> list = accountDetails.getData();
                        showAccountDialog(list);
                    } else {
                        Tools.showCustomDialog(MainActivity.this, "Alert !", accountDetails.getMsg());
                    }

                    //SingletonSupport.getInstance().sortLists = list;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AccountDetails> call, Throwable t) {
                Log.d("CallEmail Activity : ", t.toString() + "");
                call.cancel();
                pdg.dismiss();

            }
        });

    }

    public void getCartCount(String user_id, final String table) {
        APIInterface apiInterface;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        // final ProgressDialog pdg = ProgressIndicator.ShowLoading(con);
        Call<CartCount> countriesCall = apiInterface.CartCount(user_id, table);
        countriesCall.enqueue(new Callback<CartCount>() {
            @Override
            public void onResponse(Call<CartCount> call, Response<CartCount> response) {
                // pdg.dismiss();
                Log.d("doGetProductGrid", response.code() + "");
                final CartCount resource = response.body();
                SingletonSupport.getInstance().cartCount = resource.getCount();
                if (SingletonSupport.getInstance().isLogin && SingletonSupport.getInstance().cartCount != 0) {
                    setUpBadge(String.valueOf(SingletonSupport.getInstance().cartCount));
                }

            }

            @Override
            public void onFailure(Call<CartCount> call, Throwable t) {
                // pdg.dismiss();
                Log.d("doGetProductGrid", t.toString() + "");
                call.cancel();
            }
        });

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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {  /*Closes the Appropriate Drawer*/
            drawer.closeDrawer(GravityCompat.END);
        } else {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.dialog_info);
            dialog.setCancelable(true);
            TextView title = (TextView) dialog.findViewById(R.id.title);
            title.setText("Alert !");
            TextView content = (TextView) dialog.findViewById(R.id.content);
            content.setText("Are you sure you want to close this APP?");
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setText("OK");
            ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* Intent intent=badgenew Intent(getActivity(), CheckoutActivity.class);
                startActivity(intent);*/
                    dialog.onBackPressed();
                    dialog.dismiss();
                    System.exit(0);
                    //  overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        int color = Color.parseColor(getResources().getString(R.string.color));
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
        menu.findItem(R.id.action_hometheme).setVisible(false);
        menuItemNotification = menu.findItem(R.id.action_notification);
        MenuItemBadge.update(this, menuItemNotification, new MenuItemBadge.Builder()
                .iconDrawable(ContextCompat.getDrawable(this, R.drawable.ic_notification))
                .iconTintColor(color)
                .textBackgroundColor(getResources().getColor(R.color.colorAccent))
                .textColor(Color.WHITE));
        if (userSessionManager.getNotification().equalsIgnoreCase("yes")) {
            MenuItemBadge.getBadgeTextView(menuItemNotification).
                    setBadgeCount("1");
        } else {
            MenuItemBadge.getBadgeTextView(menuItemNotification).
                    setBadgeCount("0");
        }


        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SingletonSupport.getInstance().isLogin) {
            setUpBadge(String.valueOf(SingletonSupport.getInstance().cartCount));
            bottomNavigation.setNotification(String.valueOf(SingletonSupport.getInstance().cartCount),
                    1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            if (!CheckNetwork.isConnected(getApplicationContext())) {
                Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
            } else {
                isrefresh = true;
                getDrawerList(userSessionManager.getUserID());
            }
            return true;
        } else if (id == R.id.action_notification) {
            if (!CheckNetwork.isConnected(getApplicationContext())) {
                Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
            } else {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                //getDrawerList(userSessionManager.getUserID());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
