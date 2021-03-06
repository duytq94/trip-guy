package com.dfa.vinatrip.services.chat;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.domains.chat.BaseMessage;
import com.dfa.vinatrip.domains.chat.StatusUserChat;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by duytq on 09/28/2017.
 */

public interface RestChatService {
    @GET("history/{planId}")
    Observable<RestMessageResponse<List<BaseMessage>>> getHistory(
            @Path("planId") long groupId,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );

    @GET("status/{planId}")
    Observable<RestMessageResponse<List<StatusUserChat>>> getStatus(
            @Path("planId") long groupId
    );
}
