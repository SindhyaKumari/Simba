package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.util.Log;

public class LevenshteinDistance {
	
	private List<HashMap<Integer,String>> product_list;
	private ArrayList<String> Product;
	private ArrayList<String> newProductlist;
	private ArrayList<Integer> PIDList;
	private String tempPro, tempPrev;
	private int calDistanceOfEachString [], size;
	private int thershold = 5;
	private int thersholdPercentage = 50;
	
	public LevenshteinDistance (ArrayList<String> Product ,List<HashMap<Integer,String>> product_list){
    
		size = product_list.size();
		newProductlist = new ArrayList<String>();
		this.Product = Product;
		this.product_list = product_list;
		calDistanceOfEachString = new int[size];
		PIDList = new ArrayList<Integer>();
    }

	public String[] findDistanceOfAllProducts(){
		int mostMatchedStringIndex;
		
	//	Log.e("before ","Before distance"+size);
	////	print();
		for(int i = 0 ; i < Product.size() ; i++){
			initialize();
			tempPro = Product.get(i);	
			getValuefromMapAndCalculateDistance(tempPro);
			
			mostMatchedStringIndex = (minDistance());
			//Log.e("min distance"," "+ mostMatchedStringIndex);
			Product.set(i, getValueFromMapThatMatches(mostMatchedStringIndex,i,tempPro));
		}
	//	Log.e("after "," \n \n After distance");
	//	print();
		return convertPIDListIntoArray();
	}
	
	/*
	 * Getting value that matches at particular index of map
	 */
	private String getValueFromMapThatMatches(int index, int i,String tempProduct){
		boolean check = false;
		for(HashMap<Integer, String> map: product_list) {
	        for(Entry<Integer, String> mapEntry: map.entrySet()) {
	        	  if(mapEntry.getKey() == (index+1)){
		                tempPrev = mapEntry.getValue();
						if(checkThershold(tempProduct,tempPrev,index)){
							//    Log.e("tempPrev", tempPrev);
							//    Log.e("id new ", String.valueOf(mapEntry.getKey()));
		                        PIDList.add(mapEntry.getKey());       
		                        check = true;
		                        break;
		                }
						else{
							  //  Log.e("else - tempPrev", tempProduct);
						        newProductlist.add(tempProduct);
		                        check = true;
		                        break;  
						}
					}
	        }
	        if(check)
	        	break;
	        
	    }
		
		return tempPrev;		
	}
	
	/*
	 * Check  thershold and thershold percentage
	 */
	private boolean checkThershold(String tempPro, String tempNew, int index){
            
        int tempProlen = tempPro.length();
		int newtemplen = tempNew.length();
	//	Log.e("TempProduct", tempPro);
//		Log.e("TempNewProduct", tempNew);
		int distance = calDistanceOfEachString[index];

	//	Log.e("DIstnace", String.valueOf(distance));
		double differencePercentage;
		boolean check = false;
		if(tempProlen > newtemplen){
		     differencePercentage = distance/newtemplen;
		}else{
		     differencePercentage = distance/tempProlen;
		}
		
		if((distance < thershold) && (differencePercentage < thersholdPercentage))
                 check = true;
        				 
	    return check;
		 
	}
	
	
	/*
	 * Get value from list by iterating Hashmap and than calculate distance
	 */
	private void getValuefromMapAndCalculateDistance(String tempPro){
		Log.e("distance of ", tempPro);
		for(HashMap<Integer, String> map: product_list) {
	        for(Entry<Integer, String> mapEntry: map.entrySet()) {
	            calDistanceOfEachString[(mapEntry.getKey()-1)] = calculateDistance(tempPro,mapEntry.getValue());
	          //  Log.e("distance", calDistanceOfEachString[(mapEntry.getKey()-1)]+" ");
	        }
	    }
	}
	
	/*
	 * Getting new Product List
	 */
	public ArrayList<String> getNewProductList(){
		return newProductlist;
	}
	
	/*
	 * Find the one whose distance is minimum which means this string is more similar towards given item
	 */
	private int minDistance(){
		int min = 0;

	    	for(int j = 1 ; j < size; j++){
	    		if(calDistanceOfEachString[min] > calDistanceOfEachString[j]){
	    			min = j;
	    		}
	    	}
	    	
	    return min;
	}
	/*
	 * Initialize array of calDistanceofEachString with zero
	 */
	private void initialize(){
		for(int i = 0 ; i < size ; i++){
			calDistanceOfEachString[i] = 0;
		}
	}
		
	private String[] convertPIDListIntoArray(){
		
		String[] temp= new String[PIDList.size()];
		for(int i = 0; i < PIDList.size() ; i++){
			temp[i] = String.valueOf(PIDList.get(i));
			Log.e("temp stirng " , temp[i]);
		}
		return temp;
	}

	 
	/*
	 * Compute Levenshtein Distance
	 */
	private  int  calculateDistance(String newProduct, String prevProduct){
		
	    int distance[][]; // matrix
	    int newProductlen; // length of s
	    int prevProductlen; // length of t
	    int i; // iterates through s
	    int j; // iterates through t
	    char newProduct_i; // ith character of s
	    char prevProduct_j; // jth character of t
	    int cost; // cost

	    // Step 1
	    newProductlen = newProduct.length();
	    prevProductlen = prevProduct.length();
	    if (newProductlen == 0)
	      return prevProductlen;
	    if (prevProductlen == 0)
	      return newProductlen;
	    distance = new int[newProductlen + 1][prevProductlen + 1];

	    // Step 2
	    for (i = 0; i <= newProductlen; i++)
	      distance[i][0] = i;
	    for (j = 0; j <= prevProductlen; j++)
	      distance[0][j] = j;

	    // Step 3
	    for (i = 1; i <= newProductlen; i++) {
	      newProduct_i =  newProduct.charAt(i - 1);

	      // Step 4
	      for (j = 1; j <= prevProductlen; j++) {
	        prevProduct_j = prevProduct.charAt(j - 1);

	        // Step 5
	        if (newProduct_i == prevProduct_j)
	          cost = 0;
	        else
	          cost = 1;

	        // Step 6
	        distance[i][j] = minimum(distance[i - 1][j] + 1, distance[i][j - 1] + 1, distance[i - 1][j - 1] + cost);
	      }
	    }

	    // Step 7
	    return distance[newProductlen][prevProductlen];
	  } 
	       
	   //Get minimum of three values
	    private  static int minimum(int a, int b, int c) {
	    int mi = a;
	    if (b < mi)
	      mi = b;
	    if (c < mi)
	      mi = c;
	    return mi;
	  }
	    
	   private void print(){
		   for(int i = 0 ; i < Product.size() ; i++){
			   Log.e("Product" ,Product.get(i));
		   }
	   }
}
