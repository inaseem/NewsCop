package ali.naseem.newscop.utils.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ali.naseem.newscop.models.Topics;

@Dao
public interface TopicsDao {
    @Query("SELECT * FROM topics")
    List<Topics> getAll();

    @Query("SELECT * FROM topics where id = :id")
    Topics getTopic(int id);

    @Insert
    void insertAll(Topics... topics);

    @Insert
    void insertAll(List<Topics> topics);

    @Delete
    void delete(Topics topics);

    @Query("DELETE from topics")
    void deleteAll();
}
