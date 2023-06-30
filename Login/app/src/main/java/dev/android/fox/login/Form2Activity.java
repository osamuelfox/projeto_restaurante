package dev.android.fox.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Form2Activity extends AppCompatActivity {

    Form2Activity f2 = new Form2Activity();

    private EditText editEndereco, editBairro, editCidade, editUf, editCep;
    private Button bt_CriarConta;

    private TextView text_telaLogin;

    private ProgressBar progressBar;

    String[] mensagens = {"Preencha todos os campos", "Cadasro Realizado com sucesso"};

    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_nova_conta2);


        IniciarComponentes();

        telaDeLogin();

        bt_CriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endereco = editEndereco.getText().toString();
                String bairro = editBairro.getText().toString();
                String cidade = editCidade.getText().toString();
                String uf = editUf.getText().toString();
                String cep = editCep.getText().toString();
                if (endereco.isEmpty() || bairro.isEmpty() || cidade.isEmpty() || uf.isEmpty() || cep.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    SalvarDadosUsuario(v);
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            telaDeLogin();

                        }
                    }, 3000);

                }
            }
        });

    }

    private void telaDeLogin() {
        text_telaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Form2Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void SalvarDadosUsuario(View v) {

        String endereco = editEndereco.getText().toString();
        String bairro = editBairro.getText().toString();
        String cidade = editCidade.getText().toString();
        String uf = editUf.getText().toString();
        String cep = editCep.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("endereco", endereco);
        usuarios.put("bairro", bairro);
        usuarios.put("cidade", cidade);
        usuarios.put("uf", uf);
        usuarios.put("cep", cep);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d("db", "Sucesso ao salvar os dados");

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("db_erro", "Erro ao salvar dados" + e.toString());

            }
        });
    }

    private void IniciarComponentes() {

        editEndereco = findViewById(R.id.edit_endereco);
        editBairro = findViewById(R.id.edit_bairro);
        editCidade = findViewById(R.id.edit_cidade);
        editUf = findViewById(R.id.edit_uf);
        editCep = findViewById(R.id.edit_cep);
        progressBar = findViewById(R.id.progresseBar);

        bt_CriarConta = findViewById(R.id.bt_criarConta);

        text_telaLogin = findViewById(R.id.text_tela_login);

    }
}