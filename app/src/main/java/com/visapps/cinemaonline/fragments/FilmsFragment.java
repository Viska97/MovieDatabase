package com.visapps.cinemaonline.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.visapps.cinemaonline.R;
import com.visapps.cinemaonline.activities.FilmActivity;
import com.visapps.cinemaonline.activities.ProfileActivity;
import com.visapps.cinemaonline.adapters.FilmsAdapter;
import com.visapps.cinemaonline.api.ApiService;
import com.visapps.cinemaonline.dialogs.FilterDialog;
import com.visapps.cinemaonline.enums.RequestError;
import com.visapps.cinemaonline.interfaces.FilmsPresenterCallback;
import com.visapps.cinemaonline.interfaces.MainActivityCallback;
import com.visapps.cinemaonline.models.Film;
import com.visapps.cinemaonline.models.Filter;
import com.visapps.cinemaonline.presenters.FilmsPresenter;
import com.visapps.cinemaonline.presenters.MainPresenter;
import com.visapps.cinemaonline.utils.Utils;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FilmsFragment extends Fragment implements FilmsPresenterCallback{

    private MainActivityCallback callback;
    private FilmsPresenter presenter;
    private FilmsAdapter adapter;

    private SwipeRefreshLayout refresher;
    private TextView emptystate;
    private ProgressDialog progress;
    private FloatingActionButton fab;

    private FilterDialog dialog = new FilterDialog();

    public FilmsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_films, container, false);
        progress = new ProgressDialog(getActivity());
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(getString(R.string.loading));
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.RequestFilter();
            }
        });
        refresher = view.findViewById(R.id.refresher);
        emptystate = view.findViewById(R.id.emptystate);
        RecyclerView filmslist = view.findViewById(R.id.filmslist);
        adapter = new FilmsAdapter(getActivity());
        adapter.setCallback(new FilmsAdapter.FilmsAdapterCallback() {
            @Override
            public void onClick(int id) {
                Intent intent = new Intent(getActivity(), FilmActivity.class);
                intent.putExtra("FilmID", id);
                startActivity(intent);
            }

            @Override
            public void onRemove(int id) {

            }
        });
        filmslist.setLayoutManager(new GridLayoutManager(getActivity(), Utils.calculateNoOfColumns(getActivity())));
        filmslist.setAdapter(adapter);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.RequestFilms(true);
            }
        });
        if(presenter == null){
            presenter = new FilmsPresenter();
            SharedPreferences account = getActivity().getSharedPreferences("Account", MODE_PRIVATE);
            String login = account.getString("login",null);
            String password = account.getString("password",null);
            presenter.setLogin(login);
            presenter.setPassword(password);
        }
        presenter.onAttach(this);
        presenter.RequestFilms(false);
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (MainActivityCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onLoadFilms(List<Film> films) {
        emptystate.setVisibility(View.GONE);
        adapter.setItems(films);
    }

    @Override
    public void onLoadFilter(Filter filter) {
        dialog.setFilter(filter, getActivity());
        dialog.setCallback(new FilterDialog.FilterDialogCallback() {
            @Override
            public void onFilterSelected(String name,String year, String genre, String country, String orderby) {
                presenter.onFilterSelected(name,year,genre,country,orderby);
            }
        });
        dialog.show(getChildFragmentManager(),"FILTER");
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
        fab.setVisibility(View.GONE);
    }

    @Override
    public void RemoveLoading() {
        refresher.setRefreshing(false);
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void ShowProgress() {
        progress.show();
        fab.setVisibility(View.GONE);
    }

    @Override
    public void RemoveProgress() {
        progress.dismiss();
        fab.setVisibility(View.VISIBLE);
    }

    private void showErrorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.error))
                .setMessage(message)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.reload), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.RequestFilms(true);
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
