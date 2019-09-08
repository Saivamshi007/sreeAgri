package com.MithraAandS.projectsa2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class RiceCatagory extends Fragment {
    private ImageView organicImg,Inorganic;



    public RiceCatagory() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_rice_catagory, container, false);

        organicImg=rootView.findViewById(R.id.organicImage);
        Inorganic=rootView.findViewById(R.id.InorganicImg);

        organicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RiceFragment NAME = new RiceFragment();
                fragmentTransaction.replace(R.id.main_container, NAME);
                Bundle args = new Bundle();
                args.putString("RiceKey", "OrganicRice");
                NAME.setArguments(args);
                fragmentTransaction.commit();

            }
        });

        Inorganic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RiceFragment NAME = new RiceFragment();
                fragmentTransaction.replace(R.id.main_container, NAME);
                Bundle args = new Bundle();
                args.putString("RiceKey", "Rice");
                NAME.setArguments(args);
                fragmentTransaction.commit();
               /* Intent intent = new Intent(getActivity(), RiceDescription.class);
                intent.putExtra("loadsPosition","Rice");
                startActivity(intent);*/

            }
        });




        return rootView;
    }


}
