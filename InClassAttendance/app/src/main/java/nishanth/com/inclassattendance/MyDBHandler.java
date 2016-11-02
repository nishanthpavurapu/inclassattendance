package nishanth.com.inclassattendance;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.ResultSet;

/**
 * Created by MIKE on 5/4/2016.
 */
public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="inclassattendance.db",TABLE_COURSE="class_db",TABLE_ATTENDANCE="attendance";
    private static final String CLASS_ID="classid",CLASS_SEM="semester",CLASS_YEAR="year",USER_ID="userid";
    private static final String ATTENDANCE_CLASS_ID="classid",ATTENDANCE_STUDENT_ID="studentid",ATTENDANCE_DATE="attendeddate",ATTENDANCE_DEVICEID="deviceid";

    public MyDBHandler(Context context) {
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q1="CREATE TABLE "+TABLE_COURSE+" ("+CLASS_ID+" TEXT, "+CLASS_SEM+" TEXT, "+CLASS_YEAR+" TEXT, "+USER_ID+" TEXT)";
        String q2="CREATE TABLE "+TABLE_ATTENDANCE+" ("+ATTENDANCE_CLASS_ID+" TEXT, "+ATTENDANCE_STUDENT_ID+" TEXT, "+ATTENDANCE_DATE+" TEXT, "+ATTENDANCE_DEVICEID+" TEXT)";
        db.execSQL(q1);
        db.execSQL(q2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_COURSE+" ;");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ATTENDANCE+" ;");
        onCreate(db);
    }
    public void addClass(String cid,String sem,String year,String cuid){
        ContentValues values=new ContentValues();
        values.put(CLASS_ID,cid);
        values.put(CLASS_SEM,sem);
        values.put(CLASS_YEAR,year);
        values.put(USER_ID, cuid);
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_COURSE, null, values);
        db.close();
    }
    public void addAttendace(String classid, String studentid, String attendeddate,String deviceid)
    {
        ContentValues values = new ContentValues();
        values.put(ATTENDANCE_CLASS_ID,classid);
        values.put(ATTENDANCE_STUDENT_ID,studentid);
        values.put(ATTENDANCE_DATE,attendeddate);
        values.put(ATTENDANCE_DEVICEID,deviceid);
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_ATTENDANCE,null,values);
        db.close();
    }
    public Cursor findClass(String cid){
        String[] proj={CLASS_ID,CLASS_SEM,CLASS_YEAR};
        SQLiteDatabase db=getReadableDatabase();
        String q="classid = \""+cid+"\"";
        Cursor c1=db.query(TABLE_COURSE, proj, q, null, null, null, null);
        return c1;
    }
    public Cursor verifyAttendance(String classid,String attendeddate){
        String[] proj={ATTENDANCE_CLASS_ID,ATTENDANCE_DATE};
        SQLiteDatabase db=getReadableDatabase();
        String q="classid = \""+classid+"\"" + " and attendeddate = \""+attendeddate+"\"";
        Cursor c1=db.query(TABLE_ATTENDANCE,proj,q,null,null,null,null);
        return c1;
    }
}
