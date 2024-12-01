package console;

import code_clone.CloneCheck;
import huffman.mainDecode;
import huffman.mainEncode;
import metrices.*;
import searching.Search;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {

    private static String forwardDir;
    private static String currentPath;
    private Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        Command ob = new Command();
        ob.command();
    }

    public void command() throws IOException {
        while (true) {
            showPrompt();
            String choice = scan.nextLine().trim();

            if (choice.equalsIgnoreCase("help")) {
                showHelp();
            } else if (choice.equalsIgnoreCase("clone") || choice.equals("1")) {
                handleClone();
            } else if (choice.equalsIgnoreCase("File_Compress & File_Decompress") || choice.equals("2")) {
                handleFileCompression();
            } else if (choice.equalsIgnoreCase("search") || choice.equals("3")) {
                handleSearch();
            } else if (choice.equalsIgnoreCase("Metrics") || choice.equals("4")) {
                showMetricsMenu();
            } else if (choice.equalsIgnoreCase("exit") || choice.equals("5")) {
                System.exit(0);
            } else {
                handleDirectoryNavigation(choice);
            }
        }
    }

    private void showPrompt() {
        if (currentPath == null) {
            currentPath = getcurrentPath();
        }
        System.out.print(currentPath + ">");
    }

    private void showHelp() {
        System.out.println("\t1. clone");
        System.out.println("\t2. File_Compress & File_Decompress");
        System.out.println("\t3. Search");
        System.out.println("\t4. Metrics");
        System.out.println("\t5. exit");
    }

    private void showMetricsMenu() {
        System.out.println("\t4. Metrics");
        System.out.println("\t\tFile Count --> fc");
        System.out.println("\t\tMethod Count --> mc");
        System.out.println("\t\tLine of Code --> loc");
        System.out.println("\t\tAverage LOC of a class --> a_loc");
    }

    private void handleClone() throws IOException {
        System.out.println("\tSelect two projects:");
        projectPath();
    }

    private void handleFileCompression() {
        System.out.println("\tFor Compress --> fcom");
        System.out.println("\tFor Decompress --> dcom");

        String choice = scan.nextLine().trim();
        if (choice.equalsIgnoreCase("fcom")) {
            new mainEncode().Compress(currentPath);
        } else if (choice.equalsIgnoreCase("dcom")) {
            new mainDecode().Decompress(currentPath);
        }
    }

    private void handleSearch() {
        System.out.print("\tWrite \"query\" and projectname: ");
        String queryWithFile = scan.nextLine().trim();
        performSearch(queryWithFile);
    }

    private void handleDirectoryNavigation(String choice) {
        Pattern forwardPattern = Pattern.compile("(?i)\\b(cd)\\b\\s+(.+)");
        Matcher forwardMatcher = forwardPattern.matcher(choice);

        if (choice.equalsIgnoreCase("cd")) {
            currentPath = getcurrentPath();
        } else if (choice.matches("(?i)\\bcd\\b\\s*\\.\\.")) {
            currentPath = backDirectory(currentPath);
        } else if (forwardMatcher.matches()) {
            forwardDirectory(forwardMatcher.group(2));
        } else {
            System.out.println("'" + choice + "' is not recognized as a command");
        }
    }

    private void performSearch(String queryWithFile) {
        String newpath = pathGenerate(currentPath);
        try {
            String query = extractQuery(queryWithFile);
            String projectName = extractProjectName(queryWithFile);
            String projectPath = newpath + "\\" + projectName;

            if (isValidSearchInput(query, projectName)) {
                SearchResult(newpath, projectName, query);
            }
        } catch (Exception e) {
            System.out.println("Wrong Command");
        }
    }

    private boolean isValidSearchInput(String query, String projectName) {
        if ((projectName.isEmpty() && !query.isEmpty()) || (query.isEmpty() && !projectName.isEmpty())) {
            System.out.println("Wrong Command");
            return false;
        } else if (query.isEmpty() && projectName.isEmpty()) {
            System.out.println("Wrong Command");
            return false;
        }
        return true;
    }

    private String extractQuery(String queryWithFile) {
        return queryWithFile.substring(queryWithFile.indexOf("\"") + 1, queryWithFile.lastIndexOf("\"")).trim();
    }

    private String extractProjectName(String queryWithFile) {
        return queryWithFile.substring(queryWithFile.lastIndexOf("\"") + 1).trim();
    }

    private String getcurrentPath() {
        if (currentPath == null) {
            return FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        }
        return currentPath;
    }

    private void projectPath() throws IOException {
        ArrayList<String> projectList = new ArrayList<>(2);

        String firstProject = Input("\t\tFirst Project:");
        if (!isValidProjectName(firstProject)) {
            System.out.println("\tInvalid project name");
            return;
        }

        String secondProject = Input("\t\tSecond Project:");
        if (!isValidProjectName(secondProject)) {
            System.out.println("\tInvalid project name");
            return;
        }

        projectExist(firstProject);
        projectExist(secondProject);

        projectList.add(firstProject);
        projectList.add(secondProject);
        CloneCheck cloneCheck = new CloneCheck();
        cloneCheck.Code_clone(firstProject, secondProject);
    }

    private boolean isValidProjectName(String projectName) {
        return !projectName.isEmpty() && !projectName.contains(".");
    }

    private String Input(String prompt) {
        System.out.print(prompt);
        return scan.nextLine().trim();
    }

    private void forwardDirectory(String dirName) {
        String forwardPath = currentPath + "\\" + dirName;
        checkFileExist(forwardPath);
    }

    private void checkFileExist(String path) {
        Path p = Paths.get(path);
        try {
            if (Files.exists(p)) {
                currentPath = p.toString();
                forwardDir = currentPath;
            } else {
                System.out.println("The program cannot find the path specified.");
            }
        } catch (Exception e) {
            System.out.println("Invalid path");
        }
    }

    private String back
