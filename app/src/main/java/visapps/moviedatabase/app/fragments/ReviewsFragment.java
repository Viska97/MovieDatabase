package visapps.moviedatabase.app.fragments;


import android.content.DialogInterface;
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
import visapps.moviedatabase.app.adapters.MarkAdapter;
import visapps.moviedatabase.app.api.ApiService;
import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.interfaces.InfoPresenterCallback;
import visapps.moviedatabase.app.models.Mark;
import visapps.moviedatabase.app.presenters.ReviewsPresenter;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment implements InfoPresenterCallback {

    private int FilmID=1;

    private ReviewsPresenter presenter;
    private MarkAdapter adapter;

    private SwipeRefreshLayout refresher;
    private TextView emptystate;

    public void setFilmID(int filmID) {
        FilmID = filmID;
    }

    public ReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        refresher = view.findViewById(R.id.refresher);
        emptystate = view.findViewById(R.id.emptystate);
        RecyclerView filmslist = view.findViewById(R.id.reviews);
        adapter = new MarkAdapter(getActivity());
        filmslist.setLayoutManager(new LinearLayoutManager(getContext()));
        filmslist.setAdapter(adapter);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.GetReviews();
            }
        });
        presenter = new ReviewsPresenter();
        SharedPreferences account = getActivity().getSharedPreferences("Account", MODE_PRIVATE);
        String login = account.getString("login",null);
        String password = account.getString("password",null);
        presenter.Init(login,password, FilmID);
        presenter.onAttach(this);
        presenter.GetReviews();
        return view;
    }

    @Override
    public void onLoad(Object object) {
        emptystate.setVisibility(View.GONE);
        adapter.setItems((List<Mark>) object);
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
                        presenter.GetReviews();
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
