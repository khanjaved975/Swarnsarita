package com.project.jewelmart.swarnsarita.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.jewelmart.swarnsarita.BannerDetialActivity;
import com.project.jewelmart.swarnsarita.ProductDetailActivity;
import com.project.jewelmart.swarnsarita.adapters.CustomCatRecycleAdapter;
import com.project.jewelmart.swarnsarita.adapters.CustomStockCatRecycleAdapter;
import com.project.jewelmart.swarnsarita.adapters.RichProductAdapter;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.models.Image;
import com.project.jewelmart.swarnsarita.models.Images;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.CartAcknowledge;
import com.project.jewelmart.swarnsarita.pojo.CartCount;
import com.project.jewelmart.swarnsarita.pojo.Collection;
import com.project.jewelmart.swarnsarita.pojo.Home;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.parseColor;

public class HomeFragment extends Fragment {
    //for Home
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private AdapterImageSlider2 adapterImageSlider2;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    public static boolean enableSelection = false;
    View view;
    APIInterface apiInterface;
    List<Home.BrandBanner> bannerlist;
    List<Home.FinalCollection> productList;
    List<String> producttitle;
    public int themetype = 0;
    private Parcelable recyclerViewState;
    public static final String ORIENTATION = "orientation";
    private RecyclerView mRecyclerView;
    private boolean mHorizontal;
    LinearLayout rich_Product;
    UserSessionManager userSessionManager;
    CardView banner;
    List<com.project.jewelmart.swarnsarita.pojo.Collection> parentlist;
    Home.FinalCollection.ProductAssign cartProduct;
    private int cartPossition = 0,listposition=0;
     RichProductAdapter richProductAdapter;
    RecyclerView richproductlist;
    // private LocationManager mLocationManager;
    List<Home.FinalCollection> pList;
    private static String[] PERMISSIONS_LOCATION = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE};

    private static final int REQUEST_LOCATION = 10;
    public int widgetID=100;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_layout, container, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        //setHasOptionsMenu(true);
        userSessionManager = new UserSessionManager(getActivity());
        //initComponent();
        if (!CheckNetwork.isConnected(getActivity())) {
            Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection ," +
                    " Please Try after Connecting");
        } else {
            getHome();
            if (SingletonSupport.getInstance().isLogin) {
                if (!CheckNetwork.isConnected(getActivity())) {
                    Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , Please Try after Connecting");
                } else {
                    getCartCount(userSessionManager.getUserID(), "cart");
                }
            }
        }
        rich_Product = (LinearLayout) view.findViewById(R.id.rich_Product);
        banner = (CardView) view.findViewById(R.id.banner);
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        LinearLayout.LayoutParams layoutParams;
        if (Tools.isTablet(getActivity())) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
            if (!Tools.isLandscape(getActivity())) {
                layoutParams = new LinearLayout.LayoutParams(width, height / 2 - height / 5);
            } else {
                double customized_height = width / 2.8;
                layoutParams = new LinearLayout.LayoutParams(width, (int) customized_height);
            }
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
            if (!Tools.isLandscape(getActivity())) {
                layoutParams = new LinearLayout.LayoutParams(width, height / 2 - height / 4);
            } else {
                double customized_height = width / 2.8;
                layoutParams = new LinearLayout.LayoutParams(width, (int) customized_height);
            }
        }
        layoutParams.setMargins(0, 0, 0, 0);
        banner.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ActivityCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    || (ActivityCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                // Camera permission has not been granted.
                requestLocationPermission();

            }
        }
        return view;
    }

    int selectedItem = 0;
    private void setupAdapter(final List<Home.FinalCollection> productList, int hometype) {
        try {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            pList = productList;
            int linearItemCount=rich_Product.getChildCount();
            for ( int i = 0; i < productList.size(); i++) {
                List<Home.FinalCollection.ProductAssign> list = new ArrayList<>();
                list = productList.get(i).getProductAssign();
                FontBoldTextView titletext = new FontBoldTextView(getActivity());
                titletext.setTextSize(15f);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                params.setMargins(15, 6, 10, 8);
                titletext.setLayoutParams(params);
                //titletext.setTypeface(Typeface.DEFAULT_BOLD);
                titletext.setTextColor(getResources().getColor(R.color.colorAccent));
                titletext.setTextSize(18f);
                if (hometype == 0) {
                    titletext.setGravity(Gravity.CENTER);
                } /*else if (hometype == 1) {
                titletext.setGravity(Gravity.CENTER);
            } else if (hometype == 2) {
                titletext.setGravity(Gravity.CENTER);
            }*/
                titletext.setText(productList.get(i).getKey());//producttitle.get(i)
                // if (orderList.get(i).subcategories.size() > 0) {
                richproductlist = new RecyclerView(getActivity());
                if (hometype == 0) {
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                    richproductlist.setLayoutManager(llm);
              /*  GridLayoutManager llm;
                if (Tools.isLandscape(getActivity())){
                    llm = badgenew GridLayoutManager(getActivity(), 3);
                }else{
                    llm = badgenew GridLayoutManager(getActivity(), 2);
                }
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                richproductlist.setLayoutManager(llm);*/
                    //   richproductlist.setHasFixedSize(true);
                    richproductlist.setNestedScrollingEnabled(false);
                    recyclerViewState = richproductlist.getLayoutManager().onSaveInstanceState();
                } /*else if (hometype == 1) {
                GridLayoutManager llm;
                if (Tools.isLandscape(getActivity())){
                     llm = badgenew GridLayoutManager(getActivity(), 3);
                }else{
                    llm = badgenew GridLayoutManager(getActivity(), 2);
                }
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                richproductlist.setLayoutManager(llm);
            } else if (hometype == 2) {
                GridLayoutManager llm;
                if (Tools.isLandscape(getActivity())){
                    llm = badgenew GridLayoutManager(getActivity(), 2);
                }else{
                    llm = badgenew GridLayoutManager(getActivity(), 1);
                }
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                richproductlist.setLayoutManager(llm);
            }*/

/*        LinearLayout.LayoutParams layoutParams = badgenew LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, height / 2 - (height / 10));*/
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                richproductlist.setLayoutParams(layoutParams);
                richProductAdapter = new RichProductAdapter(getActivity(), list, hometype,(linearItemCount+1)+i);
                richproductlist.setAdapter(richProductAdapter);
                OverScrollDecoratorHelper.setUpOverScroll(richproductlist, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
                richProductAdapter.setOnItemClickListener(new RichProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Home.FinalCollection.ProductAssign obj, int position,int linearLayoutPosition) {
                        listposition=linearLayoutPosition;
                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                        intent.putExtra("mode_type", "home_products");
                        intent.putExtra("table", "product_master");
                        intent.putExtra("collection_id", "");
                        intent.putExtra("my_collection_id", "");
                        intent.putExtra("collection_sku_code", obj.getProduct_id());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                });

                richProductAdapter.setImageClickListener(new RichProductAdapter.OnImageClickListener() {
                    @Override
                    public void onItemClick(View view, Home.FinalCollection.ProductAssign obj, String type, int pos,int linearLayoutPosition) {
                        listposition=linearLayoutPosition;
                        if (type.toString().toLowerCase().equals("cart")) {
                            if (SingletonSupport.getInstance().isLogin) {
                                CartGrid(obj.getProduct_id(), "1",
                                        "product_master", userSessionManager.getUserID(),
                                        "cart");
                                //Productgridpojo.Result p = obj;
                                cartProduct = obj;
                                cartPossition = pos;
                                //  p.setIn_cart("1");
                                //  adapterGrid.updatelist(pos, p);
                                //  adapterGrid.notifyItemRangeChanged(0, List.size() - 1);
                                //  recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                            } else {
                                Tools.showCustomDialog(getActivity(), "Alert !", "Please Login");
                            }
                        } else if (type.toString().toLowerCase().equals("wishlist")) {
                   /* if (SingletonSupport.getInstance().isLogin) {
                        CartGrid(obj.getProduct_inventory_id(), "1", "product_master", userSessionManager.getUserID(), "wishlist");
                        Productgridpojo.Result p = obj;
                        p.setIn_wishlist("1");
                        adapterGrid.updatelist(pos, p);
                        adapterGrid.notifyItemRangeChanged(0, List.size() - 1);
                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    } else {
                        Tools.showCustomDialog(ProductGridActivity.this, "Alert !", "Please Login");
                    }
                        Intent intent = new2 Intent(ProductGridActivity.this, ProductDetailActivity.class);
                        intent.putExtra("mode_type", mode_type);
                        intent.putExtra("table", table);
                        intent.putExtra("collection_id", collection_id);
                        intent.putExtra("my_collection_id", my_collection_id);
                        intent.putExtra("old", false);
                        intent.putExtra("collection_sku_code", obj.getProduct_inventory_id());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        */

                        } else if (type.toString().toLowerCase().equals("cartplus")) {
                            if (SingletonSupport.getInstance().isLogin) {
                                CartGridPlusMinus(obj.getProduct_id(), "1",
                                        "product_master", userSessionManager.getUserID(),
                                        "cart", "1");
                                //Productgridpojo.Result p = obj;
                                cartProduct = obj;
                                cartPossition = pos;
                                //  p.setIn_cart("1");
                                //  adapterGrid.updatelist(pos, p);
                                //  adapterGrid.notifyItemRangeChanged(0, List.size() - 1);
                                //  recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                            } else {
                                Tools.showCustomDialog(getActivity(), "Alert !", "Please Login");
                            }
                        } else if (type.toString().toLowerCase().equals("cartminus")) {
                            if (SingletonSupport.getInstance().isLogin) {
                                CartGridPlusMinus(obj.getProduct_id(), "1",
                                        "product_master", userSessionManager.getUserID(),
                                        "cart", "0");
                                // Productgridpojo.Result p = obj;
                                cartProduct = obj;
                                cartPossition = pos;
                                //  p.setIn_cart("1");
                                //  adapterGrid.updatelist(pos, p);
                                //  adapterGrid.notifyItemRangeChanged(0, List.size() - 1);
                                //  recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                            } else {
                                Tools.showCustomDialog(getActivity(), "Alert !", "Please Login");
                            }
                        }
                    }
                });

                richProductAdapter.setOnMoreButtonClickListener(new RichProductAdapter.OnMoreButtonClickListener() {
                    @Override
                    public void onItemClick(View view, Home.FinalCollection.ProductAssign obj, MenuItem item, int pos) {
                        if (item.getTitle().toString().toLowerCase().equals("details")) {
                            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                            intent.putExtra("mode_type", "home_products");
                            intent.putExtra("table", "product_master");
                            intent.putExtra("collection_id", "");
                            intent.putExtra("my_collection_id", "");
                            intent.putExtra("old", false);
                            intent.putExtra("collection_sku_code", obj.getCollectionSkuCode());
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        } else if (item.getTitle().toString().toLowerCase().contains("cart")) {
                            if (!CheckNetwork.isConnected(getActivity())) {
                                Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , Please Try after Connecting");
                            } else {
                                CartGrid(obj.getCollectionSkuCode(), "1", obj.getCollectionSkuCode(),
                                        userSessionManager.getUserID(), "cart");
                                Home.FinalCollection.ProductAssign p = obj;
                                p.setIncart("1");
                                richProductAdapter.updatelist(pos, p);
                                richProductAdapter.notifyItemRangeChanged(0, productList.size() - 1);
                                richproductlist.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                            }
                        } else if (item.getTitle().toString().toLowerCase().contains("wishlist")) {
                            if (!CheckNetwork.isConnected(getActivity())) {
                                Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , Please Try after Connecting");
                            } else {
                                CartGrid(obj.getCollectionSkuCode(), "1", obj.getCollectionSkuCode(),
                                        userSessionManager.getUserID(), "wishlist");
                                Home.FinalCollection.ProductAssign p = obj;
                                p.setInwishlist("1");
                                richProductAdapter.updatelist(pos, p);
                                richProductAdapter.notifyItemRangeChanged(0, productList.size() - 1);
                                richproductlist.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                            }
                        } else {
                            Snackbar.make(view, " (" + item.getTitle() + ") clicked", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
              /*  titletext.setId(widgetID++);
                richproductlist.setId(widgetID++);*/
                richproductlist.setTag("recycle"+"_"+widgetID++);
                linearItemCount++;
                rich_Product.addView(titletext);
               // linearItemCount= linearItemCount+1;
                rich_Product.addView(richproductlist);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateListItem(String cartCount, String resource) {
        if (!enableSelection) {
            if (cartCount.equals("0")) {
                cartProduct.setIncart("0");
                cartProduct.setQuantity("0");
            } else {
                cartProduct.setIncart("1");
                cartProduct.setQuantity(cartCount);
            }
        }
        enableSelection = false;
        int linearcount=0;
        if (listposition%2==0){
            linearcount=listposition+1;
        }else{
            linearcount=listposition;
        }
       /* richProductAdapter = (RichProductAdapter) ((RecyclerView)rich_Product.getChildAt(richProductAdapter.getLinearLayoutPosition()))
                .getAdapter();*/
        if(rich_Product.getChildAt(linearcount) instanceof  RecyclerView){
            if(((RecyclerView)rich_Product.getChildAt(linearcount))
                    .getAdapter() instanceof  RichProductAdapter){
                richProductAdapter = (RichProductAdapter) ((RecyclerView)rich_Product.getChildAt(linearcount))
                        .getAdapter();
                richProductAdapter.updatelist(cartPossition, cartProduct);

                richProductAdapter.notifyItemRangeChanged(0, pList.size() - 1);
                richproductlist.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            }
        }



    }

    private void setCategory() {
        try {
            rich_Product.removeAllViews();
            rich_Product.setVisibility(View.VISIBLE);
            parentlist = SingletonSupport.getInstance().designcollectionlist;
            if (parentlist != null) {
                if (parentlist.size() > 0) {
                    RecyclerView featuredList = new RecyclerView(getActivity());
                    GridLayoutManager llm = new GridLayoutManager(getActivity(), 2);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    featuredList.setLayoutManager(llm);
                    FontBoldTextView titletext = new FontBoldTextView(getActivity());
                    titletext.setTextSize(15f);
//                                LinearLayout.LayoutParams layoutParams = badgenew LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.MATCH_PARENT, height / 2 - (height / 10));
                    GridLayoutManager.LayoutParams layoutParams = new GridLayoutManager.LayoutParams(
                            GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                    featuredList.setLayoutParams(layoutParams);
                    // featuredList.setHasFixedSize(true);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    params.setMargins(15, 6, 10, 8);
                    OverScrollDecoratorHelper.setUpOverScroll(featuredList, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
                    titletext.setTextColor(getResources().getColor(R.color.colorAccent));
                    titletext.setLayoutParams(params);
                    featuredList.setNestedScrollingEnabled(false);
                    // titletext.setLayoutParams(layoutParams);
                    //titletext.setTypeface(Typeface.DEFAULT_BOLD);
                    titletext.setTextColor(getResources().getColor(R.color.colorAccent));
                    titletext.setTextSize(18f);
                    titletext.setGravity(Gravity.CENTER);
                    titletext.setText("Categories");
                    CustomCatRecycleAdapter customRecycleAdapter = new CustomCatRecycleAdapter(getActivity(), (ArrayList<Collection>) parentlist);
                    featuredList.setAdapter(customRecycleAdapter);
                   /*titletext.setId(widgetID++);
                    featuredList.setId(widgetID++);*/
                    rich_Product.addView(titletext);
                    rich_Product.addView(featuredList);

                    //  }
                }

                if (parentlist.size() > 0) {
                    RecyclerView featuredList = new RecyclerView(getActivity());
                    GridLayoutManager llm = new GridLayoutManager(getActivity(), 2);
                    llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                    featuredList.setLayoutManager(llm);
                    FontBoldTextView titletext = new FontBoldTextView(getActivity());
                    titletext.setTextSize(15f);
//                                LinearLayout.LayoutParams layoutParams = badgenew LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.MATCH_PARENT, height / 2 - (height / 10));
                    GridLayoutManager.LayoutParams layoutParams = new GridLayoutManager.LayoutParams(
                            GridLayoutManager.LayoutParams.WRAP_CONTENT, 540);
                    featuredList.setLayoutParams(layoutParams);
                    // featuredList.setHasFixedSize(true);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    params.setMargins(15, 6, 10, 8);
                    OverScrollDecoratorHelper.setUpOverScroll(featuredList, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
                    titletext.setTextColor(getResources().getColor(R.color.colorAccent));
                    titletext.setLayoutParams(params);
                    featuredList.setNestedScrollingEnabled(false);
                    // titletext.setLayoutParams(layoutParams);
                    //titletext.setTypeface(Typeface.DEFAULT_BOLD);
                    titletext.setTextColor(getResources().getColor(R.color.colorAccent));
                    titletext.setTextSize(18f);
                    titletext.setGravity(Gravity.CENTER);

                    titletext.setText("Stock Product Category");
                    CustomStockCatRecycleAdapter customRecycleAdapter = new CustomStockCatRecycleAdapter(getActivity(), (ArrayList<Collection>) parentlist);
                    featuredList.setAdapter(customRecycleAdapter);
                    titletext.setId(widgetID++);
                    featuredList.setId(widgetID++);
                    rich_Product.addView(titletext);
                    rich_Product.addView(featuredList);

                    //  }
                }
            } else {
                Toast.makeText(getActivity(), "No data found ,Please try again", Toast.LENGTH_LONG).show();
            }

            setupAdapter(productList, themetype);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void requestLocationPermission() {
        Log.i("Fragment 1", "Location permission has NOT been granted. Requesting permission.");
        if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                (android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_LOCATION, REQUEST_LOCATION);
        } else {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_LOCATION, REQUEST_LOCATION);
        }
    }

    public void CartGridPlusMinus(String product_id, String quantity, String product_inventory_table, String user_id,
                                  String cart_wish_table, String plus) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(getActivity());
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
                            updateListItem(resource.getData().getQuantity(), null);
                        }
                        getCartCount(userSessionManager.getUserID(), "cart");
                    } else {
                        Toast.makeText(getActivity(), resource.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Response code: " + response.code(),
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //resume tasks needing this permission
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ORIENTATION, mHorizontal);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // tilAddress = (TextInputLayout) view.findViewById(R.id.tilAddress);
        //tieAddress = (TextInputEditText) view.findViewById(R.id.tieAddress);
        //  tieAddress.addTextChangedListener(badgenew CustomTextWatcher(tieAddress));
    }

    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<Image> items;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, Image obj);
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<Image> items) {
            this.act = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public Image getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<Image> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Image o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);
            ImageView image = (ImageView) v.findViewById(R.id.image);
            MaterialRippleLayout lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
            displayImageOriginal(act, image, o.image);
            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, o);
                    }
                }
            });

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];
        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getActivity());
            int width_height = 14;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 0, 10, 0);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            dots[i].setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey_60),
                    PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
            dots[current].setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent),
                    PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    public static void displayImageOriginal(Context ctx, ImageView img, @DrawableRes int drawable) {
        try {
            Glide.with(ctx).load(drawable)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
        } catch (Exception e) {
        }
    }

    public static void displayImageOriginal2(Context ctx, ImageView img, String path, String name) {
        try {
            Glide.with(ctx).load(path + name)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    public void getHome() {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(getActivity());
        Call<List<Home>> countriesCall = apiInterface.doGetHome("android", userSessionManager.getUserID());
        countriesCall.enqueue(new Callback<List<Home>>() {
            @Override
            public void onResponse(Call<List<Home>> call, Response<List<Home>> response) {
                try {
                    Log.d("home fragment", response.code() + "");
                    layout_dots = (LinearLayout) view.findViewById(R.id.layout_dots);
                    viewPager = (ViewPager) view.findViewById(R.id.pager);
                    adapterImageSlider2 = new AdapterImageSlider2(getActivity(), new ArrayList<Images>());
                    List<Home> resource = response.body();
                    bannerlist = resource.get(0).getBrandBanner();
                    productList = resource.get(0).getFinalCollection();
                    //producttitle = resource.get(0).getHotProductsLabel();
                    final List<Images> items = new ArrayList<>();
                    for (int i = 0; i < bannerlist.size(); i++) {
                        Images img = new Images();
                        img.name = bannerlist.get(i).getBrandImage();
                        img.path = resource.get(0).getBasePath();
                        img.desc = bannerlist.get(i).getDescription();
                        items.add(img);
                    }
                    adapterImageSlider2.setItems(items);
                    adapterImageSlider2.setOnItemClickListener(new AdapterImageSlider2.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, Images obj) {
                            Intent intent=new Intent(getActivity(), BannerDetialActivity.class);
                            intent.putExtra("title","Banner Details");
                            intent.putExtra("desc",obj.desc);
                            intent.putExtra("img",obj.path+obj.name);
                            startActivity(intent);
                        }
                    });
                    viewPager.setAdapter(adapterImageSlider2);
                    viewPager.setCurrentItem(0);
                    addBottomDots(layout_dots, adapterImageSlider2.getCount(), 0);
                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
                        }

                        @Override
                        public void onPageSelected(int pos) {
                            // ((TextView) view.findViewById(R.id.title)).setText(items.get(pos).name);
                            //((TextView) view.findViewById(R.id.brief)).setText(items.get(pos).brief);
                            addBottomDots(layout_dots, adapterImageSlider2.getCount(), pos);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                        }
                    });
                    startAutoSlider(adapterImageSlider2.getCount());
                    //  setupAdapter(productList, themetype);
                    setCategory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pdg.dismiss();
            }

            @Override
            public void onFailure(Call<List<Home>> call, Throwable t) {
                Log.d("home fragment", t.toString() + "");
                pdg.dismiss();
                call.cancel();
            }

        });

    }

    private static class AdapterImageSlider2 extends PagerAdapter {

        private Activity act;
        private List<Images> items;

        private AdapterImageSlider2.OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, Images obj);
        }

        public void setOnItemClickListener(AdapterImageSlider2.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider2(Activity activity, List<Images> items) {
            this.act = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public Images getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<Images> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Images o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);
            ImageView image = (ImageView) v.findViewById(R.id.image);
            MaterialRippleLayout lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
            displayImageOriginal2(act, image, o.path, o.name);
            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, o);
                    }
                }
            });

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);
        }

    }

    public void getCartCount(String user_id, final String table) {

        final ProgressDialog pdg = ProgressIndicator.ShowLoading(getActivity());
        Call<CartCount> countriesCall = apiInterface.CartCount(user_id, table);
        countriesCall.enqueue(new Callback<CartCount>() {
            @Override
            public void onResponse(Call<CartCount> call, Response<CartCount> response) {
                pdg.dismiss();
                Log.d("doGetProductGrid", response.code() + "");
                final CartCount resource = response.body();
                if (resource!=null) {
                    if (resource.getCount() != null) {
                        SingletonSupport.getInstance().cartCount = resource.getCount();
                    } else {
                        SingletonSupport.getInstance().cartCount = 0;
                    }
                }
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
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        //MenuItem item = menu.findItem(R.id.action_hometheme);
        int color = parseColor("#666666");
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
        //item.setVisible(true);
        //menu.findItem(R.id.action_openRight).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_hometheme) {
           // if (themetype == 0) {
                setupAdapter(productList, 1);
             *//*   themetype = 1;
            } else if (themetype == 1) {
                setupAdapter(productList, 2);
                themetype = 2;
            } else if (themetype == 2) {
                setupAdapter(productList, 0);
                themetype = 0;
            }*//*
        }
        return super.onOptionsItemSelected(item);
    }*/


    public void CartGrid(String product_id, String quantity, String product_inventory_table, String user_id,
                         String cart_wish_table) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(getActivity());
        Call<CartAcknowledge> countriesCall = apiInterface.doAddToCartGrid(product_id, user_id, cart_wish_table, quantity, product_inventory_table);
        countriesCall.enqueue(new Callback<CartAcknowledge>() {
            @Override
            public void onResponse(Call<CartAcknowledge> call, Response<CartAcknowledge> response) {
                Log.d("CART GRID", response.code() + "");
                pdg.dismiss();
                if (response.code() == 200) {
                    CartAcknowledge resource = response.body();
                    if (resource.getAck().toString().equals("1")) {
                        Toast.makeText(getActivity(), resource.getMsg(), Toast.LENGTH_LONG).show();
                        getCartCount(userSessionManager.getUserID(), "cart");
                        updateListItem("1",null);
                    } else {
                        Toast.makeText(getActivity(), resource.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Response code: " + response.code(),
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


}
