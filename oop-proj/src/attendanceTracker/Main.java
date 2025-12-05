import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.regex.Pattern;

// ===============================================
// MAIN APPLICATION CLASS
// ===============================================
public class Main {

    static Student[] studentDatabase = new Student[50]; 
    static int studentCount = 0;
    
    static String[] availableSubjects = {
        "Object-Oriented Programming (OOP)",   
        "Database Management System (DBMS)",      
        "Computer Networking 1",      
        "Advanced Computer Programming",        
        "Physics",        
        "Discrete Math",      
        "Asean Literature",
        "PathFit 3"  
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=================================");
            System.out.println("  CICS ACADEMIC ATTENDANCE PORTAL");
            System.out.println("    (For 2nd Year IT Students)");
            System.out.println("=================================");
            System.out.println("\n============= User: =============");
            System.out.println("1. Student (Log or Check Records)");
            System.out.println("2. Professor (View All Records)");
            System.out.println("3. Exit System");
            System.out.print("Enter choice: ");

            int choice = 0;

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); 
            } catch (InputMismatchException e) {
                System.out.println("\n[ERROR] Invalid input! Please enter a number.");
                scanner.nextLine(); 
                continue; 
            }

            switch (choice) {
                case 1:
                    System.out.println("\n============= STUDENT PORTAL =============");
                    System.out.println("1. Log Attendance (Time In/Out)");
                    System.out.println("2. View My History");
                    System.out.println("3. Back to Main Menu");
                    System.out.print("Select Action: ");
                    System.out.print("");
                    
                    int studentAction = 0;
                    try {
                        studentAction = scanner.nextInt();
                        scanner.nextLine();
                    } catch(Exception e) { 
                        scanner.nextLine(); 
                        studentAction = 0; // Set to invalid to avoid falling through
                    }

                    if (studentAction == 1) {
                        String selectedSubject = selectSubject(scanner);
                        if (selectedSubject.equals("EXIT")) {
                            System.out.println("Returning to Main Menu...");
                            continue;
                        }

                        System.out.println("\n[!] FORMATTING MUST BE STRICTLY FOLLOWED [!]");
                        System.out.println("(Type 'EXIT' at any prompt to cancel)");
                        
                        // 1. NAME INPUT
                        String name = getStrictInput(scanner, "Enter Name (e.g., RAMIREZ, JHASPER)", true);
                        if (name.equals("EXIT")) {
                            System.out.println("\n[!] Action forfeited, NOT RECORDED");
                            continue; // Go back to main loop
                        }

                        // 2. SECTION INPUT
                        String section = getStrictInput(scanner, "Enter Section (e.g., BSIT-2109)", false);
                        if (section.equals("EXIT")) {
                            System.out.println("\n[!] Action forfeited, NOT RECORDED");
                            continue;
                        }

                        // 3. TIME IN INPUT
                        String in = getStrictInput(scanner, "Time In (e.g., 08:00 AM)", false);
                        if (in.equals("EXIT")) {
                            System.out.println("\n[!] Action forfeited, NOT RECORDED");
                            continue;
                        }

                        // 4. TIME OUT INPUT
                        String out = getStrictInput(scanner, "Time Out (e.g., 05:00 PM)", false);
                        if (out.equals("EXIT")) {
                            System.out.println("\n[!] Action forfeited, NOT RECORDED");
                            continue;
                        }
                        
                        // Only save if we didn't exit
                        Student s = new Student(name, section, in, out, selectedSubject);
                        
                        if(studentCount < studentDatabase.length) {
                            studentDatabase[studentCount] = s;
                            studentCount++;
                            System.out.println(">> Attendance Saved for " + selectedSubject + " successfully.");
                        } else {
                            System.out.println(">> Database full!");
                        }

                    } else if (studentAction == 2) {
                        System.out.println("\n-- VERIFY IDENTITY --");
                        System.out.println("\n[!] FORMATTING MUST BE STRICTLY FOLLOWED [!]");        
                        
                        String searchName = getStrictInput(scanner, "Enter Your Name (e.g., RAMIREZ, JHASPER)", true);
                        if (searchName.equals("EXIT")) {
                               System.out.println("\n[!] Action forfeited.");
                               continue;
                        }
                        
                        String searchSubject = selectSubject(scanner);
                        if (searchSubject.equals("EXIT")) {
                            System.out.println("Returning to Main Menu...");
                            continue;
                        }

                        System.out.println("\nSearching records for: " + searchName + " (" + searchSubject + ")...");
                        boolean found = false;
                        
                        System.out.println("-------------------------------------------------------------------");
                        System.out.printf("%-15s %-15s %-15s%n", "SECTION", "TIME IN", "TIME OUT");
                        System.out.println("-------------------------------------------------------------------");

                        for (int i = 0; i < studentCount; i++) {
                            Student s = studentDatabase[i];
                            if (s.getName().equals(searchName) && s.getSubject().equals(searchSubject)) {
                                System.out.printf("%-15s %-15s %-15s%n", 
                                    s.getSection(), s.getTimeIn(), s.getTimeOut());
                                found = true;
                            }
                        }
                        
                        if (!found) {
                            System.out.println("No records found. Ensure you matched the registered Uppercase format.");
                        }
                        System.out.println("-------------------------------------------------------------------");
                    } else if (studentAction == 3) {
                        System.out.println("Returning to Main Menu...");
                        continue;
                    } else {
                        System.out.println("Invalid selection.");
                    }
                    
                    handleSubMenu(scanner);
                    break;

                case 2:
                    System.out.print("Enter Professor Password (or type '0' to back): ");
                    String password = scanner.next();
                    scanner.nextLine(); 

                    if (password.equals("0")) {
                        System.out.println("Returning to Main Menu...");
                        continue;
                    }

                    if (password.equals("cics2025")) {
                        System.out.println("\n>> Access Granted.");
                        Professor prof = new Professor();
                        prof.showPortal(scanner); 
                    } else {
                        System.out.println("\n[!] ACCESS DENIED. Incorrect Password.");
                    }
                    
                    handleSubMenu(scanner);
                    break;

                case 3:
                    System.out.println("Shutting down system. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }

    /**
     * Enforces strict input format (Uppercase, optional letter-only check).
     */
    public static String getStrictInput(Scanner scanner, String prompt, boolean lettersOnly) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine();

            if (input.trim().isEmpty()) {
                System.out.println("[!] Input cannot be empty.");
                continue;
            }
            
            // Check for EXIT command (case insensitive)
            if (input.equalsIgnoreCase("EXIT")) {
                return "EXIT";
            }

            // Check for all caps
            if (!input.equals(input.toUpperCase())) {
                System.out.println("[!] Invalid Format: Please use CAPITAL LETTERS only.\n");
                continue;
            }

            // Check for letter-only constraint
            if (lettersOnly) {
                // Regex to find any digit
                if (Pattern.compile("[0-9]").matcher(input).find()) {
                    System.out.println("[!] Invalid Name: Numbers are not allowed. Use letters only.\n");
                    continue;
                }
            }

            return input; 
        }
    }

    /**
     * Prompts the user to select a subject from the list.
     */
    public static String selectSubject(Scanner scanner) {
        System.out.println("\n\n============= Select Subject: =============\n");
        for (int i = 0; i < availableSubjects.length; i++) {
            System.out.println((i + 1) + ". " + availableSubjects[i]);
        }
        System.out.println((availableSubjects.length + 1) + ". Back to Main Menu");
        System.out.print("Enter Choice (Number): ");
        
        int choice = 0;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); 
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Returning to main menu.");
            scanner.nextLine();
            return "EXIT"; // Treat as exit on bad input
        }

        if (choice > 0 && choice <= availableSubjects.length) {
            return availableSubjects[choice - 1];
        } else if (choice == availableSubjects.length + 1) {
            return "EXIT";
        } else {
            System.out.println("[!] Invalid choice. Returning to Main Menu.");
            return "EXIT";
        }
    }

    /**
     * Handles the menu presented after completing an action.
     */
    public static void handleSubMenu(Scanner scanner) {
        System.out.println("\nWhat would you like to do?");
        System.out.println("[1] Back to Main Menu");
        System.out.println("[2] Terminate System");
        System.out.print("Choice: ");
        
        try {
            int subChoice = scanner.nextInt();
            scanner.nextLine(); 
            if (subChoice == 2) {
                System.out.println("Terminating system...");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Returning to main menu.");
            scanner.nextLine(); 
        }
    }
}

