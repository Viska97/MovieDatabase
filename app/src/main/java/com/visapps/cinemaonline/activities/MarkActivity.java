package com.visapps.cinemaonline.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.visapps.cinemaonline.R;
import com.visapps.cinemaonline.api.ApiService;
import com.visapps.cinemaonline.enums.RequestError;
import com.visapps.cinemaonline.interfaces.ApiCallback;
import com.visapps.cinemaonline.models.Mark;
import com.visapps.cinemaonline.models.Response;

import io.reactivex.disposables.CompositeDisposable;

import static com.visapps.cinemaonline.activities.AuthActivity.LOGIN;
import static com.visapps.cinemaonline.activities.AuthActivity.PASSWORD;

public class MarkActivity extends AppCompatActivity {

    private ProgressDialog progress;
    private CompositeDisposable compositeDisposable;
    private NumberPicker Rating;
    private TextInputLayout Comment_layout;
    private TextInputEditText Comment;
    private int FilmID;
    private String login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(getString(R.string.loading));
        TextView FilmName = findViewById(R.id.FilmName);
        Comment_layout = findViewById(R.id.Comment_layout);
        Comment = findViewById(R.id.Comment);
        Comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Comment_layout.setError(null);
            }
        });
        Rating = findViewById(R.id.Rating);
        Rating.setMinValue(1);
        Rating.setMaxValue(10);
        Rating.setValue(10);
        Bundle extras = getIntent().getExtras();
        FilmID = extras.getInt("FilmID");
        FilmName.setText(extras.getString("FilmName"));
        SharedPreferences account = getSharedPreferences("Account", MODE_PRIVATE);
        login = account.getString("login",null);
        password = account.getString("password",null);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_submit) {
            Validate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void Validate(){
        if(!Comment.getText().toString().equals("") && Comment.getText().toString().length()<20){
            Comment_layout.setError(getString(R.string.commentrestriction));
        }
        else{
            SendMark();
        }
    }

    private void SendMark(){
        Mark mark = new Mark();
        mark.setRating(Rating.getValue());
        if(!Comment.getText().toString().equals("")){
            mark.setComment(Comment.getText().toString());
        }
        progress.show();
        compositeDisposable.add(ApiService.getInstance().updatemark(login, password, FilmID, mark, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                progress.dismiss();
                showResutDialog();
            }

            @Override
            public void onError(RequestError error) {
                progress.dismiss();
                showErrorDialog(ApiService.getInstance().getErrorMessage(MarkActivity.this,error));
            }

            @Override
            public void onNotAuthorized(RequestError error) {
                ApiService.getInstance().LogOut(MarkActivity.this);
            }
        }));
    }

    private void showResutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.markadded))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showErrorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.error))
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
