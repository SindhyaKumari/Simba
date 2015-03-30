package info.androidhive.tabsswipe;

import java.util.regex.Pattern;
import android.app.Activity;
import android.os.Bundle;

public class StringSplitter 
{

	//ResultsActivity dis = new ResultsActivity();
	String product;
	
	StringSplitter(String prod)
	{
		product = prod;
	}
	
	public String Splitter ()
	{
		boolean check;
		int i;
		
		Pattern spaces = Pattern.compile("[\t ]+");
		Pattern emptyLines = Pattern.compile("^\\s+$?", Pattern.MULTILINE);
		Pattern newlines = Pattern.compile("\\s*\\n+");
		
		product = newlines.matcher(emptyLines.matcher(spaces.matcher(product).replaceAll(" ")).replaceAll("")).replaceAll("\n");
	    
	    System.out.println(product); //checking if print the product_ string without spaces etc
		
	    String newProducts = "" ;
		String[] product_ = product.split("\n");
	    String[] temp ;
	    System.out.print("sze " + product_.length);
	    
	    for( i = 9 ; i < product_.length; i++)
	    {
	    	check = false;
	    	temp = product_[i].split(" ");  // temp =  Coke Can 150ml 65.00 T00 65.00 ;
	    	
	    	for(int j = 0 ; j < temp.length && !check  ;j++)
	    	{
	    		// when we find 150ml check forward value is  based on numeric or character 
	    		// is mein ek condition hoge like khali sirf charaters na hn wala bhi
	    		if( !(temp[j].matches("[.0-9]+")) && temp[j].matches("[a-zA-Z ]*\\d+.*") && ((j+1)<temp.length) &&  temp[j+1].matches("[.0-9]+"))
	    		{
	    			//System.out.println(" when we find 150ml check forward value is  based on numeric or character ");
	    			newProducts = newProducts + getValues (0, j, temp) + "\n";
	    			check = true;
	    		}
	    		
	    		// Wheat Sugar 4500 4.55
	    		else if(temp[j].matches("[a-zA-Z]+") && ((j+1)<temp.length) && temp[j+1].matches("[.0-9]+"))
	    		{
	    			//System.out.println("Ager tumhre pas  150ml ki traha cheex nhn arhe matlab like whet suger 15.00 56.00");
	    			newProducts = newProducts + getValues (0, j+1, temp) + "\n";
	    			check = true;
	    		}
	    		//Kurkure chips 12g
	    	    else if( !(temp[j].matches("[.0-9]+")) && temp[j].matches("[a-zA-Z ]*\\d+.*") && ((j+1)==temp.length))
	    	    {
	    	    	newProducts = newProducts + getValues (0, j, temp) + "\n";
	    	    	check = true;
	    	    }
	    		//Kurkure chips 
	    	    else if( temp[j].matches("[a-zA-Z]+")&&  ((j+1)==temp.length))
	    	    {
	    	    	newProducts = newProducts + getValues (0, j, temp) + "\n";
	    	    	check = true;
	    	    }
	    	}
	    }
    	System.out.println("This is New Products: " + newProducts);
    	return newProducts;
	}

	private static String getValues (int start, int end, String[] tem)
	{
		String str = "" ;
		for(int i = start ; i < end ; i++)
		{
			str = str + tem[i] + " ";
		}
		return str;
	}
	
}