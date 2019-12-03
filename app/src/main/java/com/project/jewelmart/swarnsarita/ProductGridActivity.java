package com.project.jewelmart.swarnsarita;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.jewelmart.swarnsarita.adapters.AdapterGridScrollProgress;
import com.project.jewelmart.swarnsarita.fragments.FragmentBottomSheetDialogFull;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.models.People;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.CartAcknowledge;
import com.project.jewelmart.swarnsarita.pojo.CartCount;
import com.project.jewelmart.swarnsarita.pojo.Count;
import com.project.jewelmart.swarnsarita.pojo.Filter;
import com.project.jewelmart.swarnsarita.pojo.FilterObject;
import com.project.jewelmart.swarnsarita.pojo.Productgridpojo;
import com.project.jewelmart.swarnsarita.pojo.SortList;
import com.project.jewelmart.swarnsarita.utils.Constant;
import com.project.jewelmart.swarnsarita.utils.MenuItemBadge;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.widgets.FontTextView;
import com.project.jewelmart.swarnsarita.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductGridActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    APIInterface apiInterface;
    public static AdapterGridScrollProgress adapterGrid;
    public String collection_id = "", table = "", name = "", sort = SingletonSupport.getInstance().sort, pos = "0",
            mode_type = "", my_collection_id = "", collection_sku_code = "";
    public List<Productgridpojo.Result> List;
    public static List<Productgridpojo.Result> MoreList;
    public Button btn_sort, btn_filter, btn_select;
    public int item_per_display = 50;
    private int constant = 0;
    public static boolean enableSelection = false;
    public int Page = 0;
    private int viewtype = 0, cartPossition = 0;
    private int total_item_count = 0;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;
    Toolbar toolbar;
    Filter filter;
    public ArrayList<String> filterOptionsKeys = new ArrayList<String>();
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    public UserSessionManager userSessionManager;
    public static List<People> items;
    public ArrayList<FilterObject> filterParams = null;
    MenuItem menuItemNotification;
    private Parcelable recyclerViewState;
    CoordinatorLayout coordinatorLayout;
    Productgridpojo.Result cartProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_product_grid);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontBoldTextView textView = (FontBoldTextView) findViewById(R.id.toolbar_title);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent() != null) {
            collection_id = getIntent().getStringExtra("collection_id");
            table = getIntent().getStringExtra("table");
            name = getIntent().getStringExtra("name");
            mode_type = getIntent().getStringExtra("mode_type");
            if (mode_type.equals("my_collection")) {
                my_collection_id = getIntent().getStringExtra("my_collection_id");
            }
        }

