package com.example.appdrinkfruit.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdrinkfruit.Adapter.HOADON_ADAPTER;
import com.example.appdrinkfruit.Model.HOADON;
import com.example.appdrinkfruit.R;

import java.util.ArrayList;

public class SPDCFragment extends Fragment {
    public ArrayList<HOADON> hoadonArrayList;

    public SPDCFragment() {
        // Required empty public constructor
    }

    public static SPDCFragment newInstance(ArrayList<HOADON> selectedProducts) {
        SPDCFragment fragment = new SPDCFragment();
        Bundle args = new Bundle();
        args.putSerializable("spdc", selectedProducts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hoadonArrayList = (ArrayList<HOADON>) getArguments().getSerializable("spdc");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_spdcfragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvSPDC);
       HOADON_ADAPTER hoadonAdapter = new HOADON_ADAPTER(getContext(),hoadonArrayList,null); // Sử dụng getActivity() thay cho getContext()
        recyclerView.setAdapter(hoadonAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // Sử dụng getActivity()
        return view;
    }
}
