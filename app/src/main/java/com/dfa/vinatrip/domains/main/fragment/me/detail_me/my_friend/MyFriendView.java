package com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_friend;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.user.FriendStatus;
import com.dfa.vinatrip.models.response.user.User;

import java.util.List;

/**
 * Created by duytq on 10/31/2017.
 */

public interface MyFriendView extends BaseMvpView {
    void getListFriendSuccess(List<User> userList, int page);

    void unfriendSuccess(FriendStatus friendStatus, int position);
}
