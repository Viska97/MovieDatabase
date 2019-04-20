package visapps.moviedatabase.app.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.moviedatabase.app.R;
import visapps.moviedatabase.app.adapters.FilmTabsAdapter;

public class FilmActivity extends AppCompatActivity {

    private FilmTabsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        int FilmID = 1;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FilmID = extras.getInt("FilmID");
        }
        else{
            finish();
        }
        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager = findViewById(R.id.viewpager);
        Bundle tabnames = new Bundle();
        tabnames.putString("FILM", getString(R.string.film));
        tabnames.putString("ACTORS", getString(R.string.actors));
        tabnames.putString("REVIEWS", getString(R.string.reviews));
        adapter = new FilmTabsAdapter(getSupportFragmentManager(),tabnames, FilmID);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(viewPager);
    }



}
