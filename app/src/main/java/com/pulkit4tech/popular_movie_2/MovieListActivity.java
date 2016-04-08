package com.pulkit4tech.popular_movie_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.pulkit4tech.popular_movie_2.data_object.MovieDetailDO;
import com.pulkit4tech.popular_movie_2.MovieDetailFragment.DetailFragmentCallbackInterface;
import com.pulkit4tech.popular_movie_2.MovieListFragment.ListFragmentCallbackInterface;


public class MovieListActivity extends AppCompatActivity implements ListFragmentCallbackInterface, DetailFragmentCallbackInterface{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    MovieListFragment listFragment;
    private MovieDetailDO movieSelected;
    MovieDetailFragment detailFragment;
    String requestURL;
    boolean isFavoritesView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);



        if (findViewById(R.id.movie_detail_container) != null) {

            mTwoPane = true;
        }

        if (savedInstanceState != null)  {
            listFragment = (MovieListFragment) getSupportFragmentManager().getFragment(savedInstanceState, "listFragment");
        }

        if (listFragment == null) {
            listFragment = MovieListFragment.newInstance(mTwoPane);
        }

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.movie_list_container, listFragment);
            fragmentTransaction.commit();


        if (mTwoPane) {
            //show the detail view in this activity by adding or replacing the detail fragment
            detailFragment = new MovieDetailFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.movie_detail_container, detailFragment);
            ft.commit();

        }
    }

    //Update the movie selected and clear the detail fragment to show new selection
    public void updateMovie(MovieDetailDO movieSelected, boolean favView) {
        this.movieSelected = movieSelected;
        detailFragment.clearView();
        detailFragment.updateMovie(movieSelected, favView);
    }

    public void startDetailActivity(MovieDetailDO movieSelected) {
        Intent intent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
        intent.putExtra("movieSelected", movieSelected);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_sortpopular:
                requestURL = getString(R.string.popular_url) + "&" + getString(R.string.api_key);
                isFavoritesView = false;
                listFragment.updateList(requestURL, isFavoritesView);
                if (detailFragment != null){
                    detailFragment.clearView();
                }
                return true;
            case R.id.action_sortrated:
                requestURL = getString(R.string.top_rate_url) + getString(R.string.api_key);
                isFavoritesView = false;
                listFragment.updateList(requestURL, isFavoritesView);
                if (detailFragment != null){
                    detailFragment.clearView();
                }
                return true;
            case R.id.action_favorites:
                isFavoritesView = true;
                if (detailFragment != null){
                    detailFragment.clearView();
                }
                listFragment.getFavorites(isFavoritesView);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Called to update the view if add/removing in favorites view
    public void updateFavoritesView() {
        if (detailFragment != null){
            detailFragment.clearView();
        }
        listFragment.getFavorites(isFavoritesView);
    }

        @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "listFragment", listFragment);
    }
}
