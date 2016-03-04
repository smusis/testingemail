package email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.kohsuke.github.GHRateLimit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

public class GetGitHubTopUsers {
	public static void main(String args[]) throws Exception
	{
		getGitHubUsers();
		//checkEmails();
		//testThread();
	}

	//Read the usernames of top contributors & get their names & emails.
	public static void getGitHubUsers() throws IOException{

		//GitHub github = GitHub.connectAnonymously();
		GitHub github = GitHub.connect("kochharps", " 5ec604cc788ffa9620db85ee0177ce9440c5cddc ");
		//github.connect("smusis2012", "d7a5a9a10e2d1f20e305e86ad205c4fe0ead3ed1");
		//System.out.println(github.getUser("smusis"));
		GHUser ghUser=new GHUser();
		FileWriter fw = new FileWriter("E:\\Research Projects\\FSE 2016\\Emails\\GitHubTopDevelopers.csv");
		BufferedWriter bw = new BufferedWriter(fw);

		int count=0;
		int userCount=0;
		TreeMap<String, Integer> userNames=new TreeMap<String,Integer>();
		ValueComparator bvc =  new ValueComparator(userNames);
		TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
		
		try (BufferedReader br = new BufferedReader(new FileReader("E:\\Research Projects\\FSE 2016\\Emails\\TopDevelopers.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				try{
					String [] split=line.split(",");
					if(split[0].equals("")||split[0] ==null){
						continue;
					}
					userNames.put(split[0],Integer.parseInt(split[2]));
				}
				catch(Exception e){
					continue;
				}
			}
		}

		//Sort by value
		
		sorted_map.putAll(userNames);

		//boolean start=false;
		//System.out.println("Done");
		for(Map.Entry<String,Integer> entry : sorted_map.entrySet()) {
			try{
				String user=entry.getKey();
				Integer contri=entry.getValue();
				System.out.println(user);
				
				user="smusis";
				GHRateLimit gLimit=github.getRateLimit();	
				gLimit=github.getRateLimit();

				if(gLimit.remaining<50){
					System.out.println("Inside Limit");
					Thread.sleep(3600000);
				}
				
				ghUser=github.getUser(user);
				//System.out.println(split[0]);
				Map<String, GHRepository> repos=ghUser.getRepositories();
				String email=ghUser.getEmail();
				if(email==null){
					continue;
				}
				if(email.equals("")){
					continue;
				}
				if(!isValidEmailAddress(email)){
					continue;
				}
				if(ghUser.getRepositories().size()<=5){
					continue;
				}
				if(ghUser.getFollowersCount()<=20){
					continue;
				}
				String name= ghUser.getName().replaceAll("[^\\x00-\\x7F]", "");
				System.out.println(name);
				if(name.contains("?")|| name.equals("")
						||name==null|| name.toLowerCase().contains("unofficial")){
					continue;
				}

				//System.out.println(ghUser.getName()+","+user+","+ghUser.getEmail()+","+contri);
				bw.write(name+","+email+","+user+","+contri);
				bw.write("\n");
				System.exit(1);
			}

			catch(FileNotFoundException f){
				try {
					TimeUnit.MINUTES.sleep(10);                //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				continue;
			}
			//			catch(IOException n){
			//				try {
			//					TimeUnit.MINUTES.sleep(20);                //1000 milliseconds is one second.
			//				} catch(InterruptedException ex) {
			//					Thread.currentThread().interrupt();
			//				}
			//				continue;
			//			}

			catch(NullPointerException n){
				continue;
			}
			catch (Exception e) {
				try {
					TimeUnit.MINUTES.sleep(20);                //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				continue;
			}

		}
		bw.close();
		//ghUser=github.getUser("smusis");
		//System.out.println(ghUser.getEmail());
	}

	//To check if emails are proper or not
//	public static void checkEmails() throws IOException{
//		FileWriter fw = new FileWriter("E:\\Research Projects\\ICSE 2016\\FinalUsers5.csv");
//		BufferedWriter bw = new BufferedWriter(fw);
//		try (BufferedReader br = new BufferedReader(new FileReader("E:\\Research Projects\\ICSE 2016\\GitHubUsersFinal4.csv"))) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				String[] split=line.split(",");
//				if(isValidEmailAddress(split[2])){
//					bw.write(split[0]+","+split[1]+","+split[2]+","+split[3]);
//					bw.write("\n");
//				}
//				else
//				{
//					System.out.println(split[2]);
//				}
//			}
//		}
//		bw.close();
//	}

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

	public static void testThread() throws InterruptedException
	{
		System.out.println("Before Thread");
		//Thread.sleep(20000);
		System.out.println("After Thread");
		int i=0;
		while(i<10){
			try{
				System.out.println(i);
				i++;
				if(i==5){
					throw new FileNotFoundException();
				}
			}
			catch(Exception e){
				System.out.println("Exc");
				continue;
			}
		}
	}
}

class ValueComparator implements Comparator<String> {

	Map<String, Integer> base;
	public ValueComparator(Map<String, Integer> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with equals.    
	public int compare(String a, String b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}