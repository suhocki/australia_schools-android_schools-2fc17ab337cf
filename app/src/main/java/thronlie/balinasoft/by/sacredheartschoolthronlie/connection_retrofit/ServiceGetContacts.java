package thronlie.balinasoft.by.sacredheartschoolthronlie.connection_retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import thronlie.balinasoft.by.sacredheartschoolthronlie.constants.Constants;
import thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_retrofit.ContactsObject;

public interface ServiceGetContacts {

    @GET(Constants.CONTACT)

    Call<ContactsObject> getContacts();
}


