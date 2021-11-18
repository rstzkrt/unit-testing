package b_Money;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;

	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("Bank name doesn`t match","SweBank",SweBank.getName());
        assertEquals("Bank name doesn`t match","Nordea",Nordea.getName());
        assertEquals("Bank name doesn`t match","DanskeBank",DanskeBank.getName());
	}

	@Test
	public void testGetCurrency() {
        assertEquals("Bank currency doesnt match","SEK",SweBank.getCurrency().getName());
        assertEquals("Bank currency doesnt match","SEK",Nordea.getCurrency().getName());
        assertEquals("Bank currency doesnt match","DKK",DanskeBank.getCurrency().getName());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		try {
			SweBank.openAccount("Resat");
		} catch (AccountExistsException e) {
			fail("Account already exist");
		}

		try {
			SweBank.openAccount("Resat");
			fail("Account already exist");
			Nordea.openAccount("Bob");
			fail("Account already exist");
		} catch (AccountExistsException e) {
		}
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		assertEquals("Before deposit account balance doesnt match", Integer.valueOf(0), SweBank.getBalance("Bob"));
		SweBank.deposit("Bob", new Money(5_00, SEK));
		assertEquals("After deposit account balance doesnt match", Integer.valueOf(5_00), SweBank.getBalance("Bob"));


	}
	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		assertEquals("Balance doesnt match", Integer.valueOf(0), SweBank.getBalance("Ulrika"));

		SweBank.deposit("Ulrika", new Money(1000, SEK));
		assertEquals("Balance doesnt match after deposit", Integer.valueOf(1000), SweBank.getBalance("Ulrika"));
		SweBank.withdraw("Ulrika", new Money(1000, SEK));
		assertEquals("Ulrika after withdraw", Integer.valueOf(0), SweBank.getBalance("Ulrika"));
	}

	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		assertEquals("Balance doesnt match", Integer.valueOf(0), DanskeBank.getBalance("Gertrud"));
		try {
			assertEquals("Balance doesnt match", Integer.valueOf(0), DanskeBank.getBalance("unknown"));
		} catch (AccountDoesNotExistException e) {
		}
	}

	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		SweBank.deposit("Ulrika", new Money(1234_00, SEK));

		try {
			SweBank.transfer("Ulrika", DanskeBank, "unknown", new Money(1234_00, SEK));
			fail("Account doesnt exist");
			SweBank.transfer("unknown", DanskeBank, "Gertrud", new Money(1234_00, SEK));
			fail("Account doesnt exist");

		} catch (AccountDoesNotExistException e) {
		}
		SweBank.transfer("Ulrika", SweBank, "Bob", new Money(123400, SEK));
		assertEquals("Balances are not equal", Integer.valueOf(0), SweBank.getBalance("Ulrika"));
		assertEquals("Balances are not equal",Integer.valueOf(1234_00) , SweBank.getBalance("Bob"));
	}

	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {

		DanskeBank.deposit("Gertrud", new Money(123456_00, DKK));
		SweBank.deposit("Bob", new Money(12345_00, SEK));

		SweBank.addTimedPayment(
				"Bob", "payment", 1, 1, new Money(1234_00, SEK), DanskeBank, "Gertrud");
		assertEquals("0", Integer.valueOf(123456_00), DanskeBank.getBalance("Gertrud"));
		assertEquals("0", Integer.valueOf(12345_00), SweBank.getBalance("Bob"));
		SweBank.tick();
		assertEquals("1", Integer.valueOf(123456_00), DanskeBank.getBalance("Gertrud"));
		assertEquals("1", Integer.valueOf(12345_00), SweBank.getBalance("Bob"));
		SweBank.tick();
		assertEquals("2", Integer.valueOf(124381_50), DanskeBank.getBalance("Gertrud"));
		assertEquals("2", Integer.valueOf(11111_00), SweBank.getBalance("Bob"));
		SweBank.tick();
		assertEquals("3", Integer.valueOf(124381_50), DanskeBank.getBalance("Gertrud"));
		assertEquals("3", Integer.valueOf(11111_00), SweBank.getBalance("Bob"));

	}
}