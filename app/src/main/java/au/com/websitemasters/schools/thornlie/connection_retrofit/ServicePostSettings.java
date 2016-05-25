package au.com.websitemasters.schools.thornlie.connection_retrofit;

import au.com.websitemasters.schools.thornlie.constants.Constants;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import au.com.websitemasters.schools.thornlie.objects_for_retrofit.SettingsObject;

public interface ServicePostSettings {

    @FormUrlEncoded
    @POST(Constants.SETTINGS)

    Call<SettingsObject> postSettings(
            @Field("device_uid") String device_uid,
            @Field("device_alerts") String device_alerts,
            @Field("device_newsletters") String device_newsletters,
            @Field("device_news") String device_news,
            @Field("device_parents_info") String device_parents_info,
            @Field("device_kindy") String device_kindy,
            @Field("device_pre_primary") String device_pre_primary,
            @Field("device_year1") String device_year1,
            @Field("device_year2") String device_year2,
            @Field("device_year3") String device_year3,
            @Field("device_year4") String device_year4,
            @Field("device_year5") String device_year5,
            @Field("device_year6") String device_year6
    );


}


