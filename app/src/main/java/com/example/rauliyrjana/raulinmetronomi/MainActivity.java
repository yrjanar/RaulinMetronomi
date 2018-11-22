package com.example.rauliyrjana.raulinmetronomi;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.preference.EditTextPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {
//public class MainActivity extends ListActivity {
    private BiisiDataSource dataSource;

    TextView tempo; //tekstinäkymä tempolukemalle
    SeekBar saatoPalkki; // punainen säätöpalkki tempolle
    SoundPool klikkiSoundit; //soundPool on Androidin äänikokoelma
    Switch onOff;// on luokka eli kytkin joka on joko pääällä tai pois
    int klikki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataSource=new BiisiDataSource(this);// luodaan uusi tietokanta
        dataSource.open();// avataan se

        List<Biisi> values=dataSource.getAllBiisi();// values on lista tietokannan biisejä
        ArrayAdapter<Biisi> adapter=new ArrayAdapter<Biisi>(this, android.R.layout.simple_list_item_1, values);
        //setListAdapter(adapter);// adapterin asetukset


        tempo = (TextView) findViewById(R.id.tempo); // kytketään textViewBPM -painike tähän
        saatoPalkki = (SeekBar) findViewById(R.id.saatoPalkki);
        onOff = (Switch) findViewById(R.id.onOff);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        klikkiSoundit = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();
        klikki = klikkiSoundit.load(this, R.raw.tick, 1);

        saatoPalkki.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                playSound(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {// missä asennossa kytkin on
                if(isChecked){
                    playSound(saatoPalkki.getProgress());// jos on päällä, soittaa klikin
                }
                else {
                    klikkiSoundit.autoPause();// muuten on paussia
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    // seekbarin säädöt
    public void paljonVahemmanTempoa(View v){// vähennetään tempoa seekbarilla
        saatoPalkki.setProgress(saatoPalkki.getProgress()-10);
    }
    public void vahanVahemmanTempoa(View v) {// vähennetään tempoa seekbarilla
        saatoPalkki.setProgress(saatoPalkki.getProgress() - 1);
    }
    public void vahanLisaaTempoa(View v){// lisätään tempoa
        saatoPalkki.setProgress(saatoPalkki.getProgress()+1);
    }
    public void paljonLisaaTempoa(View v){// lisätään paljon tempoa
        saatoPalkki.setProgress(saatoPalkki.getProgress()+10);
    }

    private void playSound(int progress){// soittoa
        int beatsPerMinute = progress+30; //seekbarin alarajaksi on 50 ja yläraja 200, riittävät
        tempo.setText(Integer.toString(beatsPerMinute));// asetetaan tempo näytölle
        float beatsPerSecond = (beatsPerMinute/60f)/2;// muunnos iskuiksi sekunnissa

        if(onOff.isChecked())// jos tosi, niin soitetaan näillä parametreilla
            klikkiSoundit.play(klikki, 1, 1, 0, -1, beatsPerSecond);
    }
}

