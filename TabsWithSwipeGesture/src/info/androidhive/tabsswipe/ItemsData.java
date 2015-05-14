package info.androidhive.tabsswipe;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemsData extends Activity{

	public static final String KEY_ITEMNAME = "item_name";
	public static final String KEY_LISTID = "list_id";
	public static final String KEY_ITEMID = "item_id";
	public static final String KEY_LIST = "list_name";
	public static final String KEY_USERID = "uid";
	private static final String DATABASE_NAME = "ShoppingListManagerDB1";
	private static final String TABLE_LIST = "ListTable";
	private static final String TABLE_USERANDLISTID = "UserAndListTable";
	private static final String TABLE_ITEMS = "ItemsTable";
	private static final int DATABASE_VERSION = 9;

	private DBHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
	
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
	
			db.execSQL( "CREATE TABLE " + TABLE_LIST+ " (" + KEY_LISTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_LIST + " TEXT NOT NULL UNIQUE );");
			db.execSQL( "CREATE TABLE " + TABLE_ITEMS+ " (" + KEY_ITEMID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_ITEMNAME + " TEXT NOT NULL, " + KEY_LISTID + " INTEGER );");
			db.execSQL( "CREATE TABLE " + TABLE_USERANDLISTID+ " (" + KEY_LISTID + " INTEGER PRIMARY KEY, " +
					KEY_USERID + " INTEGER NOT NULL );");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERANDLISTID);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
			onCreate(db);
			onCreate(db);
			onCreate(db);
		}
	}
	
	public ItemsData (Context c) {
		ourContext=c;
	}
	
	public ItemsData open() throws SQLException{
		ourHelper = new DBHelper (ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	
	public ItemsData close(){
		ourHelper.close();
		return this;
	}

	public long createListEntry(String listname) {
	
		ContentValues cv_2 = new ContentValues();
		cv_2.put(KEY_LIST, listname);
		return ourDatabase.insert(TABLE_LIST, null, cv_2);
	}
	
	public long createItemsListEntry(int listid, String itemname) {
		
		ContentValues cv_2 = new ContentValues();
		cv_2.put(KEY_ITEMNAME, itemname);
		cv_2.put(KEY_LISTID, listid);
		return ourDatabase.insert(TABLE_ITEMS, null, cv_2);
	}
	
	public long createUserIDAndListEntry(int listid, int userid) {
		
		ContentValues cv_2 = new ContentValues();
		cv_2.put(KEY_LISTID, listid);
		cv_2.put(KEY_USERID, userid);
		return ourDatabase.insert(TABLE_USERANDLISTID, null, cv_2);
	}
	
	public int getCountDataList(int id) {
	
		String[] columns = new String[] {KEY_LISTID, KEY_USERID};
		Cursor cu = ourDatabase.query(TABLE_USERANDLISTID, columns, KEY_USERID + "=?",new String[]{String.valueOf(id)}, null, null, null, null); 
		int result_list = cu.getCount();
		//int result_list = cu.getCount();
		return result_list;
	}
	
	public int getlastId() {
		
		String[] columns = new String[] {KEY_LISTID, KEY_LIST};
		Cursor cursor = ourDatabase.query(TABLE_LIST, columns, null, null, null, null,null); 
		cursor.moveToLast();
		int id = cursor.getColumnIndex(KEY_LISTID);
		int last_id = cursor.getInt(id);
		return last_id;
	}
	
	public String getDataList(int i) {
		
		String[] columns = new String[] {KEY_LISTID, KEY_USERID};
		String[] listcolumns = new String[] {KEY_LISTID, KEY_LIST};
		String  listid = "";
		String result_list = "";
		String[] listids ;
		
		Cursor cursor = ourDatabase.query(TABLE_USERANDLISTID, columns, KEY_USERID + "=?" ,new String[]{String.valueOf(i)}, null, null, null, null); 
		
	
			if (cursor.moveToFirst() && cursor != null) {
				do {	
					int id = cursor.getColumnIndex(KEY_LISTID);
					listid = listid + cursor.getInt(id) + "\n";
				 Log.d("pgala", String.valueOf(listid));
				} while (cursor.moveToNext());
			// Log.d("pgala", String.valueOf(listid));
			}
		
          cursor.close();
	    if(listid.length() > 0){
		  listids = listid.split("\n");
		
		for(int j = 0 ; j < listids.length ; j++){
			int id = Integer.parseInt(listids[j]);
	   		System.out.print("	iddd  " + 	id );
			/*Cursor cursor1 = ourDatabase.query(TABLE_LIST, listcolumns, KEY_LISTID + "=?" ,new String[]{String.valueOf(12)}, null,null, null, null); 
            if(cursor1 != null  && cursor1.moveToFirst()){
            	System.out.print("	hello i am in cursor1 ");	
			//cursor1.moveToFirst(); 
		    int iid =cursor1.getColumnIndex(KEY_LIST);
			*/
     		result_list = result_list +  getListNames(id,listcolumns) +"\n";
     		System.out.print("	result_list  " + 	result_list  + " " );
     		
            }
		}
		  
		return result_list;
	}
	
	private String getListNames(int id,String[] listcolumns){
		String listname= "";

		Cursor cursor1 = ourDatabase.query(TABLE_LIST, listcolumns, KEY_LISTID + "=?" ,new String[]{String.valueOf(id)}, null,null, null, null); 
		System.out.print("	hello i out of cursor " + id + " " + cursor1);
		if(cursor1 != null  && cursor1.moveToFirst()){
        	listname = cursor1.getString(cursor1.getColumnIndex(KEY_LIST));
        }
		cursor1.close();
 		return listname;
 	
	}
	
 
	public String getDataItems(String itemValue) {
	
		int listid;
		String lname = itemValue;
		String result_list = "";;
		String[] itemcolumns = new String[] {KEY_ITEMID, KEY_ITEMNAME, KEY_LISTID};
		String[] listcolumns = new String[] {KEY_LISTID, KEY_LIST};
		
		Cursor listcursor = ourDatabase.query(TABLE_LIST, listcolumns, KEY_LIST + "=?" ,new String[]{lname}, null, null, null, null); 
		
		if(listcursor !=null){
			listcursor.moveToFirst();
			listid =  listcursor.getColumnIndex(KEY_LISTID);

		Cursor itemcursor = ourDatabase.query(TABLE_ITEMS, itemcolumns , KEY_LISTID + "=?" ,new String[]{String.valueOf(listcursor.getInt(listid))} , null, null, null, null); 
		if(itemcursor != null){
		
		
		int iItemName = itemcursor.getColumnIndex(KEY_ITEMNAME);
		
		for (itemcursor.moveToFirst(); !itemcursor.isAfterLast(); itemcursor.moveToNext())
			{
			result_list = result_list + itemcursor.getString(iItemName) +"\n";
			}
		}
		}
		return result_list;
	}
	
	public String getDataItemsView (int itemPosition) {
		
		
		String[] columns = new String[] {KEY_ITEMID, KEY_ITEMNAME, KEY_LISTID};
		Cursor cu = ourDatabase.query(TABLE_ITEMS, columns, KEY_LIST + "=" + itemPosition , null , null, null, null, null); 
		String result_list = "";
		int iID = cu.getColumnIndex(KEY_ITEMID);
		int iitemName = cu.getColumnIndex(KEY_ITEMNAME);
		int listid = cu.getColumnIndex(KEY_LISTID);
		
		for (cu.moveToFirst(); !cu.isAfterLast(); cu.moveToNext())
			{
			result_list = result_list + "List no: " +cu.getString(iID) + "\nList name: " + cu.getString(iitemName) + 
							"\nList Items: \n" +cu.getInt(listid) +"\n  ";
			}
		return result_list;
	}

	public void deleteList(String value) {

		ourDatabase.delete(TABLE_LIST, KEY_LIST + "=?", new String[]{value});
	
	}
	
	public void deleteuserIdAndList(String value) {

		ourDatabase.delete(TABLE_USERANDLISTID, KEY_LIST + "=?", new String[]{value});
	
	}
	//Deleting all items
	public void deleteAllItems()
	{
				ourDatabase =ourHelper.getWritableDatabase();
				ourDatabase.delete(TABLE_LIST, null, null);
				ourDatabase.delete(TABLE_USERANDLISTID, null, null);
				ourDatabase.delete(TABLE_ITEMS, null, null);
	}
	
}


