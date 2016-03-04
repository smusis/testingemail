package tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class CombineEmailCommits {


	public static void main(String args[]) throws Exception
	{
		combine();
	}

	//This function combines different email commit files to combine names of developers of diff projects
	public static void combine() throws IOException{
		ArrayList<String> allFiles=new ArrayList<>();
		allFiles.add("testers_TestCommits_info.csv");
		allFiles.add("testers_TestCommits_info1.csv");
		allFiles.add("testers_TestCommits_info2.csv");
		allFiles.add("testers_TestCommits_info3.csv");
		allFiles.add("testers_TestCommits_info4.csv");
		allFiles.add("testers_TestCommits_info5.csv");
		allFiles.add("testers_TestCommits_info6.csv");
		allFiles.add("testers_TestCommits_info7.csv");
		allFiles.add("testers_TestCommits_info8.csv");
		allFiles.add("testers_TestCommits_info9.csv");
		
		TreeMap<String, Integer> commitCount=new TreeMap<String, Integer>();
		TreeMap<String, HashSet<String>> authorRepos=new TreeMap<String, HashSet<String>>();
		TreeMap<String, HashSet<String>> filesChanged=new TreeMap<String, HashSet<String>>();
		
		ValueComparator bvc =  new ValueComparator(commitCount);
		TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);

		//The files which contains repos and commits
		for(String str: allFiles){
			final BufferedReader br=new BufferedReader(new FileReader("E:/Research Projects/FSE 2016/EmailCommits/"+str));
			String inputLine="";
			while ((inputLine = br.readLine()) != null)
			{
				//System.out.println(inputLine);
				String[] split=inputLine.split(",");
				//System.out.println(split.length);
				if(split.length==4){
					if(split[0].contains("?")){
						continue;
					}
					String nameEmail=split[0]+"==="+split[1];
					if(commitCount.containsKey(nameEmail)){
						commitCount.put(nameEmail, commitCount.get(nameEmail)+Integer.parseInt(split[split.length-1]));
					}
					else{
						commitCount.put(nameEmail, Integer.parseInt(split[split.length-1]));
					}

					if(authorRepos.containsKey(nameEmail)){
						HashSet<String> addrepo=authorRepos.get(nameEmail);
						addrepo.add(split[split.length-2]);
						authorRepos.put(nameEmail, addrepo);
					}
					else{
						HashSet<String> addrepo=new HashSet<String>();
						addrepo.add(split[split.length-2]);
						authorRepos.put(nameEmail, addrepo);
					}
				}

				//Sometimes name contains comma so length becomes 5
				if(split.length==5){
					if(split[0].contains("?")){
						continue;
					}
					String nameEmail=split[0]+" "+split[1]+"==="+split[2];
					if(commitCount.containsKey(nameEmail)){
						commitCount.put(nameEmail, commitCount.get(nameEmail)+Integer.parseInt(split[split.length-1]));
					}
					else{
						commitCount.put(nameEmail, Integer.parseInt(split[split.length-1]));
					}

					if(authorRepos.containsKey(nameEmail)){
						HashSet<String> addrepo=authorRepos.get(nameEmail);
						addrepo.add(split[split.length-2]);
						authorRepos.put(nameEmail, addrepo);
					}
					else{
						HashSet<String> addrepo=new HashSet<String>();
						addrepo.add(split[split.length-2]);
						authorRepos.put(nameEmail, addrepo);
					}
				}
			}
			br.close();
		}


		sorted_map.putAll(commitCount);
//		ArrayList<String> readFiles=new ArrayList<>();
//		allFiles.add("testers_TestCommitFiles_info.csv");
//		allFiles.add("testers_TestCommitFiles_info1.csv");
//		allFiles.add("testers_TestCommitFiles_info2.csv");
//		allFiles.add("testers_TestCommitFiles_info3.csv");
//		//The files which contains repos and commits
//		for(String str: readFiles){
//			final BufferedReader br=new BufferedReader(new FileReader("E:/Research Projects/FSE 2016/EmailCommits/"+str));
//			String inputLine="";
//			while ((inputLine = br.readLine()) != null)
//			{
//				HashSet<String> addFiles=new HashSet<>();
//				String[] split=inputLine.split(",");
//				String nameEmail="";
//				if(isValidEmailAddress(split[1])){
//					nameEmail=split[0]+"==="+split[1];
//					for(int i=2;i<split.length;i++){
//						
//					}
//				}
//
//				if(isValidEmailAddress(split[2])){
//
//				}
//			}
//		}
		
		PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter("E:/Research Projects/FSE 2016/EmailCommits/TopTesters.csv")));
		
		System.out.println(sorted_map.size());
		for(Map.Entry<String, Integer> entry: sorted_map.entrySet()){
			//System.out.println(entry.getKey());
			HashSet<String> repos=authorRepos.get(entry.getKey());
			String allRepos="";
			for(String str:repos){
				allRepos=str+","+allRepos;
			}
			
			writer.write(entry.getKey()+"==="+entry.getValue()+"==="+allRepos+"\n");
			System.out.println(entry.getKey()+"==="+entry.getValue()+"==="+allRepos);
		}
		writer.close();
	}


	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}
	
	public static boolean checkFileExtension(String file) {
		if(file.endsWith(".c")||file.endsWith(".cs")||file.endsWith(".cpp")||file.endsWith(".m")||file.endsWith(".mm")||
				file.endsWith("")||
				file.endsWith("")||file.endsWith("")||file.endsWith("")||file.endsWith("")||file.endsWith("")||file.endsWith("")||
				file.endsWith("")){
			return true;
		}
		else{
			return false;
		}
	}
}
