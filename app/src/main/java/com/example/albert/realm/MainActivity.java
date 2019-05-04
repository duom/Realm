package com.example.albert.realm;

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
    EditText txtName,txtAge,txtId;
    Button btnAdd,btnView,btnDelete, btnModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();


        txtId= (EditText) findViewById(R.id.txtId);
        txtName = (EditText) findViewById(R.id.txtName);
        txtAge = (EditText) findViewById(R.id.txtAge);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnView = (Button) findViewById(R.id.btnView);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        txtView = findViewById(R.id.txtView);
        btnModify = findViewById(R.id.btnModify);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveToDatabase(txtName.getText().toString().trim(), txtAge.getText().toString().trim());
                calculaID();

            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshDatabase()
;
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=txtName.getText().toString();
                String age = txtAge.getText().toString();
                deleteFromDatabase(name,age);

            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= txtName.getText().toString();
                String age = txtAge.getText().toString();
                updateFromDatabase(name,age);

            }
        });

    }

    private void deleteFromDatabase(String name, String age) {

        final RealmResults<Person> persons = realm.where(Person.class)
                .equalTo("name", name)
                .or()
                .equalTo("age", age)
                .findAll();


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // remove single match
                persons.deleteFromRealm(0);

            }
        });
    }

    private void updateFromDatabase(final String name, final String age) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                int id = Integer.parseInt(String.valueOf(Integer.parseInt(txtId.getText().toString())));
                Person person = realm.where(Person.class)
                        .equalTo("id", id)
                        .findFirst();
                if (person != null) {
                    person.setName(name);
                    person.setAge(age);

                    realm.insertOrUpdate(person);
                    refreshDatabase();
                }

            }

        });
    }

    private void refreshDatabase() {

        RealmResults<Person> result = realm.where(Person.class).findAllAsync();
        result.load();
        String output="";

        for(Person person : result){

            output+=person.toString();

        }

        txtView.setText(output);
    }

    private void saveToDatabase(final String name, final String age) {


        final int index= calculaID();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Person person = bgRealm.createObject(Person.class,index);
                person.setName(name);
                person.setAge(age);
                //person.setId(index);

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
