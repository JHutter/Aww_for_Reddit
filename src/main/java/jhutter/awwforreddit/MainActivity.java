//package jhutter.awwforreddit;
package jhutter.awwforreddit;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;



import static jhutter.awwforreddit.R.id.imageView;



// TODO figure out mp4s with glide? should make data smaller?
public class MainActivity extends AppCompatActivity {
    ImageView imgView;
    NavStacks navStacks;
    private AdView mAdView;
    PopupMenu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //FloatingActionButton settingsBtn = (FloatingActionButton) findViewById(R.id.settingsBtn);
        //setSupportActionBar((Toolbar) settingsBtn);
        AdRequest adRequest = new AdRequest.Builder().build();
        String adAppId = this.getResources().getString(R.string.adappid);
        MobileAds.initialize(this, adAppId);
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(adRequest);
        /*if (!adRequest.isTestDevice(this)){
            String adAppId = this.getResources().getString(R.string.adappid);
            //MobileAds.initialize(this, adAppId);
            mAdView = (AdView) findViewById(R.id.adView);
            mAdView.loadAd(adRequest);
        }*/


        /*if (!isTestDevice()){
            String adAppId = this.getResources().getString(R.string.adappid);
            MobileAds.initialize(this, adAppId);
            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }*/



        imgView = (ImageView) findViewById(imageView);
        imgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.placeSnackBar), "Don't like this image?", Snackbar.LENGTH_LONG)
                        .setAction("ADD TO BAN LIST", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new BannedForward().execute();
                            }
                        });
                snackbar.show();
                return true;
            }
        });

        FloatingActionButton forward = (FloatingActionButton) findViewById(R.id.forward);
        forward.setVisibility(View.INVISIBLE);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideTop();
                new LoadForward().execute();
            }
        });

        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        back.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideTop();
                new LoadBackward().execute();
            }
        });


        menu = new PopupMenu(this, findViewById(R.id.settingsBtn));
        MenuInflater inflater = menu.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu.getMenu());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.settingsBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.show();
            }
        });
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("menu", "in menu listener");
                hideTop();
                int id = item.getItemId();
                boolean checkedStatus = item.isChecked();
//
//                //ArrayList<String> newSubreddits = new ArrayList<>();
//
//
//
//
                //noinspection SimplifiableIfStatement
                /*if (id == R.id.action_settings) {
                    Log.d("menu", "you clicked actions settings");
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    return true;
                }*/
                item.setChecked(!checkedStatus);
                navStacks.resetSubreddits((String) item.getTitle());

                return true;
            }
        });

        new CreateLinks(new View(getApplicationContext())).execute();
    }



    private class CreateLinks extends AsyncTask<Void, Integer, Boolean> {
        private View rootView;  //source: https://stackoverflow.com/questions/43517060/how-do-i-use-snackbar-in-onpostexecute-method-in-asynctask-class

        CreateLinks(View view) {
            this.rootView = view;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            navStacks = new NavStacks();
            return true;
        }

        @Override
        protected void onPreExecute(){
            loadingMsg(rootView.getContext());
        }

        @Override
        protected void onPostExecute(Boolean b) {
            forwardVisible();
        }
    }

    private class LoadForward extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... params) {
            return navStacks.forward();
        }

        public void onPostExecute(String url) {
            updateImgView(url);
            backVisibility();
        }
    }

    private class BannedForward extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... params) {
            return navStacks.bannedForward();
        }

        public void onPostExecute(String url) {
            updateImgView(url);
            backVisibility();
        }
    }

    private class LoadBackward extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... params) {
            return navStacks.back();
        }

        public void onPostExecute(String url) {
            updateImgView(url);
            backVisibility();
        }
    }

    private void updateImgView(String curLink){
        Log.d("imgView", curLink);
        imgView = (ImageView) findViewById(imageView);


        Glide.with(getApplicationContext())
                .load(curLink)
//                .apply(new RequestOptions().centerInside().transform(new RoundedCorners(80)))
//                .apply(new RequestOptions().centerCrop().transform(new RoundedCorners(80))) better...
//                .apply(new RequestOptions().fitCenter().transform(new RoundedCorners(80)))
                .apply(new RequestOptions().circleCrop())


                        //transform(new RoundedCorners(80)))
                //.circleCrop()
//                .override(400,400)
//                .fitCenter()
                .into(imgView);





    }

    public void forwardVisible(){
        FloatingActionButton forward = (FloatingActionButton) findViewById(R.id.forward);
        forward.setVisibility(View.VISIBLE);
    }

    public void loadingMsg(Context context){
        Toast.makeText(context, "Animal pictures coming up!", Toast.LENGTH_LONG).show();
    }

    public void backVisibility(){
        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        if (navStacks.canBack()){
            back.setVisibility(View.VISIBLE);
        }
        else {
            back.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        hideTop();
    }

    public void hideTop(){
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        /*ActionBar actionBar = getActionBar();
        actionBar.hide();*/
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.hide();
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();

        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }*/
    }

    private boolean isTestDevice() {
        String testLabSetting =
                Settings.System.getString(getContentResolver(), "firebase.test.lab");
        return "true".equals(testLabSetting);
    }

}
