package visapps.moviedatabase.app.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.moviedatabase.app.R;
import visapps.moviedatabase.app.activities.MarkActivity;
import visapps.moviedatabase.app.api.ApiService;
import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.interfaces.FilmPresenterCallback;
import visapps.moviedatabase.app.models.Film;
import visapps.moviedatabase.app.models.Mark;
import visapps.moviedatabase.app.presenters.FilmPresenter;

import static android.content.Context.MODE_PRIVATE;

public class FilmFragment extends Fragment implements FilmPresenterCallback{


    private FilmPresenter presenter;
    private ImageView Poster;
    private FloatingActionButton fab;
    private SwipeRefreshLayout refresher;
    private ScrollView FilmContainer;
    private TextView FilmName, Genre, FilmCountry, FilmYear, AgeLimit, Rating, Director, FDescription;
    private Button favbutton, sharebutton, markbutton;
    private String link;
    private int FilmID;
    private boolean markbuttonenabled=false;

    public FilmFragment() {
        // Required empty public constructor
    }

    public int getFilmID() {
        return FilmID;
    }

    public void setFilmID(int filmID) {
        FilmID = filmID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_film, container, false);
        Poster = view.findViewById(R.id.Poster);
        FilmName = view.findViewById(R.id.FilmName);
        Genre = view.findViewById(R.id.Genre);
        FilmCountry = view.findViewById(R.id.FilmCountry);
        FilmYear = view.findViewById(R.id.FilmYear);
        AgeLimit = view.findViewById(R.id.AgeLimit);
        Rating = view.findViewById(R.id.Rating);
        Director = view.findViewById(R.id.Director);
        FDescription = view.findViewById(R.id.FDescription);
        markbutton = view.findViewById(R.id.markbutton);
        favbutton = view.findViewById(R.id.favbutton);
        sharebutton = view.findViewById(R.id.sharebutton);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Share();
            }
        });
        favbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.ChangeFavoriteStatus();
            }
        });
        markbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mark();
            }
        });
        FilmContainer = view.findViewById(R.id.FilmContainer);
        refresher = view.findViewById(R.id.refresher);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.LoadFilm();
            }
        });
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Play();
            }
        });
        SharedPreferences account = getActivity().getSharedPreferences("Account", MODE_PRIVATE);
        String login = account.getString("login",null);
        String password = account.getString("password",null);
        presenter = new FilmPresenter();
        presenter.onAttach(this);
        presenter.Init(login,password,FilmID);
        presenter.LoadFilm();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.LoadFilm();
    }

    @Override
    public void onLoadFilm(Film film) {
        FilmName.setText(film.getFilmName());
        Genre.setText(film.getGenre());
        FilmCountry.setText(film.getCountry());
        FilmYear.setText(String.valueOf(film.getFilmYear()));
        if(film.getRating() == null){
            Rating.setText(" -");
        }
        else{
            Rating.setText(String.valueOf(film.getRating()));
        }
        Director.setText(film.getDirector());
        FDescription.setText(film.getFdescription());
        AgeLimit.setText(String.valueOf(film.getAgeLimit()) + "+");
        link = film.getSourceLink();
        Picasso.get().load(film.getPosterLink()).fit().centerCrop().into(Poster);
    }

    @Override
    public void onLoadReview(Mark mark) {

    }

    @Override
    public void onLoadFavorite(boolean added) {
        if(added){
            favbutton.setText(getString(R.string.removefromfavorites));
        }
        else{
            favbutton.setText(getString(R.string.addtofavorites));
        }
    }

    @Override
    public void onLoadMark(boolean markbuttonenabled) {
        this.markbuttonenabled = markbuttonenabled;
        markbutton.setEnabled(markbuttonenabled);
    }

    @Override
    public void onLoadVideo() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }

    @Override
    public void onError(RequestError error) {
        showErrorDialog(ApiService.getInstance().getErrorMessage(getActivity(),error));
    }

    @Override
    public void onLogOut() {
        ApiService.getInstance().LogOut(getActivity());
    }

    @Override
    public void ShowLoading() {
        refresher.setRefreshing(true);
    }

    @Override
    public void RemoveLoading() {
        refresher.setRefreshing(false);
    }

    @Override
    public void ShowPage() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(FilmName.getText().toString());
        fab.show();
        FilmContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void HidePage() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(activity.getString(R.string.film));
        fab.hide();
        FilmContainer.setVisibility(View.GONE);
    }

    private void Share(){
        ShareCompat.IntentBuilder.from(getActivity())
                .setText(getActivity().getString(R.string.sharefirst)+ " " + FilmName.getText().toString() + " " + getActivity().getString(R.string.sharesecond))
                .setType("text/plain").setChooserTitle(getActivity().getString(R.string.sharetitle))
                .startChooser();
    }

    private void Play(){
        presenter.PlayFilm();
    }

    private void Mark(){
        Intent intent = new Intent(getActivity(), MarkActivity.class);
        intent.putExtra("FilmName", FilmName.getText().toString());
        intent.putExtra("FilmID", FilmID);
        startActivity(intent);
    }

    @Override
    public void DisableButtons() {
        favbutton.setEnabled(false);
        markbutton.setEnabled(false);
        sharebutton.setEnabled(false);
        fab.hide();
    }

    @Override
    public void EnableButtons() {
        favbutton.setEnabled(true);
        markbutton.setEnabled(markbuttonenabled);
        sharebutton.setEnabled(true);
        fab.show();
    }

    private void showErrorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.error))
                .setMessage(message)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HidePage();
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.reload), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.LoadFilm();
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDeattach();
    }

}
