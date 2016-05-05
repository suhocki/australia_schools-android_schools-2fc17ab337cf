package thronlie.balinasoft.by.sacredheartschoolthronlie.connection_retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import thronlie.balinasoft.by.sacredheartschoolthronlie.constants.Constants;
import thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_retrofit.ANNEObject;

public interface ServicePostANNE {

    @FormUrlEncoded
    @POST(Constants.LIST)

    Call<ANNEObject> postANNE(
            @Field("device_uid") String device_uid,
            @Field("last_date_newsletters") long last_date_newsletters,
            @Field("last_date_events") long last_date_events,
            @Field("last_date_news") long last_date_news,
            @Field("last_date_alerts") long last_date_alerts
    );


}


