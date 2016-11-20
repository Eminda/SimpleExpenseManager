package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Eminda on 2016-11-20.
 */
public class PersistanceTransactionDao implements TransactionDAO,Serializable{
    private transient SQLiteDatabase database;

    public PersistanceTransactionDao(SQLiteDatabase db){
        this.database = db;
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String sql = "INSERT INTO TRANSACTION_DATA (Account_no,Type,Date_Entered,Amount) VALUES (?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1,accountNo);
        statement.bindLong(2,ExpenseType.EXPENSE==expenseType?0:1);
        statement.bindLong(3,date.getTime());
        statement.bindDouble(4,amount);


        statement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor c = database.rawQuery("SELECT Account_no,Type,Date_Entered,Amount FROM TRANSACTION_DATA",null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(c.moveToFirst()) {
            do{
                Transaction t = new Transaction(new Date(c.getLong(2)),c.getString(0),c.getLong(1)==0?ExpenseType.EXPENSE:ExpenseType.INCOME,c.getDouble(3));
                transactions.add(t);
            }while (c.moveToNext());
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor c = database.rawQuery("SELECT Account_no,Type,Date_Entered,Amount FROM TRANSACTION_DATA limit " + limit,null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(c.moveToFirst()) {
            do {
                Transaction t = new Transaction(new Date(c.getLong(2)),c.getString(0),c.getLong(1)==0?ExpenseType.EXPENSE:ExpenseType.INCOME,c.getDouble(3));
                transactions.add(t);
            } while (c.moveToNext());
        }

        return transactions;
    }
}