//        if (SingletonSupport.getInstance().sortLists != null && !SingletonSupport.getInstance().sortLists.isEmpty()) {
//            sort = SingletonSupport.getInstance().sortLists.get(0).getValue().toString();
//        }

        if (Tools.isTablet(ProductGridActivity.this)) {
            ProductGridActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        } else {
            ProductGridActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        }

        // toolbar.setTitle(name);
        textView.setText(name);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        userSessionManager = new UserSessionManager(this);
        btn_sort = (Button) findViewById(R.id.btn_sort);
        btn_filter = (Button) findViewById(R.id.btn_filter);
        btn_select = (Button) findViewById(R.id.btn_select);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        initRecycleView();
        //getPeopleData(ProductGridActivity.this);
        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (List != null) {
                    bottom_sheet = findViewById(R.id.bottom_sheet);
                    mBehavior = BottomSheetBehavior.from(bottom_sheet);
                    showBottomSheetDialog();
                } else {
                    Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "No Data Found");
                }
            }
        });

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (List != null) {
                    if (!table.equalsIgnoreCase("inventory_master")) {
                        //  Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "Filter Functionality is  temporarily deactive");
                        if (FragmentBottomSheetDialogFull.filterlist != null) {
                            if (!FragmentBottomSheetDialogFull.filterlist.isEmpty()) {
                                FragmentBottomSheetDialogFull.filterlist.clear();
                            }
                        }
                        if (FragmentBottomSheetDialogFull.filterSendMap != null) {
                            if (!FragmentBottomSheetDialogFull.filterSendMap.isEmpty()) {
                                FragmentBottomSheetDialogFull.filterSendMap.clear();
                            }
                        }
                        getFilter(userSessionManager.getUserID(), collection_id, "all_filter", table);
                    }else{
                        Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "Filter in Stock product is not allowed");
                    }
                } else {
                    Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "No Data Found");
                }
            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (List != null) {
                    if (enableSelection) {
                        enableSelection = false;
                        btn_select.setBackgroundColor(btn_select.getContext().getResources().getColor(R.color.white));
                    } else {
                        enableSelection = true;
                        btn_select.setBackgroundColor(btn_select.getContext().getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "No Data Found");
                }
            }
        });
        if (!CheckNetwork.isConnected(getApplicationContext())) {
            Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "No Internet Connection , Please Try after Connecting");
        } else {
            if (SingletonSupport.getInstance().isLogin) {
                getProductCount(userSessionManager.getUserID(), mode_type, table, collection_id, my_collection_id);
            } else {
                constant = 20;
                toolbar.setTitle(name + " " + "(" + constant + ")");
                getProductGrid(collection_id, mode_type, table, String.valueOf(item_per_display),
                        String.valueOf(Page), userSessionManager.getUserID(), my_collection_id, sort);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void showBottomSheetDialog() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        View view = getLayoutInflater().inflate(R.layout.sheet_list, null);
        FontBoldTextView tvTitle = (FontBoldTextView) view.findViewById(R.id.tvTitle);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int sortpos = Integer.valueOf(pos);
        recyclerView.setAdapter(new ItemSortAdapter(this,
                (ArrayList<SortList>) SingletonSupport.getInstance().sortLists, sortpos,
                new ItemSortAdapter.ItemListener() {
                    @Override
                    public void onItemClickSort(SortList item, int position, int sortPos, String value) {
                        sort = String.valueOf(sortPos);
                        SingletonSupport.getInstance().sort = sort;
                        pos = String.valueOf(position);
                        Page = 0;
                        // Toast.makeText(ProductGridActivity.this,""+sortPos,Toast.LENGTH_LONG).show();
                        getProductGrid(collection_id, mode_type, table, String.valueOf(item_per_display),
                                String.valueOf(Page), SingletonSupport.getInstance().user_id, my_collection_id, sort);
                        mBottomSheetDialog.dismiss();
                    }
                }));

        (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.hide();
            }
        });
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // set background transparent
        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

    public static void ToggleSelection(int position, Context context, String product_id) {
        adapterGrid.toggleSelection(position);
        if (adapterGrid.getSelectedItems() != null && adapterGrid.getSelectedItems().size() > 0) {
            if (!SingletonSupport.getInstance().multiSelectedIds.contains(product_id)) {
                SingletonSupport.getInstance().multiSelectedQty.add("1");
                SingletonSupport.getInstance().multiSelectedIds.add(product_id);
                Toast.makeText(context, MoreList.get(position).getProduct_inventory_id(), Toast.LENGTH_SHORT).show();
            } else {
                SingletonSupport.getInstance().multiSelectedQty.remove("1");
                SingletonSupport.getInstance().multiSelectedIds.remove(product_id);
                Toast.makeText(context, "remove" + MoreList.get(position).getProduct_inventory_id(), Toast.LENGTH_SHORT).show();
            }
        } else {
            SingletonSupport.getInstance().multiSelectedIds.clear();
            SingletonSupport.getInstance().multiSelectedQty.clear();
        }
    }

    public void getProductCount(String user_id, final String modetype, final String table,
                                final String collection_id,
                                final String my_collection_id) {
        Map<String, String> params = new HashMap<>();
        params.put("table", table);
        params.put("collection_id", collection_id);
        params.put("user_id", user_id);
        params.put("record", String.valueOf(item_per_display));
        params.put("page_no", String.valueOf(Page));
        params.put("my_collection_id", my_collection_id);
        params.put("sort_by", sort);
        params.put("product_status", "1");
        if (SingletonSupport.getInstance().isfilter) {
            params.put("mode_type", "filter_data");
            if (FragmentBottomSheetDialogFull.filterSendMap != null) {
                for (Map.Entry e : FragmentBottomSheetDialogFull.filterSendMap.entrySet()) {
                    if (!params.containsKey(e.getKey()))
                        params.put(e.getKey().toString(), e.getValue().toString());
                }
            }
        } else {
            if (!my_collection_id.isEmpty()) {
                params.put("mode_type", modetype);
            } else {
                params.put("mode_type", "filter_data");
            }
        }
        ProgressDialog pdg = null;
        // if (!SingletonSupport.getInstance().isfilter) {
        pdg = ProgressIndicator.ShowLoading(ProductGridActivity.this);
        // }

        // final ProgressDialog pdg = ProgressIndicator.ShowLoading(ProductGridActivity.this);
        Call<Count> countriesCall = apiInterface.doGetCount(params);
        final ProgressDialog finalPdg = pdg;
        countriesCall.enqueue(new Callback<Count>() {
            @Override
            public void onResponse(Call<Count> call, Response<Count> response) {
                if (finalPdg != null) {
                    finalPdg.dismiss();
                }
                try {
                    Log.d("doGetProductGrid", response.code() + "");
                    final Count resource = response.body();
                    if (resource.getCount() != null) {
                        if (SingletonSupport.getInstance().isLogin) {
                            constant = resource.getCount();
                            toolbar.setTitle(name + " " + "(" + constant + ")");
                        } else {
                            constant = 20;
                            toolbar.setTitle(name + " " + "(" + constant + ")");
                        }
                        getProductGrid(collection_id, modetype, table, String.valueOf(item_per_display),
                                String.valueOf(Page), userSessionManager.getUserID(), my_collection_id, sort);
                    } else {
                        Tools.showCustomDialog(ProductGridActivity.this
                                , "Alert !", "No Product Found for this category.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Count> call, Throwable t) {
                if (finalPdg != null) {
                    finalPdg.dismiss();
                }
                Log.d("doGetProductGrid", t.toString() + "");
                call.cancel();
            }

        });

    }

    public void getMoreProductGrid(String id, String modetype, String table, String record, final String page, String usedid,
                                   String my_collection_id, String sort) {
        Map<String, String> params = new HashMap<>();
        params.put("table", table);
        if (SingletonSupport.getInstance().isLogin) {
            params.put("mode_type", modetype);
        } else {
            params.put("mode_type", "without_login");
        }
        params.put("collection_id", id);
        params.put("user_id", usedid);
        params.put("record", record);
        params.put("page_no", page);
        params.put("my_collection_id", my_collection_id);
        params.put("sort_by", sort);
        params.put("product_status", "1");
        if (SingletonSupport.getInstance().isfilter) {
            params.put("mode_type", "filter_data");
            if (FragmentBottomSheetDialogFull.filterSendMap != null) {
                for (Map.Entry e : FragmentBottomSheetDialogFull.filterSendMap.entrySet()) {
                    if (!params.containsKey(e.getKey()))
                        params.put(e.getKey().toString(), e.getValue().toString());
                }
            }
        } else {
            params.put("mode_type", modetype);
        }
        ProgressDialog pdg = null;
        // if (!SingletonSupport.getInstance().isfilter) {
        pdg = ProgressIndicator.ShowLoading(ProductGridActivity.this);
        // }
        Call<Productgridpojo> countriesCall = apiInterface.doGetProductGrid2(params);
        final ProgressDialog finalPdg = pdg;
        countriesCall.enqueue(new Callback<Productgridpojo>() {
            @Override
            public void onResponse(Call<Productgridpojo> call, Response<Productgridpojo> response) {
                Log.d("doGetProductGrid", response.code() + "");
                finalPdg.dismiss();
                final Productgridpojo resource = response.body();
                if (resource.getResult() != null) {
                    MoreList = resource.getResult();
                    total_item_count += MoreList.size();
                    Page++;
                    adapterGrid.insertData(MoreList);
                } else {
                    if (SingletonSupport.getInstance().isLogin) {
                        Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "No data found");
                    } else {
                        Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "Please login to view all product");
                    }
                }
            }

            @Override
            public void onFailure(Call<Productgridpojo> call, Throwable t) {
                Log.d("doGetProductGrid", t.toString() + "");
                finalPdg.dismiss();

                call.cancel();
            }
        });

    }

    private void loadNextData() {
        if (MoreList != null) {
            MoreList.clear();
        }
        if (constant <= total_item_count) {
        } else {
            if (!CheckNetwork.isConnected(getApplicationContext())) {
                Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
            } else {
                if (!SingletonSupport.getInstance().isLogin) {
                    if (total_item_count >= 20) {
                        Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "Please login to view all product and design");
                    } else {
                        adapterGrid.setLoading();
                        //String p=String.valueOf(Page);
                        getMoreProductGrid(collection_id, mode_type, table, String.valueOf(item_per_display),
                                String.valueOf(Page), userSessionManager.getUserID(), my_collection_id, sort);
                    }
                } else {
                    adapterGrid.setLoading();
                    //String p=String.valueOf(Page);
                    getMoreProductGrid(collection_id, mode_type, table, String.valueOf(item_per_display),
                            String.valueOf(Page), userSessionManager.getUserID(), my_collection_id, SingletonSupport.getInstance().sort);
                }
            }
        }
    }

    public void getProductGrid(String id, String modetype, final String table, String record,
                               String page, String usedid,
                               final String my_collection_id, String sort) {
        try {

            Map<String, String> params = new HashMap<>();
            params.put("table", table);
            params.put("collection_id", id);
            params.put("user_id", usedid);
            params.put("record", record);
            params.put("page_no", page);
            params.put("my_collection_id", my_collection_id);
            params.put("sort_by", sort);
            params.put("product_status", "1");
            if (SingletonSupport.getInstance().isfilter) {
                params.put("mode_type", "filter_data");
                if (FragmentBottomSheetDialogFull.filterSendMap != null) {
                    for (Map.Entry e : FragmentBottomSheetDialogFull.filterSendMap.entrySet()) {
                        if (!params.containsKey(e.getKey()))
                            params.put(e.getKey().toString(), e.getValue().toString());
                    }
                }
            } else {
                if (SingletonSupport.getInstance().isLogin) {
                    params.put("mode_type", modetype);
                } else {
                    params.put("mode_type", "without_login");
                }
            }
            ProgressDialog pdg = null;
            // if (!SingletonSupport.getInstance().isfilter) {
            pdg = ProgressIndicator.ShowLoading(ProductGridActivity.this);
            // }
            //SingletonSupport.getInstance().isfilter = false;
            Call<Productgridpojo> countriesCall = apiInterface.doGetProductGrid2(params);
            final ProgressDialog finalPdg = pdg;
            countriesCall.enqueue(new Callback<Productgridpojo>() {
                @Override
                public void onResponse(Call<Productgridpojo> call, Response<Productgridpojo> response) {
                    Log.d("doGetProductGrid", response.code() + "");
                    if (finalPdg != null) {
                        finalPdg.dismiss();
                    }
                    final Productgridpojo resource = response.body();
                    if (resource != null) {
                        if (resource.getResult() != null) {
                            List = resource.getResult();
                            setRecyclerView(List);
                        } else {
                            Tools.showCustomDialog(ProductGridActivity.this
                                    , "Alert !", "No Product Found for this category.");
                        }
                    } else {
                        Tools.showCustomDialog(ProductGridActivity.this
                                , "Alert !", "No Product Found for this category.");
                    }

                }

                @Override
                public void onFailure(Call<Productgridpojo> call, Throwable t) {
                    Log.d("doGetProductGrid", t.toString() + "");
                    if (finalPdg != null) {
                        finalPdg.dismiss();
                    }
                    call.cancel();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initRecycleView() {
        if (viewtype == 0) {
            if (Tools.isLandscape(ProductGridActivity.this)) {
                recyclerView.setLayoutManager(new GridLayoutManager(ProductGridActivity.this, 2,
                        GridLayoutManager.HORIZONTAL, false));
                //recyclerView.addItemDecoration(badgenew SpacingItemDecoration(8, ProductGridFragment.dpToPx(this, 2), false));
                //OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(ProductGridActivity.this, 2,
                        GridLayoutManager.VERTICAL, false));
                //  OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
            }
        } /*else if (viewtype == 1) {
            if (Tools.isLandscape(ProductGridActivity.this)) {
                recyclerView.setLayoutManager(badgenew GridLayoutManager(ProductGridActivity.this, 1, GridLayoutManager.HORIZONTAL,
                        false));
              //  OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
            } else {
                recyclerView.setLayoutManager(badgenew GridLayoutManager(ProductGridActivity.this, 1, GridLayoutManager.VERTICAL, false));
               // OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
            }
        } else if (viewtype == 2) {
            if (Tools.isLandscape(ProductGridActivity.this)) {
                recyclerView.setPadding(0, 0, 0, 0);
                recyclerView.setLayoutManager(badgenew GridLayoutManager(ProductGridActivity.this, 3,
                        GridLayoutManager.VERTICAL, false));
               // OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
            } else {
                recyclerView.setPadding(0, 0, 0, 20);
                recyclerView.setLayoutManager(badgenew GridLayoutManager(ProductGridActivity.this, 1,
                        GridLayoutManager.HORIZONTAL, false));
                //OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
            }
        } else if (viewtype == 3) {
            recyclerView.setPadding(0, 0, 0, 30);
            recyclerView.setLayoutManager(badgenew GridLayoutManager(ProductGridActivity.this, 2,
                    GridLayoutManager.HORIZONTAL, false));
            //OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        }*/
        recyclerView.setHasFixedSize(true);
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

    }

    public void setRecyclerView(final List<Productgridpojo.Result> List) {

        adapterGrid = new AdapterGridScrollProgress(ProductGridActivity.this, item_per_display,
                List, viewtype);
        recyclerView.setAdapter(adapterGrid);
        total_item_count = List.size();
        Page++;
        actionModeCallback = new ActionModeCallback();
        adapterGrid.setOnItemLongClickListener(new AdapterGridScrollProgress.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, Productgridpojo.Result obj, int position) {
                enableActionMode(position);
            }
        });

        adapterGrid.setOnItemClickListener(new AdapterGridScrollProgress.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Productgridpojo.Result obj, int position) {
                if (enableSelection) {
                    enableActionMode(position);
                } else {
                    if (adapterGrid.getSelectedItemCount() > 0) {
                        enableActionMode(position);
                    } else {

                        ArrayList<String> list = new ArrayList<String>();
                        Intent intent = new Intent(ProductGridActivity.this, ProductDetailActivity.class);
                        intent.putExtra("mode_type", mode_type);
                        intent.putExtra("table", table);
                        intent.putExtra("collection_id", collection_id);
                        intent.putExtra("my_collection_id", my_collection_id);
                        intent.putExtra("collection_sku_code", obj.getProduct_inventory_id());
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);

                    }
                }
            }
        });

        adapterGrid.setImageClickListener(new AdapterGridScrollProgress.OnImageClickListener() {
            @Override
            public void onItemClick(AdapterGridScrollProgress.OriginalViewHolder view, Productgridpojo.Result obj, String type, int pos) {
                if (type.toString().toLowerCase().equals("cart")) {
                    if (SingletonSupport.getInstance().isLogin) {
                        CartGrid(obj.getProduct_inventory_id(), "1",
                                table, userSessionManager.getUserID(),
                                "cart");
                        //Productgridpojo.Result p = obj;
                        cartProduct = obj;
                        cartPossition = pos;
                        //  p.setIn_cart("1");
                        //  adapterGrid.updatelist(pos, p);
                        //  adapterGrid.notifyItemRangeChanged(0, List.size() - 1);
                        //  recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    } else {
                        Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "Please Login");
                    }
                } else if (type.toString().toLowerCase().equals("wishlist")) {
                    if (SingletonSupport.getInstance().isLogin) {
                        CartGrid(obj.getProduct_inventory_id(), "1",table, userSessionManager.getUserID(), "wishlist");
                        Productgridpojo.Result p = obj;
                        p.setIn_wishlist("1");
                        adapterGrid.updatelist(pos, p);
                        adapterGrid.notifyItemRangeChanged(0, List.size() - 1);
                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    } else {
                        Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "Please Login");
                    }
                   /* Intent intent = new2 Intent(ProductGridActivity.this, ProductDetailActivity.class);
                    intent.putExtra("mode_type", mode_type);
                    intent.putExtra("table", table);
                    intent.putExtra("collection_id", collection_id);
                    intent.putExtra("my_collection_id", my_collection_id);
                    intent.putExtra("old", false);
                    intent.putExtra("collection_sku_code", obj.getProduct_inventory_id());
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);*/

                } else if (type.toString().toLowerCase().equals("cartplus")) {
                    if (SingletonSupport.getInstance().isLogin) {
                        CartGridPlusMinus(obj.getProduct_inventory_id(), "1",
                                table, userSessionManager.getUserID(),
                                "cart", "1");
                        //Productgridpojo.Result p = obj;
                        cartProduct = obj;
                        cartPossition = pos;
                        //  p.setIn_cart("1");
                        //  adapterGrid.updatelist(pos, p);
                        //  adapterGrid.notifyItemRangeChanged(0, List.size() - 1);
                        //  recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    } else {
                        Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "Please Login");
                    }
                } else if (type.toString().toLowerCase().equals("cartminus")) {
                    if (SingletonSupport.getInstance().isLogin) {
                        CartGridPlusMinus(obj.getProduct_inventory_id(), "1",
                                table, userSessionManager.getUserID(),
                                "cart", "0");
                        // Productgridpojo.Result p = obj;
                        cartProduct = obj;
                        cartPossition = pos;
                        //  p.setIn_cart("1");
                        //  adapterGrid.updatelist(pos, p);
                        //  adapterGrid.notifyItemRangeChanged(0, List.size() - 1);
                        //  recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    } else {
                        Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "Please Login");
                    }
                }

            }
        });

        adapterGrid.setOnMoreButtonClickListener(new AdapterGridScrollProgress.OnMoreButtonClickListener() {
            @Override
            public void onItemClick(View view, Productgridpojo.Result obj, MenuItem item, int pos) {
                if (item.getTitle().toString().toLowerCase().equals("details")) {
                    Intent intent = new Intent(ProductGridActivity.this, ProductDetailActivity.class);
                    intent.putExtra("mode_type", mode_type);
                    intent.putExtra("table", table);
                    intent.putExtra("collection_id", collection_id);
                    intent.putExtra("my_collection_id", my_collection_id);
                    intent.putExtra("old", false);
                    intent.putExtra("collection_sku_code", obj.getProduct_inventory_id());
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else if (item.getTitle().toString().toLowerCase().contains("cart")) {
                    if (SingletonSupport.getInstance().isLogin) {
                        CartGrid(obj.getProduct_inventory_id(),
                                "1",
                                "product_master",
                                userSessionManager.getUserID(),
                                "cart");
                        Productgridpojo.Result p = obj;
                        cartProduct = obj;
                        cartPossition = pos;
//                        p.setIn_cart("1");
//                        adapterGrid.updatelist(pos, p);
//                        adapterGrid.notifyItemRangeChanged(0, List.size() - 1);
//                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    } else {
                        Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "Please Login");
                    }
                } else if (item.getTitle().toString().toLowerCase().contains("wishlist")) {
                    if (SingletonSupport.getInstance().isLogin) {
                        CartGrid(obj.getProduct_inventory_id(), "1", "product_master", userSessionManager.getUserID(), "wishlist");
                        Productgridpojo.Result p = obj;
                        cartProduct = obj;
                        cartPossition = pos;
                       /* p.setIn_wishlist("1");
                        adapterGrid.updatelist(pos, p);
                        adapterGrid.notifyItemRangeChanged(0, List.size() - 1);
                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);*/
                    } else {
                        Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "Please Login");
                    }
                    /*Intent intent = new Intent(ProductGridActivity.this, ProductDetailActivity.class);
                    intent.putExtra("mode_type", mode_type);
                    intent.putExtra("table", table);
                    intent.putExtra("collection_id", collection_id);
                    intent.putExtra("my_collection_id", my_collection_id);
                    intent.putExtra("old", false);
                    intent.putExtra("collection_sku_code", obj.getProduct_inventory_id());
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);*/
                } else {
                    Snackbar.make(view, " (" + item.getTitle() + ") clicked", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        adapterGrid.setOnLoadMoreListener(new AdapterGridScrollProgress.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                loadNextData();
            }
        });

    }


    public void updateListItem(String cartCount, CartAcknowledge resource) {
        if (!enableSelection) {
            if (cartCount.equals("0")) {
                cartProduct.setIn_cart("0");
                cartProduct.setQuantity("0");
            } else {
                cartProduct.setIn_cart("1");
                cartProduct.setQuantity(cartCount);
            }
        }
        enableSelection = false;

        adapterGrid.updatelist(cartPossition, cartProduct);
        adapterGrid.notifyItemRangeChanged(0, List.size() - 1);
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        /*if (resource != null) {
            if (resource.getData().getNextOffer() != null) {
                String offer = "Coupan Name : " + resource.getData().getNextOffer().getCoupanName() + "\n"
                        + "Description : " + resource.getData().getNextOffer().getDescription() +
                        "\nFrom Weight :" + resource.getData().getNextOffer().getWeightFrom() +
                        "\nTo Weight :" + resource.getData().getNextOffer().getWeightTo()
                        + "\n\nCurrent Total Weight : " + resource.getData().getTotalWeight();
                String img=resource.getData().getNextOffer().getImage();
                showCustomDialogOffer("Next Offer Applied On", offer,img);
            }
        }*/
    }

    public  void showCustomDialogOffer(String t, String msg,String img) {
        final Dialog dialog = new Dialog(ProductGridActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info_offers);
        dialog.setCancelable(true);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.offer_image);
        String path="http://juliejuhi.goldguru.biz/public/backend/offers/";
        Glide.with(ProductGridActivity.this).load(path+img).placeholder(R.drawable.logo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
        title.setText(t);
        TextView content = (TextView) dialog.findViewById(R.id.content);
        content.setText(msg);
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


    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        adapterGrid.toggleSelection(position);
        int count = adapterGrid.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Tools.setSystemBarColor(ProductGridActivity.this, R.color.colorAccent);
            mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            int color = Color.parseColor(getResources().getString(R.string.color));
           // int color = Color.parseColor(get);//d2ac67
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_cart) {
                String ids = null;
                String idquantity = null;
                for (int i = 0; i < SingletonSupport.getInstance().multiSelectedIds.size(); i++) {
                    if (i == 0) {
                        ids = SingletonSupport.getInstance().multiSelectedIds.get(i);
                        idquantity = "1";
                    } else {
                        ids += "," + SingletonSupport.getInstance().multiSelectedIds.get(i);
                        idquantity += "," + idquantity;
                    }
                }

                CartGrid(ids, idquantity, table, userSessionManager.getUserID(), "cart");
                for (int i = 0; i < SingletonSupport.getInstance().multiSelectedPos.size(); i++) {
                    Productgridpojo.Result p = List.get(SingletonSupport.getInstance().
                            multiSelectedPos.get(i));
                    p.setIn_cart("1");
                    p.setQuantity("1");
                    adapterGrid.updatelist(SingletonSupport.getInstance().
                            multiSelectedPos.get(i), p);
                    adapterGrid.notifyItemRangeChanged(0, List.size() - 1);
                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                }

                mode.finish();
                btn_select.setBackgroundColor(btn_select.getContext().getResources().getColor(R.color.white));
                return true;
            } else {
                enableSelection = false;
                btn_select.setBackgroundColor(btn_select.getContext().getResources().getColor(R.color.white));
                mode.finish();
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapterGrid.clearSelections();
            actionMode = null;
            Tools.setSystemBarColor(ProductGridActivity.this, R.color.red_600);
        }
    }

    public void CartGrid(String product_id, String quantity, String product_inventory_table, String user_id,
                         final String cart_wish_table) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(ProductGridActivity.this);
        Call<CartAcknowledge> countriesCall = apiInterface.doAddToCartGrid(product_id, user_id, cart_wish_table, quantity, product_inventory_table);
        countriesCall.enqueue(new Callback<CartAcknowledge>() {
            @Override
            public void onResponse(Call<CartAcknowledge> call, Response<CartAcknowledge> response) {
                Log.d("CART GRID", response.code() + "");
                pdg.dismiss();
                if (response.code() == 200) {
                    CartAcknowledge resource = response.body();
                    if (resource.getAck().equals("1")) {
                        Toast.makeText(getApplicationContext(), resource.getMsg(), Toast.LENGTH_LONG).show();
                        if (!cart_wish_table.equals("wishlist")) {
                            updateListItem("1", resource);
                        } else {
                            enableSelection = false;
                        }
                        getCartCount(userSessionManager.getUserID(), "cart");
                    } else {
                        Toast.makeText(getApplicationContext(), resource.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    enableSelection = false;
                    Toast.makeText(getApplicationContext(), "Response code: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CartAcknowledge> call, Throwable t) {
                pdg.dismiss();
                enableSelection = false;
                call.cancel();
            }
        });
    }


    public void CartGridPlusMinus(String product_id, String quantity, String product_inventory_table, String user_id,
                                  String cart_wish_table, String plus) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(ProductGridActivity.this);
        Call<CartAcknowledge> countriesCall = apiInterface.doAddToCartGridPlus(product_id, user_id, cart_wish_table,
                quantity, product_inventory_table, plus);
        countriesCall.enqueue(new Callback<CartAcknowledge>() {
            @Override
            public void onResponse(Call<CartAcknowledge> call, Response<CartAcknowledge> response) {
                Log.d("CART GRID", response.code() + "");
                pdg.dismiss();
                if (response.code() == 200) {
                    CartAcknowledge resource = response.body();
                    if (resource.getAck().equals("1")) {
                        // Toast.makeText(getApplicationContext(), resource.getMsg(), Toast.LENGTH_LONG).show();
                        if (resource.getData() == null) {
                            updateListItem("0", null);
                        } else {
                            updateListItem(resource.getData().getQuantity(), resource);
                        }
                        getCartCount(userSessionManager.getUserID(), "cart");
                    } else {
                        Toast.makeText(getApplicationContext(), resource.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Response code: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CartAcknowledge> call, Throwable t) {
                pdg.dismiss();
                call.cancel();
            }
        });
    }

    public void getCartCount(String user_id, final String table) {

        final ProgressDialog pdg = ProgressIndicator.ShowLoading(ProductGridActivity.this);
        Call<CartCount> countriesCall = apiInterface.CartCount(user_id, table);
        countriesCall.enqueue(new Callback<CartCount>() {
            @Override
            public void onResponse(Call<CartCount> call, Response<CartCount> response) {
                pdg.dismiss();
                Log.d("doGetProductGrid", response.code() + "");
                final CartCount resource = response.body();
                SingletonSupport.getInstance().cartCount = resource.getCount();
                MenuItemBadge.getBadgeTextView(menuItemNotification).
                        setBadgeCount(String.valueOf(SingletonSupport.getInstance().cartCount));
            }

            @Override
            public void onFailure(Call<CartCount> call, Throwable t) {
                pdg.dismiss();

                Log.d("doGetProductGrid", t.toString() + "");
                call.cancel();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        //
    }


    public void getFilter(String user_id, String collection_id, String mode_type,
                          String table) {
        String url = Constant.Base_Url + "Filter_Parameter?user_id=" + user_id + "&collection_id=" + collection_id + "&mode_type=" + mode_type + "&table=" + table;
        Map<String, String> params = new HashMap<>();
        // the POST parameters:
        params.put("user_id", user_id);
        params.put("collection_id", collection_id);
        params.put("mode_type", mode_type);
        params.put("table", "product_master");
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(ProductGridActivity.this);
        Log.d("URL", url);
        AndroidNetworking.get(url)
//                .addBodyParameter("user_id", user_id)
//                .addBodyParameter("collection_id", collection_id)
//                .addBodyParameter("mode_type", mode_type)
//                .addBodyParameter("table", table)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonResponse = response;
                            if (jsonResponse != null && jsonResponse.length() > 0) {
                                Iterator<String> iter = jsonResponse.keys();
                                while (iter.hasNext()) {
                                    String key = iter.next();
                                    filterOptionsKeys.add(key);
                                }
                            }
                            filterParams = new ArrayList<FilterObject>();
                            if (!filterParams.isEmpty()) {
                                filterParams.clear();
                            }
                            for (int i = 0; i < filterOptionsKeys.size(); i++) {
                                FilterObject filter = new FilterObject();
                                JSONArray jarr = jsonResponse.getJSONArray(filterOptionsKeys.get(i));
                                if (jarr.length() > 0) {
                                    for (int j = 0; j < jarr.length(); j++) {
                                        JSONObject jobj = jarr.getJSONObject(j);
                                        String name = filterOptionsKeys.get(i);
                                        name = toTitleCase(name.replace('_', ' ').toLowerCase());
                                        filter.filterOptionName = name;
                                        filter.key = filterOptionsKeys.get(i);
                                        if (jobj.has("min_" + filterOptionsKeys.get(i))) {
                                            filter.min = new DecimalFormat("#0.000").format(Float.
                                                    parseFloat(jobj.getString("min_" + filterOptionsKeys.get(i))));
                                        }
                                        if (jobj.has("max_" + filterOptionsKeys.get(i))) {
                                            filter.max = new DecimalFormat("#0.000").format(Float.parseFloat(jobj.
                                                    getString("max_" + filterOptionsKeys.get(i))));
                                        }
                                        if (jobj.has("type")) {
                                            filter.type = jobj.getString("type");
                                        } else {
                                            filter.type = "";
                                        }
//                                        if (jobj.has("id")) {
//                                            filter.id.add(jobj.getString("id"));
//                                                if (filtjobj.getString("id"))) {
//                                                    filter.checked.add(true);
//                                                    filter.changedId.add(jobj.getString("id"));
//                                                } else {
//                                                    filter.checked.add(false);
//                                                }
//                                        }
                                    }
                                    filterParams.add(filter);
                                }
                            }
                            Log.d("!!!!", response.toString());
                            pdg.dismiss();
                            FragmentBottomSheetDialogFull fragment = new FragmentBottomSheetDialogFull();
                            fragment.setPeople(filterParams);
                            fragment.show(getSupportFragmentManager(), "dialog");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pdg.dismiss();
                    }
                });

       /* StringRequest postRequest = badgenew StringRequest(Request.Method.POST, url,
                badgenew com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                badgenew com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        pdg.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = badgenew HashMap<>();
                // the POST parameters:
                params.put("user_id", user_id);
                params.put("collection_id", collection_id);
                params.put("mode_type", mode_type);
                params.put("table", table);
                return params;
            }
        };

        postRequest.setRetryPolicy(badgenew DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(postRequest);*/
    }

    public String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }

    static class ItemSortAdapter extends RecyclerView.Adapter<ItemSortAdapter.ViewHolder> {

        private Activity mContext;
        private int sortPosi;
        private ArrayList<SortList> mItems;
        private ItemListener mListener;
        private View itemView;

        public ItemSortAdapter(Activity mContext, ArrayList<SortList> items, int sortPosi, ItemListener listener) {
            this.mContext = mContext;
            mItems = items;
            mListener = listener;
            this.sortPosi = sortPosi;
        }

        public void setListener(ItemListener listener) {
            mListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter2, parent, false);

            return new ViewHolder(itemView);
        }

        public void unhighlightCurrentRow(View rowView) {
            // Drawable d = mContext.getResources().getDrawable(R.drawable.ic_checked);
            ImageView check = (ImageView) rowView.findViewById(R.id.ivCheck);
            // check.setImageDrawable(d);
            check.setVisibility(View.GONE);
            rowView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        public void highlightCurrentRow(View rowView) {
            //Drawable d = mContext.getResources().getDrawable(R.drawable.ic_checked);
            ImageView check = (ImageView) rowView.findViewById(R.id.ivCheck);
            //check.setImageDrawable(d);
            check.setVisibility(View.VISIBLE);
            rowView.setBackgroundColor(mContext.getResources().getColor(R.color.overlay_light_30));

        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (position == sortPosi) {
                highlightCurrentRow(itemView);
//            notifyDataSetChanged();
            } else {
                unhighlightCurrentRow(itemView);
                // notifyDataSetChanged();
            }
            holder.setData(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public FontTextView textView;
            public SortList item;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                textView = (FontTextView) itemView.findViewById(R.id.textView);
            }

            public void setData(SortList item) {
                this.item = item;
                textView.setText(item.getLabel() + " "
                        + Character.toTitleCase(item.getType().charAt(0)) + item.getType().substring(1, item.getType().length()));
            }

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (item.getValue() != null && !item.getValue().isEmpty()) {
                        String sortValue = item.getValue();
                        int sortPosi = Integer.parseInt(String.valueOf(item.getValue()));
                        int pos = getAdapterPosition();
                        mListener.onItemClickSort(item, pos, sortPosi, sortValue);
                    } else {
                        Log.e("ERROR", "Sort Position is Empty");
                    }
                }
            }
        }

        public interface ItemListener {
            void onItemClickSort(SortList item, int pos, int position, String value);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grid, menu);
       // int color = Color.parseColor(getResources().getString(R.string.color));
        int color = getResources().getColor(R.color.white);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }

        menuItemNotification = menu.findItem(R.id.action_cart2);
        MenuItemBadge.update(this, menuItemNotification, new MenuItemBadge.Builder()
                .iconDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_cart))
                .iconTintColor(Color.WHITE)
                .textBackgroundColor(getResources().getColor(R.color.colorAccent))
                .textColor(Color.WHITE));
        if (SingletonSupport.getInstance().isLogin && SingletonSupport.getInstance().cartCount != 0) {
            MenuItemBadge.getBadgeTextView(menuItemNotification).
                    setBadgeCount(String.valueOf(SingletonSupport.getInstance().cartCount));
        } else {
            MenuItemBadge.getBadgeTextView(menuItemNotification).
                    setBadgeCount("0");
        }

       /* MenuItem item = menu.findItem(R.id.action_cart2);
        BitmapDrawable iconBitmap = (BitmapDrawable) item.getIcon();
        LayerDrawable icon = badgenew LayerDrawable(badgenew Drawable[]{iconBitmap});
        // LayerDrawable icon = (LayerDrawable) menu.findItem(R.id.action_cart).getIcon();
        Utils.setBadgeCount(this, icon, 5);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                SingletonSupport.getInstance().isfilter = false;
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
            case R.id.action_cart2:
                if (SingletonSupport.getInstance().isLogin) {
                    intent = new Intent(ProductGridActivity.this, CartActivity.class);
                    intent.putExtra("from", "cart");
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    Tools.setSnackBar(coordinatorLayout, "Please Login");
                }
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
          /*  case R.id.action_wishlist2:
                if (SingletonSupport.getInstance().isLogin) {
                    intent = badgenew Intent(ProductGridActivity.this, CartActivity.class);
                    intent.putExtra("from", "wishlist");
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    Tools.setSnackBar(coordinatorLayout, "Please Login");
                }
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;

            case R.id.action_list:
                viewtype = 1;
                Page = 0;
                initRecycleView();
                setRecyclerView(List);
                return true;

            case R.id.action_listdefault:
              *//*  viewtype = 0;
                Page = 0;

                initRecycleView();
                setRecyclerView(List);*//*
                return true;

            case R.id.action_grid:
               *//* viewtype = 2;
                Page = 0;
                initRecycleView();
                setRecyclerView(List);*//*
                return true;

            case R.id.action_grid2:
//                viewtype = 3;
//                Page = 0;
//                initRecycleView();
//                setRecyclerView(List);
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}
