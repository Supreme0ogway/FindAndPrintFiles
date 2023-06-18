//Author: William Lattus
//This program, given a directory, will search through every directory/folder in that base direcotry and find every pdf or extention file asked for

//java imports
import java.util.Scanner;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

//************** need to fix *******************//
//*************** goal *************************//
//find all files with the extention within a directory
//to output all names into a file of choice
//*************** addations ********************//
///Users/williamlattus/Downloads

public class FindAllFilesInDirectory {
	public static void main(String[] args) {
		//create variables to be set in the do while
		Scanner scanner = new Scanner(System.in);
		String fileType;//extention
		String directory = "";
		boolean runBefore = false;
		List<String> allFilePaths = new ArrayList<>();//contains every chosen file of file type
		
		//runs once to get input... if it is valid then continue else do the loop again
		boolean fileMatchestype = false;
		do {
			if(runBefore) {
				System.out.println("Enter a valid file type.");
			} else {
				//enter information to be looked up
				System.out.println("Enter \"Q\" to quit the programh");
				System.out.println("Enter the local path to the directory you would like to search through.");
				File pathEx;//initialized here to check if file exists
				boolean runThroughPathExists = false;
				do {
					//error checking if filepath does not exist
					if(runThroughPathExists) {
						System.out.println("The file path entered does not exist. Enter a new file path.");
					}
					directory = scanner.nextLine();//gets all input from the path
					quit(directory, scanner);//force quit
					pathEx = new File(directory);
					runBefore = true;//marks true when it has been run before (applies only if a wrong file type was applied	
					runThroughPathExists = true;
				} while(!pathEx.exists());
			}
			//get the file type again or for the first time
			System.out.println("Enter the file extention you want to search for (enter \"ALL\" to print every file).\nExample: .pdf, .xlsx, .txt, .jpg, etc.");
			fileType = scanner.nextLine();
			quit(fileType, scanner);
			
			//exit condition
			if(fileType.matches("\\..+")) {
				fileMatchestype = true;
			} else if (fileType.equals("ALL")) {
				fileMatchestype = true;
			}
			
		} while(!fileMatchestype);//condition to run after running once
		
		//directory = "/Users/williamlattus/Downloads";
		//print output
		File rootDir = new File(directory);
		System.out.println("Base directory: " + directory);
		searchFiles(rootDir, "", fileType, allFilePaths);//(root directory, relative path (should be empty), extention, list to add filepaths to)
		
		//write to text file?
		System.out.println("\nDo you want to print the output to a file? (y/yes, n/no)");
		String outputFileChoice;
		boolean outputChoiceRunOnce = false;
		boolean correctInput = false;
		do {
			//error the first time re enter input
			if(outputChoiceRunOnce) {
				System.out.println("Answer either \"no\", \"n\", \"yes\", \"y\". Answer is not case sensitive.");
			}
			//get input
			outputFileChoice = scanner.next();//get answer
			quit(outputFileChoice, scanner);//force quit
			outputChoiceRunOnce = true;
			
			//checks if it is valid input
			if(outputFileChoice.toLowerCase().equals("yes") || outputFileChoice.toLowerCase().equals("no") 
					|| outputFileChoice.toLowerCase().equals("y") || outputFileChoice.toLowerCase().equals("n")) {
				correctInput = true;
			}
		} while(!correctInput);//if it is yes y no n
		
		//print output to file
		String outputFileType;
		if(outputFileChoice.toLowerCase().equals("yes") || outputFileChoice.toLowerCase().equals("y")) {
			System.out.println("Enter your desired file type. Options: \".txt\", \".csv\" (non formated excel file), ");
			//get a real file type and desired file type
			boolean desiredFileRunOnce = false;
			correctInput = false;//used later with checking for .txt or .csv
			do {
				if(desiredFileRunOnce) {
					System.out.println("Enter a valid output type. Options: \".txt\", \".csv\" (non formated excel file), ");
				}
				outputFileType = scanner.next();
				quit(outputFileType, scanner);
				desiredFileRunOnce = true;
				
				//condition to run while
				if(outputFileType.toLowerCase().equals(".txt") ||//funn until one of these conditions is true not false
					outputFileType.toLowerCase().equals(".csv")) {
					correctInput = true;
				}
				
			} while(!correctInput);
			
			//output file path decision
			String outputFilePathDir = "";
			boolean outputFilePathExists = false;
			boolean alreadySearched = false;
			String outputFileName = "";
			String outputFileNameAndDir;
			boolean nameScanned = false;
			boolean validOutputPath = false;//for checking the output path
			System.out.println("Enter a name for your file." + "\nThe file cannot contain invalid characters:\n(Windows): <, >, :, \", \\, |, ?, *" +
					"\n(MacOS, Unix, Linix): \"/\" (forward slash)" + "\nExample: \"newFile\", \"new_File\", \"new File\"" + 
					"\nDo not enter the extention like \".pdf\".");
			do {
				//save file name from previous attempt
				if(!alreadySearched) {
					//check for correct file name
					do {
						//System.out.println(scanner.nextLine());
						if(nameScanned) {
							System.out.println("Enter a valid file name.");
						}
						scanner.skip("[\r\n]+");
						outputFileName = scanner.nextLine();	
						quit(outputFileName, scanner);
						nameScanned = true;
					} while(outputFileName.contains("/") || outputFileName.equals("") || outputFileName.equals(null));//check for valid file name
				}
				//enter valid file path or enter path
				if(alreadySearched) {
					System.out.println("Enter a valid file path directory/folder for your output file.");
				} else {
					System.out.println("Enter the directory/folder path to store the output.");
				}
				//get the path
				outputFilePathDir = scanner.nextLine();
				quit(outputFilePathDir, scanner);
				//place to put the file
				outputFileNameAndDir = outputFilePathDir + File.separator + outputFileName + outputFileType;
				
				//check if it already exists
				File outputFileAlreadyExistsFile = new File(outputFilePathDir);
				if(outputFileAlreadyExistsFile.exists()) {
					
					outputFilePathExists = true;
				} else { outputFilePathExists = false; }
				
				//allowing the pass through for the while
				if(outputFilePathExists || !outputFilePathDir.equals("") || !outputFilePathDir.equals(null)) {
					validOutputPath = true;
				}
				//confirming the first loop is complete
				alreadySearched = true;
			} while(!validOutputPath);//if its not empty and it exists
			
			//output to specific type
			switch(outputFileType) {
				case ".txt":
					writeToTextFile(directory, outputFileNameAndDir, allFilePaths);//try catch built in
					break;
				case ".csv":
					writeToCSVFile(directory, outputFileNameAndDir, allFilePaths);
					break;
				default:
					writeToTextFile(directory, outputFileNameAndDir, allFilePaths);
			}
			
		}//end of if user answers yes
		
		scanner.close();
		System.out.println("Program Complete!");
	}
	
