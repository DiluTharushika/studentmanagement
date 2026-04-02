import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManager {

    public StudentManager() {
        createTable();
    }

    // Create table if not exists
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS students ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "marks REAL NOT NULL"
                + ")";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    // Add student with AUTOINCREMENT ID
    // Returns the generated ID, or -1 if failed
    public int registerStudent(String name, double marks) {
        String sql = "INSERT INTO students(name, marks) VALUES(?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setDouble(2, marks);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the newly generated ID
            }
        } catch (SQLException e) {
            System.err.println("Error registering student: " + e.getMessage());
        }
        return -1;
    }

    // CLI Compatibility: Add student with explicit ID
    public boolean addStudent(Student s) {
        String sql = "INSERT INTO students(id, name, marks) VALUES(?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, s.id);
            pstmt.setString(2, s.name);
            pstmt.setDouble(3, s.marks);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    // Get all students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("marks")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching students: " + e.getMessage());
        }

        return students;
    }

    // Search student by ID
    public Student searchStudent(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("marks")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error searching student ID: " + e.getMessage());
        }

        return null;
    }

    // Search students by name (Fuzzy Search)
    public List<Student> searchStudentsByName(String name) {
        List<Student> results = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                results.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("marks")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error searching student name: " + e.getMessage());
        }

        return results;
    }

    // Delete student by ID
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    // Update student by ID
    public boolean updateStudent(Student s) {
        String sql = "UPDATE students SET name = ?, marks = ? WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, s.name);
            pstmt.setDouble(2, s.marks);
            pstmt.setInt(3, s.id);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    // Get average marks
    public double getAverageMarks() {
        String sql = "SELECT AVG(marks) AS avg_marks FROM students";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("avg_marks");
            }

        } catch (SQLException e) {
            System.err.println("Error calculating average: " + e.getMessage());
        }

        return 0.0;
    }

    // ==========================================
    // UPDATED METHOD: Get ALL top students (handles ties)
    // ==========================================
    public List<Student> getTopStudents() {
        List<Student> topStudents = new ArrayList<>();
        
        // SQL to find students where marks equal the maximum marks in the table
        String sql = "SELECT * FROM students WHERE marks = (SELECT MAX(marks) FROM students)";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                topStudents.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("marks")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error getting top students: " + e.getMessage());
        }

        return topStudents;
    }
    
    // Optional: Helper method to get just the names as a String for display
    public String getTopStudentsNames() {
        List<Student> topStudents = getTopStudents();
        if (topStudents.isEmpty()) {
            return "-";
        }
        
        StringBuilder names = new StringBuilder();
        for (int i = 0; i < topStudents.size(); i++) {
            names.append(topStudents.get(i).name);
            if (i < topStudents.size() - 1) {
                names.append(", "); // Add comma between names
            }
        }
        return names.toString();
    }
}