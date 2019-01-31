package ali.naseem.newscop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import ali.naseem.newscop.fragments.Everything;
import ali.naseem.newscop.fragments.TopFive;
import ali.naseem.newscop.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private View favourites;
    private NestedScrollView scrollView;
    private Everything everything;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = findViewById(R.id.scrollView);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.headlinesFrame, TopFive.newInstance())
                .replace(R.id.articlesFrame, everything = Everything.newInstance())
                .commit();
        favourites = findViewById(R.id.favourites);
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SourcesActivity.class);
                intent.putExtra(Constants.START, "no");
                startActivity(intent);
                finish();
            }
        });
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                        .getScrollY()));

                if (diff == 0) {
                    // your pagination code
                    everything.loadMore();
                }
            }
        });
    }
}
