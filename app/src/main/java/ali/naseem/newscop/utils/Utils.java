package ali.naseem.newscop.utils;

import android.arch.persistence.room.Room;
import android.content.Context;

public class Utils {
    private static final Utils ourInstance = new Utils();

    public static void initialize(Context context) {
        database = Room.databaseBuilder(context,
                AppDatabase.class, Constants.DB_NAME).build();
    }

    public static Utils getInstance() {
        return ourInstance;
    }

    private static AppDatabase database;

    private Utils() {
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
