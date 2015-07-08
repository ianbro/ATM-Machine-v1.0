import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * @author Ian
 * @date Feb 4, 2015
 * @project bank_program_version1
 * @todo TODO
 */
public class Account {

	private double balance;
	public User user;
	
	private File transactionFile;
	private Scanner transactionReader;
	private PrintWriter transactionWriter;
	public ArrayList<String> transactions;
	
	public Account(){}
	
	public Account(String name, double balance, String password){
		this.user = new User(name, password);
		this.balance = balance;
	}
	
	public Account(String name){
		this.user = new User(name);
	}
	
	public String toString(){
		return (this.user.getName() + "," + this.balance + "," + this.user.getPassword());
	}
	
	public void deposit(double amount){
		System.out.println(this.balance);
		this.balance += amount;
		transactions.add(formatDate(new Date()) + " ----- Deposit: $" + amount + ", Starting Balance: " + Double.valueOf(this.balance - amount) + ", new Balance: " + this.balance);
		System.out.println(transactions.get(transactions.size() -1));
	}
	
	public void withdrawal(double amount){
		String passGuess = JOptionPane.showInputDialog("Password:");
		if(passGuess.equals(this.user.getPassword())){
			System.out.println(this.balance);
			this.balance -= amount;
			transactions.add(formatDate(new Date()) + " ----- Withdrawal: $" + amount + ", Starting Balance: " + Double.valueOf(this.balance + amount) + ", new Balance: " + this.balance);
			System.out.println(transactions.get(transactions.size() -1));
		}
		else{
			JOptionPane.showMessageDialog(ATM.atm, "Sorry, that is incorrect.");
		}
	}
	
	//transaction stuff
	public void setupTransactionFile(){
		createNewAccountFile();
		
		try{
			transactionReader = new Scanner(transactionFile);
		} catch(IOException e) {
			JOptionPane.showConfirmDialog(ATM.atm, "Sorry, could not find file transactions.txt");
		}
		
		transactions = new ArrayList<String>();
		while(transactionReader.hasNext()){
			transactions.add(transactionReader.nextLine());
		}
		
		transactionReader.close();
		
		try{
			transactionWriter = new PrintWriter(transactionFile);
		} catch(IOException e){
			JOptionPane.showConfirmDialog(ATM.atm, "Sorry, could not find file transactions.txt");
		}
	}
	
	public static String formatDate(Date date){
		String newDate;
		String month;
		int monthNum;
		int dateNum;
		int year;
		newDate = date.toString();
		month = newDate.substring(4,7);
		
			switch(month){
			case "Jan":
				monthNum = 1;
				break;
			case "Feb":
				monthNum = 2;
				break;
			case "Mar":
				monthNum = 3;
				break;
			case "Apr":
				monthNum = 4;
				break;
			case "May":
				monthNum = 5;
				break;
			case "Jun":
				monthNum = 6;
				break;
			case "Jul":
				monthNum = 7;
				break;
			case "Aug":
				monthNum = 8;
				break;
			case "Sep":
				monthNum = 9;
				break;
			case "Oct":
				monthNum = 10;
				break;
			case "Nov":
				monthNum = 11;
				break;
			case "Dec":
				monthNum = 12;
				break;
			default:
				JOptionPane.showMessageDialog(ATM.atm, "Error found in the month. Class User, Line: " + Thread.currentThread().getStackTrace()[1].getLineNumber());
				monthNum = 0;
				break;
			}
		
		dateNum = Integer.valueOf(newDate.substring(8,10));
		year = Integer.valueOf(newDate.substring(24,28));
		
		newDate = (monthNum + "/" + dateNum + "/" + year);
		
		return newDate;
	}
	
	private void createNewAccountFile(){
		Path newFilePath = Paths.get(this.user.getName() + ".txt");
		try {
			Files.createFile(newFilePath);
		} catch (IOException e) {}
		
		transactionFile = new File(this.user.getName() + ".txt");
	}
	
	public void closeAccountFiles(){
		int transSize = transactions.size();
		for(int i = 0; i < transSize; i ++){
			transactionWriter.println(transactions.get(i));
		}
		
		int numAccounts = ATM.accounts.size();
		for(int i = 0; i < numAccounts; i ++){
			ATM.accountWriter.println(ATM.accounts.get(i).toString());
		}
		
		transactionWriter.close();
		ATM.accountWriter.close();
	}
	
	public void transfer(double amount, Account recipiant){
		
	}
}
