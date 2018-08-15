package www.codycarlton.login.signup;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import www.codycarlton.com.clockio.DashboardActivity;
import www.codycarlton.com.clockio.R;

public class Login_Signup_Activity extends AppCompatActivity
        implements LoginFragment.LoginListener, SignupFragment.Signuplistener {

    FragmentManager fragmentManager;
    LoginFragment loginFragment;
    SignupFragment signupFragment;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        checkAuth();

        setContentView(R.layout.activity_login_signup_activity);

        loginFragment = new LoginFragment();
        fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.login_signup_container, loginFragment, "Test").commit();

    }

    private void checkAuth(){
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            Intent dashboardIntent = new Intent(this, DashboardActivity.class);
            startActivity(dashboardIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }

    @Override
    public void goToCreateAccount() {

        signupFragment = new SignupFragment();
        fragmentManager.beginTransaction()
                .add(R.id.login_signup_container, signupFragment, "Test2").addToBackStack(null).commit();
    }

    @Override
    public void login(String email, String password) {

        if (validateEmail(email) && validatePassword(password)){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                gotoDashboard();
                            }else{
                                //TODO: Determine what to do if login was not successful

                            }
                        }
                    });
        }

    }//login(String email, String password)

    @Override
    public void createAccount(String email, String password) {

        if (validateEmail(email) && validatePassword(password)){

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                gotoDashboard();

                            }else{
                                //TODO: Determine what to do if create account was not successful
                            }
                        }
                    });

        }
    }//createAccount(String email, String password)

    @Override
    public void backPressed(){
        onBackPressed();
    }

    private boolean validateEmail(String email){

        if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;

        }else if (email.isEmpty()){

            Snackbar.make(findViewById(R.id.coordinatorLayout), "Email field is empty.",
                    Snackbar.LENGTH_LONG ).show();
            return false;

        }else{

            Snackbar.make(findViewById(R.id.coordinatorLayout), "Not a valid email.",
                    Snackbar.LENGTH_LONG ).show();
            return false;
        }

    }//validateEmail(String email)

    private boolean validatePassword(String password) {

        if (password.length() < 6){
            Snackbar.make(findViewById(R.id.coordinatorLayout), "Password must have at least 6 characters.",
                    Snackbar.LENGTH_LONG).show();
        }

        if (!password.isEmpty() && password.length() >= 6) {
            return true;

        } else if (password.isEmpty()) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), "Password field is empty.",
                    Snackbar.LENGTH_LONG).show();
            return false;
        }

        return false;
    }//validatePassword(String password)

    private void gotoDashboard(){
        Intent dashBoardIntent = new Intent(this, DashboardActivity.class);
        startActivity(dashBoardIntent);
    }//gotoDashboard

}//End of line
