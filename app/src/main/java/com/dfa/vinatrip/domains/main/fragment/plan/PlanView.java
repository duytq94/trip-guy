package com.dfa.vinatrip.domains.main.fragment.plan;

import com.dfa.vinatrip.base.BaseMvpView;

import java.util.List;

/**
 * Created by duytq on 10/30/2017.
 */

public interface PlanView extends BaseMvpView {
    void getPlanSuccess(List<Plan> planList);
}
