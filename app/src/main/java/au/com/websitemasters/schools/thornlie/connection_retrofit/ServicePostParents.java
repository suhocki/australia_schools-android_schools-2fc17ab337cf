package au.com.websitemasters.schools.thornlie.connection_retrofit;

import au.com.websitemasters.schools.thornlie.objects_for_retrofit.ParentsObject;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServicePostParents {

    @FormUrlEncoded
    @POST("data/getInfo")

    Call<ParentsObject> postParents(
            @Field("pinfo_id") String pinfo_id
    );
}


