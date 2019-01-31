package ali.naseem.newscop.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ali.naseem.newscop.R;
import ali.naseem.newscop.models.everything.Article;
import ali.naseem.newscop.utils.Utils;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private List<Article> items;
    private Context context;

    public ArticlesAdapter(List<Article> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        switch (type) {
            case 0:
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.headline_item, viewGroup, false);
                return new ViewHolder(view);
            default:
                View view2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.headline_item2, viewGroup, false);
                return new ViewHolder(view2);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position%4;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Article item = items.get(position);
        Glide.with(context)
                .load(item.getUrlToImage())
                .apply(bitmapTransform(new RoundedCornersTransformation(20, 0)))
                .into(holder.imageView);
        holder.source.setText(item.getSource().getName());
        holder.date.setText(Utils.getFormatted(item.getPublishedAt()));
        holder.headline.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView source, date, headline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            source = itemView.findViewById(R.id.source);
            date = itemView.findViewById(R.id.date);
            headline = itemView.findViewById(R.id.headline);
        }
    }
}
