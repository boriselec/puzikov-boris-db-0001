

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.acme.bankapp.domain.bank.CheckingAccount;
import com.acme.bankapp.domain.bank.NegativeArgumentException;
import com.acme.bankapp.domain.bank.NotEnoughFundsException;
import com.acme.bankapp.domain.bank.OverDraftLimitExceededException;
import com.acme.bankapp.domain.bank.SavingAccount;

public class AccountTest {

	@Before
	public void setup(){
		
	}
	@Test(expected = NegativeArgumentException.class)
	public void checkingAccountthrowExceptionWhenBalanceIsNegative() throws NegativeArgumentException {
		CheckingAccount checkingAccount = new CheckingAccount(0, -10, 10);
	}
	@Test(expected = NegativeArgumentException.class)
	public void savingAccountthrowExceptionWhenBalanceIsNegative() throws NegativeArgumentException {
		SavingAccount savingAccount = new SavingAccount(0, -10);
	}
	@Test(expected = NegativeArgumentException.class)
	public void checkingAccountthrowExceptionWhenOverdrawIsNegative() throws NegativeArgumentException {
		CheckingAccount checkingAccount = new CheckingAccount(0, 10, -10);
	}
	@Test(expected = OverDraftLimitExceededException.class)
	public void withdrawCheckingAccountthrowExceptionWhenCantWithdraw() 
			throws OverDraftLimitExceededException, NegativeArgumentException {
		CheckingAccount checkingAccount = new CheckingAccount(0, 10, 10);
		checkingAccount.withdraw(100);
	}
	@Test(expected = NotEnoughFundsException.class)
	public void withdrawSavingAccountthrowExceptionWhenCantWithdraw() 
			throws NotEnoughFundsException, NegativeArgumentException {
		SavingAccount savingAccount = new SavingAccount(0, 10);
		savingAccount.withdraw(100);
	}
	@Test
	public void balanceIsOkWhenConstructorWorked() throws NegativeArgumentException{
		try{
		SavingAccount savingAccount = new SavingAccount(0, 10);
		CheckingAccount checkingAccount = new CheckingAccount(0, 10, 10);
		
		assertEquals(savingAccount.getBalance(), 10, 0.000001);
		assertEquals(checkingAccount.getBalance(), 10, 0.000001);
		}
		catch (Exception e){
			assertTrue(false);
		}
		
	}

}
