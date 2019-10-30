package com.project.jewelmart.swarnsarita.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.project.jewelmart.swarnsarita.models.Account;
import com.project.jewelmart.swarnsarita.R;
import com.project.jewelmart.swarnsarita.adapters.AdapterListSectioned;
import com.project.jewelmart.swarnsarita.utils.Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Carlos Vargas on 8/10/16.
 */

public class AccountFragment extends Fragment {
    //for Home
   View view;


    private RecyclerView recyclerView;
    private AdapterListSectioned mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.account_layout, container, false);
        initComponent();

        return view;
    }


    private void initComponent() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(false);

        List<Account> items = getAccountData(getActivity());
        //items.addAll(getAccountData(getActivity()));
        //items.addAll(getAccountData(getActivity()));

        int sect_count = 0;
        int sect_idx = 0;
        List<String> months =getStringsMonth(getActivity());
        for (int i = 0; i < items.size() / 3; i++) {
            items.add(sect_count, new Account(months.get(sect_idx), true));
            sect_count = sect_count + 4;
            sect_idx++;
        }




        //set data and list adapter
        mAdapter = new AdapterListSectioned(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListSectioned.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Account obj, int position) {
                Snackbar.make(view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    public static List<String> getStringsMonth(Context ctx) {
        List<String> items = new ArrayList<>();
        String arr[] = ctx.getResources().getStringArray(R.array.month);
        for (String s : arr) items.add(s);
        Collections.addAll(items);
        return items;
    }

    public static List<Account> getAccountData(Context context) {
        List<Account> items = new ArrayList<>();
        TypedArray drw_arr = context.getResources().obtainTypedArray(R.array.people_images);
        String name_arr[] = context.getResources().getStringArray(R.array.people_names);

        for (int i = 0; i < drw_arr.length(); i++) {
            Account obj = new Account();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.name = name_arr[i];
            obj.email = Tools.getEmailFromName(obj.name);
            obj.imageDrw = context.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        //Collections.shuffle(items);
        return items;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // tilAddress = (TextInputLayout) view.findViewById(R.id.tilAddress);
        //tieAddress = (TextInputEditText) view.findViewById(R.id.tieAddress);
        //  tieAddress.addTextChangedListener(badgenew CustomTextWatcher(tieAddress));
    }



}
