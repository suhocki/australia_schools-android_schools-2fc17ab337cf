package au.com.websitemasters.schools.thornlie.connection_retrofit;

import au.com.websitemasters.schools.thornlie.objects_for_retrofit.ContactsObject;
import retrofit2.Call;
import retrofit2.http.GET;
import au.com.websitemasters.schools.thornlie.constants.Constants;

public interface ServiceGetContacts {

    @GET(Constants.CONTACT)

    Call<ContactsObject> getContacts();
}


