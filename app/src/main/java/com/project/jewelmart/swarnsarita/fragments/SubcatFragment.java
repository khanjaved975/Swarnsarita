package com.project.jewelmart.swarnsarita.fragments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.project.jewelmart.swarnsarita.adapters.LeftDrawerAdapter;
import com.project.jewelmart.swarnsarita.models.LeftDrawer;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.Collection;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.ProductGridActivity;
import com.project.jewelmart.swarnsarita.R;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by t2d on 30/03/16.
 */
public class SubcatFragment extends Fragment {

    View view;
    List<LeftDrawer> drawerList = new ArrayList<LeftDrawer>();
    private RecyclerView mDrawerList1;

    int backStackEntryCount = 0;
    FragmentManager frgManager;
    FragmentTransaction frgTransaction;
    Fragment fragment;
    private CharSequence mTitle;
    private int backCount;
    public Intent intent;
    protected TextView text;
    public LeftDrawerAdapter adapter;
    private SubcatFragment fragment1;
    private List<Collection.Subcategory> catsubCat;
    private List<Collection.Subcategory> subCat;
    private String[] dataList1;
    private int mcount;
    private Toolbar toolbar;
    private LinearLayout subCatMain;
    private int menuBg, menuHeaderColor, menuSeparatorColor, menuSelectionColor, navigationBg, navigationfg;
    private int[] menuSeparatorColorArr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.subcat_frag, container, false);


        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        // toolbar.setBackgroundColor(navigationBg);
        FontBoldTextView mTitle = (FontBoldTextView) toolbar.findViewById(R.id.toolbar_title);
        //mTitle.setTextColor(R.color.rippelColor);
        ImageView back = (ImageView) toolbar.findViewById(R.id.ic_LeftNavBack);
        Drawable ic_back = getResources().getDrawable(R.drawable.back);
        //ic_back.setColorFilter(navigationfg, PorterDuff.Mode.SRC_ATOP);
        back.setImageDrawable(ic_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else {
                    getActivity().onBackPressed();
                }
            }
        });

        subCatMain = (LinearLayout) view.findViewById(R.id.subCatMain);
        subCatMain.setBackgroundColor(menuBg);
        mDrawerList1 = (RecyclerView) view.findViewById(R.id.left_drawer1);
        ColorDrawable sage = new ColorDrawable(menuSeparatorColor);
        mDrawerList1.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDrawerList1.setHasFixedSize(true);
        OverScrollDecoratorHelper.setUpOverScroll(mDrawerList1, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        SingletonSupport.getInstance().backStackCount = 1;
        // mDrawerList1.setOnItemClickListener(badgenew DrawerItemClickListener());

        Bundle b = this.getArguments();
        if (b != null) {
            catsubCat = (ArrayList<Collection.Subcategory>) b.getSerializable("subcat");
            String catName = "";
           /* if (SingletonClass.getinstance() != null)
                if (SingletonClass.getinstance().settings != null && SingletonClass.getinstance().settings.get("show_short_code_in_category") != null
                        && SingletonClass.getinstance().settings.get("show_short_code_in_category").equalsIgnoreCase("no")) {
                    if (this.getArguments().getString("selectedCatName").contains("("))
                        catName = this.getArguments().getString("selectedCatName").substring(0, this.getArguments().getString("selectedCatName").indexOf("(") - 1);
                    else
                        catName = this.getArguments().getString("selectedCatName");
                } else {
                }*/
            catName = this.getArguments().getString("selectedCatName");

            mTitle.setText("" + catName.toUpperCase());
            Log.e("SubCat", "" + catsubCat);
        }
        drawerList = new ArrayList<LeftDrawer>();
        if (catsubCat != null && catsubCat.size() > 0) {
            for (int i = 0; i < catsubCat.size(); i++) {

                /*if (catsubCat.get(i).short_code.equals("") || catsubCat.get(i).short_code.toUpperCase().equals("ALL")) {
                    drawerList.add(badgenew DrawerItem(catsubCat.get(i).cat_name, catsubCat.get(i).count));
                } else {
                    if (SingletonClass.getinstance().settings.get("show_short_code_in_category").toLowerCase().equals("no")) {
                        drawerList.add(badgenew DrawerItem(catsubCat.get(i).cat_name , catsubCat.get(i).count));
                    }else{
                        drawerList.add(badgenew DrawerItem(catsubCat.get(i).cat_name + " (" + catsubCat.get(i).short_code.toUpperCase() + ")", catsubCat.get(i).count));
                    }
                }*/
                drawerList.add(new LeftDrawer(catsubCat.get(i).getColName(), catsubCat.get(i).getPosition()));

            }
        }
        adapter = new LeftDrawerAdapter(getActivity(), drawerList, ItemAnimation.LEFT_RIGHT);
        mDrawerList1.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new LeftDrawerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, LeftDrawer obj, int position) {
                fragment = null;
                Log.d("In MainActivity", "Position :: " + Integer.toString(position));
                String selected = drawerList.get(position).getName();
                subCat = catsubCat.get(position).getSubcategory();
                String catId = catsubCat.get(position).getId();

                if (subCat != null && !subCat.isEmpty()) {
                    addFragWithArray(subCat, selected);
                } else {
                    if (!CheckNetwork.isConnected(getActivity())) {
                        Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , Please Try after Connecting");
                    } else {
                        Intent intent = new Intent(getActivity(), ProductGridActivity.class);
                        intent.putExtra("collection_id", catId);
                        intent.putExtra("mode_type", "normal");
                        intent.putExtra("name", catsubCat.get(position).getColName());
                        intent.putExtra("table", "product_master");
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                }
            }
        });


        return view;
    }

    public class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            fragment = null;
            Log.d("In MainActivity", "Position :: " + Integer.toString(position));
            String selected = drawerList.get(position).getName();
            subCat = catsubCat.get(position).getSubcategory();
            String catId = catsubCat.get(position).getId();
            // String catProductCount = "" + catsubCat.get(position).count;
            // String imagetype = "" + catsubCat.get(position).image_type;
            // String sortPosition = "" + catsubCat.get(position).default_sort;
            // Log.wtf("!!!imagetypehome", imagetype);

            if (subCat != null && !subCat.isEmpty()) {
                addFragWithArray(subCat, selected);
            } else {
                Intent intent = new Intent(getActivity(), ProductGridActivity.class);
                intent.putExtra("collection_id", catsubCat.get(position).getId());
                intent.putExtra("name", catsubCat.get(position).getColName());
                intent.putExtra("mode_type", "normal");
                intent.putExtra("table", "product_master");
                startActivity(intent);


               /* Intent intent = badgenew Intent(getActivity(), ProductViewActivity.class);
                intent.putExtra("cat_id", catId);
                if (position != 0)
                    intent.putExtra("mode", "filter");
                else
                    intent.putExtra("mode", "all_filter");
                String catName = "";
                if (SingletonClass.getinstance().settings.get("show_short_code_in_category").equalsIgnoreCase("no")) {
                    if (selected.contains("("))
                        catName = selected.substring(0, selected.indexOf("(") - 1);
                    else
                        catName = selected;
                } else {
                    catName = selected;
                }
                intent.putExtra("cat_name", catName);
                intent.putExtra("matrix_count", catProductCount);
                intent.putExtra("image_type", imagetype);
                intent.putExtra("sort", sortPosition);
                startActivity(intent);*/
            }
        }

    }

    //    private void addFragWithArray(JSONArray subject) {
    private void addFragWithArray(List<Collection.Subcategory> subcat, String selected) {
        frgManager = getFragmentManager();
        fragment1 = new SubcatFragment();
        frgTransaction = frgManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("subcat", (Serializable) subcat);
        bundle.putString("selectedCatName", selected);
        fragment1.setArguments(bundle);
        frgTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        frgTransaction.replace(R.id.mainListFrame, fragment1);
        frgTransaction.addToBackStack("subcat1");
        frgTransaction.commit();
        getFragmentManager().executePendingTransactions();
    }

}
