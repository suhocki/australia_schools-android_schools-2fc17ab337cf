package thronlie.balinasoft.by.sacredheartschoolthronlie.connection_retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_retrofit.ParentsObject;

public interface ServicePostParents {

    @FormUrlEncoded
    @POST("data/getInfo")

    Call<ParentsObject> postParents(
            @Field("pinfo_id") String pinfo_id
    );
}


