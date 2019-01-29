
package ali.naseem.newscop.models.sources;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(tableName = "sources")
public class Source {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private String id;
    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;
    @ColumnInfo(name = "description")
    @SerializedName("description")
    @Expose
    private String description;
    @ColumnInfo(name = "url")
    @SerializedName("url")
    @Expose
    private String url;
    @ColumnInfo(name = "category")
    @SerializedName("category")
    @Expose
    private String category;
    @ColumnInfo(name = "language")
    @SerializedName("language")
    @Expose
    private String language;
    @ColumnInfo(name = "country")
    @SerializedName("country")
    @Expose
    private String country;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
