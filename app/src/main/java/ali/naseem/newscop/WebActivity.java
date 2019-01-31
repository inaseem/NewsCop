package ali.naseem.newscop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import ali.naseem.newscop.utils.Constants;

public class WebActivity extends AppCompatActivity {

    static WebView webView;
    private ProgressBar progressBar;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = findViewById(R.id.webView);
        Intent intent = getIntent();
        url = intent.getStringExtra(Constants.URL);
        progressBar = findViewById(R.id.progressBar);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(true);
        String newUA = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        webView.getSettings().setUserAgentString(newUA);
        progressBar.setProgress(0);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                progressBar.setProgress(progress);
//                setTitle("Loading...");
//                setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if (progress == 100)
                    progressBar.setVisibility(View.GONE);
            }
        });
        if (savedInstanceState != null) {
            // Restore the previous URL and history stack
            webView.restoreState(savedInstanceState);
        }
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
        webView.loadUrl(url);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (webView != null)
            webView.destroy();
    }
}
