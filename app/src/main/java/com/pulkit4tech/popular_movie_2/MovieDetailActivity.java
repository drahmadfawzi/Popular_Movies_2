package com.pulkit4tech.popular_movie_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.pulkit4tech.popular_movie_2.data_object.MovieDetailDO;
import com.pulkit4tech.popular_movie_2.MovieDetailFragment.DetailFragmentCallbackInterface;


public class MovieDetailActivity extends AppCompatActivity implements DetailFragmentCallbackInterface {

    private MovieDetailFragment detailFragment;
    MovieDetailDO movieSelected = new MovieDetailDO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        movieSelected = intent.getParcelableExtra("movieSelected");

        setContentView(R.layout.activity_movie_detail);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (detailFragment == null) {
            detailFragment = MovieDetailFragment.newInstance(movieSelected);
        } else {
            detailFragment.updateMovie(movieSelected, false);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.movie_detail_container, detailFragment);
        fragmentTransaction.commit();
    }

    public void updateFavoritesView(){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            NavUtils.navigateUpTo(this, new Intent(this, MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
