package email;


import java.io.BufferedReader;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class EmailTesting2 {
	public static void main(String args[]) throws Exception
	{
		getNamesNEmailsRemaining();
		//testMsgException();
	}

	//Get Names & emails of remaining email addresses
	public static void getNamesNEmailsRemaining() throws IOException, InterruptedException{
		final BufferedReader br=new BufferedReader(new FileReader("data/data.csv"));
		//TreeMap<String, String> nameNEmails=new TreeMap<String, String>();
		TreeMap<String, HashSet<String>> nameNEmails=new TreeMap<String, HashSet<String>>();
		String inputLine="";
		int count=0;
		while ((inputLine = br.readLine()) != null)
		{
			//System.out.println(inputLine);
			//System.exit(1);
			try{
				String[] split=inputLine.split("===");
				String [] splitName=split[0].split(" ");
				HashSet<String> repos=new HashSet<>();
				String[] splitRepos=split[3].trim().split(",");
				for(int i=0;i<splitRepos.length;i++){
					if(!splitRepos[i].trim().equals("")){
						repos.add(splitRepos[i]);
					}
				}

//				if(inputLine.contains("RS===sayrer@gmail.com")){
//					System.out.println(splitName.length);
//				}
				if(splitName.length>1){
					if(splitName[0].length()==2 || splitName[0].length()==3){	
						nameNEmails.put(splitName[0]+" "+splitName[1]+","+split[1].toLowerCase()+","+split[2],repos);
						//System.out.println(inputLine);
						//count++;
					}
					else{
						nameNEmails.put(splitName[0]+","+split[1].toLowerCase()+","+split[2],repos);
						//System.out.println(inputLine);
						//count++;
					}
				}
				else{
					nameNEmails.put(split[0]+","+split[1].toLowerCase()+","+split[2],repos);
				}

				//				count++;
				//				if(count==1000){
				//					break;
				//				}
				//Thread.sleep(60000);
			}
			catch(Exception ex) {
				//Thread.currentThread().interrupt();
			}
		}
		br.close();
		//System.out.println(count);
		//System.out.println(nameNEmails.size());
		//		try {
		//			TimeUnit.MINUTES.sleep(45);                //1000 milliseconds is one second.
		//		} catch(InterruptedException ex) {
		//			Thread.currentThread().interrupt();
		//		}
		sendEmailOutlookTLS(nameNEmails);
	}


	//Configure outlook
	public static void sendEmailOutlookTLS(TreeMap<String, HashSet<String>> remToSend) throws InterruptedException, IOException {
		final String username = "yunzhang28@zju.edu.cn";//xxia@zju.edu.cn//xinxiazjucn@gmail.com
		final String password = "900923";//smusis2012 //Sunday!19
		int tries = 0;

		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		//props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.host", "smtp.zju.edu.cn");//smtp.gmail.com //smtp.zju.edu.cn
		props.put("mail.smtp.port", "25");//gmail - 587 //zju - 25

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		//FileWriter fw = new FileWriter("E:/users/kochharps/Projects/FSE 2016/Emails/Exception.csv");
		//BufferedWriter bw = new BufferedWriter(fw);

		int count=0;
		boolean start=true;
		for(Map.Entry<String, HashSet<String>> entry: remToSend.entrySet()){
			try {
				//				if(start){
				//					TimeUnit.MINUTES.sleep(60);
				//					start=false;
				//				}
				String [] split=entry.getKey().split(",");
				String name=split[0];
				String email=split[1];
				int commits=Integer.parseInt(split[2]);
				//commits=5234;
				HashSet<String> repos=entry.getValue();

				if(commits>100000){
					commits=100000;
				}
				else if(commits>=50000 && commits<100000){
					commits=50000;
				}
				else if(commits>=25000 && commits<50000){
					commits=25000;
				}
				else if(commits>=10000 && commits<25000){
					commits=10000;
				}
				else if(commits>=5000 && commits<10000){
					commits=5000;
				}
				else if(commits>=2500 && commits<5000){
					commits=2500;
				}
				else if(commits>=2000 && commits<2500){
					commits=2000;
				}
				else if(commits>=1500 && commits<2000){
					commits=1500;
				}
				else if(commits>=1000 && commits<1500){
					commits=1000;
				}
				else if(commits>=500 && commits<1000){
					commits=500;
				}
				else if(commits>=400 && commits<500){
					commits=400;
				}
				else if(commits>=300 && commits<400){
					commits=300;
				}
				else if(commits>=200 && commits<300){
					commits=200;
				}
				else if(commits>=100 && commits<200){
					commits=100;
				}
				else if(commits>=50 && commits<100){
					commits=50;
				}
				else if(commits>=25 && commits<50){
					commits=25;
				}
				else if(commits>=10 && commits<25){
					commits=10;
				}

				int projectCount=0;
				String projects="";
				if(repos.size()==1){
					for(String str:repos){
						projects=str;
					}
				}
				if(repos.size()==2){
					for(String str:repos){
						projects=str+", "+projects;
					}
					projects=projects.substring(0,projects.length()-1);
				}
				if(repos.size()==3 || repos.size()>3){
					int countrep=0;
					for(String str:repos){
						projects=str+", "+projects;
						countrep++;
						if(countrep==3){
							break;
						}
					}
					projects=projects.substring(0,projects.length()-1);
				}

				//System.out.println(remToSend.size());
				//email="pavneets1988@gmail.com";//kochharps.2012@phdis.smu.edu.sg
				//abhisheksh.2014@phdis.smu.edu.sg
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("yunzhang28@zju.edu.cn")); //xxia@zju.edu.cn//xinxiazjucn@gmail.com
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(email));
				//message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("xxia@zju.edu.cn"));

				//System.out.println(remToSend.size());
				System.out.println(name);
				System.out.println(email);
				//System.out.println(commits);
				//System.out.println(projects);
				//System.exit(1);
				//				String mailBody = StringEscapeUtils.unescapeHtml4(
				//						" <a href=\"http://www.surveygizmo.com/s3/2269653/How-Practitioners-Perceive-Automated-Debugging-G\">"
				//								+ "<u><b>Click here to participate in the survey</b></u></a>");

				message.setSubject("What Makes a Good Test Case?");
				message.setContent("Dear " +name+","+
						"<br><br>We are researchers from Singapore and China who are studying characteristics of "
						+ "good test cases/suites, how practitioners write test cases and what are the issues practitioners "
						+ "commonly face related to writing and maintaining test cases. "
						+ "<br><br>"

						+ "We analyzed your commit history and find that you have contributed to project(s) on GitHub such as <b>"
						+ projects +"</b> and in more than "+commits+" commits you have added or edited a test file. "
						+ "Being an active member in the open-source community, we thought "
						+ "your suggestions would be highly useful to the software engineering research and practitioner "
						+ "community. We will be very grateful if you would participate in "
						+ "the following survey: <br><br>"

						+ "https://www.surveygizmo.com/s3/2625311/What-Makes-a-Good-Test-Case-G"
						
						+ "<br><br>Your response will be kept anonymous and only aggregate information "
						+ "will be reported. We would also like to request for your help to encourage "
						+ "your colleagues and friends to fill in the survey. If you have further questions, "
						+ "please just let us know.<br><br>"

						+ "As a token of our appreciation, we will do a raffle to give out a <b>50 USD Amazon</b> "
						+ "voucher each to two randomly selected participants. If you are interested in this "
						+ "raffle please fill in this email id at the end of the survey. "
						+ "<br><br>Thank you very much!"
						+ "<br><br>Yours Sincerely, "
						+ "<br><br>Yun Zhang,<br>PhD Student,"
						+ "<br>Zhejiang University, China"
						+ "<br> Email - yunzhang28@zju.edu.cn"
						+ "<br>"
						+ "<br>Xin Xia,<br>Research Assistant Professor,"
						+ "<br>Zhejiang University, China"
						+ "<br>http://mypage.zju.edu.cn/en/xinxia"
						+ "<br> Email - xxia@zju.edu.cn"
						+ "<br>"
						+ "<br>Shanping Li,<br>Professor,"
						+ "<br>Zhejiang University, China","text/html");

				message.setSentDate(new Date());
				Transport.send(message);
				//System.out.println(mailBody);
				count++;
				System.out.println("Done. Success count: " + count);
				//				if(count==50){
				//					TimeUnit.MINUTES.sleep(30);
				//					count=0;
				//				}
				try {
					TimeUnit.MINUTES.sleep((long)1.1);                //1000 milliseconds is one second.
					//count=0;
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				//continue;
				//}
				tries = 0;
				//System.exit(1);
			}
			catch (MessagingException e) {
				tries++;
				//System.out.println("Exception happens. Tries: " + tries);
				System.out.println(e.getMessage());
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				//System.out.println(dateFormat.format(date));

				PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter("data/exception.csv",true)));
				System.out.println("Exception Thrown");
				writer.write(entry.getKey()+"==="+entry.getValue());
				writer.write("\n");
				writer.close();
				//TimeUnit.MINUTES.sleep((long)Math.pow(1.1,tries+1)); 
				//System.exit(1);
				//				try {
				//					TimeUnit.MINUTES.sleep(60);                //1000 milliseconds is one second.
				//					count=0;
				//					bw.write(nameNEmail);
				//				} catch(InterruptedException ex) {
				//					Thread.currentThread().interrupt();
				//				}
				continue;
			}
			catch (NumberFormatException e) {
				System.out.println(e.getMessage());
				PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter("data/exception.csv",true)));
				System.out.println("Exception Thrown");
				writer.write(entry.getKey()+"==="+entry.getValue());
				writer.write("\n");
				writer.close();
				//System.exit(1);
				continue;
			}
		}
		//bw.close();
	}


	public static void testMsgException() throws IOException{
		for(int i=0;i<5;i++){
			try{
				throw new MessagingException();
			}
			catch(MessagingException e){
				PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter("E:/users/kochharps/Projects/FSE 2016/Emails/Exception.csv",true)));
				System.out.println("Exception Thrown");
				writer.write("abcd");
				writer.write("\n");
				writer.close();
			}
		}
	}

}
