package ali.naseem.newscop.utils;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ali.naseem.newscop.models.headlines.Source;

public class Converter2 {
    @TypeConverter
    public static Source fromString(String value) {
        Type type = new TypeToken<Source>() {
        }.getType();
        return new Gson().fromJson(value, type);

    }

    @TypeConverter

    public static String fromSource(Source source) {

        Gson gson = new Gson();

        String json = gson.toJson(source);

        return json;

    }
}
