package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class GetBugFixes {
	public static void main(String[] args) throws Exception {
		getCommitData();
	}

	//Get Commit Data from CommitLogs & Store in DB
	public static void getCommitData() throws Exception
	{

		final BufferedReader br = new BufferedReader(new FileReader("E:/Research Projects/ISSTA 2016/Repos/httpclient/log")); 

		String str ="";
		String line="";

		String author="";
		String date="";
		String comment="";
		String commit="";

		boolean merge=false;
		TreeMap<String,String> commitComment=new TreeMap<String, String>();

		//line=br.readLine();
		boolean alreadyContains=false;

		while ((line= br.readLine())!= null && line.length()!=0) { 


			while(!(line.startsWith("commit ")))
			{

				boolean contains=false;
				String packageStart="";

				
				
				
				if(line.contains("Author:"))
				{
					String semi[]=line.split(":");
					author=semi[1];
				}
				else if(line.startsWith("Date:"))
				{
					//System.out.println(commit);
					String semi[]=line.split("Date:");
					date=semi[1];
				}
				else if(line.contains("Merge:"))
				{
					merge=true;

				}
				else
				{
					
					line.trim();
					if(!line.contains("git-svn-id")){
						comment=comment+line+"\n";
					}
					

				}

				if((line = br.readLine())==null)
				{

					break;
				}

				
				//System.out.println("======");
			}


			if(!merge)
			{
				if(commit.length()!=0)
				{
					if(comment.contains("error")||comment.contains("bug")||comment.contains("fix")||comment.contains("issue")||
							comment.contains("mistake")||comment.contains("incorrect")||comment.contains("fault")||
							comment.contains("defect")||comment.contains("flaw"))
					commitComment.put(commit, comment);
					//System.out.println(comment);
				}

			}

			if(line!=null)
			{
				if(line.contains("commit "))
				{
					String comm[]=line.split("commit ");
					commit=comm[1];
					merge=false;
				}
			}

		}

	}
}
