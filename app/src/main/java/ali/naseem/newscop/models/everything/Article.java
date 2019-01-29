
package ali.naseem.newscop.models.everything;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "articles")
public class Article {

    @ColumnInfo(name = "source")
    @SerializedName("source")
    @Expose
    private Source source;
    @ColumnInfo(name = "author")
    @SerializedName("author")
    @Expose
    private String author;
    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;
    @ColumnInfo(name = "description")
    @SerializedName("description")
    @Expose
    private String description;
    @ColumnInfo(name = "url")
    @SerializedName("url")
    @Expose
    private String url;
    @ColumnInfo(name = "urlToImage")
    @SerializedName("urlToImage")
    @Expose
    private String urlToImage;
    @ColumnInfo(name = "publishedAt")
    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;
    @ColumnInfo(name = "content")
    @SerializedName("content")
    @Expose
    private String content;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
