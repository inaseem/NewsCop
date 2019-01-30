package ali.naseem.newscop.utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import ali.naseem.newscop.models.everything.Article;
import ali.naseem.newscop.models.sources.Source;
import ali.naseem.newscop.utils.daos.ArticlesDao;
import ali.naseem.newscop.utils.daos.HeadlinesDao;
import ali.naseem.newscop.utils.daos.SourcesDao;

@Database(entities = {Source.class, Article.class, ali.naseem.newscop.models.headlines.Article.class}, version = 2, exportSchema = true)
@TypeConverters({Converters.class, Converter2.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract SourcesDao sourcesDao();

    public abstract ArticlesDao articlesDao();

    public abstract HeadlinesDao headlinesDao();
}
