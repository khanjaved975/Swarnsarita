package com.project.jewelmart.swarnsarita.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.project.jewelmart.swarnsarita.CheckoutActivity;
import com.project.jewelmart.swarnsarita.adapters.AdapterCartSummary;
import com.project.jewelmart.swarnsarita.adapters.AdapterProductCart;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.interfaces.OnLoadMoreListener;
import com.project.jewelmart.swarnsarita.models.CartSummary;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.Acknowledge;
import com.project.jewelmart.swarnsarita.pojo.Cart;
import com.project.jewelmart.swarnsarita.pojo.CartSummaryModel;
import com.project.jewelmart.swarnsarita.utils.Constant;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;
import com.project.jewelmart.swarnsarita.widgets.SpacingItemDecoration;
import com.project.jewelmart.swarnsarita.R;
import com.project.jewelmart.swarnsarita.SplashActivity;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.Tools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Carlos Vargas on 8/10/16.
 */

public class CartFragment extends Fragment implements OnLoadMoreListener {
    //for Home
    View view;
    APIInterface apiInterface;
    List<Cart.Datum> cartList;
    AdapterProductCart mAdapter;
    RecyclerView recyclerView;
    LinearLayout nodata;
    Button btn_checkout;
    Button btn_summary;
    UserSessionManager userSessionManager;

