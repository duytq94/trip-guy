package com.dfa.vinatrip.domains.province_detail.view_all.festival.festival_detail;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.request.FeedbackRequest;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.feedback.FeedbackService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 12/29/2017.
 * duonghd1307@gmail.com
 */

public class FestivalDetailPresenter extends BasePresenter<FestivalDetailView> {
    private AccountService accountService;
    private FeedbackService feedbackService;
    private Subscription subscription;

    @Inject
    public FestivalDetailPresenter(EventBus eventBus, AccountService accountService, FeedbackService feedbackService) {
        super(eventBus);
        this.accountService = accountService;
        this.feedbackService = feedbackService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void sendFeedback(int placeId, FeedbackRequest feedbackRequest) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        String token = accountService.getCurrentUser().getAccessToken();
        subscription = feedbackService.postFestivalFeedback(token, placeId, feedbackRequest)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                })
                .subscribe(feedbackResponse -> {
                    if (isViewAttached()) {
                        feedbackResponse.setProfile(accountService.getCurrentUser());
                        getView().postFestivalFeedbackSuccess(feedbackResponse);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }

    public void getFestivalFeedback(int placeId, int page, int per_page) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = feedbackService.getFestivalFeedback(placeId, page, per_page)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                })
                .subscribe(feedbackResponses -> {
                    if (isViewAttached()) {
                        getView().getFestivalFeedbackSuccess(feedbackResponses);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }

    public void loginWithEmail(AuthRequest authRequest) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = accountService.login(authRequest)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                }).subscribe(user -> {
                    if (isViewAttached()) {
                        getView().signInSuccess(user);
                        EventBus.getDefault().post(user);

                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
}
