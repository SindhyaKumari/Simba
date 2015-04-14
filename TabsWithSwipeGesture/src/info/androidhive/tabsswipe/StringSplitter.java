package info.androidhive.tabsswipe;

import java.util.regex.Pattern;
import android.app.Activity;
import android.os.Bundle;

public class StringSplitter 
{

	private String Products , newProducts , StoreName , InvoiceNumber ;
	private String numericPattern = "[.0-9]+";
	private String alphaNumericPattern = "[a-zA-Z ]*\\d+.*";
	private String alphaNumericAndSpecialCharactersPattern = "[A-Za-z0-9$&+,:;=?@#/|~`'<>.-^*()%!]+";
	private String alphabetsPattern = "[a-zA-Z]+";
	private String specialCharactersPattern = "[$&+,:<;=?@#|~`'()<>.-^*>%!]+";
	private String slashesPattern = ".*[/\\\\].*";
	private String roundBracketPattern = "\\((.+?)\\)";
	private String alphabetsAndSlashPattern = "^[a-zA-Z\\-]+$";
	
	StringSplitter(String pro){
		Products = pro;
	}
	
	
	public String Stringsplitter(){
		String[] product_,temp;
		boolean check = false;
		
		removeUnnecessarySpacesAndNextLine();
		product_ = splitStringOnBasisOfNextLine();
		getStoreNameAndInvoice(product_);
		
		for(int i = 9 ; i < product_.length; i++){
		   check = false;	
		   temp = splitStringOnBasisOfSpace(i,product_);
		   for(int j = 0 ; j < temp.length && !check  ;j++){  
			   
			   //Checking alphanumeric character like 150ml
				if(!(isNumeric(temp,j)) && (isAlphaNumeric(temp,j) && j!=0) && ((j+1) < (callength(temp))) && 
				     ((isNumeric(temp,j+1)) || isAlphaNumericAndSpecialCharacter(temp ,j+1))){
				   	  //	 System.out.println("check 0" + temp[j]);
				   		newProducts = newProducts + getValues (0, j, temp) + "\n";
				   		check = true;
				}
				//Checking numeric after character like Wheat Sugar 4500 4.55
				else if(isAlphabets(temp,j) && ((j+1) < callength(temp)) && (isNumeric(temp,j+1))){
				   	//  System.out.println("check 1" + temp[j]);
		      		    newProducts = newProducts + getValues (0, j+1, temp) + "\n";
		   		        check = true;
		   	   }
			   //Checking alphanumeric after character like Wheat Sugar 12g
			   else if( !(isNumeric(temp,j)) && (isAlphaNumeric(temp,j) && j!=0)&& (((j+1)==callength(temp)))){
				   //	  System.out.println("check 2" + temp[j]);
				   		newProducts = newProducts + getValues (0, j, temp) + "\n";
				   		check = true;
			   }
			   //Checking word is based upon only characters like Kurkure chips
			   else if((!(isNumeric(temp,j)) && isAlphabets(temp,j)) &&  ((j+1)==callength(temp)) || is7up(temp ,j)){
				   	   //	 System.out.println("check 3" + temp[j]);
			   		    newProducts = newProducts + getValues (0, j+1, temp) + "\n";
			   	    	check = true;
			   }
			   //Checking if word contain special characters only  and j+1 equal to length of string skip them
			   else if(!(isNumeric(temp,j)) && isSpecialCharacter(temp,j) && ((j+1)!=callength(temp))){
					   System.out.print("check new" + temp[j]);
					   check = false;
			   }
			   else if(!(isNumeric(temp,j)) &&  (((j+1)==callength(temp)) && (isSlashes(temp ,j) || isAlphabets(temp,j))) && (j==0)){  
			   	   	   System.out.println("check 4" + temp[j]);
			     	   newProducts = newProducts + getValues (0, j+1, temp) + "\n";
			     	   check = true;
			   }
			   else if(!(isNumeric(temp,j)) &&  (((j+1)==callength(temp)) && ((isSlashes(temp ,j))|| isAlphabets(temp,j) 
					   || (isSpecialCharacter(temp,j) && j!=0) || (isRoundBrackets(temp ,j) && j!=0)))){ 
			       	   System.out.println("check 5" + temp[j]);
			   		   newProducts = newProducts + getValues (0, j, temp) + "\n";
			   		   check = true;
			   }
			   else if(!(isNumeric(temp,j)) &&  (((j+1)==callength(temp)) && ((isAlphabetsAndSlash(temp,j))))){
			     	   System.out.println("check 6" + temp[j]);
			  		   newProducts = newProducts + getValues (0, j+1, temp) + "\n";
			  		   check = true;
			   }
			  // checking if it contains only numeric like 15.00 56.6
			   else if ((temp[j].matches("[.0-9]+"))){
			   		check = true;
			   }
			       
			   
		   }
		}
		return newProducts;
		
	}
	
