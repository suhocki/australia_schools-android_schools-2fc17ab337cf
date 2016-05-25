package au.com.websitemasters.schools.thornlie.utils;

public interface ServerObserverRetrofit<Object, String> {

    void successExecuteObject(Object obj);

    void failedExecute(String err);
}