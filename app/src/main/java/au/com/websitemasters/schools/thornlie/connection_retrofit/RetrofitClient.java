package au.com.websitemasters.schools.thornlie.connection_retrofit;

import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

import au.com.websitemasters.schools.thornlie.utils.ServerObserverRetrofit;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import au.com.websitemasters.schools.thornlie.objects_for_retrofit.ANNEObject;
import au.com.websitemasters.schools.thornlie.objects_for_retrofit.ContactsObject;
import au.com.websitemasters.schools.thornlie.objects_for_retrofit.ParentsObject;
import au.com.websitemasters.schools.thornlie.objects_for_retrofit.SettingsObject;

public class RetrofitClient {

    private ServerObserverRetrofit<Object, String> serverObserverRetrofit;
    private final String POST_SCHOOL = "http://balinasoft.com/SacredHeartSchoolThornlie/";

    public void postToken(String token, String uid) throws JSONException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(POST_SCHOOL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServicePostTokenAndUid service = retrofit.create(ServicePostTokenAndUid.class);

        Call<ResponseBody>  result = service.postToken(token, uid);
        result.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseToString = response.body().string();
                    Log.d("rklogs", "responseToString_" + responseToString);
                } catch (NullPointerException e) {
                    Log.d("rklogs", "!response is null");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("rklogs", "onFailure");
            }
        });
    }

    public void postParents(String pinfo_id)  {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(POST_SCHOOL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServicePostParents service = retrofit.create(ServicePostParents.class);
        Call<ParentsObject>  result = service.postParents(pinfo_id);
        result.enqueue(new Callback<ParentsObject>() {
            @Override
            public void onResponse(Call<ParentsObject> call, Response<ParentsObject> response) {
                try {
                    serverObserverRetrofit.successExecuteObject(response.body());
                } catch (NullPointerException e) {
                    Log.d("rklogs", "!response is null");
                }
            }
            @Override
            public void onFailure(Call<ParentsObject> call, Throwable t) {
                Log.d("rklogs", "onFailure");
            }
        });
    }

    public void postANNE(String uid, long last_date_newsletters, long last_date_events,
                         long last_date_news, long last_date_alerts, long last_date_pinfo)  {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(POST_SCHOOL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServicePostANNE service = retrofit.create(ServicePostANNE.class);
        Call<ANNEObject>  result = service.postANNE(
                uid,
                last_date_newsletters,
                last_date_events,
                last_date_news,
                last_date_alerts,
                last_date_pinfo);
        result.enqueue(new Callback<ANNEObject>() {
            @Override
            public void onResponse(Call<ANNEObject> call, Response<ANNEObject> response) {
                Object obj = null;
                try {
                    obj  = response.body();


                } catch (NullPointerException e) {
                    Log.d("rklogs", "error (postANNE): response.body() is null");
                }
                try {
                    serverObserverRetrofit.successExecuteObject(obj);
                } catch (NullPointerException e) {
                    Log.d("rklogs", "error (postANNE): serverObserverRetrofit.successExecuteObject is null_" + e);
                }
            }
            @Override
            public void onFailure(Call<ANNEObject> call, Throwable t) {
                Log.d("rklogs", "onFailure postANNE_" + t);
                serverObserverRetrofit.failedExecute("");
            }
        });
    }

    public void getContacts() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(POST_SCHOOL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServiceGetContacts service = retrofit.create(ServiceGetContacts.class);
        Call<ContactsObject>  result = service.getContacts();
        result.enqueue(new Callback<ContactsObject>() {
            @Override
            public void onResponse(Call<ContactsObject> call, Response<ContactsObject> response) {
                Object obj = null;
                try {
                    obj = response.body();
                } catch (NullPointerException e) {
                    Log.d("rklogs", "error (ContactsObject): response.body() is null");
                }
                try {
                    serverObserverRetrofit.successExecuteObject(obj);
                } catch (NullPointerException e) {
                    Log.d("rklogs", "error (ContactsObject): serverObserverRetrofit.successExecuteObject is null");
                }
            }
            @Override
            public void onFailure(Call<ContactsObject> call, Throwable t) {
                Log.d("rklogs", "onFailure Contacts_" + t);
            }
        });
    }


    public void postSettings(String device_uid, String  device_alerts, String device_newsletters,
                             String device_news, String device_parents_info, String device_kindy,
                             String device_pre_primary, String device_year1, String device_year2,
                             String device_year3, String device_year4, String device_year5,
                             String device_year6)  {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(POST_SCHOOL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServicePostSettings service = retrofit.create(ServicePostSettings.class);
        Call<SettingsObject>  result = service.postSettings(
                device_uid,
                device_alerts,
                device_newsletters,
                device_news,
                device_parents_info,
                device_kindy,
                device_pre_primary,
                device_year1,
                device_year2,
                device_year3,
                device_year4,
                device_year5,
                device_year6);

        result.enqueue(new Callback<SettingsObject>() {
            @Override
            public void onResponse(Call<SettingsObject> call, Response<SettingsObject> response) {

                try {
                    SettingsObject settObject = (SettingsObject) response.body();
                    Log.d("rklogs", "settObject.status_" + settObject.status);
                    Log.d("rklogs", "settObject.data_" + (settObject.data).toString());
                } catch (NullPointerException e) {
                    Log.d("rklogs", "error (postSett): response.body() is null");
                }
            }
            @Override
            public void onFailure(Call<SettingsObject> call, Throwable t) {
                Log.d("rklogs", "onFailure postSett_" + t);
            }
        });
    }

    public void setServerObserverRetrofit(ServerObserverRetrofit<Object, String> serverObserverRetrofit) {
        this.serverObserverRetrofit = serverObserverRetrofit;
    }
}
