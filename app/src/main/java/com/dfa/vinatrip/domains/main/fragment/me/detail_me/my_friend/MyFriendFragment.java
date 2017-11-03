package com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_friend;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend.ListUserAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.utils.AdapterUserListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@EFragment(R.layout.fragment_my_friend)
public class MyFriendFragment extends BaseFragment<MyFriendView, MyFriendPresenter>
        implements MyFriendView, AdapterUserListener {

    @ViewById(R.id.fragment_my_friend_rv_list_friends)
    RecyclerView rvListFriends;
    @ViewById(R.id.fragment_my_friend_ll_friend_not_available)
    LinearLayout llFriendNotAvailable;

    private List<User> friendList;
    private ListUserAdapter adapter;

    @App
    protected MainApplication application;
    @Inject
    protected MyFriendPresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerMyFriendComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .build().inject(this);
    }

    @Override
    public MyFriendPresenter createPresenter() {
        return presenter;
    }

    @AfterViews
    public void init() {
        friendList = new ArrayList<>();
        adapter = new ListUserAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(rvListFriends.getContext(), layoutManager.getOrientation());
        rvListFriends.addItemDecoration(decoration);
        rvListFriends.setLayoutManager(layoutManager);

        presenter.getListFriend(1, 10);
    }

    @Override
    public void showLoading() {
//        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getListFriendSuccess(List<User> friendList) {
        if (friendList.size() == 0) {
            llFriendNotAvailable.setVisibility(View.VISIBLE);
        } else {
            llFriendNotAvailable.setVisibility(View.GONE);
            this.friendList.addAll(friendList);
            adapter.setListUser(this.friendList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBtnActionClick(int position, String command) {

    }
}
