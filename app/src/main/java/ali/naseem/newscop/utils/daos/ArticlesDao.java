package ali.naseem.newscop.utils.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ali.naseem.newscop.models.everything.Article;

@Dao
public interface ArticlesDao {
    @Query("SELECT * FROM articles")
    List<Article> getAll();

    @Insert
    void insertAll(Article... articles);

    @Insert
    void insertAll(List<Article> articles);

    @Delete
    void delete(Article article);

    @Query("DELETE FROM articles")
    void deleteAll();

}
