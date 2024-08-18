package com.example.entrevista;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

public class ModificarEliminarEntrevistaActivity extends AppCompatActivity {

    private EditText editTextDescripcion, editTextPeriodista;
    private ImageView imageViewEntrevistado;
    private Button buttonModificarImagen, buttonGuardarCambios, buttonEliminarEntrevista;
    private Uri imagenUri;
    private FirebaseFirestore db;
    private Entrevista entrevista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_eliminar_entrevista);

        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        editTextPeriodista = findViewById(R.id.editTextPeriodista);
        imageViewEntrevistado = findViewById(R.id.imageViewEntrevistado);
        buttonModificarImagen = findViewById(R.id.buttonModificarImagen);
        buttonGuardarCambios = findViewById(R.id.buttonGuardarCambios);
        buttonEliminarEntrevista = findViewById(R.id.buttonEliminarEntrevista);

        db = FirebaseFirestore.getInstance();
        entrevista = (Entrevista) getIntent().getSerializableExtra("Entrevista");

        if (entrevista != null) {
            cargarDatos();
        }

        buttonModificarImagen.setOnClickListener(v -> seleccionarImagen());
        buttonGuardarCambios.setOnClickListener(v -> guardarCambios());
        buttonEliminarEntrevista.setOnClickListener(v -> eliminarEntrevista());
    }

    private void cargarDatos() {
        editTextDescripcion.setText(entrevista.getDescripcion());
        editTextPeriodista.setText(entrevista.getPeriodista());

        try {
            byte[] imageInBytes = entrevista.getImagenUrl().getBytes(1, (int) entrevista.getImagenUrl().length());
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageInBytes, 0, imageInBytes.length);
            imageViewEntrevistado.setImageBitmap(bitmap);
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            imagenUri = data.getData();
            imageViewEntrevistado.setImageURI(imagenUri);
        }
    }

    private void guardarCambios() {
        String descripcion = editTextDescripcion.getText().toString();
        String periodista = editTextPeriodista.getText().toString();

        if (descripcion.isEmpty() || periodista.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (imagenUri != null) {
                Bitmap bitmap = ((BitmapDrawable) imageViewEntrevistado.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageInBytes = baos.toByteArray();
                Blob imagenBlob = new SerialBlob(imageInBytes);
                entrevista.setImagenUrl(imagenBlob);
            }

            entrevista.setDescripcion(descripcion);
            entrevista.setPeriodista(periodista);
            entrevista.setFecha(new Date());

            db.collection("entrevistas").document(String.valueOf(entrevista.getIdOrden())).set(entrevista)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Entrevista actualizada", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar la entrevista", Toast.LENGTH_SHORT).show());

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al convertir la imagen a Blob", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarEntrevista() {
        db.collection("entrevistas").document(String.valueOf(entrevista.getIdOrden())).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Entrevista eliminada", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar la entrevista", Toast.LENGTH_SHORT).show());
    }

    private class SerialBlob implements Blob {
        public SerialBlob(byte[] imageInBytes) {
        }

        @Override
        public long length() throws SQLException {
            return 0;
        }

        @Override
        public byte[] getBytes(long l, int i) throws SQLException {
            return new byte[0];
        }

        @Override
        public InputStream getBinaryStream() throws SQLException {
            return null;
        }

        @Override
        public long position(byte[] bytes, long l) throws SQLException {
            return 0;
        }

        @Override
        public long position(Blob blob, long l) throws SQLException {
            return 0;
        }

        @Override
        public int setBytes(long l, byte[] bytes) throws SQLException {
            return 0;
        }

        @Override
        public int setBytes(long l, byte[] bytes, int i, int i1) throws SQLException {
            return 0;
        }

        @Override
        public OutputStream setBinaryStream(long l) throws SQLException {
            return null;
        }

        @Override
        public void truncate(long l) throws SQLException {

        }

        @Override
        public void free() throws SQLException {

        }

        @Override
        public InputStream getBinaryStream(long l, long l1) throws SQLException {
            return null;
        }
    }
}
