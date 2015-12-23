package com.zagapps.eventblank.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.zagapps.eventblank.R;
import com.zagapps.eventblank.models.Event;
import com.zagapps.eventblank.models.Locations;
import com.zagapps.eventblank.models.Meta;
import com.zagapps.eventblank.models.Sessions;
import com.zagapps.eventblank.models.Speakers;
import com.zagapps.eventblank.models.Texts;
import com.zagapps.eventblank.models.Tracks;

import java.io.File;
import java.util.ArrayList;


public class EventDatabase extends SQLiteAssetHelper
{
    private static EventDatabase sEventDatabase;

    private static final int DATABASE_VERSION = 1;

    private EventDatabase(Context context,String path)
    {
        super(context, context.getResources().getString(R.string.database_name),
                path, null, DATABASE_VERSION);
    }

    public static EventDatabase get(Context context)
    {
        if(sEventDatabase==null)
        {
            File file = new File(context.getFilesDir().getPath()+"/"+
                    context.getResources().getString(R.string.database_name));
            String path;
            if(file.exists())
                path=context.getFilesDir().getPath()+"/";
            else
                path=null;

            sEventDatabase = new EventDatabase(context, path);
        }

        return sEventDatabase;
    }

    public enum ColumnNames
    {
        is_favorite, speaker ,begin_time, title,
        id_session, id_speaker
    }

    public enum Tables
    {
        speakers,events,tracks,sessions,locations,texts,meta
    }

    public Event getEvent()
    {
        SQLiteDatabase database=getWritableDatabase();
        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();

        queryBuilder.setTables(Tables.events.name());
        Cursor cursor=queryBuilder.query(database,null,null,null,null,null,null);
        cursor.moveToFirst();

        Event event=new Event(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getLong(3),
                cursor.getLong(4),cursor.getString(5),cursor.getBlob(6),cursor.getString(7),
                cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getInt(11)!=0,
                cursor.getString(12), cursor.getString(13));

        cursor.close();
        return event;
    }

    public static void resetSingletons() {
        if (sEventDatabase != null) {
            sEventDatabase.close();
            sEventDatabase = null;
        }
    }

    public ArrayList<Speakers> getSpeakers()
    {
        SQLiteDatabase database=getWritableDatabase();


        try
        {
            database.execSQL("ALTER TABLE "+ Tables.speakers.name() + " ADD COLUMN "+ ColumnNames.is_favorite.name() +" INTEGER DEFAULT 0");
        } catch (SQLException e) {
            Log.i("ADD COLUMN "+ ColumnNames.is_favorite.name() , ColumnNames.is_favorite.name()+" already exists");
        }
        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();

        queryBuilder.setTables(Tables.speakers.name());
        Cursor cursor=queryBuilder.query(database,null,null,null,null,null, ColumnNames.speaker.name()+" ASC");

        cursor.moveToFirst();

        ArrayList<Speakers> speakers=new ArrayList<>();
        for(int i=0;i<cursor.getCount();i++)
        {
            speakers.add(new Speakers(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getBlob(4), cursor.getString(5),cursor.getInt(6) !=0));

            cursor.moveToNext();
            if(cursor.isAfterLast())
                break;
        }

        cursor.close();
        return speakers;
    }

    public ArrayList<Tracks> getTracks()
    {
        SQLiteDatabase database=getWritableDatabase();
        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();

        queryBuilder.setTables(Tables.tracks.name());
        Cursor cursor=queryBuilder.query(database,null,null,null,null,null,null);

        cursor.moveToFirst();
        ArrayList<Tracks> tracks=new ArrayList<>();
        for(int i=0;i<cursor.getCount();i++)
        {
            tracks.add(new Tracks(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));

            cursor.moveToNext();
            if(cursor.isAfterLast())
                break;
        }
        cursor.close();

        return tracks;
    }

    public ArrayList<Sessions> getSessions()
    {
        SQLiteDatabase database=getWritableDatabase();
        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();

        queryBuilder.setTables(Tables.sessions.name());
        Cursor cursor=queryBuilder.query(database,null,null,null,null,null, ColumnNames.begin_time.name()+" ASC");

        cursor.moveToFirst();
        ArrayList<Sessions> sessions=new ArrayList<>();

        for(int i=0;i<cursor.getCount();i++)
        {
            sessions.add(new Sessions(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getLong(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6) != 0, cursor.getInt(7)));
            cursor.moveToNext();
            if(cursor.isAfterLast())
                break;
        }
        cursor.close();

        return sessions;
    }

    public ArrayList<Locations> getLocations()
    {
        SQLiteDatabase database=getWritableDatabase();
        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();

        queryBuilder.setTables(Tables.locations.name());
        Cursor cursor=queryBuilder.query(database, null, null, null, null, null, null);

        cursor.moveToFirst();

        ArrayList<Locations> locations=new ArrayList<>();
        for(int i=0;i<cursor.getCount();i++)
        {
            locations.add(new Locations(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getBlob(3),cursor.getDouble(4),cursor.getDouble(5)));

            cursor.moveToNext();
            if(cursor.isAfterLast())
                break;
        }

        cursor.close();

        return locations;
    }

    public ArrayList<Texts> getTexts()
    {
        SQLiteDatabase database=getWritableDatabase();
        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();

        queryBuilder.setTables(Tables.texts.name());
        Cursor cursor=queryBuilder.query(database, null, null, null, null, null, ColumnNames.title.name()+" ASC");
        cursor.moveToFirst();

        ArrayList<Texts> texts=new ArrayList<>();

        for(int i=0;i<cursor.getCount();i++)
        {
            texts.add(new Texts(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            cursor.moveToNext();
            if(cursor.isAfterLast())
                break;
        }
        cursor.close();

        return texts;
    }

    public Meta getMeta()
    {
        SQLiteDatabase database=getWritableDatabase();
        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();

        queryBuilder.setTables(Tables.meta.name());
        Cursor cursor=queryBuilder.query(database,null,null,null,null,null,null);

        cursor.moveToFirst();

        Meta meta=new Meta(cursor.getDouble(0));
        cursor.close();

        return meta;
    }

    public void setSessionFavourite(int id, int favourite)
    {
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL("UPDATE "+Tables.sessions.name()+" SET "+ ColumnNames.is_favorite.name()+"=" + favourite + " WHERE "+ ColumnNames.id_session.name()+"="+ id);
    }

    public void setSpeakerFavourite(int id, int favourite)
    {
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL("UPDATE "+Tables.speakers.name()+" SET "+ ColumnNames.is_favorite.name()+"=" + favourite + " WHERE "+ ColumnNames.id_speaker.name()+"=" + id);
    }

}
