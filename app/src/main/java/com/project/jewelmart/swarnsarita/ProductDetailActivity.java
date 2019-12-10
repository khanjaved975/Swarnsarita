package com.project.jewelmart.swarnsarita;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.models.Image;
import com.project.jewelmart.swarnsarita.models.Image2;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.Acknowledge;
import com.project.jewelmart.swarnsarita.pojo.ProductDetail2;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;
import com.project.jewelmart.swarnsarita.utils.ViewAnimation;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.widgets.FontTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Double.parseDouble;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageButton bt_toggle_items, bt_toggle_description;
    LinearLayout Lin_item_detail, lin_description;
    private View lyt_expand_items, lyt_expand_description;
    private NestedScrollView nested_scroll_view;
    APIInterface apiInterface;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private RecyclerView recyclerView;
    private int animation_type = ItemAnimation.BOTTOM_UP;
    private LabelValueAdapter mAdapter;
    public String collection_id = "", table = "product_master", mode_type = "", my_collection_id = "", collection_sku_code = "";
    public boolean isold;
    private View back_drop;
    private boolean rotate = false;
    private View lyt_mic;
    private View lyt_call;
    public ImageView cart_icon, wishlist_icon;
    static List<ProductDetail2> list;
    public List<String> label_list, value_list;
    UserSessionManager userSessionManager;
    MaterialSpinner spinnerkarat, spinnerSize, spinnertone, spinnerpiece;
    LinearLayout karat_layout, size_layout, tone_layout, piece_layout;
    String oldkarat, newkarat, oldsize, newsize, oldpiece, newpiece, oldtone,
            newtone, oldgrows, newgrows, oldnet, newnt;
    FontBoldTextView txttotalprice, tv_qty, product_name;
    FontTextView txtcert_value, txthall_value, lbr_rate_value, lbr_name_value, lbr_amount_value;
    Toolbar toolbar;

    @BindView(R.id.review)
    EditText et_review;

    @BindView(R.id.melting_spinner)
    MaterialSpinner melting_spinner;

    private List<String> meltingList, colorList, toneList, polishList;
    private ArrayAdapter<String> meltingAdapter, colorAdapter, toneAdapter, polishAdapter;
    private String strMelting = "";
    private String strColor = "";
    private String strTone = "";
    private String strPolish = "";
    @BindView(R.id.top_leyer)
    LinearLayout top_leyer;

    @BindView(R.id.labour_layout)
    LinearLayout labour_layout;

    @BindView(R.id.charges_layout)
    LinearLayout charges_layout;

    @BindView(R.id.color_layout)
    LinearLayout color_layout;

    @BindView(R.id.polish_layout)
    LinearLayout polish_layout;

    @BindView(R.id.tone_layout)
    LinearLayout tone_layout1;

    @BindView(R.id.tone_spinner)
    MaterialSpinner tone_spinner;

    @BindView(R.id.color_spinner)
    MaterialSpinner color_spinner;

    @BindView(R.id.polish_spinner)
    MaterialSpinner polish_spinner;

    @BindView(R.id.card_wishlist)
    CardView card_wishlist;

    @BindView(R.id.card_cart)
    CardView card_cart;

    @BindView(R.id.desingno)
    FontTextView desingno;

    @BindView(R.id.product_status)
    FontBoldTextView product_status;

    Boolean cart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_product_detail);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        ButterKnife.bind(this);
        top_leyer.setVisibility(View.VISIBLE);
        if (getIntent() != null) {
            mode_type = getIntent().getStringExtra("mode_type");
            table = getIntent().getStringExtra("table");
            collection_id = getIntent().getStringExtra("collection_id");
            isold = getIntent().getBooleanExtra("old", false);
            collection_sku_code = getIntent().getStringExtra("collection_sku_code");
            if (mode_type.equals("my_collection")) {
                my_collection_id = getIntent().getStringExtra("my_collection_id");
            }
        }
        userSessionManager = new UserSessionManager(this);
        if (Tools.isTablet(ProductDetailActivity.this)) {
            ProductDetailActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        } else {
            ProductDetailActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        }
        initToolbar();
        initComponent();
        if (!CheckNetwork.isConnected(getApplicationContext())) {
            Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
        } else {
            getProductDetail(table, mode_type, collection_id, my_collection_id,
                    userSessionManager.getUserID(), collection_sku_code);
        }

    }

    public void getProductDetail(String table, String modetype, String colid, String my_colid,
                                 String userid, String collection_sku_code) {
        if (!SingletonSupport.getInstance().isLogin) {
            modetype = "without_login";
        }
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(ProductDetailActivity.this);
        Call<List<ProductDetail2>> countriesCall = apiInterface.doGetProductDetails(table, modetype,
                colid, my_colid,
                userid, collection_sku_code);
        countriesCall.enqueue(new Callback<List<ProductDetail2>>() {
            @Override
            public void onResponse(Call<List<ProductDetail2>> call, Response<List<ProductDetail2>> response) {
                Log.d("getProductDetail", response.code() + "");
                pdg.dismiss();
                final List<ProductDetail2> resource = response.body();
                list = resource;
                setValues();
            }

            @Override
            public void onFailure(Call<List<ProductDetail2>> call, Throwable t) {
                Log.d("getProductDetail", t.toString() + "");
                pdg.dismiss();
                call.cancel();
            }
        });

    }

    public void setValues() {
        try {
            strMelting = list.get(0).getDefaulMeltingId();
            label_list = list.get(0).getKeyLabel();
            value_list = list.get(0).getKeyValue();
            product_name.setText("Name : "+list.get(0).getCollectionName());
            desingno.setText("Design No : "+list.get(0).getDesign_number());
            if (list.get(0).getProduct_status().equalsIgnoreCase("1")){
                product_status.setText("In Stock");
            }else{
                product_status.setText("On Order");
            }
            toolbar.setTitle(list.get(0).getProductName());
            List<Image2> items = new ArrayList<>();
            for (int i = 0; list.get(0).getImageName().size() > i; i++) {
                Image2 obj = new Image2();
                obj.image = (String) list.get(0).getImageName().get(i);
                items.add(obj);
            }
            adapterImageSlider.setItems(items);
            viewPager.setAdapter(adapterImageSlider);

            if (list.get(0).getInWishlist() != null) {
                if (list.get(0).getInWishlist().toString().equals("1")) {
                    wishlist_icon.setVisibility(View.VISIBLE);
                }
            }
            if (list.get(0).getInCart() != null) {
                if (list.get(0).getInCart().toString().equals("1")) {
                    cart_icon.setVisibility(View.VISIBLE);
                }
            }
            // displaying selected image first
            viewPager.setCurrentItem(0);
            setAdapter();
            if (list.get(0).getCertificationRates() != null && list.get(0).getHallmarkingRates() != null) {
                if (!list.get(0).getCertificationRates().isEmpty() && list.get(0).getCertificationRates().size() > 0) {
                    txtcert_value.setText("\u20B9  " + list.get(0).getCertificationRates().get(0).getRate());
                }
                if (!list.get(0).getHallmarkingRates().isEmpty()
                        && list.get(0).getHallmarkingRates().size() > 0) {
                    txthall_value.setText("\u20B9  " + list.get(0).getHallmarkingRates().get(0).getRate());
                }
            } else {
                charges_layout.setVisibility(View.GONE);
            }

            addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int pos) {
                    addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });


            if (SingletonSupport.getInstance().meltinglist != null) {
                meltingList = new ArrayList<>();
                for (int i = 0; i < SingletonSupport.getInstance().meltinglist.size(); i++) {
                    meltingList.add(SingletonSupport.getInstance().meltinglist.get(i).getMeltingName());
                }
                meltingAdapter = new ArrayAdapter<String>(ProductDetailActivity.this, android.R.layout.simple_spinner_item, meltingList);
                meltingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params2.setMargins(0, 10, 0, 0);
                // melting_spinner.setLayoutParams(params2);
                melting_spinner.setAdapter(meltingAdapter);
                melting_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        strMelting = SingletonSupport.getInstance().meltinglist.get(position).getId();
                        int pos = label_list.indexOf("melting");
                        mAdapter.updatelist(oldkarat, SingletonSupport.getInstance().meltinglist.get(position).getMeltingName(), pos);
                    }
                });
            } else {
                melting_spinner.setVisibility(View.GONE);
            }

            melting_spinner.setVisibility(View.GONE);

            if (SingletonSupport.getInstance().colors != null) {
                colorList = new ArrayList<>();
                for (int i = 0; i < SingletonSupport.getInstance().colors.size(); i++) {
                    colorList.add(SingletonSupport.getInstance().colors.get(i).getColorType());
                }
                colorAdapter = new ArrayAdapter<String>(ProductDetailActivity.this, android.R.layout.simple_spinner_item, colorList);
                colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                if (!String.valueOf(label_list.indexOf("color")).isEmpty()) {
                    int j = Integer.valueOf(label_list.indexOf("color"));
                    String v = value_list.get(j);
                    int k = SingletonSupport.getInstance().colors.indexOf(v);
                    if (k != -1) {
                        strColor = SingletonSupport.getInstance().colors.get(k).getId();
                    }
                }

                params2.setMargins(0, 10, 0, 0);
                //color_spinner.setLayoutParams(params2);
                color_spinner.setAdapter(colorAdapter);
                color_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        strColor = SingletonSupport.getInstance().colors.get(position).getId();
                        int pos = label_list.indexOf("color");

                        mAdapter.updatelist(oldkarat, SingletonSupport.getInstance().colors.get(position).getColorType(), pos);
                    }
                });
            } else {
                color_layout.setVisibility(View.GONE);
            }

            if (SingletonSupport.getInstance().tone != null) {
                toneList = new ArrayList<>();
                for (int i = 0; i < SingletonSupport.getInstance().tone.size(); i++) {
                    toneList.add(SingletonSupport.getInstance().tone.get(i).getToneType());
                }
                toneAdapter = new ArrayAdapter<String>(ProductDetailActivity.this, android.R.layout.simple_spinner_item, toneList);
                toneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

                if (!String.valueOf(label_list.indexOf("tone")).isEmpty()) {
                    int j = Integer.valueOf(label_list.indexOf("tone"));
                    String v = value_list.get(j);
                    int k = SingletonSupport.getInstance().tone.indexOf(v);
                    if (k != -1) {
                        strTone = SingletonSupport.getInstance().tone.get(k).getId();
                    }
                }

                params2.setMargins(0, 10, 10, 0);
                // tone_spinner.setLayoutParams(params2);
                tone_spinner.setAdapter(toneAdapter);
                tone_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        strTone = SingletonSupport.getInstance().tone.get(position).getId();
                        int pos = label_list.indexOf("tone");
                        mAdapter.updatelist(oldkarat, SingletonSupport.getInstance().tone.get(position).getToneType(), pos);
                    }
                });
            } else {
                tone_layout1.setVisibility(View.GONE);
            }

            if (SingletonSupport.getInstance().polish != null) {
                polishList = new ArrayList<>();
                for (int i = 0; i < SingletonSupport.getInstance().polish.size(); i++) {
                    polishList.add(SingletonSupport.getInstance().polish.get(i).getPolishType());
                }
                polishAdapter = new ArrayAdapter<String>(ProductDetailActivity.this, android.R.layout.simple_list_item_1, polishList);
                polishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params2.setMargins(0, 10, 0, 0);
                if (!String.valueOf(label_list.indexOf("polish")).isEmpty()) {
                    int j = Integer.valueOf(label_list.indexOf("polish"));
                    String v = value_list.get(j);
                    int k = SingletonSupport.getInstance().polish.indexOf(v);
                    if (k != -1) {
                        strPolish = SingletonSupport.getInstance().polish.get(k).getId();
                    }
                }
                // polish_spinner.setLayoutParams(params2);
                polish_spinner.setAdapter(polishAdapter);
                polish_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        strPolish = SingletonSupport.getInstance().polish.get(position).getId().toString();
                        int pos = label_list.indexOf("polish");
                        mAdapter.updatelist(oldkarat, SingletonSupport.getInstance().polish.get(position).getPolishType(), pos);
                    }
                });
            } else {
                polish_layout.setVisibility(View.GONE);
            }


           /* if (list.get(0).getKaratSelection() == null || list.get(0).getKaratSelection().isEmpty()) {
               // karat_layout.setVisibility(View.GONE);
            } else {
                oldkarat = list.get(0).getProductDefaultData().getKarat();
                spinnerkarat.setItems(list.get(0).getKaratSelection());
                spinnerkarat.setOnItemSelectedListener(new2 MaterialSpinner.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        Toast.makeText(ProductDetailActivity.this, "clicked",
                                Toast.LENGTH_LONG).show();
                        newkarat = list.get(0).getKaratSelection().get(position);
                        int pos = label_list.indexOf("karat");
                        mAdapter.updatelist(oldkarat, newkarat, pos);
                        oldkarat = newkarat;
                        changeKarat();
                    }
                });
            }

            if (list.get(0).getSizeSelection() == null || list.get(0).getSizeSelection().isEmpty()) {
               // size_layout.setVisibility(View.GONE);
            } else {
                oldsize = list.get(0).getProductDefaultData().getSize();
                spinnerSize.setItems(list.get(0).getSizeSelection());
                spinnerSize.setOnItemSelectedListener(new2 MaterialSpinner.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        newsize = list.get(0).getSizeSelection().get(position);
                        int pos = label_list.indexOf("size");
                        mAdapter.updatelist(oldsize, newsize, pos);
                        oldsize = newsize;
                    }
                });
            }

            if (list.get(0).getPieceSelection() == null || list.get(0).getPieceSelection().isEmpty()) {
                piece_layout.setVisibility(View.GONE);
            } else {
                oldpiece = list.get(0).getProductDefaultData().getPiece();
                spinnerpiece.setItems(list.get(0).getPieceSelection());
                spinnerpiece.setOnItemSelectedListener(new2 MaterialSpinner.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        newpiece = list.get(0).getPieceSelection().get(position);
                        int pos = label_list.indexOf("piece");
                        mAdapter.updatelist(oldpiece, newpiece, pos);
                        oldpiece = newpiece;
                    }
                });
            }

            if (list.get(0).getToneSelection() == null || list.get(0).getToneSelection().isEmpty()) {
                tone_layout.setVisibility(View.GONE);
            } else {
                oldtone = list.get(0).getProductDefaultData().getTone();
                spinnertone.setItems(list.get(0).getToneSelection());
                spinnertone.setOnItemSelectedListener(new2 MaterialSpinner.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        newtone = list.get(0).getToneSelection().get(position);
                        int pos = label_list.indexOf("tone");
                        mAdapter.updatelist(oldtone, newtone, pos);
                        oldtone = newtone;
                    }
                });
            }*/
            if (list.get(0).getLabourDetails() != null) {
                lbr_name_value.setText(list.get(0).getLabourDetails().get(0).getLabourName());
                lbr_amount_value.setText(list.get(0).getLabourDetails().get(0).getAmount().toString());
                lbr_rate_value.setText(list.get(0).getLabourDetails().get(0).getRate().toString());
            } else {
                labour_layout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Log.d("set_Value", e.getMessage());
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FontBoldTextView textView = (FontBoldTextView) findViewById(R.id.toolbar_title);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (!Tools.isTablet(ProductDetailActivity.this)) {
                    if (scrollRange + verticalOffset == 0) {
                        //collapsingToolbarLayout.setTitle(list.get(0).getProductName());
                        if (list != null) {
                            if (!list.isEmpty()) {
                                textView.setText(list.get(0).getProductName());
                            }
                        }
                        // collapsingToolbarLayout.setCollapsedTitleTextColor(Color.GRAY);
                        isShow = true;
                    } else if (isShow) {
                        textView.setText("");
                        // collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                        isShow = false;
                    }
                }
            }
        });

    }

    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_mic);
            ViewAnimation.showIn(lyt_call);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_mic);
            ViewAnimation.showOut(lyt_call);
            back_drop.setVisibility(View.GONE);
        }
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(ContextCompat.getColor(this, R.color.overlay_dark_40), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        txttotalprice = (FontBoldTextView) findViewById(R.id.totalprice);
        product_name = (FontBoldTextView) findViewById(R.id.product_name);
        txtcert_value = (FontTextView) findViewById(R.id.cert_value);
        txthall_value = (FontTextView) findViewById(R.id.hall_value);
        lbr_rate_value = (FontTextView) findViewById(R.id.lbr_rate_value);
        lbr_name_value = (FontTextView) findViewById(R.id.lbr_name_value);
        lbr_amount_value = (FontTextView) findViewById(R.id.lbr_amount_value);
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        Lin_item_detail = (LinearLayout) findViewById(R.id.item_detail);
        lin_description = (LinearLayout) findViewById(R.id.lin_description);
        // section items
        bt_toggle_items = (ImageButton) findViewById(R.id.bt_toggle_items);
        lyt_expand_items = (View) findViewById(R.id.lyt_expand_items);
        Lin_item_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(bt_toggle_items, lyt_expand_items);
            }
        });

        bt_toggle_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_items);
            }
        });

        bt_toggle_description = (ImageButton) findViewById(R.id.bt_toggle_description);
        lyt_expand_description = (View) findViewById(R.id.lyt_expand_description);
        bt_toggle_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_description);
            }
        });

        lin_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(bt_toggle_description, lyt_expand_description);
            }
        });

        tv_qty = (FontBoldTextView) findViewById(R.id.tv_qty);

        ((FloatingActionButton) findViewById(R.id.fab_qty_sub)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isold) {
                    int qty = Integer.parseInt(tv_qty.getText().toString());
                    if (qty > 1) {
                        qty--;
                        tv_qty.setText(qty + "");
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Can not make changes", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((FloatingActionButton) findViewById(R.id.fab_qty_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isold) {
                    int qty = Integer.parseInt(tv_qty.getText().toString());
                    if (qty < 500) {
                        qty++;
                        tv_qty.setText(qty + "");
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Can not make changes", Toast.LENGTH_SHORT).show();
                }
            }
        });

        card_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = null;
                final JSONArray jsonArray = new JSONArray();
                jsonObject = createJSON("wishlist");
                jsonArray.put(jsonObject);
                if (!CheckNetwork.isConnected(getApplicationContext())) {
                    Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
                } else {
                    if (SingletonSupport.getInstance().isLogin) {
                        Cart(jsonArray.toString());
                    } else {
                        Tools.showCustomDialog(ProductDetailActivity.this, "Alert !", "Please Login");
                    }
                }
            }
        });


        card_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = null;
                final JSONArray jsonArray = new JSONArray();
                jsonObject = createJSON("cart");
                jsonArray.put(jsonObject);
                if (!CheckNetwork.isConnected(getApplicationContext())) {
                    Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
                } else {
                    if (SingletonSupport.getInstance().isLogin) {
                        cart = true;
                        Cart(jsonArray.toString());

                    } else {
                        Tools.showCustomDialog(ProductDetailActivity.this, "Alert !", "Please Login");
                    }
                }
            }
        });


        karat_layout = (LinearLayout) findViewById(R.id.karat_layout);
        piece_layout = (LinearLayout) findViewById(R.id.piece_layout);
        tone_layout = (LinearLayout) findViewById(R.id.tone_layout);
        size_layout = (LinearLayout) findViewById(R.id.size_layout);
        spinnerkarat = (MaterialSpinner) findViewById(R.id.karat_spinner);
        spinnerpiece = (MaterialSpinner) findViewById(R.id.piece_spinner);
        spinnertone = (MaterialSpinner) findViewById(R.id.tone_spinner);
        spinnerSize = (MaterialSpinner) findViewById(R.id.spinnerSize);
        back_drop = findViewById(R.id.back_drop);
        cart_icon = (ImageView) findViewById(R.id.cart_icon);
        wishlist_icon = (ImageView) findViewById(R.id.wishlist_icon);
        final FloatingActionButton fab_mic = (FloatingActionButton) findViewById(R.id.fab_mic);
        final FloatingActionButton fab_call = (FloatingActionButton) findViewById(R.id.fab_call);
        final FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        lyt_mic = findViewById(R.id.lyt_mic);
        lyt_call = findViewById(R.id.lyt_call);
        ViewAnimation.initShowOut(lyt_mic);
        ViewAnimation.initShowOut(lyt_call);
        back_drop.setVisibility(View.GONE);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isold) {
                    toggleFabMode(v);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Can not make changes", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fab_add);
            }
        });
        fab_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = null;
                final JSONArray jsonArray = new JSONArray();
                jsonObject = createJSON("cart");
                jsonArray.put(jsonObject);
                if (!CheckNetwork.isConnected(getApplicationContext())) {
                    Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
                } else {
                    if (SingletonSupport.getInstance().isLogin) {
                        cart = true;
                        Cart(jsonArray.toString());

                    } else {
                        Tools.showCustomDialog(ProductDetailActivity.this, "Alert !", "Please Login");
                    }
                }
            }
        });
        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = null;
                final JSONArray jsonArray = new JSONArray();
                jsonObject = createJSON("wishlist");
                jsonArray.put(jsonObject);
                if (!CheckNetwork.isConnected(getApplicationContext())) {
                    Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
                } else {
                    if (SingletonSupport.getInstance().isLogin) {
                        Cart(jsonArray.toString());
                    } else {
                        Tools.showCustomDialog(ProductDetailActivity.this, "Alert !", "Please Login");
                    }
                }
            }
        });

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        viewPager = (ViewPager) findViewById(R.id.pager);
        if (!Tools.isTablet(ProductDetailActivity.this)) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    width, width);
            viewPager.setLayoutParams(layoutParams);
        } else {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    width / 2, height - 80);
            viewPager.setLayoutParams(layoutParams);
        }

        adapterImageSlider = new AdapterImageSlider(this, new ArrayList<Image2>());

    }

    private void setAdapter() {
        //set data and list adapter
        mAdapter = new LabelValueAdapter(label_list, value_list);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
     /*mAdapter.setOnItemClickListener(badgenew AdapterListAnimation.OnItemClickListener() {
            @Override
            public void onItemClick(View view, People obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });*/
    }

    private void toggleSection(View bt, final View lyt) {
        boolean show = toggleArrow(bt);
        if (show) {
            ViewAnimation.collapse(lyt);
        } else {
            ViewAnimation.expand(lyt, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                   /* if (Tools.isTablet(ProductDetailActivity.this)) {
                        Tools.scrollTo(scrollView, lyt);
                    }else {*/
                    Tools.nestedScrollTo(nested_scroll_view, lyt);
                    // }
                }
            });
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<Image2> items;

        private OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, Image obj);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<Image2> items) {
            this.act = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public Image2 getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<Image2> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final Image2 o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView image = (ImageView) v.findViewById(R.id.image);
            MaterialRippleLayout lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
            Tools.displayImageOriginal(act, image, o.image, "public/backend/product_images/zoom_image/");
            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // if (onItemClickListener != null) {
                    Log.i("TAG", "This page was clicked: " + position);
                    // onItemClickListener.onItemClick(v, o);
                    //getPosters();
                  /*  String[] str = badgenew String[list.get(0).getImageName().size()];
                    for (int i = 0; list.get(0).getImageName().size() > i; i++) {
                        str[i] = POSTERS_PATH + list.get(0).getImageName().get(i);
                    }
                    badgenew ImageViewer.Builder(v.getContext(), str)
                            .setStartPosition(0).setBackgroundColorRes(R.color.white)
                            .show();*/

                    Intent intent = new Intent(v.getContext(), LargeViewActivity.class);
                    intent.putExtra("image_list", (Serializable) list.get(0).getImageName());
                    intent.putExtra("path", "public/backend/product_images/zoom_image/");
                    v.getContext().startActivity(intent);
                    // Toast.makeText(v.getContext(),"clicked",Toast.LENGTH_LONG).show();
                    //  }
                }
            });

            ((ViewPager) container).addView(v, 0);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }

    public int getRandomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(156), random.nextInt(156), random.nextInt(156));
    }

    public class LabelValueAdapter extends RecyclerView.Adapter<LabelValueAdapter.MyViewHolder> {

        private List<String> labelList, valueList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public FontTextView label, value;

            public MyViewHolder(View view) {
                super(view);
                label = (FontTextView) view.findViewById(R.id.label);
                value = (FontTextView) view.findViewById(R.id.value);
            }
        }

        private int lastPosition = -1;
        private boolean on_attach = true;

        private void setAnimation(View view, int position) {
            if (position > lastPosition) {
                ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
                lastPosition = position;
            }
        }

        public LabelValueAdapter(List<String> labelList, List<String> valueList) {
            this.labelList = labelList;
            this.valueList = valueList;
        }

        public void updatelist(String currentvalue, String newValue, int pos) {
            valueList.remove(pos);
            valueList.add(pos, newValue);
            notifyItemChanged(pos);
            //notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String label = labelList.get(position);
            label = label.replace("_", " ");
            String value = valueList.get(position);
            holder.label.setText(label);
            holder.value.setText(value);
            setAnimation(holder.itemView, position);
        }

        @Override
        public int getItemCount() {
            return labelList.size();
        }
    }

    public void Cart(String str) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(ProductDetailActivity.this);
        Call<Acknowledge> countriesCall = apiInterface.doAddToCart(str);
        countriesCall.enqueue(new Callback<Acknowledge>() {
            @Override
            public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                Log.d("CART", response.code() + "");
                pdg.dismiss();
                if (response.code() == 200) {
                    Acknowledge resource = response.body();
                    if (resource.getAck().toString().equals("1")) {
                        if (cart) {
                            SingletonSupport.getInstance().cartCount = SingletonSupport.getInstance().cartCount + 1;
                        }
                        Toast.makeText(getApplicationContext(), resource.getMsg(), Toast.LENGTH_LONG).show();
                        finish();
                        //Tools.showCustomDialog(ProductDetailActivity.this, "Response", resource.getMsg());
                    } else {

                        Tools.showCustomDialog(ProductDetailActivity.this, "Response", resource.getMsg());
                        // Toast.makeText(getApplicationContext(), resource.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Response code: " + response.code(), Toast.LENGTH_LONG).show();
                }
                cart = false;
            }

            @Override
            public void onFailure(Call<Acknowledge> call, Throwable t) {
                pdg.dismiss();
                call.cancel();
            }
        });
    }

    private JSONObject createJSON(String table2) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("user_id", userSessionManager.getUserID());
            obj.put("table", table2);
            obj.put("product_id", list.get(0).getProductMasterId());
            obj.put("product_inventory_table", table);
            obj.put("gross_wt", value_list.get(0));
            obj.put("net_wt", value_list.get(1));
            //obj.put("size", "0");
            obj.put("karat", "0");
            obj.put("price", "0");
            obj.put("no_quantity", tv_qty.getText().toString());
            obj.put("device_type", "android");
            obj.put("remarks", et_review.getText().toString());
            obj.put("certification", "no");
            obj.put("hallmarking", "no");
            obj.put("tone", strTone);
            obj.put("polish", strPolish);
            obj.put("color", strColor);
            // obj.put("size", et_size.getText().toString());

            //obj.put("length", "no");
            obj.put("melting_id", strMelting);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void changeKarat() {
        double karatRate = 0;
        double defaultNetWt = 0;
        double newNetWt = 0;
        double changedkaratD = 0;
        double previousKaratD = 0;
        double defaultKaratD = 0;
        oldgrows = list.get(0).getProductDefaultData().getNetWt();
        if (!oldgrows.isEmpty()) {
            defaultNetWt = parseDouble(oldgrows);
        }

        if (oldgrows.equalsIgnoreCase(newgrows) &&
                oldkarat.equalsIgnoreCase(newkarat)) {
            newgrows = "" + list.get(0).getProductDefaultData().getGrossWt();
            newNetWt = defaultNetWt;
            defaultNetWt = new BigDecimal(newNetWt).doubleValue();
        } else {
            if (previousKaratD < changedkaratD && changedkaratD != defaultKaratD) {
                for (int i = 0; i < list.get(0).getKaratRates().size(); i++) {
                    if (list.get(0).getKaratRates() != null &&
                            list.get(0).getKaratRates().get(i).getKaratValue().equalsIgnoreCase(oldkarat)
                            && list.get(0).getKaratRates().get(i).getKaratValue().equalsIgnoreCase(newkarat)) {
                        karatRate = parseDouble(list.get(0).getKaratRates().get(i).getRate().toString());
                        double ratePer = karatRate / 100;
                        double ratePerVal = defaultNetWt * ratePer;
                        newNetWt = defaultNetWt + ratePerVal;
                        defaultNetWt = new BigDecimal(newNetWt).doubleValue();
                    }
                }
            } else if (oldgrows.equalsIgnoreCase(newgrows) && changedkaratD == defaultKaratD) {
                newNetWt = defaultNetWt;
                defaultNetWt = new BigDecimal(newNetWt).doubleValue();
            }
        }


       /* if (product.sizeCalculationType != null && product.sizeCalculationType.equalsIgnoreCase("proportionate")) {
            if (!previousSize.isEmpty() && parseDouble(previousSize) != 0) {
                newNetWt = (changeSizeD * defaultNetWt) / parseDouble(previousSize);
                defaultNetWt = badgenew BigDecimal(newNetWt).doubleValue();
            }
        }*/

        txttotalprice.setText(String.valueOf(defaultNetWt));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        int color = Color.parseColor(getResources().getString(R.string.color));
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
        menu.getItem(0).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
            case R.id.action_cart:
                if (SingletonSupport.getInstance().isLogin) {
                    intent = new Intent(ProductDetailActivity.this, com.project.jewelmart.swarnsarita.CartActivity.class);
                    intent.putExtra("from", "cart");
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    Tools.setSnackBar(Lin_item_detail, "Please Login");
                }
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
