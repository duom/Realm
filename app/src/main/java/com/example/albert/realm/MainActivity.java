package com.example.albert.realm;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.albert.realm.Model.Person;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {


    private Realm realm;
    TextView txtView;
    EditText txtNom, txtEdat,txtId, txtSexe,txtTelefon;
    Button btnAfegeix,btnView, btnElimina, btnModifica, btnBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();

        txtId= findViewById(R.id.txtId);
        txtNom = findViewById(R.id.txtNom);
        txtEdat = findViewById(R.id.txtEdat);
        txtSexe = findViewById(R.id.txtSexe);
        btnAfegeix = findViewById(R.id.btnAfegeix);
        btnView = findViewById(R.id.btnView);
        btnElimina = findViewById(R.id.btnElimina);
        txtView = findViewById(R.id.txtView);
        btnModifica = findViewById(R.id.btnModifica);
        btnBusca = findViewById(R.id.btnBusca);
        txtTelefon = findViewById(R.id.txtTelefon);



        btnAfegeix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom= txtNom.getText().toString();
                String edat = txtEdat.getText().toString();
                String sexe = txtSexe.getText().toString();
                String telefon = txtTelefon.getText().toString();

                afegeixDatabase(nom,edat,sexe,telefon);
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshDatabase();
            }
        });

        btnElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtId.getText().toString().length()>0) {
                    int id = Integer.parseInt(txtId.getText().toString());
                    String nom = txtNom.getText().toString();
                    String edat = txtEdat.getText().toString();
                    String sexe = txtSexe.getText().toString();
                    String telefon = txtTelefon.getText().toString();


                    eliminaDeDatabase(id, nom, edat, sexe,telefon);
                } else {

                    if (txtId.getText().toString().isEmpty()) {
                        String nom = txtNom.getText().toString();
                        String edat = txtEdat.getText().toString();
                        String sexe = txtSexe.getText().toString();
                        String telefon = txtTelefon.getText().toString();

                        eliminaDeDatabaseSenseId(nom, edat, sexe,telefon);
                    }
                }
            }
        });

        btnModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom= txtNom.getText().toString();
                String edat = txtEdat.getText().toString();
                String sexe = txtSexe.getText().toString();
                String telefon = txtTelefon.getText().toString();
                actualitzaDesdeDatabase(nom,edat,sexe,telefon);

            }
        });

        btnBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtId.getText().toString().length()>0) {
                    int id = Integer.parseInt(txtId.getText().toString());
                    String nom = txtNom.getText().toString();
                    String edat = txtEdat.getText().toString();
                    String sexe = txtSexe.getText().toString();
                    String telefon = txtTelefon.getText().toString();

                    actualitzaBusqueda(id, nom, edat, sexe,telefon);
                } else {

                    if (txtId.getText().toString().isEmpty()) {
                        String name = txtNom.getText().toString();
                        String edat = txtEdat.getText().toString();
                        String gender = txtSexe.getText().toString();
                        String telefon = txtTelefon.getText().toString();

                        actualitzaBusquedaSenseId(name, edat, gender,telefon);
                    }
                }
            }
        });
    }


    private void eliminaDeDatabase(final int id, final String name, final String age, final String gender, final String telefon) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {


                Person persons = realm.where(Person.class)
                        .equalTo("id", id)
                        .or()
                        .equalTo("name", name)
                        .or()
                        .equalTo("age", age)
                        .or()
                        .equalTo("gender", gender)
                        .or()
                        .equalTo("telefon", telefon)
                        .findFirst();

                if(persons!= null) {
                    persons.deleteFromRealm();
                    refreshDatabase();
                }}
        });
    }

    private void eliminaDeDatabaseSenseId(final String name, final String age, final String gender, final String telefon) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Person persons = realm.where(Person.class)
                        .equalTo("name", name)
                        .or()
                        .equalTo("age", age)
                        .or()
                        .equalTo("gender", gender)
                        .or()
                        .equalTo("telefon", telefon)
                        .findFirst();

                if(persons!= null) {
                    persons.deleteFromRealm();
                    refreshDatabase();
                }}
        });
    }

    private void actualitzaDesdeDatabase(final String name, final String age, final String gender, final String telefon) {
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int id = Integer.parseInt(String.valueOf(Integer.parseInt(txtId.getText().toString())));
                Person person = realm.where(Person.class)
                        .equalTo("id", id)
                        .findFirst();
                person.setName(name);
                person.setAge(age);
                person.setGender(gender);
                person.setTelefon(telefon);
                realm.insertOrUpdate(person);
                refreshDatabase();

            }
        });
    }


    private void refreshDatabase() {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Person> result = realm.where(Person.class).findAllAsync();
        result.load();
        String output="";

        for(Person person : result){

            output+=person.toString();
        }
        txtView.setText(output);
    }

    private void actualitzaBusqueda(int id, String name, String age, String gender, String telefon) {
        Realm realm = Realm.getDefaultInstance();
        String output="";

        RealmResults<Person> result = realm.where(Person.class)

                .equalTo("id", id)
                .or()
                .equalTo("name", name)
                .or()
                .equalTo("age", age)
                .or()
                .equalTo("gender", gender)
                .or()
                .equalTo("telefon", telefon)
                .findAll();

        for(Person person : result){

            output+=person.toString();
        }
        txtView.setText(output);
    }

    private void actualitzaBusquedaSenseId(String name, String age, String gender, String telefon) {
        Realm realm = Realm.getDefaultInstance();
        String output="";

        RealmResults<Person> result = realm.where(Person.class)

                .equalTo("name", name)
                .or()
                .equalTo("age", age)
                .or()
                .equalTo("gender", gender)
                .or()
                .equalTo("telefon", telefon)
                .findAll();

        for(Person person : result){

            output+=person.toString();
        }
        txtView.setText(output);
    }


    private void afegeixDatabase(final String name, final String age, final String gender, final String telefon) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final int index= calculaID();
                Person person = realm.createObject(Person.class,index);
                person.setName(name);
                person.setAge(age);
                person.setGender(gender);
                person.setTelefon(telefon);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.v("Success","--------------OK---------------");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("Failed", error.getMessage());
            }
        });

    }

    private final static int calculaID(){

        Realm realm = Realm.getDefaultInstance();
        Number currentIdNum = realm.where(Person.class).max("id");

        int nextId;
        if (currentIdNum == null){
            nextId = 0;
        }else {
            nextId = currentIdNum.intValue()+1;
        }
        return nextId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
