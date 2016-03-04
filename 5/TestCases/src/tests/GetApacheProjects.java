package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class GetApacheProjects {
	public static void main(String[] args) throws Exception {
		//getApacheURLS();
		readJSON();
	}

	public static void getApacheURLS() throws Exception
	{
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;


		try {
			for(int i=1;i<=25;i++){
				url = new URL("https://api.github.com/orgs/apache/repos?page="+i);
				is = url.openStream();  // throws an IOException
				br = new BufferedReader(new InputStreamReader(is));
				PrintWriter out=new PrintWriter(new File("E:/Research Projects/ISSTA 2016/ApacheJSON/"+i+".json"));
				while ((line = br.readLine()) != null) {
					out.write(line);	
				}
				out.close();
			}

		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}	
	}

	public static void readJSON() throws Exception
	{
		final File folder = new File("E:/Research Projects/ISSTA 2016/ApacheJSON");
		final List<File> fileList = Arrays.asList(folder.listFiles());
		JSONParser parser = new JSONParser();
		ArrayList<String> projects=new ArrayList<String>();
		for(int i=0;i<fileList.size();i++)
		{
			Object obj  = parser.parse(new FileReader(fileList.get(i)));
			JSONArray array = (JSONArray)  obj;
			//System.out.println(array.size());
			for(int j=0;j<array.size();j++){
				JSONObject site = (JSONObject)array.get(j); // Exception happens here.

				//System.out.println(site.get("clone_url"));
				String lang=(String)site.get("language");
				//System.out.println(lang);
				if(lang!=null && lang.equals("Java")){
					projects.add((String)site.get("clone_url"));
				}
			}
		}

		PrintWriter out=new PrintWriter(new File("E:/Research Projects/ISSTA 2016/ApacheProjects.txt"));
		for(String str:projects){
			out.write(str+"\n");
		}
		out.close();

		checkPOMs(projects);
	}

	public static void checkPOMs(ArrayList<String> projects) throws Exception
	{
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		PrintWriter out=new PrintWriter(new File("E:/Research Projects/ISSTA 2016/FinalApacheProjects.txt"));

		
		try {
			for(String str: projects){
				System.out.println(str);
				StringBuffer buf=new StringBuffer();
				url = new URL(str);
				is = url.openStream();  // throws an IOException
				br = new BufferedReader(new InputStreamReader(is));
				boolean pom=false;
				while ((line = br.readLine()) != null) {
					buf.append(line);
				}

				
				if(buf.toString().contains("pom.xml")){
					out.write(str+"\n");
				}
				
			}

		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				out.close();
				if (is != null) is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}	
	}
}
