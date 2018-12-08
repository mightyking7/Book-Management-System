package Model;

import javafx.util.StringConverter;

/**
 * 
 * @author isaacbuitrago
 * @param <T>
 */
public class StringStringConverter<T> extends StringConverter<T> 
{

	@Override
	public String toString(T object) 
	{
		// TODO Auto-generated method stub
		return (String) object;
	}

	@Override
	public T fromString(String string)
	{
		// TODO Auto-generated method stub
		return (T) string;
	}

}
