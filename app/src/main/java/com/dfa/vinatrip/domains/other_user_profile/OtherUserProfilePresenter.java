package com.dfa.vinatrip.domains.other_user_profile;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 1/5/2018.
 * duonghd1307@gmail.com
 */

public class OtherUserProfilePresenter extends BasePresenter<OtherUserProfileView> {
    private Subscription subscription;
    private AccountService accountService;

    @Inject
    public OtherUserProfilePresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }

    public void getUserInfo(long userId) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = accountService.getUserInfo(userId)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                }).subscribe(user -> {
                    if (isViewAttached()) {
                        getView().getUserInfoSuccess(user);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
}
