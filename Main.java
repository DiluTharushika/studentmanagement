import java.util.Scanner;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        while (true) {
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.println("6. Show Statistics");
            System.out.println("7. Update Student");
            System.out.print("Choose an option: ");

            int choice;
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
                continue;
            }

            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id;
                    if (sc.hasNextInt()) {
                        id = sc.nextInt();
                        sc.nextLine();
                    } else {
                        System.out.println("Invalid ID. Please enter a number.");
                        sc.next();
                        break;
                    }

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    if (name.isBlank()) {
                        System.out.println("Error: Name cannot be empty.");
                        break;
                    }

                    System.out.print("Enter Marks: ");
                    double marks;
                    if (sc.hasNextDouble()) {
                        marks = sc.nextDouble();
                        sc.nextLine();

                        if (marks < 0 || marks > 100) {
                            System.out.println("Error: Marks must be between 0 and 100.");
                            break;
                        }
                    } else {
                        System.out.println("Invalid marks. Please enter a number.");
                        sc.next();
                        break;
                    }

                    boolean added = manager.addStudent(new Student(id, name, marks));
                    if (added) {
                        System.out.println("Student added successfully!");
                    } else {
                        System.out.println("Error: Student with this ID already exists.");
                    }
                    break;

                case 2:
                    List<Student> students = manager.getAllStudents();
                    if (students.isEmpty()) {
                        System.out.println("No students found.");
                    } else {
                        System.out.println("\n--- Student List ---");
                        System.out.format("%-10s %-20s %-10s\n", "ID", "NAME", "MARKS");
                        System.out.println("-------------------------------------------");
                        for (Student s : students) {
                            System.out.format("%-10d %-20s %-10.2f\n", s.id, s.name, s.marks);
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter ID to search: ");
                    if (sc.hasNextInt()) {
                        int searchId = sc.nextInt();
                        sc.nextLine();

                        Student found = manager.searchStudent(searchId);
                        if (found != null) {
                            System.out.println("Student found:");
                            System.out.println("ID: " + found.id + ", Name: " + found.name + ", Marks: " + found.marks);
                        } else {
                            System.out.println("Student not found.");
                        }
                    } else {
                        System.out.println("Invalid ID input.");
                        sc.next();
                    }
                    break;

                case 4:
                    System.out.print("Enter ID to delete: ");
                    if (sc.hasNextInt()) {
                        int deleteId = sc.nextInt();
                        sc.nextLine();

                        boolean deleted = manager.deleteStudent(deleteId);
                        if (deleted) {
                            System.out.println("Student deleted successfully.");
                        } else {
                            System.out.println("Student with this ID does not exist.");
                        }
                    } else {
                        System.out.println("Invalid ID input.");
                        sc.next();
                    }
                    break;

                case 5:
                    System.out.println("Goodbye!");
                    sc.close();
                    return;

                case 6:
                    List<Student> allStudents = manager.getAllStudents();

                    if (allStudents.isEmpty()) {
                        System.out.println("No students to show statistics.");
                    } else {
                        double average = manager.getAverageMarks();
                        System.out.println("Average Marks: " + String.format("%.2f", average));
                        System.out.println("Top Student(s): " + manager.getTopStudentsNames());
                    }
                    break;

                case 7:
                    System.out.print("Enter Student ID to Update: ");
                    if (sc.hasNextInt()) {
                        int updateId = sc.nextInt();
                        sc.nextLine();

                        Student existing = manager.searchStudent(updateId);
                        if (existing == null) {
                            System.out.println("Error: Student not found.");
                            break;
                        }

                        System.out.print("Enter New Name (or press enter to keep '" + existing.name + "'): ");
                        String newName = sc.nextLine();
                        if (newName.isBlank()) newName = existing.name;

                        System.out.print("Enter New Marks (or enter -1 to keep '" + existing.marks + "'): ");
                        double newMarks;
                        if (sc.hasNextDouble()) {
                            newMarks = sc.nextDouble();
                            sc.nextLine();
                            if (newMarks == -1) newMarks = existing.marks;
                            if (newMarks < 0 || newMarks > 100) {
                                System.out.println("Error: Marks must be between 0 and 100.");
                                break;
                            }
                        } else {
                            System.out.println("Invalid marks.");
                            sc.next();
                            break;
                        }

                        boolean updated = manager.updateStudent(new Student(updateId, newName, newMarks));
                        if (updated) {
                            System.out.println("Student updated successfully!");
                        } else {
                            System.out.println("Error updating student.");
                        }
                    } else {
                        System.out.println("Invalid ID.");
                        sc.next();
                    }
                    break;

                default:
                    System.out.println("Invalid choice. Please choose 1-7.");
            }
        }
    }
}