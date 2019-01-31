package ali.naseem.newscop.utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import ali.naseem.newscop.models.Topics;
import ali.naseem.newscop.models.everything.Article;
import ali.naseem.newscop.models.sources.Source;
import ali.naseem.newscop.utils.daos.ArticlesDao;
import ali.naseem.newscop.utils.daos.HeadlinesDao;
import ali.naseem.newscop.utils.daos.SourcesDao;
import ali.naseem.newscop.utils.daos.TopicsDao;

@Database(entities = {Source.class, Article.class, ali.naseem.newscop.models.headlines.Article.class, Topics.class}, version = 3)
@TypeConverters({Converters.class, Converter2.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract SourcesDao sourcesDao();

    public abstract ArticlesDao articlesDao();

    public abstract HeadlinesDao headlinesDao();

    public abstract TopicsDao topicsDao();
}
