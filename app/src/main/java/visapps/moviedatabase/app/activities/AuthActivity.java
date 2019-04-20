package visapps.moviedatabase.app.activities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.moviedatabase.app.R;
import visapps.moviedatabase.app.api.ApiService;
import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.interfaces.ApiCallback;

import io.reactivex.disposables.CompositeDisposable;


public class AuthActivity extends AppCompatActivity {

    public static final String LOGIN = "LOGIN";
    public static final String PASSWORD = "PASSWORD";
    public static final int REQUEST_REGISTER = 3001;

    private TextInputEditText login, password;
    private ProgressDialog progress;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(getString(R.string.loading));
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        Button loginbutton = findViewById(R.id.loginbutton);
        Button registerbutton = findViewById(R.id.registerbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
        compositeDisposable = new CompositeDisposable();
    }

    private void Login(){
        if(login.getText().toString().equals("") || password.getText().toString().equals("")){
            return;
        }
        progress.show();
        compositeDisposable.add(ApiService.getInstance().auth(login.getText().toString(),password.getText().toString(), new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                progress.dismiss();
                Intent data = new Intent();
                data.putExtra(LOGIN, login.getText().toString());
                data.putExtra(PASSWORD, password.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onError(RequestError error) {
                progress.dismiss();
                showErrorDialog(ApiService.getInstance().getErrorMessage(AuthActivity.this,error));
            }

            @Override
            public void onNotAuthorized(RequestError error) {
                progress.dismiss();
                showErrorDialog(ApiService.getInstance().getErrorMessage(AuthActivity.this,error));
            }
        }));
    }

    private void Register(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivityForResult(intent,REQUEST_REGISTER);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==REQUEST_REGISTER){
            if(resultCode==RESULT_OK){
                String log = data.getStringExtra(AuthActivity.LOGIN);
                String pass = data.getStringExtra(AuthActivity.PASSWORD);
                login.setText(log);
                password.setText(pass);
                Login();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
