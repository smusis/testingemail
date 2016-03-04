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

public class TestCasesEmailCoding {
	public static void main(String args[]) throws Exception
	{
		getNamesNEmailsRemaining();
	}

	//Get Names & emails of remaining email addresses
	public static void getNamesNEmailsRemaining() throws IOException, InterruptedException{
		final BufferedReader br=new BufferedReader(new FileReader("E:/Research Projects/FSE 2016/Emails/GitHubTopDevelopers.csv"));
		//TreeMap<String, String> nameNEmails=new TreeMap<String, String>();
		ArrayList<String> nameNEmails=new ArrayList<String>();
		String inputLine="";
		while ((inputLine = br.readLine()) != null)
		{
			//System.out.println(inputLine);
			try{
				String[] split=inputLine.split(",");
				String [] splitName=split[0].split(" ");
				if(splitName[0].length()==2){
					nameNEmails.add(splitName[0]+" "+splitName[1]+","+split[1].toLowerCase()+","+split[3]);
				}
				else{
					nameNEmails.add(splitName[0]+","+split[1].toLowerCase()+","+split[3]);
				}
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
				String [] split=nameNEmail.split(",");
				String name=split[0];
				String email=split[1];
				int commits=Integer.parseInt(split[2]);
				commits=5234;

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
				else if(commits>=1500 && commits<2500){
					commits=1500;
				}
				else if(commits>=1000 && commits<1500){
					commits=1000;
				}
				else if(commits>=500 && commits<1000){
					commits=500;
				}
				
				//email="kochharps.2012@phdis.smu.edu.sg";
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("xxia@zju.edu.cn"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(email));
				System.out.println(name);
				System.out.println(email);
				//System.exit(1);
				//				String mailBody = StringEscapeUtils.unescapeHtml4(
				//						" <a href=\"http://www.surveygizmo.com/s3/2269653/How-Practitioners-Perceive-Automated-Debugging-G\">"
				//								+ "<u><b>Click here to participate in the survey</b></u></a>");

				message.setSubject("Developer Coding Proficiency");
				message.setContent("Dear " +name+","+
						"<br><br>We are researchers from Singapore and China who are studying what factors "
						+ "practitioners view as important to determine coding proficiency, what knowledge is "
						+ "required to develop better code and what actions developers take to improve their coding skills. "
						+ "<br><br>"

						+"I analyzed your commit history and find that you have contributed to many projects on GitHub "
						+ "and made more than "+commits+" commits. Being an active member in the open-source community, I thought "
						+ "your suggestions would be highly useful to the software engineering research community. As such, "
						+ "I would like to get more information related to your contribution on GitHub, "
						+ "in particular to the coding proficiency.<br><br>"
						
						+ "https://www.surveygizmo.com/s3/2625336/Developer-Coding-Proficiency-G"
						
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
					TimeUnit.MINUTES.sleep((long)1.1);                //1000 milliseconds is one second.
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



}
