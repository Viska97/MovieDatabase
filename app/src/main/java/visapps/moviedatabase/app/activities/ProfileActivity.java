package visapps.moviedatabase.app.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.moviedatabase.app.R;
import visapps.moviedatabase.app.api.ApiService;
import visapps.moviedatabase.app.enums.RequestError;
import visapps.moviedatabase.app.interfaces.ApiCallback;
import visapps.moviedatabase.app.models.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;

public class ProfileActivity extends AppCompatActivity {

    private User user;
    private ProgressDialog progress;
    private RadioButton maleButton, femaleButton, notsetButton;
    private TextView loginlabel, emaillabel, usertypelabel, year,month,day;
    private TextInputEditText fio, password;
    private TextInputLayout password_layout, fio_layout;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        loginlabel = findViewById(R.id.loginlabel);
        emaillabel = findViewById(R.id.emaillabel);
        usertypelabel = findViewById(R.id.usertypelabel);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        day = findViewById(R.id.day);
        fio = findViewById(R.id.fio);
        password = findViewById(R.id.password);
        fio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                fio_layout.setError(null);
            }
        });
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
        maleButton = findViewById(R.id.maleButton);
        femaleButton = findViewById(R.id.femaleButton);
        notsetButton = findViewById(R.id.notsetButton);
        password_layout = findViewById(R.id.password_layout);
        fio_layout = findViewById(R.id.fio_layout);
        Button datebutton = findViewById(R.id.datebutton);
        datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });
        Button savebutton = findViewById(R.id.savebutton);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate();
            }
        });
        compositeDisposable = new CompositeDisposable();
        LoadProfile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            LoadProfile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void LoadProfile(){
        password.setText("");
        password_layout.setError(null);
        fio_layout.setError(null);
        progress.show();
        SharedPreferences account = getSharedPreferences("Account", MODE_PRIVATE);
        String log = account.getString("login",null);
        String pass = account.getString("password",null);
        compositeDisposable.add(ApiService.getInstance().getprofile(log, pass, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                progress.dismiss();
                user = (User) object;
                setData();
            }

            @Override
            public void onError(RequestError error) {
                progress.dismiss();
                showErrorDialog(ApiService.getInstance().getErrorMessage(ProfileActivity.this,error));
            }

            @Override
            public void onNotAuthorized(RequestError error) {
                ApiService.getInstance().LogOut(ProfileActivity.this);
            }
        }));
    }

    private void Validate(){
        boolean passgood=false;
        boolean fiogood = false;
        if(!password.getText().toString().equals("")){
            passgood = CheckPassword(password.getText().toString());
        }
        else{
            passgood=true;
        }
        if(!fio.getText().toString().equals("")){
            fiogood = CheckFIO(fio.getText().toString());
        }
        else{
            fiogood=true;
        }
        if(!passgood){
            password_layout.setError(getString(R.string.passerror));
        }
        if(!fiogood){
            fio_layout.setError(getString(R.string.fioerror));
        }
        if(fiogood && passgood){
            UpdateProfile();
        }
    }

    private void UpdateProfile(){
        SharedPreferences account = getSharedPreferences("Account", MODE_PRIVATE);
        String log = account.getString("login",null);
        final String pass = account.getString("password",null);
        String newpass="";
        User newuser = new User();
        if(!password.getText().toString().equals("")){
            newpass = password.getText().toString();
        }
        else{
            newpass = pass;
        }
        final String newpassword = newpass;
        newuser.setPasswd(newpass);
        if(!fio.getText().toString().equals("")){
            newuser.setFio(fio.getText().toString());
        }
        if(maleButton.isChecked()){
            newuser.setGender("м");
        }
        if(femaleButton.isChecked()){
            newuser.setGender("ж");
        }
        if(!(year.getText().equals("1900") && month.getText().equals("01") && day.getText().equals("01"))){
            newuser.setBirthday(year.getText().toString() + "-" + String.format("%02d", Integer.valueOf(month.getText().toString())) + "-" + String.format("%02d", Integer.valueOf(day.getText().toString())) + "T00:00:00.000+00:00");
            //newuser.setBirthday("2012-10-01T00:00:00.000+02:00");
        }
        progress.show();
        compositeDisposable.add(ApiService.getInstance().updateprofile(log, pass, newuser, new ApiCallback() {
            @Override
            public void onSuccess(Object object) {
                if(!newpassword.equals(pass)){
                    SharedPreferences account = getSharedPreferences("Account", MODE_PRIVATE);
                    SharedPreferences.Editor editor = account.edit();
                    editor.putString("password", newpassword);
                    editor.apply();
                    Intent i = getPackageManager()
                            .getLaunchIntentForPackage( getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else {
                    showSuccessDialog();
                }
            }
            @Override
            public void onError(RequestError error) {
                showErrorDialog(ApiService.getInstance().getErrorMessage(ProfileActivity.this,error));
            }

            @Override
            public void onNotAuthorized(RequestError error) {
                ApiService.getInstance().LogOut(ProfileActivity.this);
            }
        }));
    }

    private void setData(){
        loginlabel.setText(user.getNickName());
        emaillabel.setText(user.getEmail());
        if(user.getBirthday() != null){
            year.setText(user.getBirthday().substring(0,4));
            month.setText(user.getBirthday().substring(5,7));
            day.setText(user.getBirthday().substring(8,10));
        }
        else{
            year.setText("1900");
            month.setText("01");
            day.setText("01");
        }
        if(user.getFio() != null){
            fio.setText(user.getFio());
        }
        switch(user.getGender()){
            default:
                notsetButton.setChecked(true);
                break;
            case "м":
                maleButton.setChecked(true);
                break;
            case "ж":
                femaleButton.setChecked(true);
                break;
        }
        switch (user.getUserType()){
            case "м":
                usertypelabel.setText(getString(R.string.moderator));
                break;
            case "а":
                usertypelabel.setText(getString(R.string.admin));
                break;
            default:
                usertypelabel.setText(getString(R.string.user));
                break;
        }
    }

    private void setDate() {
        new DatePickerDialog(ProfileActivity.this, d,
                Integer.valueOf(year.getText().toString()),
                Integer.valueOf(month.getText().toString()),
                Integer.valueOf(day.getText().toString()))
                .show();
    }

    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String datestring = String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth);
            Date date = new Date();
            try{
                date = sdf.parse(datestring);
            }
            catch(Exception e){

            }
            Calendar now= Calendar.getInstance();
            Calendar datecalendar = Calendar.getInstance();
            datecalendar.setTimeInMillis(date.getTime());
            if(now.before(datecalendar)){
                showDateErrorDialog();
            }
            else{
                ProfileActivity.this.year.setText(String.valueOf(year));
                ProfileActivity.this.month.setText(String.valueOf(monthOfYear));
                ProfileActivity.this.day.setText(String.valueOf(dayOfMonth));
            }
        }
    };

    private void showSuccessDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.successupdate))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoadProfile();
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
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.reload), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoadProfile();
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDateErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.error))
                .setMessage(getString(R.string.dateincorrect))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        setDate();
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

    private boolean CheckFIO(String fio){
        return fio.length() >= 10 && fio.length() <= 75 && fio.matches("^[А-Яа-я ]+$");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
