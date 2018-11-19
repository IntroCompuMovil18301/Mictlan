package javeriana.edu.co.mockups;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    TextView correo;
    TextView password;
    Button crearCuenta;
    Button logIn;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        correo = (TextView) findViewById(R.id.m_correo);
        password = (TextView) findViewById(R.id.m_password);
        crearCuenta = (Button) findViewById(R.id.ma_registrarse_but);
        logIn = (Button) findViewById(R.id.ma_ingresar_but);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    Log.d("=>" , "onAuthStateChanged:signdein" + currentUser.getUid());
                    startActivity(new Intent(MainActivity.this, Home.class));
                } else {
                    Log.d("=>" , "onAuthStateChanged:signded_out");
                }

            }
        };

        crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CrearCuentaActivity.class);
                startActivity(intent);
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {

                    mAuth.signInWithEmailAndPassword(correo.getText().toString(), password.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(MainActivity.this, Home.class));
                                    } else {
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }


        });

        /*mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });*/
    }
    private boolean validateForm() {
        boolean valid = true;
        String correoAux = correo.getText().toString();
        String contraseñaAux = password.getText().toString();
        if (TextUtils.isEmpty(correoAux)) {
            correo.setError("Required.");
            valid =false;
        } else {
            correo.setError(null);
        }

        if (TextUtils.isEmpty(contraseñaAux)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return  valid;
    }


}
