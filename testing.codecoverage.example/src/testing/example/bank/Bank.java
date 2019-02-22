package testing.example.bank;

import java.util.Collection;
import java.util.NoSuchElementException;

public class Bank {

	private Collection<BankAccount> bankAccounts;

	public Bank(Collection<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

	public int openNewBankAccount(double initialBalance) {
		BankAccount newBankAccount = new BankAccount();
		newBankAccount.setBalance(initialBalance);
		bankAccounts.add(newBankAccount);
		return newBankAccount.getId();
	}

	public void deposit(int bankAccountId, double amount) {
		findBankAccountById(bankAccountId).deposit(amount);
	}

	public void withdraw(int bankAccountId, double amount) {
		findBankAccountById(bankAccountId).withdraw(amount);
	}

	private BankAccount findBankAccountById(int bankAccountId) {
		return bankAccounts.stream()
			.filter(a -> a.getId() == bankAccountId)
			.findFirst()
			.orElseThrow(
				() -> new NoSuchElementException
					("No account found with id: " + bankAccountId));
	}
}
