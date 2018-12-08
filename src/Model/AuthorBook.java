package Model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorBook 
{

	private Author author;
	private Book book;
	private int royalty;
	private String royaltyFormatted;
	private boolean newRecord = true;
	
	public final static int ROYALTY_PRECISION = 100000;  // precision for royalty
	public final static int DECIMAL_POINTS = 5;
	public final static int HUNDRED_SCALE = 100;
	
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
		return BigDecimal.valueOf(royalty).movePointLeft(DECIMAL_POINTS);
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
		this.royaltyFormatted = NumberFormat.getPercentInstance().format(this.getRoyalty());
		
		return royaltyFormatted;
	}
	
	// Parses a formatted string for royalty
	public void setRoyaltyFormatted(String royaltyFormatted) throws Exception
	{
		// trim white space and the percent symbol
		String royalty = royaltyFormatted.trim().replaceAll("%", "");
		
		// validate input
		if(royalty.matches("[^\\d\\.]"))
		{
			throw new Exception("Invalid Royalty: Can only contain digits and percentage symbols");
		}
		
		// parse royalty numerical value
		Pattern royaltyPattern = Pattern.compile("(\\d+).(\\d+)");
		
		Matcher match = royaltyPattern.matcher(royalty);
		
		if(match.find())
		{
			// parse hundreds digits
			int hundred = Integer.parseInt(match.group(1));
			
			// parse tens digits
			int tens = Integer.parseInt(match.group(2));
			
			// set the new royalty
			this.royalty = (hundred * HUNDRED_SCALE) + tens;
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