	/*
	 * 
	 */
	private void getStoreNameAndInvoice(String[] product_){
		String [] t;
		StoreName = product_[0];
	    InvoiceNumber = product_[5];
	    t = InvoiceNumber.split("#");
	    InvoiceNumber = t[1];
	    
	    System.out.print("StoreName" + StoreName + "\n " + "Invoice Number" + InvoiceNumber);
	}
	
	/*
	 *  Removing Unnecessary Spaces And NextLine
	 */
    private void removeUnnecessarySpacesAndNextLine(){
		
		Pattern spaces = Pattern.compile("[\t ]+");
		Pattern emptyLines = Pattern.compile("^\\s+$?", Pattern.MULTILINE);
		Pattern newlines = Pattern.compile("\\s*\\n+");
		Products = newlines.matcher(emptyLines.matcher(spaces.matcher(Products).replaceAll(" ")).replaceAll("")).replaceAll("\n");
	}
    
    /*
     * Splitting String On Basis of NextLine
     */
    private String[] splitStringOnBasisOfNextLine(){ 	
    	return Products.split("\n");
    }
    
    /*
     * Splitting String On Basis of Spaces
     */
    private String[] splitStringOnBasisOfSpace(int i,String[] product_){
    	return product_[i].split(" ");  
    }
    
    /*
     * Check given word is numeric 
     */
    private boolean isNumeric(String[] temp , int j){
    	return temp[j].matches(numericPattern);
    }
    
    
    /*
     * Check given word is alphanumeric 
     */
    private boolean isAlphaNumeric(String[] temp , int j){
    	return temp[j].matches(alphaNumericPattern);
    }
    
    /*
     * Calculate length of temp string
     */
    private int callength(String[] temp){
    	return temp.length;
    }
    
    /*
     * Check given word is alphanumeric and special characters
     */
    private boolean isAlphaNumericAndSpecialCharacter(String[] temp , int j){
    	return temp[j].matches(alphaNumericAndSpecialCharactersPattern);
    }
    
    /*
     * Check given word is based only on alphabets
     */
    private boolean isAlphabets(String[] temp , int j){
    	return temp[j].matches(alphabetsPattern);
    }
    
    /*
     * Check given word is special characters
     */
    private boolean isSpecialCharacter(String[] temp , int j){
    	return temp[j].matches(specialCharactersPattern);
    }
    
    /*
     * Check given word contain slash
     */
    private boolean isSlashes(String[] temp , int j){
    	return temp[j].matches(slashesPattern);
    }
    
    /*
     * Check given word contain round brackets or character in between them
     */
    private boolean isRoundBrackets(String[] temp , int j){
    	return temp[j].matches(roundBracketPattern);
    }
    
    /*
     * Check given word contain alphabets and slashes
     */
    private boolean isAlphabetsAndSlash(String[] temp , int j){
    	return temp[j].matches(alphabetsAndSlashPattern );
    }
    
    /*
     * Concatenate strings
     */
    private String getValues(int start, int end, String[] tem){
		
		String str = "" ;
		for(int i = start ; i < end ; i++){
            if(!(tem[i].equals("\n")))
			   str = str + tem[i] + " ";	
		}	
		return str;
	}
    /*
     * Check given word is 7up
     */
    private boolean is7up(String[] temp , int j){
    	return temp[j].equals("7up");
    }

    /*
     * Get store Name
     */
    public String getStoreName(){
     return StoreName;
    } 
    
    /*
     * Get Invoice number
     */
    public String getInvoiceNumber(){
     return InvoiceNumber;
    }
	
}