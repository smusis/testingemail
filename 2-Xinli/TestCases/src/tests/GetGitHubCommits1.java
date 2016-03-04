package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
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

public class GetGitHubCommits1 {
	public static void main(String args[]) throws Exception
	{
		getEmails();
		//testGitHub();
	}


	//For all the top 1000 projects (sorted by Stars+forks), get the commits, test commits & test files added/edited
	public static void getEmails() throws IOException, InterruptedException{
		GitHub github = GitHub.connect("smusis", "d7a5a9a10e2d1f20e305e86ad205c4fe0ead3ed1");
		//github.connect("smusis2012", "d7a5a9a10e2d1f20e305e86ad205c4fe0ead3ed1");
		//System.out.println(github.getUser("smusis"));
		//GHUser ghUser=new GHUser();
		//GHRepository ghRepository=new GHRepository();
		//GHCommit ghCommit=new GHCommit();

		TreeMap<String, TreeMap<String, Integer>> committersInfo=new TreeMap<String,TreeMap<String, Integer>>();
		TreeMap<String, TreeMap<String, Integer>> committersTestInfo=new TreeMap<String,TreeMap<String, Integer>>();
		TreeMap<String, TreeMap<String, HashSet<String>>> committersTestFilesInfo=new TreeMap<String,TreeMap<String, HashSet<String>>>();

		int count=0;
		try (BufferedReader br = new BufferedReader(new FileReader("E:\\Research Projects\\FSE 2016\\projects_final.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {

				count++;
				if(count<51){
					continue;//To leave the header in the file
				}
				if(count==52){
					break;
				}

				try{
					String [] split=line.split(",");
					if(split[0].equals("")||split[0] ==null){
						continue;
					}

					String projectSplit[]=split[split.length-1].split("/");
					//System.out.println(split[3]);
					String project=projectSplit[3]+"/"+projectSplit[4];
					System.out.println(project);
					GHRepository ghRepository=github.getRepository(project);

					if(ghRepository!=null){
						ArrayList<String> commitList=new ArrayList<>();
						PagedIterable<GHCommit> it=ghRepository.listCommits();
						PagedIterator<GHCommit> iter=it.iterator();
						while(iter.hasNext()){
							GHCommit ghcom=iter.next();
							String commit=ghcom.getSHA1();
							//System.out.println(commit);
							commitList.add(commit);

							//GHCommit ghCommit=ghRepository.getCommit(commit);
							//GHUser ghUser=ghCommit.getAuthor();
							//userList.add(ghUser.getName());
							//System.out.println(ghUser.getName());
						}
						System.out.println(commitList.size());
						for(String str:commitList){
							//System.out.println(str);
							GHRateLimit gLimit=github.getRateLimit();
							//System.out.println(str);
							//System.out.println(gLimit.remaining);
							if(gLimit.remaining<=2){
								Thread.sleep(3600000);
							}
							GHCommit ghCommit=ghRepository.getCommit(str);
							GHUser ghUser=new GHUser();
							ghUser=ghCommit.getAuthor();
							List<GHCommit.File> comFiles=ghCommit.getFiles();
							if(ghUser!=null){
								if(ghUser.getName()!=null && !ghUser.getName().equals("") &&
										ghUser.getEmail()!=null && !ghUser.getName().equals("") && 
										isValidEmailAddress(ghUser.getEmail())){
									String userInfo=ghUser.getName()+","+ghUser.getEmail();
									//System.out.println(ghUser.getName());
									//System.out.println(ghUser.getEmail());

									//Add all the commits
									TreeMap<String, Integer> tempCommits=new TreeMap<>();

									if(committersInfo.containsKey(userInfo)){
										tempCommits=committersInfo.get(userInfo);
										if(tempCommits.containsKey(project)){
											tempCommits.put(project, tempCommits.get(project)+1);
										}
										else{
											tempCommits.put(project, 1);
										}
									}
									else{
										tempCommits.put(project, 1);
										committersInfo.put(userInfo, tempCommits);
									}

									//Add all the test files
									boolean testCommit=false;							
									//System.out.println("Files "+comFiles.size());
									HashSet<String> getTestFiles=new HashSet<>();
									for(GHCommit.File comStr:comFiles){
										String fileSplit[]=comStr.getFileName().split("/");
										String fileName=fileSplit[fileSplit.length-1];
										//String fileName=comStr.getFileName();
										if(fileName.toLowerCase().contains("test")&&
												(comStr.getStatus().contains("added")||comStr.getStatus().contains("modified"))){
											testCommit=true;
											getTestFiles.add(comStr.getFileName());
										}
										//System.out.println(comStr.getFileName());
										//System.out.println(comStr.getStatus() );
									}

									if(testCommit){
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
									}

									if(testCommit){
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
						}
						//ghCommit=ghRepository.getCommit("6083cfcf84d818bc6c1805f8bca521c621500622");
						//ghUser=ghCommit.getAuthor();
						//System.out.println(ghUser.getName());
						//System.exit(1);
					}
				}
				//				catch(Exception e){
				//					System.out.println("Exception");
				//					continue;
				//				}
				catch(Exception e){
					System.out.println("Exception");
					continue;
				}
				//				finally{
				//
				//				}
			}
		}

		System.out.println("After loop");
		PrintWriter writeCommits = new PrintWriter("E:\\Research Projects\\FSE 2016\\testers_Commits_info.csv");
		for(Map.Entry<String, TreeMap<String, Integer>> entry: committersInfo.entrySet()){
			TreeMap<String, Integer> tempCommits=new TreeMap<String,Integer>();
			ValueComparator bvc =  new ValueComparator(tempCommits);
			TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);

			for(Map.Entry<String, Integer> toSort: entry.getValue().entrySet()){
				tempCommits.put(toSort.getKey(), toSort.getValue());
			}

			sorted_map.putAll(tempCommits);
			for(Map.Entry<String, Integer> innerEntry: sorted_map.entrySet()){
				//System.out.println(entry.getKey()+" "+innerEntry.getKey()+" "+innerEntry.getValue());
				writeCommits.write(entry.getKey()+","+innerEntry.getKey()+","+innerEntry.getValue()+"\n");
			}
		}
		writeCommits.close();

		PrintWriter writeTestCommits = new PrintWriter("E:\\Research Projects\\FSE 2016\\testers_TestCommits_info.csv");
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

		PrintWriter writeTestCommitFiles = new PrintWriter("E:\\Research Projects\\FSE 2016\\testers_TestCommitFiles_info.csv");
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