// INTERFACE FOR ATTENDANCE ACTIONS AND ABSTRACTION
interface AttendanceActions {
    void timeIn();
    void timeOut();
}


// ABSTRACT BASE CLASS AND ENCAPSULATION

abstract class SchoolMember {
    private String name;
    private String role;

    public SchoolMember(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() { return name; }
    public String getRole() { return role; }

    public abstract void showPortal(Scanner scanner); //POLYMORPHISM
}

// INHERITANCE
class Student extends SchoolMember implements AttendanceActions {
    private String section;
    private String timeInRecord;
    private String timeOutRecord;
    private String subject; 

    public Student() {
        super("Unknown", "Student");
    }

    public Student(String name, String section, String timeIn, String timeOut, String subject) {
        super(name, "Student");
        this.section = section;
        this.timeInRecord = timeIn;
        this.timeOutRecord = timeOut;
        this.subject = subject;
    }

    @Override
    public void timeIn() {
        System.out.println("System: Recording Time In...");
    }

    @Override
    public void timeOut() {
        System.out.println("System: Recording Time Out...");
    }

    public String getSection() { return section; }
    public String getTimeIn() { return timeInRecord; }
    public String getTimeOut() { return timeOutRecord; }
    public String getSubject() { return subject; }

    @Override
    public void showPortal(Scanner scanner) {
        // Implementation is primarily done within Main's switch case
    }
    
    @Override
    public String toString() {
        return "Name: " + getName() + " | Subject: " + subject + " | IN: " + timeInRecord;
    }
}

// PROFESSOR CONCRETE CLASS

class Professor extends SchoolMember {
    
    public Professor() {
        super("Faculty", "Professor");
    }

    @Override
    public void showPortal(Scanner scanner) {
        System.out.println("\n--- PROFESSOR PORTAL ---");
        
        // Professor selects a subject to filter the records
        String subjectFilter = Main.selectSubject(scanner);
        if (subjectFilter.equals("EXIT")) {
            System.out.println("Returning to previous menu...");
            return;
        }
        
        System.out.println("Viewing Attendance Records for: " + subjectFilter);
        
        if (Main.studentCount == 0) {
            System.out.println("[!] No students have timed in yet.");
        } else {
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("%-30s %-15s %-15s %-15s%n", "NAME", "SECTION", "TIME IN", "TIME OUT");
            System.out.println("---------------------------------------------------------------------------");
            
            boolean found = false;
            for (int i = 0; i < Main.studentCount; i++) {
                Student s = Main.studentDatabase[i];
                
                if (s.getSubject().equals(subjectFilter)) {
                     System.out.printf("%-30s %-15s %-15s %-15s%n", 
                        s.getName(), s.getSection(), s.getTimeIn(), s.getTimeOut());
                     found = true;
                }
            }
            
            if (!found) {
                System.out.println("No students found for this subject.");
            }
            System.out.println("---------------------------------------------------------------------------");
        }
    }
}