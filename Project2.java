/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project2;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Tuna
 */
public class Project2 {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        Tree fileTree = new Tree();
        fileTree.Text("myfiles.txt");
        FileClass methods = new FileClass(fileTree);
        boolean end = true;
        while (end) {
            System.out.println("File System Menu:");
            System.out.println("1- List Contents");
            System.out.println("2- Add Directory");
            System.out.println("3- Add File");
            System.out.println("4- Delete Directory");
            System.out.println("5- Delete File");
            System.out.println("6- Search by Name");
            System.out.println("7- Search by Extension");
            System.out.println("8- Display Path");
            System.out.println("9- Exit");
            System.out.print("Choose an option: ");
            int select = input.nextInt();
            switch (select) {
                case 1:
                    System.out.print("Plase enter directory name to list contents: ");
                    String DName = input.next();
                    methods.listContents(DName);
                    break;
                case 2:
                    System.out.print("Plase enter parent directory name: ");
                    String parentD = input.next();
                    System.out.print("Plase enter new directory name: ");
                    String dirName = input.next();
                    methods.addDirectory(parentD, dirName);
                    break;
                case 3:
                    System.out.print("Plase enter parent directory name: ");
                    String parentDi = input.next();
                    System.out.print("Plase enter file name: ");
                    String fileName = input.next();
                    System.out.print("Plase enter file size: ");
                    int size = input.nextInt();
                    System.out.print("Plase enter extension type: ");
                    String ext = input.next();
                    methods.addFile(parentDi, fileName, ext, "", size, "USER");
                    break;
                case 4:
                    System.out.print("Plase enter directory name for delete: ");
                    String DeleteDName = input.next();
                    methods.deleteDirectory(DeleteDName);
                    break;
                case 5:
                    System.out.print("Plase enter parent directory name: ");
                    String parentDir = input.next();
                    System.out.print("Plase enter file name for delete: ");
                    String DeleteFName = input.next();
                    methods.deleteFile(DeleteFName, parentDir);
                    break;
                case 6:
                    System.out.print("Plase enter name for search: ");
                    String searchName = input.next();
                    if (methods.searchByName(searchName)) {
                        System.out.println(searchName + " found.");
                    } else {
                        System.out.println(searchName + " not found.");
                    }
                    break;
                case 7:
                    System.out.print("Plase enter file extension for search: ");
                    String searchExt = input.next();
                    methods.searchByExtension(searchExt);
                    break;
                case 8:
                    System.out.print("Plase enter file or directory name for display path: ");
                    String displayName = input.next();
                    System.out.println(methods.displayPath(displayName, fileTree.getRoot()));
                    break;
                case 9:
                    System.out.println("Exit.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choose try again.");
            }
        }
    }
}

class FileNode {
    private String name;
    private String extension;
    private String date;
    private int size;
    private String accessLevel;

    public FileNode(String name, String extension, String date, int size, String accessLevel) {
        this.name = name;
        this.extension = extension;
        this.date = date;
        this.size = size;
        this.accessLevel = accessLevel;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public int getSize() {
        return size;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "File: " + name + " , " + size + " bytes , " + accessLevel + " , date: " + date;
    }
}

class DirectoryNode {
    private String name;
    private String date;
    private int size = 0;
    private String accessLevel = "USER";
    private ArrayList<FileNode> files = new ArrayList<>();
    private ArrayList<DirectoryNode> dirs = new ArrayList<>();

    public DirectoryNode(String name) {
        this.name = name;
    }

