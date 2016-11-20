package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Eminda on 2016-11-20.
 */
public class PersistanceAccountDao implements AccountDAO,Serializable{
    private transient SQLiteDatabase database;

    public PersistanceAccountDao(SQLiteDatabase db){
        this.database = db;
    }
    @Override
    public List<String> getAccountNumbersList() {
        Cursor c = database.rawQuery("SELECT Account_no FROM ACCOUNT",null);
        List<String> accounts = new ArrayList<>();
        //Cursot always start at the end. we need to move it to the start
        if(c.moveToFirst()) {
            do {
                System.out.println(c+" dfgggg   "+c.getColumnCount());
                accounts.add(c.getString(0));
            } while (c.moveToNext());
        }
        return accounts;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor c = database.rawQuery("SELECT Account_no,Bank,Account_Holder,Amount FROM ACCOUNT",null);
        List<Account> accounts = new ArrayList<Account>();
        //Cursot always start at the end. we need to move it to the start
        if(c.moveToFirst()) {
            do {
                Account account = new Account(c.getString(0), c.getString(1), c.getString(2), c.getDouble(3));
                accounts.add(account);
            } while (c.moveToNext());
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor c = database.rawQuery("SELECT Account_no,Bank,Account_Holder,Amount FROM ACCOUNT WHERE Account_no = " + accountNo,null);
        Account account = null;

        if(c.moveToFirst()) {
            do {
                account = new Account(c.getString(0),c.getString(1),c.getString(2),c.getDouble(3));
            } while (c.moveToNext());
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {

        String sql = "INSERT INTO ACCOUNT (Account_no,Bank,Account_Holder,Amount) VALUES (?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());

        statement.executeInsert();


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql = "DELETE FROM ACCOUNT WHERE Account_no = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1,accountNo);
        statement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sql = "UPDATE ACCOUNT SET Amount= Amount ";
        if(expenseType == ExpenseType.EXPENSE){
            sql+="- ?";
        }else{
           sql+="+ ?";
        }
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindDouble(1,amount);
        statement.executeUpdateDelete();
    }
}
