package visapps.moviedatabase.app.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.moviedatabase.app.R;
import visapps.moviedatabase.app.api.ApiService;
import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.interfaces.ApiCallback;
import visapps.moviedatabase.app.models.Response;
import visapps.moviedatabase.app.models.User;

import io.reactivex.disposables.CompositeDisposable;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText login, password, email;
    private TextInputLayout login_layout, email_layout, password_layout;
    private ProgressDialog progress;
    private CompositeDisposable compositeDisposable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password_layout.setError(null);
            }
        });
        login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                login_layout.setError(null);
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                email_layout.setError(null);
            }
        });
        login_layout = findViewById(R.id.login_layout);
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);
        Button registerbutton = findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate();
            }
        });
        compositeDisposable = new CompositeDisposable();
    }

    private void Register(){
        User user = new User();
        user.setNickName(login.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPasswd(password.getText().toString());
        progress.show();
        compositeDisposable.add(ApiService.getInstance().register(user, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                progress.dismiss();
                Response response = (Response) object;
                int code = response.getCode();
                if(code == 0){
                    Intent data = new Intent();
                    data.putExtra(AuthActivity.LOGIN, login.getText().toString());
                    data.putExtra(AuthActivity.PASSWORD, password.getText().toString());
                    setResult(RESULT_OK, data);
                    finish();
                }
                else{
                    showResutDialog(code);
                }
            }

            @Override
            public void onError(RequestError error) {
                progress.dismiss();
                showErrorDialog(ApiService.getInstance().getErrorMessage(RegisterActivity.this,error));
            }

            @Override
            public void onNotAuthorized(RequestError error) {
                progress.dismiss();
                showErrorDialog(ApiService.getInstance().getErrorMessage(RegisterActivity.this,error));
            }
        }));
    }

    private void Validate(){
        boolean passgood=false;
        boolean logingood = false;
        boolean emailgood = false;
        if(CheckPassword(password.getText().toString())){
            passgood = true;
        }
        else{
            password_layout.setError(getString(R.string.passerror));
        }
        if(CheckLogin(login.getText().toString())){
            logingood = true;
        }
        else{
            login_layout.setError(getString(R.string.loginerror));
        }
        if(CheckEmail(email.getText())){
            emailgood = true;
        }
        else{
            email_layout.setError(getString(R.string.emailerror));
        }
        if(passgood && logingood && emailgood){
            Register();
        }
    }

    private void showResutDialog(int code){
        String message="";
        if(code == 1){
            message = getString(R.string.emailexistst);
        }
        else{
            message = getString(R.string.loginexistst);
        }
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

    private boolean CheckPassword(String password){
        return password.length() >= 8 && password.length() <= 50 && password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }

    private boolean CheckLogin(String login){
        return login.length()>=6 && login.length()<=25 && login.matches("[a-zA-Z0-9]*");
    }

    private boolean CheckEmail(CharSequence email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}
