package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistanceAccountDao;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistanceTransactionDao;

/**
 * Created by Eminda on 2016-11-20.
 */
public class PersistanceExpenseManager extends ExpenseManager implements Serializable{
    private transient Context context;

    public PersistanceExpenseManager(Context context){
        this.context = context;
        setup();
    }

    @Override
    public void setup(){

        SQLiteDatabase database = context.openOrCreateDatabase("140108C", context.MODE_PRIVATE, null);

        database.execSQL("CREATE TABLE IF NOT EXISTS ACCOUNT(" +
                "Account_no VARCHAR PRIMARY KEY," +
                "Bank VARCHAR," +
                "Account_Holder VARCHAR," +
                "Amount REAL" +
                " );");


        database.execSQL("CREATE TABLE IF NOT EXISTS TRANSACTION_DATA(" +
                "id INTEGER PRIMARY KEY autoincrement," +
                "Account_no VARCHAR," +
                "Type int," +
                "Date_Entered int,"+
                "Amount REAL," +
                "FOREIGN KEY (Account_no) REFERENCES ACCOUNT(Account_no)" +
                ");");


        PersistanceAccountDao accountDAO = new PersistanceAccountDao(database);
        setAccountsDAO(accountDAO);
        PersistanceTransactionDao transactionDao=new PersistanceTransactionDao(database);
        setTransactionsDAO(transactionDao);
    }
}