    List<List<String>> masterDesingList = new ArrayList<>();
    List<String> desing_quantity_weight = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.productgrid_layout, container, false);
        setHasOptionsMenu(true);
        userSessionManager = new UserSessionManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        if (Tools.isLandscape(getActivity())) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL,
                    false));
            recyclerView.addItemDecoration(new SpacingItemDecoration(4, dpToPx(getActivity(), 2),
                    false));
            recyclerView.setHasFixedSize(true);
            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new SpacingItemDecoration(2, dpToPx(getActivity(), 2), true));
            recyclerView.setHasFixedSize(true);
            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        }
        btn_checkout = (Button) view.findViewById(R.id.checkout);
        btn_summary = (Button) view.findViewById(R.id.summary);

        nodata = (LinearLayout) view.findViewById(R.id.nodata);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        if (!CheckNetwork.isConnected(getActivity())) {
            Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , Please Try after Connecting");
        } else {
            getCart(userSessionManager.getUserID(), "cart");
        }
        //set data and list adapter
        // on item list clicked
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingletonSupport.getInstance().cartCount > 0) {
                    //  checkoutCustomDialog("");
                    CartSummary(userSessionManager.getUserID(), "product_master", true);
                } else {
                    Tools.showCustomDialog(getActivity(), "Alert !", "No Product added in Cart");
                }
            }
        });

        btn_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingletonSupport.getInstance().cartCount > 0) {
                    //  checkoutCustomDialog("");
                    CartSummary2(userSessionManager.getUserID(), true);
                } else {
                    Tools.showCustomDialog(getActivity(), "Alert !", "No Product added in Cart");
                }
            }
        });

        return view;
    }

    public int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void getCart(String user_id, final String table) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(getActivity());
        Call<Cart> countriesCall = apiInterface.getCart(user_id, table);
        countriesCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                pdg.dismiss();
                Log.d("getCart", response.code() + "");
                if (response.code() == 200) {
                    try {
                        nodata.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Cart cdata = response.body();
                        final List<Cart.Datum> resource = cdata.getData();
                        if (resource != null) {
                            if (!resource.isEmpty()) {
                                SingletonSupport.getInstance().cartCount = resource.size();
                                cartList = resource;
                                mAdapter = new AdapterProductCart(getActivity(), cartList);
                                recyclerView.setAdapter(mAdapter);
                                mAdapter.setOnItemClickListener(new AdapterProductCart.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, Cart.Datum obj, int position) {
                                        Snackbar.make(view, "Item " + obj.getCollectionName() + " clicked", Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                                mAdapter.setOnMoreButtonClickListener(new AdapterProductCart.OnMoreButtonClickListener() {
                                    @Override
                                    public void onItemClick(View view, Cart.Datum obj, MenuItem item, int position) {
                                        if (item.getTitle().toString().toLowerCase().equals("delete")) {
                                            if (!CheckNetwork.isConnected(getActivity())) {
                                                Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , Please Try after Connecting");
                                            } else {
                                                mAdapter.Delete(userSessionManager.getUserID(), "cart", obj.getCartWishId(), position);
                                            }
                                        } else if (item.getTitle().toString().toLowerCase().contains("move")) {
                                            if (!CheckNetwork.isConnected(getActivity())) {
                                                Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , " +
                                                        "Please Try after Connecting");
                                            } else {
                                                mAdapter.Move(userSessionManager.getUserID(), "cart",
                                                        "wishlist", obj.getCartWishId(), position);
                                            }
                                        } else {
                                            Snackbar.make(view, obj.getCollectionName() + " (" + item.getTitle() + ") clicked",
                                                    Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                nodata.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        } else {
                            nodata.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                pdg.dismiss();
                Log.d("getCart", t.toString() + "");
                nodata.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                call.cancel();
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // tilAddress = (TextInputLayout) view.findViewById(R.id.tilAddress);
        //tieAddress = (TextInputEditText) view.findViewById(R.id.tieAddress);
        //  tieAddress.addTextChangedListener(badgenew CustomTextWatcher(tieAddress));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem item = menu.findItem(R.id.action_delete);
        int color = Color.parseColor(getResources().getString(R.string.color));
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
        menu.findItem(R.id.action_move).setVisible(false);
        menu.findItem(R.id.action_summary).setVisible(false);
        item.setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            if (!CheckNetwork.isConnected(getActivity())) {
                Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , Please Try after Connecting");
            } else {
                deleteCustomDialog();
            }
        } else if (item.getItemId() == R.id.action_move) {
            if (!CheckNetwork.isConnected(getActivity())) {
                Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , Please Try after Connecting");
            } else {
                MoveCustomDialog();
            }
        } else if (item.getItemId() == R.id.action_summary) {
            if (!CheckNetwork.isConnected(getActivity())) {
                Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , Please Try after Connecting");
            } else {
                if (SingletonSupport.getInstance().cartCount > 0) {
                    CartSummary(userSessionManager.getUserID(), "product_master", false);
                } else {
                    Tools.showCustomDialog(getActivity(), "Alert !", "No Product added in Cart");
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void CartSummary(String user_id, final String table, final boolean ischeckout) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(getActivity());
        Call<CartSummary> countriesCall = apiInterface.CartSummary(user_id, table);
        countriesCall.enqueue(new Callback<CartSummary>() {
            @Override
            public void onResponse(Call<CartSummary> call, Response<CartSummary> response) {
                pdg.dismiss();
                Log.d("Summary Cart", response.code() + "");
                final CartSummary resource = response.body();
                if (resource != null) {
                    if (!ischeckout) {
//                        cartSumaryDialog(String.valueOf(resource.getCartSummary().getGrossWt()),
//                                String.valueOf(resource.getCartSummary().getNetWt()), resource.getCartSummary().getGiftCoupanName(),
//                                resource.getCartSummary().getGiftCoupanMsg());
                    } else {
                        checkoutCustomDialog("\n Product Master : \n" + "Gross Weight : " + resource.getCartSummary().getProductMaster().getGrossWt() +
                                        "\n" + "Quantity : " + resource.getCartSummary().getProductMaster().getQuantity() + "\n\n Inventory Master : \n" + "Gross Weight : "
                                        + resource.getCartSummary().getInventoryMaster().getGrossWt()+"\n"+"Quantity : "+resource.getCartSummary().getInventoryMaster().getQuantity()
                                );
                    }
                }
            }

            @Override
            public void onFailure(Call<CartSummary> call, Throwable t) {
                pdg.dismiss();
                Log.d("Summary Cart", t.toString() + "");
                call.cancel();
            }
        });
    }

    public void CartSummary2(String user_id, final boolean ischeckout) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(getActivity());
        String url = Constant.Base_Url + "Cart_summary/total_cart?&user_id=" + user_id;
        Log.d("Cart_summary", url);
        AndroidNetworking.get(url)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pdg.dismiss();
                        Log.d("Cart_summary Reponse :", response.toString());

                        try {
                            masterDesingList.clear();
                            desing_quantity_weight.clear();
                            if (response.getString("ack").equalsIgnoreCase("1")) {
                                JSONObject jsondata = response.getJSONObject("data");
                                JSONArray category_array = jsondata.getJSONArray("category_type");
                                JSONObject jsoncartdata = jsondata.getJSONObject("cart_data");
                                for (int i = 0; i < category_array.length(); i++) {
                                    List<String> desingList = new ArrayList<>();
                                    int q = 0;
                                    float w = 0;

                                    JSONArray category_array_data = jsoncartdata.getJSONArray(category_array.getString(i));
                                    for (int j = 0; j < category_array_data.length(); j++) {
                                        JSONObject jsonObject = category_array_data.getJSONObject(j);
                                        Gson gson = new Gson();
                                        CartSummaryModel cartSummaryModel = gson.fromJson(jsonObject.toString(),
                                                CartSummaryModel.class);
                                        String quantity = "";
                                        if (!cartSummaryModel.getQuantity().equalsIgnoreCase("")) {
                                            quantity = "Quantity : " + cartSummaryModel.getQuantity();
                                        }
                                        String weight = "";
                                        if (!cartSummaryModel.getCusWeight().equalsIgnoreCase("")) {
                                            weight = "Weight : " + cartSummaryModel.getCusWeight();
                                        }
                                        String Color = "";
                                        if (!cartSummaryModel.getColor().equalsIgnoreCase("")) {
                                            Color = "Color : " + cartSummaryModel.getColor();
                                        }
                                        String design = cartSummaryModel.getProductId() + " (" + quantity + Color + weight + ")\n";
                                        desingList.add(design);

                                        q = q + Integer.valueOf(cartSummaryModel.getQuantity());
                                        if (cartSummaryModel.getCusWeight().isEmpty()) {
                                            w = w + Float.valueOf(cartSummaryModel.getNetWt());
                                        } else {
                                            w = w + Float.valueOf(cartSummaryModel.getCusWeight());
                                        }
                                    }

                                    masterDesingList.add(desingList);

                                    desing_quantity_weight.add("Total Quantity : " + q + ",\n" +
                                            "Total Weight : " + w);

                                }

                                cartSumaryDialog(jsondata.getString("total_quantity"),
                                        jsondata.getString("total_weight"), jsondata.getString("gift_coupan_name"),
                                        jsondata.getString("gift_coupan_msg"),
                                        masterDesingList, desing_quantity_weight, category_array);
                            } else {
                                Toast.makeText(getActivity(), response.getString("error"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        pdg.dismiss();
                        Toast.makeText(getActivity(), "Some Thing went wrong.", Toast.LENGTH_LONG).show();

                    }
                });
    }

    public void DeleteAll(String user_id, final String table) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(getActivity());
        Call<Acknowledge> countriesCall = apiInterface.clearCartData(user_id, table);
        countriesCall.enqueue(new Callback<Acknowledge>() {
            @Override
            public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                pdg.dismiss();
                Log.d("Delete Cart", response.code() + "");
                final Acknowledge resource = response.body();
                if (resource.getAck().equals("1")) {
                    Toast.makeText(getActivity(), resource.getMsg(), Toast.LENGTH_SHORT).show();
                    getCart(SingletonSupport.getInstance().user_id, "cart");
                    SplashActivity.getCartCount(getActivity(), userSessionManager.getUserID(), "cart");

                } else {
                    Toast.makeText(getActivity(), resource.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Acknowledge> call, Throwable t) {
                pdg.dismiss();
                Log.d("Delete Cart", t.toString() + "");
                call.cancel();
            }
        });
    }

    public void MoveAll(String user_id, final String from_table, final String to_table) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(getActivity());
        Call<Acknowledge> countriesCall = apiInterface.moveAllProduct(user_id, from_table, to_table);
        countriesCall.enqueue(new Callback<Acknowledge>() {
            @Override
            public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                pdg.dismiss();
                Log.d("Move Cart", response.code() + "");
                final Acknowledge resource = response.body();
                if (resource.getAck().equals("1")) {
                    Toast.makeText(getActivity(), resource.getMsg(), Toast.LENGTH_SHORT).show();
                    getCart(userSessionManager.getUserID(), "cart");
                    SplashActivity.getCartCount(getActivity(), userSessionManager.getUserID(), "cart");
                } else {
                    Toast.makeText(getActivity(), resource.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Acknowledge> call, Throwable t) {
                pdg.dismiss();
                Log.d("Move Cart", t.toString() + "");
                call.cancel();
            }
        });
    }

    private void checkoutCustomDialog(String data) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(false);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView offertitle = (TextView) dialog.findViewById(R.id.offer_title);
        title.setText("Alert !");
        TextView content = (TextView) dialog.findViewById(R.id.content);
        TextView content2 = (TextView) dialog.findViewById(R.id.content2);
        LinearLayout ll_offers = (LinearLayout) dialog.findViewById(R.id.ll_offers);
        ll_offers.setVisibility(View.VISIBLE);
        content2.setVisibility(View.VISIBLE);
        content.setText("Cart Summary \n " + data);
        content2.setText("\n Are you sure ? You want to check out, Click continue to proceed further ");
        TextView txtcoupanName = (TextView) dialog.findViewById(R.id.gift_coupan_name);

        TextView txtcoupanMsg = (TextView) dialog.findViewById(R.id.gift_coupan_msg);
        txtcoupanName.setVisibility(View.GONE);
        txtcoupanMsg.setVisibility(View.GONE);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                intent.putExtra("gift_id", "");
                startActivity(intent);
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

    @Override
    public void onStop() {
        super.onStop();
        if (cartList != null) {
            if (cartList.isEmpty()) {
                SingletonSupport.getInstance().cartCount = cartList.size();
            } else {
                SingletonSupport.getInstance().cartCount = 0;
            }
        } else {
            SingletonSupport.getInstance().cartCount = 0;
        }
    }

    private void cartSumaryDialog(String quantity, String weight, String coupanName,
                                  String coupanMsg,
                                  List<List<String>> masterDesingList, List<String> quantitylist, JSONArray array) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_cart_summary);
        dialog.setCancelable(true);

        RecyclerView recyclerView2 = dialog.findViewById(R.id.recyclerView);
        recyclerView2.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        recyclerView2.addItemDecoration(new SpacingItemDecoration(2, dpToPx(getActivity(), 2), true));
        recyclerView2.setHasFixedSize(true);
        AdapterCartSummary adapterCartSummary = new AdapterCartSummary(getActivity(), masterDesingList, quantitylist, array);
        recyclerView2.setAdapter(adapterCartSummary);


        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView offer = (TextView) dialog.findViewById(R.id.offer);
        title.setText("Cart Summary ");

        TextView gwt = (TextView) dialog.findViewById(R.id.quantity);
        gwt.setText("Total Quantity : " + quantity);

        TextView nwt = (TextView) dialog.findViewById(R.id.weight);
        nwt.setText("Total Weight : " + weight);

        TextView txtcoupanName = (TextView) dialog.findViewById(R.id.gift_coupan_name);
        txtcoupanName.setText("Coupan Name : " + coupanName);

        TextView txtcoupanMsg = (TextView) dialog.findViewById(R.id.gift_coupan_msg);
        txtcoupanMsg.setText("Coupan Description : \n" + coupanMsg);

        if (coupanName.equalsIgnoreCase("")){
            txtcoupanName.setVisibility(View.GONE);
            txtcoupanMsg.setVisibility(View.GONE);
            offer.setVisibility(View.GONE);
        }else{
            txtcoupanName.setVisibility(View.VISIBLE);
            txtcoupanMsg.setVisibility(View.VISIBLE);
            offer.setVisibility(View.VISIBLE);
        }

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

    private void deleteCustomDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("Alert !");
        TextView content = (TextView) dialog.findViewById(R.id.content);
        content.setText("Are you sure ? You want to delete all Cart Item , Click continue to proceed further ");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAll(userSessionManager.getUserID(), "cart");
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

    private void MoveCustomDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("Alert !");
        TextView content = (TextView) dialog.findViewById(R.id.content);
        content.setText("Are you sure ? You want to Move all Cart item to Wishlist  , Click continue to proceed further ");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveAll(userSessionManager.getUserID(), "cart", "wishlist");
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

    @Override
    public void onResume() {
        super.onResume();
       // getCart(userSessionManager.getUserID(), "cart");
    }

    @Override
    public void onLoadMore() {
        getCart(userSessionManager.getUserID(), "cart");
    }


}
