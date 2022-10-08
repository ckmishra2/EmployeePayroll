package employeePayroll;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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