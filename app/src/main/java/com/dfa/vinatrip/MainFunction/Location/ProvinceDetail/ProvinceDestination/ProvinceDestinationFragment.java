package com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceDestination;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.EachItemProvinceDetailActivity_;
import com.dfa.vinatrip.MainFunction.Location.Province;
import com.dfa.vinatrip.MainFunction.RecyclerItemClickListener;
import com.dfa.vinatrip.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_province_destination)
public class ProvinceDestinationFragment extends Fragment {

    @ViewById(R.id.fragment_province_destination_rv_destinations)
    RecyclerView rvDestinations;

    @ViewById(R.id.fragment_province_destination_srl_reload)
    SwipeRefreshLayout srlReload;

    private List<ProvinceDestination> provinceDestinationList;
    private ProvinceDestinationAdapter provinceDestinationAdapter;
    private Province province;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    @AfterViews
    void onCreateView() {
        // Get Province from ProvinceDetailFragment
        province = (Province) getArguments().getSerializable("Province");

        srlReload.setColorSchemeResources(R.color.colorMain);

        provinceDestinationList = new ArrayList<>();
        provinceDestinationAdapter =
                new ProvinceDestinationAdapter(getActivity(), provinceDestinationList, srlReload);
        rvDestinations.setAdapter(provinceDestinationAdapter);

        if (CheckNetwork.isNetworkConnected(getActivity())) {
            loadProvinceDestination();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    provinceDestinationList.clear();
                    loadProvinceDestination();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvDestinations.setLayoutManager(staggeredGridLayoutManager);

        // Catch event click on item of RecyclerView
        rvDestinations.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                rvDestinations, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intentToEachDestination =
                        new Intent(getActivity(), EachItemProvinceDetailActivity_.class);

                // Send the Destination be chosen to EachItemProvinceDetailActivity
                intentToEachDestination.putExtra("DetailDestination", provinceDestinationList.get(position));
                getActivity().startActivity(intentToEachDestination);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    public void loadProvinceDestination() {
        srlReload.setRefreshing(true);

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // if no Internet, this method will not run
        databaseReference
                .child("ProvinceDestination")
                .child(province.getName())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String name, avatar, address, description,
                                typeOfTourism, scheduleAndFee, timeSpend, province;
                        float latitude, longitude;

                        name = dataSnapshot.child("name").getValue().toString();
                        avatar = dataSnapshot.child("avatar").getValue().toString();
                        address = dataSnapshot.child("address").getValue().toString();
                        description = dataSnapshot.child("description").getValue().toString();
                        typeOfTourism = dataSnapshot.child("typeOfTourism").getValue().toString();
                        scheduleAndFee = dataSnapshot.child("scheduleAndFee").getValue().toString();
                        timeSpend = dataSnapshot.child("timeSpend").getValue().toString();
                        latitude = Float.parseFloat(dataSnapshot.child("latitude").getValue()
                                .toString());
                        longitude = Float.parseFloat(dataSnapshot.child("longitude").getValue()
                                .toString());
                        province = dataSnapshot.child("province").getValue().toString();

                        ProvinceDestination provinceDestination = new
                                ProvinceDestination(name, avatar, address, description,
                                typeOfTourism, scheduleAndFee, timeSpend, province, latitude,
                                longitude);

                        provinceDestinationList.add(provinceDestination);
                        provinceDestinationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
