package test;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.opencsv.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class JsonToCsv {

	private static Scanner scin;

	
	public static void main(String[] args) {
		//File path
		String csvFilename = "";
	
		//String sURL="";
		
		 if(args.length == 1) 	//if user wants to input one argument
		 {
				 csvFilename = new String(args[0]);		//url will be default
				 System.out.println(csvFilename);
				 
		 }
		 
		 
		 else	//use default
		 {
			 csvFilename = "./data.csv";
			
		}
		 
		
		File csvFile = new File(csvFilename); //		File object used for both writing and reading
		
		FileWriter csvFileWriter = null;
		try {
			csvFileWriter = new FileWriter(new File(csvFilename));
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//input URL
		
		String sURL = "https://jsonplaceholder.typicode.com/posts/1/comments";
	    URL url = null;
		try {
			url = new URL(sURL);
		} catch (MalformedURLException e) {
			System.err.println("Invalid url.");
		}
		//access data from URL
	    URLConnection request = null;
		try {
			request = url.openConnection();
			request.connect();
			scin = new Scanner(request.getInputStream());
			String data = "";
			while(scin.hasNextLine()) {
				data += scin.nextLine() + "\n";
			}
			//openCSV writing 
			CSVWriter writer=new CSVWriter(csvFileWriter);
			
			String[] header = {"postId","id","name","email","body"};
			
			writer.writeNext(header); //write header
			
			JSONArray jsonArray = new JSONArray(data); //convert data into Json Array
			
			for(int i=0;i<jsonArray.length();i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);  //Json array to Jaon object
				String[] rows= new String[5];
				rows[0]=jsonObject.getString("postId");
				rows[1]=jsonObject.getString("id");
				rows[2]=jsonObject.getString("name");
				rows[3]=jsonObject.getString("email");
				rows[4]=jsonObject.getString("body");
				
				writer.writeNext(rows); 	//write every object to file
			}
			writer.close();
			csvFileWriter.close();	//close file
			
			
			
			try
			{
				
				FileReader csvFileReader = null;
				csvFileReader = new FileReader(new File(csvFilename));	
				
				CSVReader csvReader=new CSVReader(csvFileReader); //openCSV reading object
				
		//string to access data.csv row entries
				String[] Records=null;
				
				try
				{
					int j=0;
				while((Records=csvReader.readNext()) != null)
				{
					if(j==0)			//read and display header
					{
						for(int k=0;k<5;k++)
						{
							if(k!=4)
							{
							System.out.print("\""+Records[k]+"\",");
							}
							else
							{
								System.out.println("\""+Records[k]+"\"");
							}
						}
					}
					else		//read all other rows
					{
					for(int k=0;k<5;k++)
					{
						//System.out.println(Records[k]+",");
						if(k!=4)
						{
							if(k<2)
							{
								System.out.print(Records[k]+",");
							}
							else
							{
						System.out.print("\""+Records[k]+"\",");
							}
						}
						else
						{
							System.out.println("\""+Records[k]+"\"");
						}
						
					}
					
					}
					j++;
				}
				}
				catch(ArrayIndexOutOfBoundsException exception)
				{
					System.out.println(exception);
				}
				
				csvReader.close();
				
			}
			
				
			catch (Exception e)
			{
				System.out.println(e);
			}
			
		} catch (IOException e) {
			System.err.println("Unable to connect.");
		} catch (JSONException e) {
			System.err.println("Invalid JSON format.");
		}
	}

}
