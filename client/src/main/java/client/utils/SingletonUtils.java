package client.utils;

import java.util.Hashtable;

public class SingletonUtils {
    private static Hashtable<String, String> parametersScene;

    public static Hashtable<String, String> getParametersScene(){
        if (parametersScene == null){
            parametersScene = new Hashtable<String, String>();
        }

        return parametersScene;
    }
}
