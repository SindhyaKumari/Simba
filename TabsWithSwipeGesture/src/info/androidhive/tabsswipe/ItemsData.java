package info.androidhive.tabsswipe;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

public class ItemsData extends Activity{

	public static final String KEY_ITEM = "item_name";
	public static final String KEY_LISTID = "list_id";
	public static final String KEY_LIST = "list_name";
	
	private static final String DATABASE_NAME = "ShoppingListDB";
	private static final String DATABASE_TABLE1 = "ListTable";
	private static final int DATABASE_VERSION = 7;

	private DBHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
	
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
	
			db.execSQL( "CREATE TABLE " + DATABASE_TABLE1+ " (" + KEY_LISTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_LIST + " TEXT NOT NULL UNIQUE, " + KEY_ITEM + " TEXT );"
		);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE1);
			onCreate(db);
		}}
	
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

	public long createListEntry(String listname, String itemname) {
	
		ContentValues cv_2 = new ContentValues();
		cv_2.put(KEY_LIST, listname);
		cv_2.put(KEY_ITEM, itemname);
		return ourDatabase.insert(DATABASE_TABLE1, null, cv_2);
	}
	
	public int getCountDataList() {
	
		String[] columns = new String[] {KEY_LISTID, KEY_LIST};
		Cursor cu = ourDatabase.query(DATABASE_TABLE1, columns, KEY_LISTID, null, null, null, null); 
		int result_list = cu.getCount();
		return result_list;
	}

	public String getDataList(int i) {
	
		
		String[] columns = new String[] {KEY_LISTID, KEY_LIST};
		Cursor cu = ourDatabase.query(DATABASE_TABLE1, columns, KEY_LISTID + "=" + i, null, null, null, null); 
		if (cu != null)
		{
			cu.moveToFirst();
			String name = cu.getString(1);
			return name;
		}
		return null;
	}
	
 
	public String getDataItems(int itemPosition, String itemValue) {
	
		itemPosition = itemPosition + 1; 
		String lname = itemValue;
		String[] columns = new String[] {KEY_LISTID, KEY_LIST, KEY_ITEM};
		Cursor cu = ourDatabase.query(DATABASE_TABLE1, columns, KEY_LISTID + "=" + itemPosition +" and " + KEY_LIST + "=?",  
		new String[] {lname} , null, null, null, null); 
		String result_list = "";
		int iID = cu.getColumnIndex(KEY_LISTID);
		int iListName = cu.getColumnIndex(KEY_LIST);
		int iItemName = cu.getColumnIndex(KEY_ITEM);
		
		for (cu.moveToFirst(); !cu.isAfterLast(); cu.moveToNext())
			{
			result_list = result_list + cu.getString(iID) + " " + cu.getString(iListName) + cu.getString(iItemName) +"\n";
			}
		return result_list;
	}
	
	public String getDataItemsView (String itemValue) {
		
		String lname = itemValue;
		
		String[] columns = new String[] {KEY_LISTID, KEY_LIST, KEY_ITEM};
		Cursor cu = ourDatabase.query(DATABASE_TABLE1, columns, KEY_LIST + "=?", new String[] {lname} , null, null, null, null); 
		String result_list = "";
		int iID = cu.getColumnIndex(KEY_LISTID);
		int iListName = cu.getColumnIndex(KEY_LIST);
		int iItemName = cu.getColumnIndex(KEY_ITEM);
		
		for (cu.moveToFirst(); !cu.isAfterLast(); cu.moveToNext())
			{
			result_list = result_list + "List no: " +cu.getString(iID) + "\nList name: " + cu.getString(iListName) + 
							"\nList Items: \n" +cu.getString(iItemName) +"\n  ";
			}
		return result_list;
	}


	
}


