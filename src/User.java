	import java.io.File;
	import java.io.FileNotFoundException;
	import java.util.ArrayList;
	import java.util.Scanner;

	import javax.swing.JOptionPane;
	import javax.swing.JPasswordField;
	import javax.swing.JTextField;

	/**
	 * @author Ian
	 * @date Jan 27, 2015
	 * @project bank_program_version1
	 * @todo TODO
	 */
	public class User {

		private String name;
		private String password;
		
		public User(String name, String password){
			this.name = name;
			this.password = password;
		}
		
		public User(String name){
			this.name = name;
		}
		
		public String getName(){
			return this.name;
		}
		
		public void setPassword(String newPass){
			password = newPass;
		}
		
		public String getPassword(){
			return password;
		}
		
		public static String getGuessPassword(JPasswordField passBlank){
			char[] guess = passBlank.getPassword();
			String pass = "";
			for(int i = 0; i < guess.length; i ++){
				pass = pass + guess[i];
			}
			
			return pass;
		}
		
		public boolean passwordIsCorrect(JPasswordField passBlank){
			String guess = getGuessPassword(passBlank);
			
			if(guess.equals((this.password))){
				return true;
			}
			return false;
		}
		
		public void setName(String name1){
			name = name1;
		}
		
		public static Account searchForUser(JTextField nameSpace, ArrayList<Account> accounts){
			for(int i = 0; i < accounts.size(); i ++){
				if(accounts.get(i).user.name.equals(nameSpace.getText())){
					return accounts.get(i);
				}
			}
			
			JOptionPane.showConfirmDialog(ATM.atm, "Sorry, this account could not be found.");
			
			return new Account("none");
		}
	}
