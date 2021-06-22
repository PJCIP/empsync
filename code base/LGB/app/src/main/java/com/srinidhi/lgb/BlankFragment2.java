package com.srinidhi.lgb;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment2 extends Fragment {
    private Button Submitbtn;
    private EditText inputjbr, inputdepart, inputvehiname, inputvehimodal, inputcc, inputPhone;
    private CircleImageView pro_image;
    private ProgressDialog loadingBar;


    public BlankFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view2 = inflater.inflate(R.layout.fragment_blank, container, false);
        Submitbtn = (Button) view2.findViewById(R.id.asset_btn);
        inputjbr = (EditText) view2.findViewById(R.id.Job_role);
        inputdepart = (EditText) view2.findViewById(R.id.Dep);
        inputvehiname = (EditText) view2.findViewById(R.id.Vehi);
        inputvehimodal = (EditText) view2.findViewById(R.id.Model_name);
        inputcc = (EditText) view2.findViewById(R.id.eng_cc);
        inputPhone = (EditText) view2.findViewById(R.id.cnfrm_phone);
//        pro_image = (CircleImageView) view2.findViewById(R.id.pro_image);
        loadingBar = new ProgressDialog(getContext());

        // Inflate the layout for this fragment
        return view2;
    }

}


