package email;

import java.io.BufferedReader;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class TestCasesEmailXinPrevious {
	public static void main(String args[]) throws Exception
	{
		getNamesNEmailsRemaining();
		//getNames();
	}

	//This is used to send emails to people who participated in Automated Debugging Survey. We only have emails (no name)
	//Get Names & emails of remaining email addresses
	public static void getNamesNEmailsRemaining() throws IOException, InterruptedException{
		final BufferedReader br=new BufferedReader(new FileReader("E:/Research Projects/FSE 2016/Emails/OnlyEmails.csv"));
		//TreeMap<String, String> nameNEmails=new TreeMap<String, String>();
		ArrayList<String> nameNEmails=new ArrayList<String>();
		String inputLine="";
		while ((inputLine = br.readLine()) != null)
		{
			try{
//				String[] split=inputLine.split(",");
//				String [] splitName=split[0].split(" ");
//				if(splitName[0].length()==2){
//					nameNEmails.add(splitName[0]+" "+splitName[1]+","+split[1].toLowerCase());
//				}
//				else{
//					nameNEmails.add(splitName[0]+","+split[1].toLowerCase());
//				}
				nameNEmails.add(inputLine);
				//Thread.sleep(60000);
			}
			catch(Exception ex) {
				//Thread.currentThread().interrupt();
			}
		}
		br.close();
		//System.out.println(nameNEmails.size());
		//		try {
		//			TimeUnit.MINUTES.sleep(45);                //1000 milliseconds is one second.
		//		} catch(InterruptedException ex) {
		//			Thread.currentThread().interrupt();
		//		}
		sendEmailOutlookTLS(nameNEmails);
	}

	//Configure outlook
	public static void sendEmailOutlookTLS(ArrayList<String> remToSend) throws InterruptedException, IOException {
		final String username = "xxia@zju.edu.cn";
		final String password = "Sunday!19";

		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.zju.edu.cn");
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		FileWriter fw = new FileWriter("E:\\Research Projects\\FSE 2016\\Exception.csv");
		BufferedWriter bw = new BufferedWriter(fw);

		int count=0;

		for(String nameNEmail:remToSend) {
			try {
//				String [] split=nameNEmail.split(",");
//				String name=split[0];
//				String email=split[1];
				String email=nameNEmail;

				//email="kochharps.2012@phdis.smu.edu.sg";
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("xxia@zju.edu.cn"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(email));
				//System.out.println(name);
				System.out.println(email);
				//System.exit(1);
				//				String mailBody = StringEscapeUtils.unescapeHtml4(
				//						" <a href=\"http://www.surveygizmo.com/s3/2269653/How-Practitioners-Perceive-Automated-Debugging-G\">"
				//								+ "<u><b>Click here to participate in the survey</b></u></a>");

				message.setSubject("What makes a Good Test Case?");
				message.setContent("Hello"+","+
						"<br><br>We are researchers from Singapore and China. Sometime back you participated in our survey and "
						+ "consented to receive future surveys. "
						+ "We are studying characteristics of good test "
						+ "cases/suites, how practitioners write test cases, what are issues practitioners commonly face "
						+ "related to writing and maintaining test cases, and what are some dos and don'ts for novice testers. "
						+ "Your response will be highly valuable for researchers in software engineering and help in "
						+ "developing tools useful for you and other practitioners.<br><br>"

						+ "https://www.surveygizmo.com/s3/2625332/What-Makes-a-Good-Test-Case-O"
						+ "<br><br>We will be very grateful if you would participate in the survey. "
						+ "Your response will be kept anonymous and only aggregate information "
						+ "will be reported. We would also like to request for your help to encourage "
						+ "your colleagues and friends to fill in the survey. If you have further questions, "
						+ "please just let us know.<br><br>"

						+ "As a token of our appreciation, we will do a raffle to give out a <b>50 USD Amazon</b> "
						+ "voucher each to two randomly selected participants. If you are interested in this "
						+ "raffle please fill in this email id at the end of the survey. "
						+ "<br><br>Thank you very much!"
						+ "<br><br>Yours Sincerely, <br>Xin Xia,<br>Research Assistant Professor,"
						+ "<br>Zhejiang University, China"
						+ "<br>http://mypage.zju.edu.cn/en/xinxia"
						+"<br>"
						+ "<br>Shanping Li,<br>Professor,"
						+ "<br>Zhejiang University, China","text/html");

				Transport.send(message);
				//System.out.println(mailBody);
				System.out.println("Done");
				count++;
				//if(count==25){
				try {
					TimeUnit.MINUTES.sleep(1);                //1000 milliseconds is one second.
					//count=0;
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				//continue;
				//}
				//System.exit(1);
			}
			catch (MessagingException e) {
				System.out.println("Exception Thrown");
				bw.write(nameNEmail);
				bw.write("\n");
				//				try {
				//					TimeUnit.MINUTES.sleep(60);                //1000 milliseconds is one second.
				//					count=0;
				//					bw.write(nameNEmail);
				//				} catch(InterruptedException ex) {
				//					Thread.currentThread().interrupt();
				//				}
				continue;
			}
		}
		bw.close();
	}

	//Get names from emails of people from  last time
	public static void getNames() throws IOException, InterruptedException{
		final BufferedReader br=new BufferedReader(new FileReader("E:/Research Projects/FSE 2016/UserEmails.csv"));
		//TreeMap<String, String> nameNEmails=new TreeMap<String, String>();
		ArrayList<String> Emails=new ArrayList<String>();
		String inputLine="";
		while ((inputLine = br.readLine()) != null)
		{
			try{
				Emails.add(inputLine.trim());
			}
			catch(Exception ex) {
				//Thread.currentThread().interrupt();
			}
		}
		br.close();
		
		TreeMap<String, String> nameEmails=new TreeMap<>();
		ArrayList<String> oldFiles=new ArrayList<>();
		oldFiles.add("GitHubUsersFinal1");
		oldFiles.add("GitHubUsersFinal2");
		oldFiles.add("GitHubUsersFinal3");
		oldFiles.add("GitHubUsersFinal4");
		oldFiles.add("GitHubUsersFinal5");
		oldFiles.add("GitHubUsersFinal6");
		
		for(String str: oldFiles){
			final BufferedReader brnew=new BufferedReader(new FileReader("E:/Research Projects/ICSE 2016/"+str+".csv"));
			while ((inputLine = brnew.readLine()) != null)
			{
				String [] split=inputLine.split(",");
				nameEmails.put(split[2].trim(), split[0]);
			}
		}
		
		TreeMap<String, String> newNameEmails=new TreeMap<>();
		for(String str:Emails){
			if(nameEmails.containsKey(str)){
				newNameEmails.put(str, nameEmails.get(str));
			}
			else{
				System.out.println(str);
			}
		}
		
		//System.out.println(newNameEmails.size());
		for(Map.Entry<String,String> entry : newNameEmails.entrySet()) {
			//System.out.println(entry.getValue()+","+entry.getKey());
		}
	}

	public static void getNamesFromEmails() throws IOException{
		final BufferedReader br=new BufferedReader(new FileReader("E:/Research Projects/FSE 2016/UserEmails.csv"));
		//TreeMap<String, String> nameNEmails=new TreeMap<String, String>();
		ArrayList<String> nameNEmails=new ArrayList<String>();
		String inputLine="";
		while ((inputLine = br.readLine()) != null)
		{
			try{
				nameNEmails.add(inputLine);
				//Thread.sleep(60000);
			}
			catch(Exception ex) {
				//Thread.currentThread().interrupt();
			}
		}
		br.close();
		
		
	}

}
