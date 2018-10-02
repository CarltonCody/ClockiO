package www.codycarlton.login.signup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import www.codycarlton.com.clockio.R;

/*
* Fragment view for allowing user to signup.*/

public class SignupFragment extends Fragment implements View.OnClickListener {

    public SignupFragment(){
    }

    public interface Signuplistener {
        void backPressed();
        void createAccount(String email, String password);
    }

    private Signuplistener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Signuplistener) context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null){
            Button createAccountButton = getView().findViewById(R.id.createAccount_button);
            Button backButton = getView().findViewById(R.id.back_button);

            createAccountButton.setOnClickListener(this);
            backButton.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {

        if (getView() != null){
            TextInputEditText emailField = getView().findViewById(R.id.email_field);
            TextInputEditText passwordField = getView().findViewById(R.id.password_field);

            switch (view.getId()){

                case R.id.back_button:
                    listener.backPressed();
                    break;

                case R.id.createAccount_button:
                    listener.createAccount(emailField.getText().toString(),
                            passwordField.getText().toString());
                    break;
            }


        }

    }

}
