import java.io.*;
import java.util.*;

public class StudentRecordProcessor {

    private final List<Student> students = new ArrayList<>();
    private double averageScore;
    private Student highestStudent;

    /**
     * Task 1 + Task 2 + Task 5 + Task 6
     */
    public void readFile() {
        try (BufferedReader read = new BufferedReader(new FileReader("input/students.txt"))) {
            String line;

            while ((line = read.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // пропускаем пустые строки

                String[] parts = line.split(",");
                if (parts.length < 2) continue; // если нет имени или оценки — пропускаем

                String name = parts[0].trim();
                String scoreStr = parts[1].trim();

                // фильтр мусора: имя должно содержать хотя бы одну букву
                if (!name.matches(".*[a-zA-Z].*")) continue;

                try {
                    double score = Double.parseDouble(scoreStr);
                    if (score < 0 || score > 100) {
                        throw new InvalidScoreException("Score out of range");
                    }
                    students.add(new Student(name, score));
                    System.out.println(line); // вывод валидной строки
                } catch (NumberFormatException | InvalidScoreException e) {
                    System.out.println("Invalid data: " + line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: input/students.txt");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Task 3 + Task 8
     */
    public void processData() {
        if (students.isEmpty()) {
            System.out.println("No valid students data");
            return;
        }

        students.sort((a, b) -> Double.compare(b.score, a.score));

        highestStudent = students.get(0);

        double sum = 0;
        for (Student s : students) {
            sum += s.score;
        }
        averageScore = sum / students.size();
    }

    /**
     * Task 4 + Task 5 + Task 8
     */
    public void writeFile() {
        if (students.isEmpty()) {
            System.out.println("Nothing to write");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output/report.txt"))) {

            writer.write("Average: " + String.format("%.1f", averageScore));
            writer.newLine();

            writer.write("Highest: " + highestStudent.name + " - " + highestStudent.score);
            writer.newLine();

            writer.newLine();
            writer.write("All students (sorted):");
            writer.newLine();

            for (Student s : students) {
                writer.write(s.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        StudentRecordProcessor processor = new StudentRecordProcessor();

        try {
            processor.readFile();
            processor.processData();
            processor.writeFile();
            System.out.println("Processing completed. Check output/report.txt");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}

class InvalidScoreException extends Exception {
    public InvalidScoreException(String mess) {
        super(mess);
    }
}

class Student {
    String name;
    double score;

    public Student(String name, double score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return name + " - " + score;
    }
}