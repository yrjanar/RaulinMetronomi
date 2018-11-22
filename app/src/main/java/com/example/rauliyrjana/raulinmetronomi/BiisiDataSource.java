package com.example.rauliyrjana.raulinmetronomi;


import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class BiisiDataSource extends ListActivity {

        private SQLiteDatabase database;
        private OmaSQLiteHelper dbHelper;
        private String[] allColumns = {
                OmaSQLiteHelper.COLUMN_ID,
                OmaSQLiteHelper.COLUMN_NIMI,
                OmaSQLiteHelper.COLUMN_TEMPO};

    public BiisiDataSource(Context context) {
        dbHelper = new OmaSQLiteHelper(context);
    }

    public void open() throws SQLException {
            database = dbHelper.getWritableDatabase();
        }

        public void close() {
            dbHelper.close();
        }

        public Biisi createBiisi(String nimi, String tempo){
            ContentValues values = new ContentValues();
            values.put(OmaSQLiteHelper.COLUMN_NIMI, nimi);
            values.put(OmaSQLiteHelper.COLUMN_TEMPO, tempo);

            long insertId = database.insert(OmaSQLiteHelper.TABLE_BIISIT, null, values);
            Cursor cursor = database.query(OmaSQLiteHelper.TABLE_BIISIT,
                    allColumns, OmaSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            Biisi newBiisi = cursorToBiisi(cursor);
            cursor.close();
            return newBiisi;
        }

        public void deleteBiisi(Biisi biisi) {
            long id = biisi.getId();
            System.out.println("Biisi poistettu id:llä " + id);
            database.delete(OmaSQLiteHelper.TABLE_BIISIT, OmaSQLiteHelper.COLUMN_ID
                    + " = " + id, null);
        }

        public List<Biisi> getAllBiisi() {
            List<Biisi> biisit = new ArrayList<Biisi>();

            Cursor cursor = database.query(OmaSQLiteHelper.TABLE_BIISIT,
                    allColumns, null, null, null, null, OmaSQLiteHelper.COLUMN_NIMI);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Biisi biisi = cursorToBiisi(cursor);
                biisit.add(biisi);
                cursor.moveToNext();
            }
            cursor.close();
            return biisit;
        }

        private Biisi cursorToBiisi(Cursor cursor) {
            Biisi biisi = new Biisi();
            biisi.setId(cursor.getLong(0));
            biisi.setNimi(cursor.getString(1));
            biisi.setTempo(cursor.getString(2));

            return biisi;
        }
    }


