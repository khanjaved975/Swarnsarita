package com.project.jewelmart.swarnsarita.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.jewelmart.swarnsarita.R;

/**
 * Created by Carlos Vargas on 8/10/16.
 */

public class OrderFragment extends Fragment {
    //for Home
   View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_layout, container, false);

        return view;
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
