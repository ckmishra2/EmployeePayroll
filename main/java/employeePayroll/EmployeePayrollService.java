package employeePayroll;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

class EmployeePayrollData {
	int id;
	String name;
	double Salary;

	public EmployeePayrollData(int id2, String name2, double salary2) {
		id = id2;
		name = name2;
		Salary = salary2;
	}

}
/*creating an employee payroll services to read and write employee
 * payroll to a console
 */
public class EmployeePayrollService {
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	List<EmployeePayrollData> employeePayrollList;

	public EmployeePayrollService() {
	}

	public EmployeePayrollService(List<EmployeePayrollData> employeePayroll) {
		employeePayrollList = employeePayroll;
	}

	public static void main(String[] args) {
		ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);

		Scanner consoleInputReader = new Scanner(System.in);
		employeePayrollService.readEmployeePayrollDataScanner(consoleInputReader);
		employeePayrollService.writeEmployeePayrollData();
		try {
			String Url = "D:\\Chandrakala\\EclipseWorkspace\\EmployeePayrollService\\src\\main\\java\\employeePayroll\\employee.txt";
			File fileobj = new File(Url);
			
			if(fileobj.exists())
			{
				System.out.println("File already exits.");
			}
			else
			{
				if(fileobj.createNewFile()) {
					System.out.println("File created: "+fileobj.getName());
				}
				else {
					System.out.println("failed to create");  
				}
			}
			if(fileobj.exists())
			{
				System.out.println("File already exits. trying to delete");
				if(fileobj.delete())                      //returns Boolean value  
				{  
					System.out.println(fileobj.getName() + "File deleted");
				}  
				else  
				{  
					System.out.println("failed to delete");  
				}
			}
			else
			{
				System.out.println("File doesn't exits.");
			}
			
		}catch (IOException e) {
				System.out.println("An error occured.");
				e.printStackTrace();
			}		
		
	}

	private void readEmployeePayrollDataScanner(Scanner consoleInputReader) {
		System.out.println("Enter Employee ID: ");
		int id = consoleInputReader.nextInt();
		System.out.println("Enter Employee Name: ");
		String name = consoleInputReader.next();
		System.out.println("Enter Employee Salary: ");
		double salary = consoleInputReader.nextDouble();
		this.employeePayrollList.add(new EmployeePayrollData(id, name, salary));
	}

	private void writeEmployeePayrollData() {
		for (EmployeePayrollData employeePayrollData : employeePayrollList) {
			System.out.println("\nWriting Employee Payroll Roaster to console\n" + "id: " + employeePayrollData.id
					+ "\n" + "name: " + employeePayrollData.name + "\n" + "salary: " + employeePayrollData.Salary);
		}
	}
}