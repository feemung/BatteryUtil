package com.e.laocow.batteryutil;

import android.app.Activity;
import android.app.FragmentTransaction;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class ShutdownDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ListView dataLV;
    private Button closeBut;
    private Logger l=Logger.getLogger(ShutdownDataFragment.class);


    public ShutdownDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_shutdown_data, container, false);
        dataLV=(ListView)view.findViewById(R.id.shutdownDataLV);
        dataLV.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1,getData()));
       closeBut=(Button)view.findViewById(R.id.closeDataFragment);
        closeBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FragmentTransaction fragmentTransaction= getActivity().getFragmentManager().beginTransaction();
                fragmentTransaction.remove(ShutdownDataFragment.this);

                fragmentTransaction.commit();
                MainActivity.mainActivity.mainLL.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
    public List<String> getData(){
        List<String> list=new ArrayList<>();
       Map<String,Integer> map= DbManager.instance().getAll();
        Iterator<Map.Entry<String,Integer>> iterator=map.entrySet().iterator();
        int i=0;
        while(iterator.hasNext()){
            Map.Entry<String,Integer> entry=iterator.next();
            String time=entry.getKey();
            Integer scale=entry.getValue();
            i++;
            list.add(String.valueOf(i)+"、 关机时间："+time+"; 剩余电量："+String.valueOf(scale)+"%");

        }
        l.d("getData:%d",list.size());
        return list;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       /* try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}
