package com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_rating;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.province.each_item_detail_province.EachItemProvinceDetailActivity_;
import com.dfa.vinatrip.domains.main.fragment.province.each_item_detail_province.rating.UserRating;
import com.dfa.vinatrip.utils.RecyclerItemClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

//import com.dfa.vinatrip.domains.main.province.detail_province.province_destination.ProvinceDestination;

@EFragment(R.layout.fragment_my_rating)
public class MyRatingFragment extends Fragment {
    
    @ViewById(R.id.fragment_my_rating_rv_list_ratings)
    RecyclerView rvListRatings;
    
    @ViewById(R.id.fragment_my_rating_ll_rating_not_available)
    LinearLayout llRatingNotAvailable;
    
    private List<UserRating> myRatingList;
    private MyRatingAdapter myRatingAdapter;
    
    @AfterViews
    void onCreateView() {
        myRatingList = new ArrayList<>();
//        myRatingList.addAll(dataService.getMyRatingList());
        
        if (myRatingList.size() != 0) {
            rvListRatings.setVisibility(View.VISIBLE);
            llRatingNotAvailable.setVisibility(View.GONE);
        } else {
            rvListRatings.setVisibility(View.GONE);
            llRatingNotAvailable.setVisibility(View.VISIBLE);
        }
        
        myRatingAdapter = new MyRatingAdapter(getActivity(), myRatingList);
        rvListRatings.setAdapter(myRatingAdapter);
        
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(rvListRatings.getContext(),
                manager.getOrientation());
        rvListRatings.addItemDecoration(decoration);
        rvListRatings.setLayoutManager(manager);
        
        rvListRatings.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), rvListRatings, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UserRating myRating = myRatingList.get(position);
                switch (myRating.getType()) {
                    case "hotel":
                        EachItemProvinceDetailActivity_.intent(getActivity()).start();
                        break;
                    case "destination":
//                        ProvinceDestination destination = new ProvinceDestination
//                                (myRating.getLocationName(), null, null, null, myRating.getLocationProvince());
//                        EachItemProvinceDetailActivity_.intent(getActivity()).destination(destination).start();
                        break;
                    case "food":
                        EachItemProvinceDetailActivity_.intent(getActivity()).start();
                        break;
                }
            }
            
            @Override
            public void onLongItemClick(View view, int position) {
                
            }
        }));
    }
}
