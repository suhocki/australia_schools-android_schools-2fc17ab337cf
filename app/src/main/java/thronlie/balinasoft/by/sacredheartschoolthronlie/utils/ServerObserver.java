package thronlie.balinasoft.by.sacredheartschoolthronlie.utils;

public interface ServerObserver<Object, String> {

    void successExecuteObject(Object obj);

    void failedExecute(String err);
}