    public int calculateSize() {
        int totalSize = 0;
        for (int i = 0; i < files.size(); i++) {
            FileNode file = files.get(i);
            totalSize += file.getSize();
        }

        for (int i = 0; i < dirs.size(); i++) {
            DirectoryNode subdir = dirs.get(i);
            totalSize += subdir.calculateSize();
        }

        this.size = totalSize;
        return totalSize;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getSize() {
        return size;
    }

    public void calculateSize(int size) {
        this.size += size;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public ArrayList<FileNode> getFiles() {
        return files;
    }

    public ArrayList<DirectoryNode> getDirs() {
        return dirs;
    }

    @Override
    public String toString() {
        return "Directory: " + name + " , size: " + size + " bytes , access: " + accessLevel + ")";
    }
}

class Tree {

    private DirectoryNode root;

    public Tree() {
        this.root = new DirectoryNode("root");
    }

    public void Text(String filename) throws IOException {
        Scanner scn = new Scanner(new File(filename));
        String line;
        DirectoryNode currentDirectory = root;
        ArrayList<DirectoryNode> parentDirs = new ArrayList<>();
        parentDirs.add(root);

        while (scn.hasNextLine()) {
            line = scn.nextLine();
            int counter = 0;
            while (counter < line.length() && line.charAt(counter) == '\t') {
                counter++;
            }
            line = line.substring(counter).trim();
            if (line.startsWith("\\")) {
                String dirName = line.substring(1).trim();
                DirectoryNode newDir = new DirectoryNode(dirName);

                while (parentDirs.size() > counter + 1) {
                    parentDirs.remove(parentDirs.size() - 1);
                }
                parentDirs.get(parentDirs.size() - 1).getDirs().add(newDir);
                parentDirs.add(newDir);

            } else if (line.contains("##")) {
                String[] parts = line.split("##");
                String fileName = parts[0].trim();
                String lastModified = parts[1].trim();
                int size = Integer.parseInt(parts[2]);
                String accessLevel = parts[3].trim();

                while (parentDirs.size() > counter + 1) {
                    parentDirs.remove(parentDirs.size() - 1);
                }

                FileNode newFile = new FileNode(fileName, fileName.substring(fileName.lastIndexOf('.') + 1), lastModified, size, accessLevel);
                parentDirs.get(parentDirs.size() - 1).getFiles().add(newFile);
            }
        }
        scn.close();

    }

    protected void updateSize(DirectoryNode x, int size) {
        x.calculateSize(size);
        if (!x.getAccessLevel().equals("SYSTEM")) {
            x.setAccessLevel("USER");
        }
    }

    public DirectoryNode getRoot() {
        return root;
    }

}

class FileClass {
    private Tree fileSys;
    
    public FileClass(Tree fileSystem) {
        this.fileSys = fileSystem;
    }

    public DirectoryNode searchDirectory(DirectoryNode temp, String name) {
        if (temp.getName().equals(name)) {
            return temp;
        }
        for (int i = 0; i < temp.getDirs().size(); i++) {
            DirectoryNode sonuc = searchDirectory(temp.getDirs().get(i), name);
            if (sonuc != null) {
                return sonuc;
            }
        }
        return null;
    }

    public void addDirectory(String parentName, String newDirectory) {
        DirectoryNode parent = searchDirectory(fileSys.getRoot(), parentName);
        if (parent != null) {
            DirectoryNode newDir = new DirectoryNode(newDirectory);
            parent.getDirs().add(newDir);
        } else {
            System.out.println("Parent directory is not found.");
        }
    }

    public void addFile(String parentName, String fileName, String extension, String date, int size, String access) {
        Date Date = new Date();
        int d = Date.getDate();
        int m = Date.getMonth() + 1;
        int y = Date.getYear() + 1900;
        date = d + "." + m + "." + y;

        DirectoryNode parent = searchDirectory(fileSys.getRoot(), parentName);
        if (parent != null) {
            FileNode newFile = new FileNode(fileName + "." + extension, extension, date, size, access);
            parent.getFiles().add(newFile);
            fileSys.updateSize(parent, size);        
        } else {
            System.out.println("Directory is not found.");
        }
    }

    public boolean deleteDirectory(String dirName) {
        DirectoryNode dir = searchDirectory(fileSys.getRoot(), dirName);
        if (dir == null) {
            System.out.println("Directory is not found.");
            return false;
        }

        if (!canDeleteDirectory(dir)) {
            System.out.println("Error because it contains SYSTEM level files or directories.");
            return false;
        }

        DirectoryNode parent = findParentDirectory(fileSys.getRoot(), dirName);
        if (parent != null) {
            for (int i = 0; i < parent.getDirs().size(); i++) {
                if (parent.getDirs().get(i).getName().equals(dirName)) {
                    parent.getDirs().remove(i);
                    System.out.println("Directory " + dirName + " is deleted.");
                    return true;
                }
            }
        }
        return false;
    }

    public DirectoryNode findParentDirectory(DirectoryNode temp, String child) {
        for (int i = 0; i < temp.getDirs().size(); i++) {
            DirectoryNode subDir = temp.getDirs().get(i);
            if (subDir.getName().equals(child)) {
                return temp;
            }
            DirectoryNode result = findParentDirectory(subDir, child);
            if (result != null) {
                return result;
            }
        }

        for (int i = 0; i < temp.getFiles().size(); i++) {
            if (temp.getFiles().get(i).getName().equals(child)) {
                return temp;
            }
        }
        return null;
    }

    private boolean canDeleteDirectory(DirectoryNode directory) {
        for (int i = 0; i < directory.getFiles().size(); i++) {
            if (directory.getFiles().get(i).getAccessLevel().equals("SYSTEM")) {
                return false;
            }
        }
        for (int i = 0; i < directory.getDirs().size(); i++) {
            if (!canDeleteDirectory(directory.getDirs().get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean deleteFile(String fileName, String parentDirName) {
        DirectoryNode parentDirectory = searchDirectory(fileSys.getRoot(), parentDirName);
        if (parentDirectory == null) {
            System.out.println("Parent directory is not found.");
            return false;
        }

        for (int i = 0; i < parentDirectory.getFiles().size(); i++) {
            if (parentDirectory.getFiles().get(i).getName().equals(fileName) && parentDirectory.getFiles().get(i).getAccessLevel().equals("USER")) {
                parentDirectory.getFiles().remove(i);
                System.out.println("File " + fileName + " is deleted.");
                return true;
            }
        }

        System.out.println("File not found or access level is SYSTEM.");
        return false;
    }

    public boolean searchByName(String name) {
        ArrayList<DirectoryNode> visited = new ArrayList<>();
        visited.add(fileSys.getRoot());
        for (int i = 0; i < visited.size(); i++) {
            DirectoryNode current = visited.get(i);
            if (current.getName().equals(name)) {
                return true;
            }
            for (int j = 0; j < current.getFiles().size(); j++) {
                if (current.getFiles().get(j).getName().equals(name)) {
                    return true;
                }
            }
            for (int j = 0; j < current.getDirs().size(); j++) {
                visited.add(current.getDirs().get(j));
            }
        }
        return false;
    }

    public void searchByExtension(String extension) {
        ArrayList<DirectoryNode> DirNode = new ArrayList<>();
        ArrayList<FileNode> ExtFile = new ArrayList<>();
        DirNode.add(fileSys.getRoot());

        for (int i = 0; i < DirNode.size(); i++) {
            DirectoryNode current = DirNode.get(i);
            for (int j = 0; j < current.getFiles().size(); j++) {
                if (current.getFiles().get(j).getExtension().equals(extension)) {
                    ExtFile.add(current.getFiles().get(j));
                }
            }
            for (int j = 0; j < current.getDirs().size(); j++) {
                DirNode.add(current.getDirs().get(j));
            }
        }

        if (ExtFile.isEmpty()) {
            System.out.println(extension + " is not found.");
        } else {
            System.out.println(extension + " files:");
            for (int i = 0; i < ExtFile.size(); i++) {
                FileNode file = ExtFile.get(i);
                System.out.println(file.getName() + ", " + file.getSize() + " bytes, last date: " + file.getDate());
            }
        }
    }
    
    public String displayPath(String target, DirectoryNode temp) {
        if (temp.getName().equals(target)) {
            return temp.getName();
        }
        for (int i = 0; i < temp.getDirs().size(); i++) {
            String path = displayPath(target, temp.getDirs().get(i));
            if (!path.isEmpty()) {
                return temp.getName() + "/" + path;
            }
        }
        for (int i = 0; i < temp.getFiles().size(); i++) {
            if (temp.getFiles().get(i).getName().equals(target)) {
                return temp.getName() + "/" + temp.getFiles().get(i).getName();
            }
        }
        return "";
    }

    public void listContents(String dirName) {
        DirectoryNode directory = searchDirectory(fileSys.getRoot(), dirName);
        if (directory != null) {
            System.out.println("Details of " + dirName + ":");
            for (int i = 0; i < directory.getDirs().size(); i++) {
                DirectoryNode child = directory.getDirs().get(i);
                child.calculateSize();
                System.out.println(child);
            }
            for (int i = 0; i < directory.getFiles().size(); i++) {
                FileNode file = directory.getFiles().get(i);
                if (file.getAccessLevel().equals("USER")) {
                    System.out.println(file);
                }
            }
        } else {
            System.out.println("Directory is not found.");
        }
    }
}
