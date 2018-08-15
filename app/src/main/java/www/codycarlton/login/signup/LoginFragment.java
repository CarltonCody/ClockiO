package www.codycarlton.login.signup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import www.codycarlton.com.clockio.R;

public class LoginFragment extends Fragment implements View.OnClickListener {

    public LoginFragment(){
    }

    public interface LoginListener {

        void goToCreateAccount();
        void login(String email, String password);

    }

    private LoginListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.listener = (LoginListener) context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null){

            Button createAccountButton = getView().findViewById(R.id.createAccount_button);
            Button loginButton = getView().findViewById(R.id.login_button);

            createAccountButton.setOnClickListener(this);
            loginButton.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {

        if (getView() != null){
            EditText emailField = getView().findViewById(R.id.email_field);
            EditText passwordField = getView().findViewById(R.id.password_field);

            switch (view.getId()) {

                case R.id.createAccount_button:
                    listener.goToCreateAccount();
                    break;

                case R.id.login_button:
                    listener.login(emailField.getText().toString(),
                            passwordField.getText().toString());
                    break;
            }

        }

    }

}
