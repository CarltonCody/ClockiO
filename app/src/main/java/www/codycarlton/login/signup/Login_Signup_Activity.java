package www.codycarlton.login.signup;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Slide;
import android.util.Patterns;
import android.view.Gravity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import www.codycarlton.com.clockio.DashboardActivity;
import www.codycarlton.com.clockio.R;

public class Login_Signup_Activity extends AppCompatActivity
        implements LoginFragment.LoginListener, SignupFragment.Signuplistener {

    private FragmentManager fragmentManager;

    private FirebaseAuth mAuth;

    //Tags for later use in referencing fragments
    private static final String LOGINFRAGMENT_TAG = "LoginFragment_TAG";
    private static final String SIGNUPFRAGMENT_TAG = "SignupFragment_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        checkAuth();

        setContentView(R.layout.activity_login_signup_activity);

        LoginFragment loginFragment = new LoginFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.login_signup_container, loginFragment, LOGINFRAGMENT_TAG).commit();

    }

    //Checking if user is signed in.If true then nav to dashboard.
    private void checkAuth(){
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            Intent dashboardIntent = new Intent(this, DashboardActivity.class);
            startActivity(dashboardIntent);
        }
    }//checkAuth()

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }

    //If user has no account setup then nav to a create account page.
    @Override
    public void goToCreateAccount() {

        SignupFragment signupFragment = new SignupFragment();
        signupFragment.setEnterTransition(new Slide(Gravity.BOTTOM));
        signupFragment.setExitTransition(new Slide(Gravity.BOTTOM));
        fragmentManager.beginTransaction()
                .add(R.id.login_signup_container, signupFragment, SIGNUPFRAGMENT_TAG).addToBackStack(null).commit();
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
                                showErrorDialog(getString(R.string.email_pw_err_msg));

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
                                showErrorDialog(getString(R.string.create_accnt_err_msg));
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

            Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.empty_email_msg,
                    Snackbar.LENGTH_LONG ).show();
            return false;

        }else{

            Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.invalid_email_msg,
                    Snackbar.LENGTH_LONG ).show();
            return false;
        }

    }//validateEmail(String email)

    private boolean validatePassword(String password) {

        if (password.length() < 6){
            Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.invalid_pw_msg,
                    Snackbar.LENGTH_LONG).show();
        }

        if (!password.isEmpty() && password.length() >= 6) {
            return true;

        } else if (password.isEmpty()) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.empty_pw_msg,
                    Snackbar.LENGTH_LONG).show();
            return false;
        }

        return false;
    }//validatePassword(String password)

    private void gotoDashboard(){
        Intent dashBoardIntent = new Intent(this, DashboardActivity.class);
        startActivity(dashBoardIntent);
    }//gotoDashboard()

    private void showErrorDialog(String errorMsg){

        final AlertDialog errorDialog;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(errorMsg);

        builder.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        errorDialog = builder.create();
        errorDialog.show();

    }//errorDialog(String errorMsg)

}
