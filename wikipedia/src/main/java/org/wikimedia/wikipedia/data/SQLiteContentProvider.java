package org.wikimedia.wikipedia.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

abstract public class SQLiteContentProvider<T> extends ContentProvider {
    private final PersistanceHelper<T> persistanceHelper;
    protected SQLiteContentProvider(PersistanceHelper<T> persistanceHelper) {
        this.persistanceHelper = persistanceHelper;
    }

    abstract protected DBOpenHelper getDbOpenHelper();

    private final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int MATCH_ALL = 1;

    @Override
    public boolean onCreate() {
        uriMatcher.addURI(getAuthority(), persistanceHelper.getTableName(), MATCH_ALL);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(persistanceHelper.getTableName());

        int uriType = uriMatcher.match(uri);

        SQLiteDatabase db = getDbOpenHelper().getReadableDatabase();
        Cursor cursor;

        switch (uriType) {
            case MATCH_ALL:
                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Uri " + uri + " does not match any matcher!");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = getDbOpenHelper().getWritableDatabase();
        switch (uriType) {
            case MATCH_ALL:
                sqlDB.insert(persistanceHelper.getTableName(), null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new IllegalArgumentException("Delete functionality not implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new IllegalArgumentException("Update functionality not implemented");
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = getDbOpenHelper().getWritableDatabase();
        sqlDB.beginTransaction();
        switch (uriType) {
            case MATCH_ALL:
                for(ContentValues value: values) {
                    sqlDB.insert(persistanceHelper.getTableName(), null, value);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        sqlDB.setTransactionSuccessful();
        sqlDB.endTransaction();
        getContext().getContentResolver().notifyChange(uri, null);
        return values.length;

    }

    protected String getAuthority() {
        return getAuthorityForTable(persistanceHelper.getTableName());
    }

    public static final String getAuthorityForTable(String table) {
        return "org.wikimedia.wikipedia." + table;
    }
}
