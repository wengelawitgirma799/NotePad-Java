package notepad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class FileReadWrite {
	public static String readfile(String fullpath) {
		String result="";
		try {
			BufferedReader reader= new BufferedReader(new FileReader(fullpath));
			StringBuilder sb = new StringBuilder();
			
			String line = null;
			while((line=reader.readLine())!= null) {
				sb.append(line + "\n");
			}
			result=sb.toString();
			reader.close();
		}catch(IOException exp) {
			JOptionPane.showMessageDialog(null, "File not Found!");
			result="";
		}
		return result;
	}
	public static boolean writefile(String fullpath,String text) {
		boolean res=true;
		try {
			FileWriter fileWriter = new FileWriter(fullpath);
			fileWriter.write(text);
			fileWriter.close();
		}catch(IOException exp) {
			JOptionPane.showMessageDialog(null, "File not Found!");
			res= false;
		}
		return res;
	}
}