	//recursive to search through all files
	//prints each file to console
	//adds all file paths to list (modifies list does not return a new list)
	private static void searchFiles(File directory, String relativePath, String extention, List<String> allFP) {
		if(directory.isDirectory()) {
			//get all files in an array which are in the directory
			File[] files = directory.listFiles();
			
			if(files != null) {//check for files
				for(File f : files) {//iterate through all files
					
					String subRelativePath = relativePath + File.separator + f.getName();//use File.separator because in windows it is "\" and mac and unix it is "/"
                    searchFiles(f, subRelativePath, extention, allFP);
				}
			} else {//permissions are bad
				System.out.println("Unable to access directory beause of permissions. Either run as Administrator (for Windows) or\nallow access to \"Full Disk Access\" (MacOS or Unix)");
			}
		} else {//end of recursion//if the extention matches
			//just chosen files
			if (extention.isEmpty() || relativePath.toLowerCase().endsWith(extention.toLowerCase())) {
				allFP.add(relativePath);//adds every file to list
	            System.out.println("File: " + relativePath);
	        } else if(extention.equals("ALL")) {//prints every file
				allFP.add(relativePath);//adds every file to list
	        	System.out.println("File: " + relativePath);
	        }
		}
	}
	
	//prints the output of each file in a text document to be stored
	private static void writeToTextFile(String baseDirPath, String pathToWrite, List<String> listOfFilePaths) {
		
		try (FileWriter fileWriter = new FileWriter(pathToWrite)) {
			
			fileWriter.write("Base directory: " + baseDirPath + "\n\n");
			
			//loop through list printing all files
			for(String s : listOfFilePaths) {
				//get file name
				String fullFilePath = baseDirPath + s;
				Path fullPath = Paths.get(fullFilePath);
				String fileName = fullPath.getFileName().toString();
				//print
				fileWriter.write("File name: " + fileName + "\n");
				fileWriter.write("Relative path: " + s + "\n");
				fileWriter.write("Absolute path: " + baseDirPath + s + "\n\n");
			}
            
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }//end of try catch // fileWriter close
	}
	
	//prints the output of each file in a csv (no format excel document)
	private static void writeToCSVFile(String baseDirPath, String pathToWrite, List<String> listOfFilePaths) {
		try (FileWriter fileWriter = new FileWriter(pathToWrite)) {
			//write to file
			fileWriter.write("Base directory: ," + baseDirPath + "\n");
			fileWriter.write("File name:,Relative Path:,Absolute Path:\n");
			//loop through list printing all files
			for(String s : listOfFilePaths) {
				//get file name
				String fullFilePath = baseDirPath + s;
				Path fullPath = Paths.get(fullFilePath);
				String fileName = fullPath.getFileName().toString();
				//print
				fileWriter.write(fileName + "," + s + "," + baseDirPath + s + "\n");
			}
		} catch(IOException e) {//writer closed
			System.out.println("An error occurred while writing to the file: " + e.getMessage());
		}
	}
	
	//quit function to quit the program anywhere using input of "Q"
	private static void quit(String input, Scanner scanner) {
		if(input.equals("Q")) {
			scanner.close();
			System.out.println("Quitting program.");
			System.exit(0);
		}
	}
}