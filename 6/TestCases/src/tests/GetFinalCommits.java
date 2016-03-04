package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

//Execute this file on Windows server
public class GetFinalCommits {
	public static void main(String args[]) throws IOException, InterruptedException 
	{
		getCompiledCommits();
	}

	public static void getCompiledCommits() throws IOException, InterruptedException{
		String line="";
		TreeMap<String, String> oldnewcommits=new TreeMap<String, String>();
		TreeMap<String, String> finaloldnewcommits=new TreeMap<String, String>();
		final BufferedReader reader=new BufferedReader(new FileReader("E:\\users\\kochharps\\Projects\\ISSTA 2016\\OldNewCommits.csv"));
		while ((line = reader.readLine()) != null){
			String split[]=line.split(",");
			oldnewcommits.put(split[0]+"--"+split[1]+"--"+split[2], split[3]);
		}
		reader.close();


		ArrayList<String> compiledCommits=new ArrayList<String>();
		final File folder = new File("E:/users/kochharps/Projects/ISSTA 2016/Err");
		final List<File> fileList = Arrays.asList(folder.listFiles());

		for(int i=0;i<fileList.size();i++)
		{
			line="";
			boolean errorFile=false;
			final BufferedReader br=new BufferedReader(new FileReader(fileList.get(i)));
			String[] split=fileList.get(i).toString().split("\\\\");
			String fileName =split[split.length-1];
			while ((line = br.readLine()) != null){
				if(line.contains("[ERROR]")){
					errorFile=true;
				}
			}
			if(!errorFile){
				compiledCommits.add(fileName);
			}
		}
		
		
		for(String str:compiledCommits){
			if(oldnewcommits.containsKey(str)){
				finaloldnewcommits.put(str, oldnewcommits.get(str));
			}
		}
	}
}
