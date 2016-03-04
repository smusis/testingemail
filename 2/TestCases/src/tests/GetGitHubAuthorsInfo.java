package tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHNotificationStream;
import org.kohsuke.github.GHRateLimit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

public class GetGitHubAuthorsInfo {
	public static void main(String args[]) throws Exception
	{
		getAuthorInfo();
		//testGitHub();
	}


	//For all the projects having log info containing test commits, get the user info & store it
	public static void getAuthorInfo() throws IOException, InterruptedException{
		GitHub github = GitHub.connect("gurgog", "8729891b459012575f513815601fe152c1b45b12 ");
		//github.connect("smusis2012", "d7a5a9a10e2d1f20e305e86ad205c4fe0ead3ed1");
		//System.out.println(github.getUser("smusis"));
		//GHUser ghUser=new GHUser();
		//GHRepository ghRepository=new GHRepository();
		//GHCommit ghCommit=new GHCommit();

		//TreeMap<String, TreeMap<String, Integer>> committersInfo=new TreeMap<String,TreeMap<String, Integer>>();
		TreeMap<String, TreeMap<String, Integer>> committersTestInfo=new TreeMap<String,TreeMap<String, Integer>>();
		TreeMap<String, TreeMap<String, HashSet<String>>> committersTestFilesInfo=new TreeMap<String,TreeMap<String, HashSet<String>>>();

		final File folder = new File("E:/Research Projects/FSE 2016/TestCommits");
		final List<File> fileList = Arrays.asList(folder.listFiles());
		int count=0;
		for(int i=0;i<fileList.size();i++)
		{
			try{
				count++;
				if(count<101){
					continue;
				}
				if(count==151){
					break;
				}
				String line="";
				String[] split=fileList.get(i).toString().split("\\\\");
				String project=split[split.length-1].replace(".txt","").replace("_", "/");
				System.out.println(project);
				if(project.equals("Automattic//s")){
					project="Automattic/_s";
				}
				final BufferedReader br = new BufferedReader(new FileReader(fileList.get(i))); 
				GHRepository ghRepository=github.getRepository(project);
				GHRateLimit gLimit=github.getRateLimit();
				if(gLimit.remaining<25){
					//Date date=gLimit.getResetDate();
					//Thread.sleep(date.getTime());
					Thread.sleep(1800000);
				}

				while ((line= br.readLine())!= null){
					try{
						gLimit=github.getRateLimit();

						if(gLimit.remaining<25){
							//Date date=gLimit.getResetDate();
							//Thread.sleep(date.getTime());
							Thread.sleep(1800000);
						}
						String[] splitCommit=line.split("===");
						String[] splitFile=splitCommit[1].split(",");
						String commit=splitCommit[0];
						HashSet<String> getTestFiles=new HashSet<>();
						for(int z=0;z<splitFile.length;z++){
							if(!splitFile[z].equals("")){
								getTestFiles.add(splitFile[z]);
							}
						}
						GHCommit ghCommit=ghRepository.getCommit(commit);
						GHUser ghUser=new GHUser();
						ghUser=ghCommit.getAuthor();
						//List<GHCommit.File> comFiles=ghCommit.getFiles();
						if(ghUser!=null){
							if(ghUser.getName()!=null && !ghUser.getName().equals("") &&
									ghUser.getEmail()!=null && !ghUser.getName().equals("") && 
									isValidEmailAddress(ghUser.getEmail())){
								String userInfo=ghUser.getName()+","+ghUser.getEmail();

								TreeMap<String, HashSet<String>> tempTestFilesCommits=new TreeMap<String,HashSet<String>>();
								if(committersTestFilesInfo.containsKey(userInfo)){
									tempTestFilesCommits=committersTestFilesInfo.get(userInfo);

									if(tempTestFilesCommits.containsKey(project)){
										HashSet<String> tempExisting=tempTestFilesCommits.get(project);
										tempExisting.addAll(getTestFiles);
										tempTestFilesCommits.put(project, tempExisting);
									}
									else{
										tempTestFilesCommits.put(project, getTestFiles);
									}
								}
								else{
									tempTestFilesCommits.put(project, getTestFiles);
									committersTestFilesInfo.put(userInfo, tempTestFilesCommits);
								}

								//Add all the commits
								TreeMap<String, Integer> tempTestCommits=new TreeMap<>();
								if(committersTestInfo.containsKey(userInfo)){
									tempTestCommits=committersTestInfo.get(userInfo);
									if(tempTestCommits.containsKey(project)){
										tempTestCommits.put(project, tempTestCommits.get(project)+1);
									}
									else{
										tempTestCommits.put(project, 1);
									}
								}
								else{
									tempTestCommits.put(project, 1);
									committersTestInfo.put(userInfo, tempTestCommits);
								}

							}
						}
					}
					catch(Exception e){
						continue;
					}
				}

				
			}
			catch(Exception e){
				continue;
			}
			
			System.out.println("After loop");
			PrintWriter writeTestCommits = new PrintWriter(new BufferedWriter(new FileWriter("E:/Research Projects/FSE 2016/testers_TestCommits_info.csv")),true);
			for(Map.Entry<String, TreeMap<String, Integer>> entry: committersTestInfo.entrySet()){
				TreeMap<String, Integer> tempCommits=new TreeMap<String,Integer>();
				ValueComparator bvc =  new ValueComparator(tempCommits);
				TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);

				for(Map.Entry<String, Integer> toSort: committersTestInfo.get(entry.getKey()).entrySet()){
					tempCommits.put(toSort.getKey(), toSort.getValue());
				}
				sorted_map.putAll(tempCommits);
				for(Map.Entry<String, Integer> innerEntry: sorted_map.entrySet()){
					//System.out.println(entry.getKey()+" "+innerEntry.getKey()+" "+innerEntry.getValue());
					writeTestCommits.write(entry.getKey()+","+innerEntry.getKey()+","+innerEntry.getValue()+"\n");
				}
			}
			writeTestCommits.close();

			PrintWriter writeTestCommitFiles =  new PrintWriter(new BufferedWriter(new FileWriter("E:/Research Projects/FSE 2016/testers_TestCommitFiles_info.csv")),true);
			for(Map.Entry<String, TreeMap<String, HashSet<String>>> entry: committersTestFilesInfo.entrySet()){
				TreeMap<String, HashSet<String>> tempCommits=new TreeMap<String, HashSet<String>>();
				tempCommits=committersTestFilesInfo.get(entry.getKey());
				for(Map.Entry<String, HashSet<String>> innerEntry: tempCommits.entrySet()){
					//System.out.println(entry.getKey()+" "+innerEntry.getKey()+" "+innerEntry.getValue());
					String files="";
					for(String str:innerEntry.getValue()){
						files=str+","+files;
					}
					writeTestCommitFiles.write(entry.getKey()+","+innerEntry.getKey()+","+files+"\n");
				}
			}
			writeTestCommitFiles.close();
		}


	}

	public static void testGitHub() throws IOException{
		GitHub github = GitHub.connect("smusis", "d7a5a9a10e2d1f20e305e86ad205c4fe0ead3ed1");
		//github.connect("smusis2012", "d7a5a9a10e2d1f20e305e86ad205c4fe0ead3ed1");
		//System.out.println(github.getUser("smusis"));
		//GHUser ghUser=new GHUser();
		//GHRepository ghRepository=new GHRepository();
		//GHCommit ghCommit=new GHCommit();


		GHRepository ghRepository=github.getRepository("smusis/multiple-languages");

		ArrayList<String> userList=new ArrayList<>();
		PagedIterable<GHCommit> it=ghRepository.listCommits();
		PagedIterator<GHCommit> iter=it.iterator();
		while(iter.hasNext()){
			GHCommit ghcom=iter.next();
			String commit=ghcom.getSHA1();
			//System.out.println(commit);
			userList.add(commit);
			//GHCommit ghCommit=ghRepository.getCommit(commit);
			//GHUser ghUser=ghCommit.getAuthor();
			//userList.add(ghUser.getName());
			//System.out.println(ghUser.getName());
		}
		System.out.println(userList.size());
		for(String str:userList){
			System.out.println(str);
			GHCommit ghCommit=ghRepository.getCommit(str);
			GHUser ghUser=new GHUser();
			ghUser=ghCommit.getAuthor();
			List<GHCommit.File> comFiles=ghCommit.getFiles();
			if(ghUser!=null){
				System.out.println(ghUser.getName());
				System.out.println(ghUser.getEmail());
				System.out.println("Files "+comFiles.size());
				for(GHCommit.File comStr:comFiles){
					System.out.println(comStr.getFileName());
					System.out.println(comStr.getStatus() );
				}
			}
		}

		//ghCommit=ghRepository.getCommit("6083cfcf84d818bc6c1805f8bca521c621500622");
		//ghUser=ghCommit.getAuthor();
		//System.out.println(ghUser.getName());
		System.exit(1);
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
}

