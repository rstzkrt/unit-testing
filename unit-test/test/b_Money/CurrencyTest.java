package b_Money;

import static org.junit.Assert.*;

import b_Money.Currency;
import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;

	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("Names do not match","SEK",SEK.getName());
		assertEquals("Names do not match","DKK",DKK.getName());
		assertEquals("Names do not match","EUR",EUR.getName());
	}

	@Test
	public void testGetRate() {
		assertEquals("Rates don`t match",Double.valueOf(0.15),SEK.getRate());
		assertEquals("Rates don`t match",Double.valueOf(0.20),SEK.getRate());
		assertEquals("Rates don`t match",Double.valueOf(1.5),SEK.getRate());
	}

	@Test
	public void testSetRate() {
		SEK.setRate(0.23);
		DKK.setRate(0.30);
		EUR.setRate(0.44);
		assertEquals("Rates dont match",Double.valueOf(0.23),SEK.getRate());
		assertEquals("Rates dont match",Double.valueOf(0.30),DKK.getRate());
		assertEquals("Rates dont match",Double.valueOf(0.44),EUR.getRate());
	}

	@Test
	public void testGlobalValue() {
		assertEquals("Universal value dont match", Integer.valueOf(15), SEK.universalValue(100));
		assertEquals("Universal value dont match", Integer.valueOf(200), DKK.universalValue(1000));
		assertEquals("Universal value dont match", Integer.valueOf(15000), EUR.universalValue(10000));
	}

	@Test
	public void testValueInThisCurrency() {
		assertEquals("Value in this currency doesnt match", 300, SEK.valueInThisCurrency(30, EUR).intValue());
		assertEquals("Value in this currency doesnt match", 75, DKK.valueInThisCurrency(10, EUR).intValue());
		assertEquals("Value in this currency doesnt match", 200, SEK.valueInThisCurrency(150, DKK).intValue());
	}
}
