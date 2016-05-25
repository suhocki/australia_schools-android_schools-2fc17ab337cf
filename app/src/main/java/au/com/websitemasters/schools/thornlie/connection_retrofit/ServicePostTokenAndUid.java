package au.com.websitemasters.schools.thornlie.connection_retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import au.com.websitemasters.schools.thornlie.constants.Constants;

public interface ServicePostTokenAndUid {

    @FormUrlEncoded
    @POST(Constants.ADD_DEVICE)

    Call<ResponseBody> postToken(
            @Field("device_token") String device_token,
            @Field("device_uid") String uid
    );
}


