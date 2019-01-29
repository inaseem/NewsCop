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

    @Query("SELECT * FROM sources WHERE id IN (:newsIds)")
    List<Source> loadAllByIds(int[] newsIds);

    @Insert
    void insertAll(Source... sources);

    @Delete
    void delete(Source source);

}
