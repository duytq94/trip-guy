package com.dfa.vinatrip.services.account;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.request.ChangePasswordRequest;
import com.dfa.vinatrip.models.request.ResetPasswordRequest;
import com.dfa.vinatrip.models.response.user.User;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface RestAccountService {
    @POST("api/auth/login")
    Observable<RestMessageResponse<User>> signIn(@Body AuthRequest authRequest);

    @POST("api/auth/register")
    Observable<RestMessageResponse<String>> signUp(@Body AuthRequest authRequest);

    @POST("api/auth/logout")
    Observable<RestMessageResponse<String>> signOut(@Header("access-token") String userToken);

    @PUT("api/user/edit")
    Observable<RestMessageResponse<User>> editProfile(@Header("access-token") String userToken,
                                                      @Body User user);

    @POST("api/auth/reset")
    Observable<RestMessageResponse<String>> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

    @POST("api/auth/change-password")
    Observable<RestMessageResponse<String>> changePassword(@Header("access-token") String userToken,
                                                           @Body ChangePasswordRequest changePasswordRequest);

    @GET("api/user/{id}/profile")
    Observable<RestMessageResponse<User>> getUserInfo(@Path("id") long userId);
}
