package com.srinidhi.lgb;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.srinidhi.lgb.Model.Users;
import com.srinidhi.lgb.Prevalent.Prevalent;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    private TextView user_name;
    private CircleImageView profile_image;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView;
    private TextView swipenametext;
    private TextView swipejbrtext;
    private TextView swipejdeptext;
    private TextView swipevnametext;
    private TextView swipevmodaltext;
    private TextView swipevengcctext;



    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        user_name = (TextView) view.findViewById(R.id.user_name);
//        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
        textView = (TextView) view.findViewById(R.id.textview);
        swipenametext = (TextView) view.findViewById(R.id.swipename);
        swipejbrtext = (TextView) view.findViewById(R.id.swipejob);
        swipejdeptext = (TextView) view.findViewById(R.id.swipedep);
        swipevnametext = (TextView) view.findViewById(R.id.swipevehiname);
        swipevmodaltext = (TextView) view.findViewById(R.id.swipevehimodalname);
//        swipevengcctext = (TextView) view.findViewById(R.id.swipevehiengcc);





            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    if (Prevalent.currentOnlineUser.getJobrole() != null) {
                        System.out.println(Prevalent.currentOnlineUser.getVehicleModalName());


                        textView.setText(Prevalent.currentOnlineUser.getName());
                        swipenametext.setText(Prevalent.currentOnlineUser.getName());
                        swipejbrtext.setText(Prevalent.currentOnlineUser.getJobrole());
                        swipejdeptext.setText(Prevalent.currentOnlineUser.getDepartment());
                        swipevnametext.setText(Prevalent.currentOnlineUser.getVehiclename());
                        swipevmodaltext.setText(Prevalent.currentOnlineUser.getVehicleModalName());
//                        swipevengcctext.setText(Prevalent.currentOnlineUser.getVehiclecc());


                    }



                    //setting Refreshing to false
                    swipeRefreshLayout.setRefreshing(false);

                }
            });


        user_name.setText("Profile Details");



        return view;
        // Inflate the layout for this fragment

        //return inflater.inflate(R.layout.fragment_blank, container, false);



    }

}
