/*
 * Parts of this class have been inspired by Google's Android Fragment Transitions: RecyclerView to ViewPager
 * available at https://github.com/google/android-transition-examples/tree/master/GridToPager
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ouftech.popularmovies;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import net.ouftech.popularmovies.Model.Movie;
import net.ouftech.popularmovies.Model.Result;
import net.ouftech.popularmovies.commons.BaseFragment;
import net.ouftech.popularmovies.commons.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A fragment for displaying an image.
 */
@FragmentWithArgs
public class DetailsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Movie> {

    private static final int MOVIE_LOADER = 9108;

    @BindView(R.id.ic_star_1_iv)
    AppCompatImageView icStar1Iv;
    @BindView(R.id.ic_star_2_iv)
    AppCompatImageView icStar2Iv;
    @BindView(R.id.ic_star_3_iv)
    AppCompatImageView icStar3Iv;
    @BindView(R.id.ic_star_4_iv)
    AppCompatImageView icStar4Iv;
    @BindView(R.id.ic_star_5_iv)
    AppCompatImageView icStar5Iv;
    @BindView(R.id.rating_tv)
    TextView ratingTv;
    @BindView(R.id.date_tv)
    TextView dateTv;
    @BindView(R.id.overview_tv)
    TextView overviewTv;
    @BindView(R.id.detail_toolbar)
    Toolbar detailToolbar;
    @BindView(R.id.backdrop_iv)
    ImageView backdropIv;

    Unbinder unbinder;

    @NonNull
    @Override
    protected String getLotTag() {
        return "DetailsFragment";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_details;
    }

    @Arg
    int position;
    private Movie movie;
    @BindView(R.id.image)
    protected ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view == null)
            return null;

        if (getActivity() instanceof MainActivity && ((MainActivity) getActivity()).getMovies() != null)
            movie = ((MainActivity) getActivity()).getMovies().get(position);

        if (movie != null)
            imageView.setTransitionName(movie.id);

        loadMovie();

        // Load the image with Glide to prevent OOM error when the image drawables are very large.
        Glide.with(this)
                .load(NetworkUtils.getW185ImageURL(movie.posterPath))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable>
                            target, boolean isFirstResource) {
                        // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going in case of a failure.
                        if (getParentFragment() != null)
                            getParentFragment().startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable>
                            target, DataSource dataSource, boolean isFirstResource) {
                        // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going when the image is ready.
                        if (getParentFragment() != null)
                            getParentFragment().startPostponedEnterTransition();
                        return false;
                    }
                })
                .into(imageView);

        Glide.with(this)
                .load(NetworkUtils.getW342ImageURL(movie.backdropPath))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable>
                            target, boolean isFirstResource) {
                        // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going in case of a failure.
                        if (getParentFragment() != null)
                            getParentFragment().startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable>
                            target, DataSource dataSource, boolean isFirstResource) {
                        // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going when the image is ready.
                        if (getParentFragment() != null)
                            getParentFragment().startPostponedEnterTransition();
                        return false;
                    }
                })
                .into(backdropIv);
        unbinder = ButterKnife.bind(this, view);

        detailToolbar.setTitle(movie.title);
        ratingTv.setText(String.valueOf(movie.voteAverage));
        dateTv.setText(getLocaleDate(movie.releaseDate));
        overviewTv.setText(movie.overview);
        displayRatingStars(movie.voteAverage);

        return view;
    }

    private void loadMovie() {
        if (movie.isFullyLoaded) {
            displayAdditionalData();
        } else {
            LoaderManager loaderManager = getActivity().getSupportLoaderManager();
            Loader<String[]> moviesLoader = loaderManager.getLoader(MOVIE_LOADER);
            if (moviesLoader == null) {
                loaderManager.initLoader(MOVIE_LOADER, null, this);
            } else {
                loaderManager.restartLoader(MOVIE_LOADER, null, this);
            }
        }
    }

    private void displayAdditionalData() {

    }

    private String getLocaleDate(@Nullable String dateString) {
        if (TextUtils.isEmpty(dateString))
            return "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");

        try {
            Date date = sdf.parse(dateString);
            java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
            return dateFormat.format(date);
        } catch (ParseException e) {
            loge(String.format("Error while parsing date %s", dateString), e);
            return "";
        }
    }

    private void displayRatingStars(float ratingFloat) {
        int rating = Math.round(ratingFloat);

        initStar(icStar1Iv, rating, 1, 2);
        initStar(icStar2Iv, rating, 3, 4);
        initStar(icStar3Iv, rating, 5, 6);
        initStar(icStar4Iv, rating, 7, 8);
        initStar(icStar5Iv, rating, 9, 10);
    }

    private void initStar(@NonNull AppCompatImageView star, int value, int min, int max) {
        if (value < min)
            star.setImageResource(R.drawable.ic_star_border_24dp);
        else if (value >= max)
            star.setImageResource(R.drawable.ic_star_24dp);
        else
            star.setImageResource(R.drawable.ic_star_half_24dp);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Movie>(getActivity().getApplicationContext()) {

            Movie result;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (result != null)
                    deliverResult(result);
                else
                    forceLoad();
            }

            @Override
            public Movie loadInBackground() {

                Movie tempMovie = null;
                try {
                    URL popularMoviesURL = NetworkUtils.getMovieURL(getActivity(), movie.id);
                    String movieResponse = NetworkUtils.getResponseFromHttpUrl(popularMoviesURL);
                    tempMovie = new Gson().fromJson(movieResponse, Movie.class);
                } catch (IOException e) {
                    loge("Error while requesting movie", e);
                }

                logd("finished");
                return tempMovie;
            }

            @Override
            public void deliverResult(Movie data) {
                result = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        if (data != null) {
            movie.countries = data.countries;
            movie.genres = data.genres;
            movie.runtime = data.runtime;
            movie.countries = data.countries;
            movie.tagline = data.tagline;
            movie.isFullyLoaded = true;
            displayAdditionalData();
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }
}