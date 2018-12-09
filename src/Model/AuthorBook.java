package Model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents Many to Many association between Books
 * and their associated author.
 *
 * @author isaacbuitrago
 */
public class AuthorBook 
{

	private Author author;
	private Book book;
	private int royalty;
	private String royaltyFormatted;
	private boolean newRecord = true;
	
	// constants related to displaying the royalty
	public final static int ROYALTY_PRECISION = 100000;  // precision for royalty
	public final static int MAX_DECIMAL= 999;
	public final static int DECIMAL_POINTS_LEFT= 5;
	public final static int DECIMAL_POINTS_RIGHT= 3;
	public final static int DECIMAL_POINTS_PERCENT= 3;
	public final static int HUNDRED = 100;
	
	public AuthorBook()
	{
		author = null;
		book = null;
		royalty = 0;
		royaltyFormatted = new String();
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	public BigDecimal getRoyalty()
	{	
		return BigDecimal.valueOf(royalty).movePointLeft(DECIMAL_POINTS_LEFT);
	}
	
	public void setRoyalty(int royalty) 
	{
		this.royalty = royalty;
	}
	
	public void setRoyalty(BigDecimal newValue) 
	{
		this.royalty = (int) (newValue.doubleValue() * ROYALTY_PRECISION);
	}

	public boolean isNewRecord() {
		return newRecord;
	}

	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}
	
	public String getRoyaltyFormatted()
	{
		DecimalFormat df = new DecimalFormat("00.000");
		
		BigDecimal big = BigDecimal.valueOf(this.royalty).movePointLeft(DECIMAL_POINTS_PERCENT);
		
		df.setRoundingMode(RoundingMode.DOWN);
		
		this.royaltyFormatted = String.format("%s %%", df.format(big.doubleValue()));
		
		return royaltyFormatted;
	}
	
	// Parses a formatted string for royalty
	public void setRoyaltyFormatted(String royaltyFormatted) throws NumberFormatException
	{
		// trim white space and the percent symbol
		String royalty = royaltyFormatted.replaceAll("%", "").trim();
		
		// validate the royalty amount
		validateRoyalty(royalty);
			
		BigDecimal big = new BigDecimal(royalty);
			
		this.royalty = big.movePointRight(DECIMAL_POINTS_RIGHT).intValue();
	}
	
	
	/**
	 * Validates digits received from input 
	 * 
	 * @param royalty received from input with symbols and spaces stripped
	 * 
	 * @throws NumberFormatException if royalty contains invalid characters,
	 * 								 is larger than 100, or has more than three 
	 * 								 digits of precision.
	 */
	private void validateRoyalty(String royalty) throws NumberFormatException
	{
		// throw exception if royalty contains anything other than a number or period
		if(royalty.matches(".+[^\\d\\.]"))
		{
			throw new NumberFormatException("Royalty can only contain digits");
		}
				
		// parse royalty numerical value
		Pattern royaltyPattern = Pattern.compile("(\\d+)\\.?(\\d+)?");
				
		Matcher match = royaltyPattern.matcher(royalty);
				
		if(match.find())
		{
			// parse hundreds digits
			String tenOrHundred = match.group(1);
					
			String decimal = match.group(2);
					
			int th = Integer.parseInt(tenOrHundred);
					
			int d;
					
			// parse decimal digits if given
			if(decimal != null)
			{
				d = Integer.parseInt(decimal);
			}
			else
			{
				decimal = "0";
				d = 0;
			}
					
			// validate that th is <= 100 and d <= 999.
			if((th == HUNDRED && d != 0) || ! (th <= HUNDRED) || ! (d <= MAX_DECIMAL))
				throw new NumberFormatException(String.format("Royalty must be less than or equal to %d\n and contain a maximum of 3 digits of precision.", HUNDRED));	
		}
		else
		{
			throw new NumberFormatException("Invalid royalty");
		}
	}
	
	/**
	 * Used to return the string representation of a Book
	 */
	@Override
	public String toString()
	{
		return String.format("%s %s", this.getAuthor().getFirstName(), this.getAuthor().getLastName());
	}

}
