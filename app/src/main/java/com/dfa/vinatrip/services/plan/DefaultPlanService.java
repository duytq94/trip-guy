package com.dfa.vinatrip.services.plan;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.domains.main.fragment.plan.UserInPlan;
import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.PlanSchedule;
import com.dfa.vinatrip.services.filter.ApiErrorFilter;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 11/01/2017.
 */

public class DefaultPlanService implements PlanService {

    private RestPlanService restPlanService;
    private NetworkProvider networkProvider;
    private ApiErrorFilter apiErrorFilter;

    public DefaultPlanService(RestPlanService restPlanService, NetworkProvider networkProvider,
                              ApiErrorFilter apiErrorFilter) {
        this.restPlanService = restPlanService;
        this.networkProvider = networkProvider;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<String> createPlan(Plan newPlan) {
        return networkProvider
                .transformResponse(restPlanService.createPlan(newPlan))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<String> updatePlan(Plan newPlan) {
        return networkProvider
                .transformResponse(restPlanService.updatePlan(newPlan))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<List<Plan>> getPlan(long userId, String title, int type, int expired, long currentTimestamp, int page, int pageSize) {
        return networkProvider
                .transformResponse(restPlanService.getPlan(userId, title, type, expired, currentTimestamp, page, pageSize))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<List<PlanSchedule>> getPlanSchedule(long planId) {
        return networkProvider
                .transformResponse(restPlanService.getPlanSchedule(planId))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<List<UserInPlan>> getPlanUser(long planId) {
        return networkProvider
                .transformResponse(restPlanService.getPlanUser(planId))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<String> cancelPlan(long userId, long planId) {
        return networkProvider
                .transformResponse(restPlanService.cancelPlan(userId, planId))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<String> removePlan(long planId) {
        return networkProvider
                .transformResponse(restPlanService.removePlan(planId))
                .compose(apiErrorFilter.execute());
    }
}
