package com.example.rauliyrjana.raulinmetronomi;

import android.app.ListActivity;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import static android.icu.text.RelativeDateTimeFormatter.Direction.THIS;


public class MainActivity extends AppCompatActivity  {

    private BiisiDataSource dataSource;
    TextView tempo; //textView for Tempo
    SeekBar saatoPalkki; // red seek bar for tempo
    SoundPool klikkiSoundit; //soundPool is sound library
    Switch onOff;
    int klikki;
    Button add;
    Button remove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource=new BiisiDataSource(this);// create a new database
        dataSource.open();// to open it

        List<Biisi> values=dataSource.getAllBiisi();// values is a list of songs in database
        ArrayAdapter<Biisi> adapter=new ArrayAdapter<Biisi>(this, android.R.layout.simple_list_item_single_choice, values);
        ListView listView = (ListView) findViewById(R.id.editListView); //when MA not extended ListActivity
        listView.setAdapter(adapter);// to determine adapter to listView


        tempo = (TextView) findViewById(R.id.textViewTempo);// connection to tVT
        saatoPalkki = (SeekBar) findViewById(R.id.saatoPalkki); //seekbar
        onOff = (Switch) findViewById(R.id.onOff); // audio on or off


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
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {// position of onoff -switch
                if(isChecked){
                    playSound(saatoPalkki.getProgress());// if on, let's play click
                }
                else {
                    klikkiSoundit.autoPause();// otherwise let's pause
                }
            }
        });


        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                ArrayAdapter<Biisi> adapter=(ArrayAdapter<Biisi>) dataSource.getListAdapter();
                Biisi biisi=null;

                EditText annaKappale = findViewById(R.id.editTextKappale);
                EditText annaTempo = findViewById(R.id.editTextTempo);

                biisi = dataSource.createBiisi(annaKappale.getText().toString(), annaTempo.getText().toString());

                annaKappale.setText("");// to clear input fields
                annaTempo.setText("");// to clear input fields
                }
        });


        remove = (Button) findViewById(R.id.poista);
        remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ArrayAdapter<Biisi> adapter = (ArrayAdapter<Biisi>) dataSource.getListAdapter();
                Biisi biisi = null;

                if (adapter.getCount() > 0) {
                    biisi = (Biisi) dataSource.getListAdapter().getItem(0);
                    dataSource.deleteBiisi(biisi);
                    //adapter.remove(biisi);
                    }
                }

        });
    }



        @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }



    // tempo setting
    public void paljonVahemmanTempoa(View v){// to decrease tempo 10 steps
        saatoPalkki.setProgress(saatoPalkki.getProgress()-10);
    }
    public void vahanVahemmanTempoa(View v) {// to decrease tempo 1 step
        saatoPalkki.setProgress(saatoPalkki.getProgress() - 1);
    }
    public void vahanLisaaTempoa(View v){// to increase tempo 1 step
        saatoPalkki.setProgress(saatoPalkki.getProgress()+1);
    }
    public void paljonLisaaTempoa(View v){// to increase tempo 10 steps
        saatoPalkki.setProgress(saatoPalkki.getProgress()+10);
    }



    private void playSound(int progress){// playing...
        int beatsPerMinute = progress+30; //seekbarin limits, lower 50 and upper 200, enough I think
        tempo.setText(Integer.toString(beatsPerMinute));// set tempo to textbox
        float beatsPerSecond = (beatsPerMinute/60f)/2;// transformation

        if(onOff.isChecked())// if true, let's play with these parameters
            klikkiSoundit.play(klikki, 1, 1, 0, -1, beatsPerSecond);
    }
}
