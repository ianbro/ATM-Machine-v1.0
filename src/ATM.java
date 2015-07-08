import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JList;

/**
 * 
 */

/**
 * @author Ian
 * @date Feb 8, 2015
 * @project atm machine
 * @todo TODO
 */
public class ATM extends JFrame{

	public static ATM atm;
	public static Account usersAccount;
	public static ArrayList<Account> accounts = new ArrayList<>();
	
	//scanners and files
	public static File accountsFile;
	public static Scanner accountsReader;
	public static PrintWriter accountWriter;
	
	//main stuff
	private static JPanel headerPanel = new JPanel();
	private static JTabbedPane menu = new JTabbedPane(JTabbedPane.TOP);
	
	//login stuff
	private static JTextField nameField;
	private static JPasswordField passwordField;
	private static JButton btnOk;
	private static JPanel loginPanel = new JPanel();
	private static boolean loggedOn = false;
	private static int numberTries = 0;
	private static JButton btnLogout = new JButton("Logout");
	private static JButton btnNewAccount = new JButton("New Account");
	
	//transaction stuff
	private static JButton btnWithdrawal = new JButton("Withdrawal");
	private static JButton btnDeposit = new JButton("Deposit");
	private static JPanel transactionOptionMenu = new JPanel();
	private static JPanel transactionPanel = new JPanel();
	private static JPanel transactionPrompt = new JPanel();
	private static JTextField amountField = new JTextField();
	private static JButton transactionOKButton = new JButton("OK");
	private static JScrollPane activityPanel = new JScrollPane();
	private static JList list;
	
	//transfer stuff
	private static JTextField transferAmountField;
	private static JTextField reipiantField;
	private static JButton transferOkButton = new JButton("OK");
	
	//new account stuff
	private static JPanel newAccountPanel = new JPanel();
	private static JTextField newNameField;
	private static JPasswordField newConfirmField;
	private static JTextField startBalField;
	private static JPasswordField confirmField;
	private static JButton btnNewCancel;
	private static JButton btnNewOK;
	
	
	
	
	//GUI setup
	public ATM(){
		super("Ian's Bank");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		
		setMenu();
		setHeaderPanel();
		
		setVisible(true);
		setNewAccountPrompt();
	}
	/**
	 * creates the header panel
	 */
	private void setHeaderPanel() {
		
		headerPanel.setBounds(0, 0, 500, 75);
		headerPanel.setBackground(Color.CYAN);
		getContentPane().add(headerPanel);
		headerPanel.setLayout(null);
		btnLogout.setBackground(Color.CYAN);
		
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btnLogout.setBounds(428, 11, 62, 23);
		headerPanel.add(btnLogout);
		
	}
	
	private static void setuptransactionList(){
		activityPanel.invalidate();
			activityPanel.removeAll();
			
			list = new JList(usersAccount.transactions.toArray());
			
			list.setBounds(0, 0, 489, 344);
			list.setVisibleRowCount(20);
		activityPanel.revalidate();
		activityPanel.add(list);
	
		list = new JList(usersAccount.transactions.toArray());
	}

	/**
	 * sets tabbedMenu
	 */
	private void setMenu() {
		menu.setBorder(null);
		menu.setBackground(Color.CYAN);
		menu.setBounds(0, 51, 494, 420);
		getContentPane().add(menu);
		
		transactionPanel.setBorder(null);
		transactionPanel.setBackground(Color.WHITE);
		
		JPanel accountViewPanel = new JPanel();
		accountViewPanel.setBorder(null);
		accountViewPanel.setBackground(Color.WHITE);
		
		JPanel transferPanel = new JPanel();
		transferPanel.setBorder(null);
		transferPanel.setBackground(Color.WHITE);
		
		menu.addTab("Make Transaction", null, transactionPanel, null);
		transactionPanel.setLayout(null);
		setTransactionOptionMenu(transactionPanel);
		setTransactionPrompt(transactionPanel);
		
		menu.addTab("View Account", null, accountViewPanel, null);
		accountViewPanel.setLayout(null);
		activityPanel.setBackground(Color.WHITE);
		activityPanel.setBounds(0, 26, 489, 344);
		
		accountViewPanel.add(activityPanel);
		activityPanel.setLayout(null);
		
		menu.addTab("Make Transfer", null, transferPanel, null);
		transferPanel.setLayout(null);
		
		JPanel panel = setTransferPrompt(transferPanel);
		
		transferAmountField = new JTextField();
		transferAmountField.setColumns(10);
		transferAmountField.setBounds(90, 65, 100, 20);
		panel.add(transferAmountField);
		
		transferOkButton.setBounds(55, 96, 89, 23);
		panel.add(transferOkButton);
	}
	/**
	 * @param transferPanel
	 * @return
	 */
	private JPanel setTransferPrompt(JPanel transferPanel) {
		JPanel transferPrompt = new JPanel();
		transferPrompt.setLayout(null);
		transferPrompt.setBackground(Color.WHITE);
		transferPrompt.setBounds(150, 140, 200, 130);
		transferPanel.add(transferPrompt);
		
		JLabel amountLabel = new JLabel("Amount");
		amountLabel.setBounds(10, 63, 55, 25);
		transferPrompt.add(amountLabel);
		
		reipiantField = new JTextField();
		reipiantField.setBounds(90, 34, 100, 20);
		transferPrompt.add(reipiantField);
		reipiantField.setColumns(10);
		
		JLabel lblRecipiant = new JLabel("Recipiant");
		lblRecipiant.setBounds(10, 37, 70, 14);
		transferPrompt.add(lblRecipiant);
		return transferPrompt;
	}
	
