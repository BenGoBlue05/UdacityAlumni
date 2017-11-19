package com.google.developer.udacityalumni.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.developer.udacityalumni.R;


public class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        Button button = view.findViewById(R.id.placeholder_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(new Intent(Intent.ACTION_VIEW).setData(
                        Uri.parse("https://github.com/BenGoBlue05/UdacityAlumni"))));
            }
        });
        return view;
    }


}
