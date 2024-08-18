package com.example.entrevista;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

public class IngresarEntrevistaActivity extends AppCompatActivity {

    private EditText editTextDescripcion, editTextPeriodista;
    private ImageView imageViewEntrevistado;
    private Button buttonAgregarImagen, buttonGuardarEntrevista;
    private Uri imagenUri;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_entrevista);

        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        editTextPeriodista = findViewById(R.id.editTextPeriodista);
        imageViewEntrevistado = findViewById(R.id.imageViewEntrevistado);
        buttonAgregarImagen = findViewById(R.id.buttonAgregarImagen);
        buttonGuardarEntrevista = findViewById(R.id.buttonGuardarEntrevista);

        db = FirebaseFirestore.getInstance();

        buttonAgregarImagen.setOnClickListener(v -> seleccionarImagen());
        buttonGuardarEntrevista.setOnClickListener(v -> guardarEntrevista());
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            imagenUri = data.getData();
            imageViewEntrevistado.setImageURI(imagenUri);
        }
    }

    private void guardarEntrevista() {
        String descripcion = editTextDescripcion.getText().toString();
        String periodista = editTextPeriodista.getText().toString();

        if (descripcion.isEmpty() || periodista.isEmpty() || imagenUri == null) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir la imagen a Blob
        Bitmap bitmap = ((BitmapDrawable) imageViewEntrevistado.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInBytes = baos.toByteArray();
        Entrevista nuevaEntrevista = getEntrevista(imageInBytes, descripcion, periodista);

        // Guardar en Firestore (podrías necesitar ajustar esto dependiendo de cómo quieras almacenar el Blob)
        db.collection("entrevistas").add(nuevaEntrevista)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Entrevista guardada exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar la entrevista", Toast.LENGTH_SHORT).show());

    }

    private static @NonNull Entrevista getEntrevista(byte[] imageInBytes, String descripcion, String periodista) {
        Blob imagenBlob = new SerialBlob(imageInBytes);

        // Crear una nueva entrevista
        Entrevista nuevaEntrevista = new Entrevista(
                // Asumimos que el idOrden se asigna automáticamente o lo calculas de alguna forma
                0, // Cambia esto por la lógica de tu id
                descripcion,
                periodista,
                new Date(),
                imagenBlob,
                null // Aún no manejamos audio aquí
        );
        return nuevaEntrevista;
    }

    private static class SerialBlob implements Blob {
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
