package visapps.moviedatabase.app.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moviedatabase.app.R;
import visapps.moviedatabase.app.activities.FilmActivity;
import visapps.moviedatabase.app.adapters.FavoritesAdapter;
import visapps.moviedatabase.app.adapters.FilmsAdapter;
import visapps.moviedatabase.app.api.ApiService;
import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.interfaces.FavoritesPresenterCallback;
import visapps.moviedatabase.app.interfaces.MainActivityCallback;
import visapps.moviedatabase.app.models.Film;
import visapps.moviedatabase.app.presenters.FavoritesPresenter;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FavoritesFragment extends Fragment implements FavoritesPresenterCallback{


    private MainActivityCallback callback;
    private FavoritesPresenter presenter;
    private FavoritesAdapter adapter;

    private SwipeRefreshLayout refresher;
    private TextView emptystate;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        refresher = view.findViewById(R.id.refresher);
        emptystate = view.findViewById(R.id.emptystate);
        RecyclerView filmslist = view.findViewById(R.id.favorites);
        adapter = new FavoritesAdapter(getActivity());
        adapter.setCallback(new FilmsAdapter.FilmsAdapterCallback() {
            @Override
            public void onClick(int id) {
                Intent intent = new Intent(getActivity(), FilmActivity.class);
                intent.putExtra("FilmID", id);
                startActivity(intent);
            }

            @Override
            public void onRemove(int id) {
                presenter.RemoveFavorite(id);
            }
        });
        filmslist.setLayoutManager(new LinearLayoutManager(getContext()));
        filmslist.setAdapter(adapter);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.GetFavorites();
            }
        });
        if(presenter == null){
            presenter = new FavoritesPresenter();
            SharedPreferences account = getActivity().getSharedPreferences("Account", MODE_PRIVATE);
            String login = account.getString("login",null);
            String password = account.getString("password",null);
            presenter.Init(login,password);
        }
        presenter.onAttach(this);
        presenter.GetFavorites();
        return view;
    }

    @Override
    public void onLoadFavorites(List<Film> films) {
        emptystate.setVisibility(View.GONE);
        adapter.setItems(films);
    }


    @Override
    public void onEmpty() {
        emptystate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLogOut() {
        ApiService.getInstance().LogOut(getActivity());
    }

    @Override
    public void onError(RequestError error) {
        showErrorDialog(ApiService.getInstance().getErrorMessage(getActivity(),error));
    }

    @Override
    public void ShowLoading() {
        adapter.clear();
        refresher.setRefreshing(true);
    }

    @Override
    public void RemoveLoading() {
        refresher.setRefreshing(false);
    }

    private void showErrorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.error))
                .setMessage(message)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        emptystate.setVisibility(View.VISIBLE);
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.reload), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.GetFavorites();
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
