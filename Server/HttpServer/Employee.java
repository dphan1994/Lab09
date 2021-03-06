public class Employee {
	String start;	
	String first;
	String last;
	String dep;
	String num;
	String gender = "";
	String title = "";
	String[] Genders = {"male","female","other"};
	String[] Titles = {"Mr.", "Ms.", "Mrs.", "Dr.","Col.","Prof."};
	public Employee(String vars)
	{
		start = vars;
		String[] splitted = start.split("\\s+");
		first = splitted[0];
		last = splitted[1];
		dep = splitted[2];
		num = splitted[3];
	}
	public Employee(String a,String b,String c,String d, boolean[] e, boolean[] f)
	{
		
		first = a;
		last = b;
		dep = c;
		num = d;
		
		for(int i = 0; i < e.length; i++)
		{
			if(e[i] == true)
			{
				gender = Genders[i];
			}
		}
		for(int i = 0; i < f.length; i++)
		{
			if(f[i] == true)
			{
				title = Titles[i];
			}
		}
	}
	
// 	public String toString()
// 	{
// 		String ret = "<br> " + title + "</br>" 
// 				+ "<br> " + first + "</br>"
// 				+ "<br> " + last + "</br>"
// 				+ "<br> " + dep + "</br>"
// 				+ "<br> " + num + "</br>"
// 				+ "<br> " + gender + "</br>";
		
// 		return ret;
// 	}
	
	public String toTable() {
        return "<tr>\n"
                + "<td>" + title + "</td>\n"
                + "<td>" + first + "</td>\n"
                + "<td>" + last + "</td>\n"
                + "<td>" + dep + "</td>\n"
                + "<td>" + num + "</td>\n"
                + "<td>" + gender + "</td>\n"
                + "</tr>";
    }
	public void print()
	{
		System.out.println();
		if(!title.equals(""))
		{	
			System.out.print(title);
		}
		System.out.print(last + ", " + first + ", " + num + ", " + dep);
		if(!gender.equals(""))
		{
			System.out.print(", " + gender);
		}
	}
	
	
}
