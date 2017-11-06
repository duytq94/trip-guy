package com.dfa.vinatrip.domains.main.fragment.plan;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.plan.PlanService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 10/30/2017.
 */

public class PlanPresenter extends BasePresenter<PlanView> {

    private AccountService accountService;
    private PlanService planService;
    private Subscription subscription;

    @Inject
    public PlanPresenter(EventBus eventBus, AccountService accountService, PlanService planService) {
        super(eventBus);
        this.accountService = accountService;
        this.planService = planService;
    }

    public boolean isLogin() {
        return accountService.loadFromStorage();
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void getPlan() {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = planService.getPlan(accountService.getCurrentUser().getId())
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(planList -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().getPlanSuccess(planList);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }
}