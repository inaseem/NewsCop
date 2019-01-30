package ali.naseem.newscop.utils.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ali.naseem.newscop.models.sources.Source;

@Dao
public interface SourcesDao {
    @Query("SELECT * FROM sources")
    List<Source> getAll();

    @Query("SELECT * FROM sources where id = :id")
    Source getSource(String id);

    @Insert
    void insertAll(Source... sources);

    @Insert
    void insertAll(List<Source> sources);

    @Delete
    void delete(Source source);

    @Query("DELETE from sources")
    void deleteAll();

}
