package com.kks.portfolio_android.api;



import com.kks.portfolio_android.retrofitmodel.user.UserReq;
import com.kks.portfolio_android.retrofitmodel.user.UserRes;
import com.kks.portfolio_android.util.Util;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

// 레트로핏 라이브러리 사용할때는 인터페이스로 먼저 선언
public interface UserApi {
    // http 메소드와 경로 설정
    // Call 에 <> 안에 들어갈 것은, 네트워크 통해서 받아온 데이터를 처리할 클래스
    // 메소드의 파라미터에는, 보낼 데이터를 처리할 클래스

    @POST(Util.SIGN_UP)
    Call<UserRes> signUp(@Body UserReq userReq);

    @POST(Util.LOGIN)
    Call<UserRes> login(@Body UserReq userReq);

    @DELETE(Util.LOGOUT)
    Call<UserRes> logout(@Header("Authorization") String token);

    @POST(Util.CHECK_ID)
    Call<UserRes> checkName(@Body UserReq userReq);

    // 프로필 사진 등록
    @Multipart
    @POST(Util.PROFILE_PHOTO)
    Call<UserRes> uploadProfile(@Header("Authorization") String token,
                                @Part MultipartBody.Part file);

    @GET(Util.USER_PAGE1+"/{user_id}")
    Call<UserRes> userPage1(@Path("user_id") int user_id);

    @GET(Util.USER_PAGE2+"/{user_id}")
    Call<UserRes> userPage2(@Path("user_id") int user_id);

    @GET(Util.GET_PAGE_PHOTO+"/{user_id}")
    Call<UserRes> getPagePhoto(@Path("user_id") int user_id,
                               @Query("offset") int offset,
                               @Query("limit") int limit);


}