	private static void setTransferAction(){
		ActionListener transferOKPressed = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Account recipiant = User.searchForUser(reipiantField, accounts);
				if(!(recipiant.user.getName().equals("none"))){
					//do transfer
					System.out.println("recipiant: " + recipiant.user.getName());
					
					recipiant.setupTransactionFile();
					performTransfer(recipiant);
					recipiant.closeAccountFiles();
					recipiant = null;
					reipiantField.setText(null);
					transferAmountField.setText(null);
				}
				else if(recipiant.user.getName().equals("none")){
					recipiant = null;
					reipiantField.setText(null);
					transferAmountField.setText(null);
				}
			}
		};
		
		if(transactionOKButton.getActionListeners().length < 1){
			transferOkButton.addActionListener(transferOKPressed);
		}
		else if(transactionOKButton.getActionListeners().length > 1){
			transactionOKButton.removeActionListener(transferOKPressed);
		}
	}
	
	private static void performTransfer(Account recipiant){
		try{
			recipiant.deposit(Double.valueOf(transferAmountField.getText()));
		} catch(Exception e) {
			JOptionPane.showMessageDialog(ATM.atm, "Please enter the amount to transfer");
			return;
		}
		usersAccount.withdrawal(Double.valueOf(transferAmountField.getText()));
	}
	/**
	 * @param transactionPanel
	 */
	private void setTransactionPrompt(JPanel transactionPanel) {
		JLabel lblAmount = new JLabel("Amount");
		
		amountField.setBounds(90, 38, 100, 20);
		amountField.setColumns(10);
		
		transactionPrompt.setBackground(Color.WHITE);
		transactionPrompt.setBounds(150, 140, 200, 130);
		
		transactionPanel.add(transactionPrompt);
		transactionPrompt.setLayout(null);
		lblAmount.setBounds(10, 36, 55, 25);
		
		transactionPrompt.add(lblAmount);
		
		transactionPrompt.add(amountField);
		transactionOKButton.setBounds(55, 72, 89, 23);
		
		transactionPrompt.add(transactionOKButton);
	}
	/**
	 * @param transactionPanel
	 */
	private void setTransactionOptionMenu(JPanel transactionPanel) {
		transactionOptionMenu.setBackground(new Color(0, 255, 255));
		transactionOptionMenu.setBounds(0, 0, 127, 72);
		transactionPanel.add(transactionOptionMenu);
		transactionOptionMenu.setLayout(null);
		
		btnDeposit.setBackground(Color.CYAN);
		btnDeposit.setBounds(0, 11, 127, 23);
		transactionOptionMenu.add(btnDeposit);
		
		btnWithdrawal.setBackground(Color.CYAN);
		btnWithdrawal.setBounds(0, 38, 127, 23);
		transactionOptionMenu.add(btnWithdrawal);
		transactionOptionMenu.setVisible(false);
	}
	
	private void setLogin() {
		loginPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		loginPanel.setBounds(150, 100, 200, 130);
		loginPanel.setLayout(null);
		
		JPanel loginTitle = new JPanel();
		loginTitle.setBackground(Color.CYAN);
		loginTitle.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		loginTitle.setBounds(0, 0, 200, 25);
		loginPanel.add(loginTitle);
		loginTitle.setLayout(null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(83, 4, 34, 16);
		lblLogin.setFont(new Font("Tahoma", Font.BOLD, 13));
		loginTitle.add(lblLogin);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(25, 39, 46, 14);
		loginPanel.add(lblName);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(25, 64, 65, 14);
		loginPanel.add(lblPassword);
		
		nameField = new JTextField();
		nameField.setBounds(93, 36, 86, 20);
		loginPanel.add(nameField);
		nameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(93, 61, 86, 20);
		loginPanel.add(passwordField);
		
		btnOk = new JButton("OK");
		btnOk.setBounds(10, 95, 60, 23);
		loginPanel.add(btnOk);
		
		btnNewAccount.setBounds(75, 95, 115, 23);
		loginPanel.add(btnNewAccount);
		
		loginPanel.setVisible(false);
		transactionPanel.add(loginPanel);
	}
	
	private void setNewAccountPrompt(){
		newAccountPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		newAccountPanel.setBounds(150, 100, 200, 180);
		newAccountPanel.setLayout(null);
		newAccountPanel.setBackground(Color.WHITE);
		
		newNameField = new JTextField();
		newNameField.setText("Firstname Lastname");
		newNameField.setBounds(54, 11, 136, 20);
		newAccountPanel.add(newNameField);
		newNameField.setColumns(10);
		
		JLabel lblnewName = new JLabel("Name:");
		lblnewName.setBounds(10, 14, 46, 14);
		newAccountPanel.add(lblnewName);
		
		JLabel lblBeginningBalance = new JLabel("Start Balance:");
		lblBeginningBalance.setBounds(10, 46, 97, 14);
		newAccountPanel.add(lblBeginningBalance);
		
		JLabel lblNewPassword = new JLabel("Password:");
		lblNewPassword.setBounds(10, 77, 73, 14);
		newAccountPanel.add(lblNewPassword);
		
		JLabel lblConfirm = new JLabel("Confirm:");
		lblConfirm.setBounds(10, 108, 86, 14);
		newAccountPanel.add(lblConfirm);
		
		btnNewOK = new JButton("OK");
		btnNewOK.setBounds(10, 147, 73, 23);
		newAccountPanel.add(btnNewOK);
		
		btnNewCancel = new JButton("Cancel");
		btnNewCancel.setBounds(104, 147, 86, 23);
		newAccountPanel.add(btnNewCancel);
		
		newConfirmField = new JPasswordField();
		newConfirmField.setBounds(104, 74, 86, 20);
		newAccountPanel.add(newConfirmField);
		
		startBalField = new JTextField();
		startBalField.setBounds(104, 42, 86, 20);
		newAccountPanel.add(startBalField);
		startBalField.setColumns(10);
		startBalField.setText("Minimum $50");
		
		confirmField = new JPasswordField();
		confirmField.setBounds(104, 105, 86, 20);
		newAccountPanel.add(confirmField);
		
	}
	
	
	
	
	//login stuff
	private static void showLogin(){
		passwordField.setText(null);
		
		atm.getContentPane().invalidate();
		transactionPrompt.setVisible(false);
		transactionOptionMenu.setVisible(false);
		atm.getContentPane().revalidate();
		loginPanel.setVisible(true);
	}
	
	private static void login(){
		showLogin();
		ActionListener loginDone = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				accountsToArrayList();
				usersAccount = User.searchForUser(nameField, accounts);
				
				if(usersAccount.user.getName() == "none"){
					return;
				}
				
				loggedOn = usersAccount.user.passwordIsCorrect(passwordField);
				
				if(loggedOn == true){
					
					nameField.setText(null);
					passwordField.setText(null);
					transactionOptionMenu.setVisible(true);
					
					usersAccount.setupTransactionFile();
					ATM.setuptransactionList();
					ATM.closeLogin();
					setLogoutAction();
					setTransferAction();
					return;
				}
				else{
					JOptionPane.showConfirmDialog(atm, "Sorry, the password is not correct.");
					passwordField.setText(null);
					numberTries ++;
					if(numberTries >= 3){
						System.exit(0);
					}
				}
			}
		};
		
		if((btnOk.getActionListeners().length < 1)){
			btnOk.addActionListener(loginDone);
		}
		if(btnOk.getActionListeners().length > 1){
			System.out.println("btnOk: " + btnOk.getActionListeners().length);
			btnOk.removeActionListener(loginDone);
			System.out.println("btnOk: " + btnOk.getActionListeners().length);
		}
	}
	
	private static void promptForLogon(){
		atm.setLogin();
	}
	
	private static void closeLogin(){
		atm.getContentPane().invalidate();
		loginPanel.setVisible(false);
		transactionOptionMenu.setVisible(true);
		atm.getContentPane().revalidate();
	}
	
	private static void openNewAccountPrompt(){
		transactionPanel.invalidate();
		loginPanel.setVisible(false);
		transactionPanel.revalidate();
		
		transactionPanel.add(newAccountPanel);
		newAccountPanel.setVisible(true);
		
		ATM.createNewAccount();
	}
	
	private static void closeNewAccountPrompt(){
		transactionPanel.invalidate();
		newAccountPanel.setVisible(false);
		transactionPanel.revalidate();
	}
	
	private static void createNewAccount(){
		ActionListener btnNewOKPressed = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try{
					double startAmount = Double.valueOf(startBalField.getText());
					if(User.getGuessPassword(newConfirmField).equals(User.getGuessPassword(confirmField))){
						if(startAmount >= 50){
							accountsToArrayList();
							
							accounts.add(new Account(newNameField.getText(), startAmount, User.getGuessPassword(newConfirmField)));
							closeNewAccountPrompt();
							transactionOptionMenu.setVisible(true);
							
							usersAccount = accounts.get(accounts.size() - 1);
							setLogoutAction();
							
							usersAccount.setupTransactionFile();
							setTransferAction();
							newNameField.setText(null);
							startBalField.setText(null);
							confirmField.setText(null);
							newConfirmField.setText(null);
						}
						else{
							JOptionPane.showMessageDialog(atm, "Please deposit a minimum of $50.");
							startBalField.setText("Minimum $50");
						}
					}
					else{
						JOptionPane.showMessageDialog(atm, "Sorry, the password confirmation does not match the first.");
						confirmField.setText(null);
					}
					
				} catch (Exception exn) {
					JOptionPane.showMessageDialog(atm, "Please fill out all of the blanks.");
				}
			}
			
		};
		
		ActionListener btnNewCancelPressed = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO set action event for cancel
				atm.getContentPane().invalidate();
				newAccountPanel.setVisible(false);
				atm.getContentPane().revalidate();
				
				showLogin();
			}
		};
		
		btnNewOK.addActionListener(btnNewOKPressed);
		btnNewCancel.addActionListener(btnNewCancelPressed);
		
		if(btnNewOK.getActionListeners().length > 1){
			btnNewOK.removeActionListener(btnNewOKPressed);
		}if(btnNewCancel.getActionListeners().length > 1){
			btnNewCancel.removeActionListener(btnNewCancelPressed);
		}
	}
	
	private static void setNewAccountBtnAction(){
		ActionListener wantsNewAccount = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ATM.openNewAccountPrompt();
			}
			
		};
		
		if(btnNewAccount.getActionListeners().length < 1){
			btnNewAccount.addActionListener(wantsNewAccount);
		}
		if(btnNewAccount.getActionListeners().length > 1){
			btnNewAccount.removeActionListener(wantsNewAccount);
		}
	}
	
	private static ActionListener btnLogoutPressed;
	private static void setLogoutAction(){
		btnLogoutPressed = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				usersAccount.closeAccountFiles();
				
				int size = usersAccount.transactions.size();
				System.out.println(size);
				for(int i = 0; i < size; i ++){
					System.out.println(usersAccount.transactions.get(i));
				}
				usersAccount.transactions = null;
				ATM.closeTransactionList();
				usersAccount = null;
				menu.setSelectedComponent(transactionPanel);
				ATM.login();
				
			}
			
		};
		
		if((btnLogout.getActionListeners().length < 1)){
			btnLogout.addActionListener(btnLogoutPressed);
		}
		if(btnLogout.getActionListeners().length > 1){
			System.out.println("btnLogout: " + btnLogout.getActionListeners().length);
			btnLogout.removeActionListener(btnLogoutPressed);
			System.out.println("btnLogout: " + btnLogout.getActionListeners().length);
		}
		
	}
	
	
	
	
	//setting up accounts
	private static void setAccountListFile(){
		Path accountsFilePath = Paths.get("accounts.txt");
		try {
			Files.createFile(accountsFilePath);
		} catch (IOException e1) {}
		
			accountsFile = new File("accounts.txt");
		
		try {
			ATM.accountsReader = new Scanner(ATM.accountsFile);
			ATM.accountsReader.useDelimiter("[,|\n|\r]+");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "sorry, could not find file " + ATM.accountsFile.getName());
		}
	}
	
	private static void accountsToArrayList(){
		if(accounts.isEmpty()){
			setAccountListFile();
		}
		
		boolean alreadyHasAccount;
		while(accountsReader.hasNext()){
			Account accountToAdd = new Account(accountsReader.next(), accountsReader.nextDouble(), accountsReader.next());
			
			alreadyHasAccount = false;
			for(int i = 0; i < accounts.size(); i ++){
				if(accountToAdd.user.getName().equals(accounts.get(i).user.getName())){
					alreadyHasAccount = true;
				}
			}
			
			if(alreadyHasAccount == false){
				accounts.add(accountToAdd);
			}
		}
		
		
		try{
			accountWriter = new PrintWriter(accountsFile);
		} catch(IOException e) {
			JOptionPane.showMessageDialog(atm, "Sorry, could not find file: accounts.txt");
		}
	}
	
	

	
	//transaction stuff
	public String transOption = null;
	private static void getDepOrWithDrawal(){
		ActionListener depositPressed = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				atm.getContentPane().invalidate();
					transactionOptionMenu.setVisible(false);
				atm.getContentPane().revalidate();
				transactionPrompt.setVisible(true);
				
				atm.transOption = "deposit";
				getTransactionAmount();
			}
			
		};
		
		ActionListener withDrawalPressed = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				atm.getContentPane().invalidate();
					transactionOptionMenu.setVisible(false);
				atm.getContentPane().revalidate();
				transactionPrompt.setVisible(true);
				
				atm.transOption = "withdrawal";
				getTransactionAmount();
			}
			
		};
		
		btnDeposit.addActionListener(depositPressed);
		btnWithdrawal.addActionListener(withDrawalPressed);
	}
	
	private static void closeTransactionList(){
		activityPanel.invalidate();
			activityPanel.removeAll();
			
			list = new JList(new String[1]);
			
			list.setBounds(0, 0, 489, 344);
			list.setVisibleRowCount(20);
		activityPanel.revalidate();
		activityPanel.add(list);
	}

	private static void getTransactionAmount(){
		
		ActionListener transactionDone = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(atm.transOption.equals("deposit")){
					
					double amountDep = 0;
					try{
						amountDep = Double.valueOf(amountField.getText());
					} catch (Exception f) {
						JOptionPane.showMessageDialog(atm, "Please enter the amount you want to deposit.");
					}
					
					usersAccount.deposit(amountDep);
					amountField.setText(null);
					transactionOptionMenu.setVisible(true);
					atm.getContentPane().invalidate();
						transactionPrompt.setVisible(false);
					atm.getContentPane().revalidate();
				}
				else if(atm.transOption.equals("withdrawal")){
					double amountWith = 0;
					try{
						amountWith = Double.valueOf(amountField.getText());
					} catch (Exception f){
						JOptionPane.showMessageDialog(atm, "Please enter the amount you want to withdrawal.");
					}
					
					usersAccount.withdrawal(amountWith);
					amountField.setText(null);
					transactionOptionMenu.setVisible(true);
					atm.getContentPane().invalidate();
						transactionPrompt.setVisible(false);
					atm.getContentPane().revalidate();
				}
				else{
					JOptionPane.showMessageDialog(atm.getContentPane(), "Sorry, Please select deposit or withdrawal.");
				}
			}

			
			
		};
		
		transactionOKButton.addActionListener(transactionDone);
		if(transactionOKButton.getActionListeners().length > 1){
			transactionOKButton.removeActionListener(transactionDone);
		}
		
	}

	
	
	
	//executing meathods
	private static void startUp(){
		atm = new ATM();
		ATM.promptForLogon();
		ATM.setNewAccountBtnAction();
		ATM.login();
	}
	
	private static void executeMainPart(){
		/**
		 * after initial login
		 */
		getDepOrWithDrawal();
	}
	
	public static void main(String[] args) {
		ATM.startUp();
		executeMainPart();
	}
}
