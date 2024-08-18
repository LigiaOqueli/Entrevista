package com.example.entrevista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaEntrevistasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEntrevistas;
    private EntrevistaAdapter entrevistaAdapter;
    private List<Entrevista> entrevistaList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_entrevistas);

        recyclerViewEntrevistas = findViewById(R.id.recyclerViewEntrevistas);
        recyclerViewEntrevistas.setLayoutManager(new LinearLayoutManager(this));

        entrevistaList = new ArrayList<>();
        entrevistaAdapter = new EntrevistaAdapter(entrevistaList);
        recyclerViewEntrevistas.setAdapter(entrevistaAdapter);

        db = FirebaseFirestore.getInstance();
        cargarEntrevistas();
    }

    private void cargarEntrevistas() {
        CollectionReference entrevistasRef = db.collection("entrevistas");
        entrevistasRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot queryDocumentSnapshots, @NonNull FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ListaEntrevistasActivity.this, "Error al cargar entrevistas", Toast.LENGTH_SHORT).show();
                    return;
                }

                entrevistaList.clear();
                for (var doc : queryDocumentSnapshots) {
                    Entrevista entrevista = doc.toObject(Entrevista.class);
                    entrevistaList.add(entrevista);
                }
                entrevistaAdapter.notifyDataSetChanged();
            }
        });
    }
}
