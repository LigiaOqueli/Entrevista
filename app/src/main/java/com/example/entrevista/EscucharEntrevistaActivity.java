package com.example.entrevista;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public class EscucharEntrevistaActivity extends AppCompatActivity {

    private Button buttonPlayAudio;
    private MediaPlayer mediaPlayer;
    private Entrevista entrevista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_entrevista);

        buttonPlayAudio = findViewById(R.id.buttonPlayAudio);
        mediaPlayer = new MediaPlayer();
        entrevista = (Entrevista) getIntent().getSerializableExtra("Entrevista");

        buttonPlayAudio.setOnClickListener(v -> reproducirAudio());
    }

    private void reproducirAudio() {
        if (entrevista != null) {
            try {
                Blob audioBlob = entrevista.getAudioUrl();
                if (audioBlob != null) {
                    byte[] audioInBytes = audioBlob.getBytes(1, (int) audioBlob.length());
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(new ByteArrayInputStream(audioInBytes).readAllBytes());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } else {
                    Toast.makeText(this, "No hay audio disponible para esta entrevista", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al reproducir el audio", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
