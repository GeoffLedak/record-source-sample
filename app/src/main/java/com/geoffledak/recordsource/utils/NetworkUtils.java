package com.geoffledak.recordsource.utils;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by geoff on 4/17/16.
 */
public class NetworkUtils {


    public static List<?> loadJson(String jsonString, Type collectionType) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, collectionType);
    }


    public static String loadJSONFromResource(Context context, int resource ) {
        if( resource <= 0 )
            return null;

        String json = null;
        InputStream is = context.getResources().openRawResource( resource );
        try {
            if( is != null ) {
                int size = is.available();
                byte[] buffer = new byte[size];
                if( buffer != null ) {
                    is.read(buffer);
                    json = new String(buffer, "UTF-8");
                }
            }
        } catch( IOException e ) {

        } finally {
            try {
                is.close();
            } catch( IOException e ) {}
        }

        return json;
    }

}
