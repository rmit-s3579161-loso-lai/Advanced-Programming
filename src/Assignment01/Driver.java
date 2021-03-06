package Assignment01;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;


/**Author: Loso
 * The role of the Driver is to control the games
 * which means it controls all the logic to execute each game round
 * Functions: processByUserInput()
 * - implement the specific methods by user input
 * 1. initialParticipantList()
 * 2. createGameByInput()
 * 3. displayCandidateList()
 * 4. displayAllGameResult()
 * 5. displayAllAthletePoints()
 * 6. findFile() <Add by Arion 01/04/17>
 */

public class Driver {
	
	public static HashMap<String, ArrayList<Participant>> participantList = new HashMap<String, ArrayList<Participant>>();
	
	public static Game currentGame;
	private ArrayList<Game> gameList = new ArrayList<Game>();	
	private ArrayList<String[]> fileList = new ArrayList<String[]>();
	private ArrayList<Participant> swimmerList = new ArrayList<Participant>();
	private ArrayList<Participant> cyclistList = new ArrayList<Participant>();
	private ArrayList<Participant> sprinterList = new ArrayList<Participant>();
	private ArrayList<Participant> superAthList = new ArrayList<Participant>();
	private ArrayList<Participant> officialList = new ArrayList<Participant>();
	private String writePath = "C:/Users/Arion/Desktop/Uni/Advanced Programming/Assignment 2/gameResults.txt";//this.getClass().getResource("gameResults.txt").getFile();
	private boolean appendToFile = true;
	
 	public Driver()
	{
		boolean bProcessResult = initialParticipantList();
		
		//data initialization fail
		if(!bProcessResult)    
			System.out.print("Failed to read file!!\n\n");
		else 
			System.out.print("Participant list read successfully!!\n\n");
	}
	
	public boolean processByUserInput(int userInput)
	{
		if(userInput != OzlympicGame.SELECT_GAME)
		{
			//check game status
			if(OzlympicGame.gameStatus == OzlympicGame.GAME_DEFAULT ||
					   currentGame == null)
				return DisplayMenuAndErrorMsg.errorMsg_GameUninitialized();	
		}
		
		//implement game control
		switch(userInput)
		{
			case OzlympicGame.SELECT_GAME:
			{
				//check game status to prevent initialization without executing
				if(OzlympicGame.gameStatus == OzlympicGame.GAME_INITIATED)
					return DisplayMenuAndErrorMsg.errorMsg_GameUnexecuted();
				
				Scanner reader = new Scanner(System.in);
				int gameType = 0;
				
				DisplayMenuAndErrorMsg.displayGameTypeMenu();
				String sInput = reader.next();
				try
				{
					//get game type
					gameType = Integer.parseInt(sInput);
				}
				catch(Exception e){
					DisplayMenuAndErrorMsg.showUserChoiceWarning(sInput);
				}
				
				boolean bValidate = DisplayMenuAndErrorMsg.inputValidation_Sub(gameType);
				if(!bValidate)
					break;
				
				// create a Game object by userInput
				createGameByInput(gameType);
				
				OzlympicGame.gameStatus = OzlympicGame.GAME_INITIATED;
			}
				break;
			case OzlympicGame.PREDICT_WINNER:
			{
				boolean bFuncWork = displayCandidateList();
				if(!bFuncWork)
					return true; //back to main menu
			}
				break;
			case OzlympicGame.START_GAME:
			{				
				//re-run game checking
				if(OzlympicGame.gameStatus == OzlympicGame.GAME_EXECUTED) {
					boolean bFuncWork = displayCandidateList();
					if(!bFuncWork)
						return true; //back to main menu
				currentGame.executeGame();
				}
				if(OzlympicGame.gameStatus == OzlympicGame.GAME_INITIATED) {
					boolean bExecuted = currentGame.executeGame();
					if(bExecuted)
						OzlympicGame.gameStatus = OzlympicGame.GAME_EXECUTED;
					}
					
					currentGame.setPredictWinner(null); //clear prediction choice	
			}
				break;
			case OzlympicGame.DISPLAY_FINALRESULT:
			{
				//display game result
				displayAllGameResults();
			}
				break;
			case OzlympicGame.DISPLAY_ATHLETEPOINTS:
			{
				displayAllAthletePoints();
			}
				break;
			case OzlympicGame.EXIT_GAME:
			default:
				return false;
		}
		return true;
	}
	
	private boolean initialParticipantList()
	{
		//Modified by Arion--------------------------
		Participant temp;	
		final int ID_INDEX = 0, TYPE_INDEX = 1, NAME_INDEX = 2, AGE_INDEX = 3, STATE_INDEX = 4;
		//Modified by Loso
		String rootPath = "C:/Users/Arion/Desktop/Uni/Advanced Programming/Assignment 1/Participants.txt";//this.getClass().getResource("Participants.csv").getFile();
		//File fileToBeFound = findFile(rootPath, "Participants.txt");
		File fileToBeFound = new File(rootPath);
		if(fileToBeFound == null)
			return fileNotFoundRecovery();
		//-------------------------------------------
	    BufferedReader br = null;
	    String line = "";
	    String cvsSplitBy = ",";
	    
		try {

            br = new BufferedReader(new FileReader(fileToBeFound.getAbsolutePath()));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                fileList.add(line.split(cvsSplitBy));
            }
            
            Set<String> unique_id = new HashSet<String>();
            
            for (int i=0; i<fileList.size(); i++){
            	if (fileList.get(i).length != 5) {
            		fileList.remove(i--);	
            	}
            	else if (!unique_id.add(fileList.get(i)[ID_INDEX])) {
            		fileList.remove(i--);	
            	}
            	else if (fileList.get(i)[ID_INDEX].isEmpty() || fileList.get(i)[TYPE_INDEX].isEmpty() || fileList.get(i)[NAME_INDEX].isEmpty() || fileList.get(i)[AGE_INDEX].isEmpty() || fileList.get(i)[STATE_INDEX].isEmpty()) {
            		fileList.remove(i--);	
            	}
            		
            }
          
            for (int i=0; i<fileList.size(); i++) {
       
            	if (fileList.get(i)[TYPE_INDEX].equalsIgnoreCase("swimmer")) {
            		temp = new Swimmer(fileList.get(i)[ID_INDEX], fileList.get(i)[NAME_INDEX],Integer.parseInt(fileList.get(i)[AGE_INDEX]),fileList.get(i)[STATE_INDEX]);
           			swimmerList.add(temp);
           		}
           		else if (fileList.get(i)[TYPE_INDEX].equalsIgnoreCase("sprinter")) {
           			temp = new Sprinter(fileList.get(i)[ID_INDEX], fileList.get(i)[NAME_INDEX],Integer.parseInt(fileList.get(i)[AGE_INDEX]),fileList.get(i)[STATE_INDEX]);
           			sprinterList.add(temp);
           		}
           		else if (fileList.get(i)[TYPE_INDEX].equalsIgnoreCase("cyclist")) {
           			temp = new Cyclist(fileList.get(i)[ID_INDEX], fileList.get(i)[NAME_INDEX],Integer.parseInt(fileList.get(i)[AGE_INDEX]),fileList.get(i)[STATE_INDEX]);
           			cyclistList.add(temp);
           		}
           		else if (fileList.get(i)[TYPE_INDEX].equalsIgnoreCase("official")) {
           			temp = new Official(fileList.get(i)[ID_INDEX], fileList.get(i)[NAME_INDEX],Integer.parseInt(fileList.get(i)[AGE_INDEX]),fileList.get(i)[STATE_INDEX]);       
           			officialList.add(temp);
            	}
            	else if (fileList.get(i)[TYPE_INDEX].equalsIgnoreCase("superathlete")) {
            		temp = new SuperAthlete(fileList.get(i)[ID_INDEX], fileList.get(i)[NAME_INDEX],Integer.parseInt(fileList.get(i)[AGE_INDEX]),fileList.get(i)[STATE_INDEX]);
           			superAthList.add(temp);
           			swimmerList.add(temp);
           			sprinterList.add(temp);
           			cyclistList.add(temp);
           		}
           	}
            
            //-------------------------------------------
            
    		participantList.put(Participant.SWIMMER, swimmerList);
    		participantList.put(Participant.CYCLIST, cyclistList);
    		participantList.put(Participant.SPRINTER, sprinterList);
    		participantList.put(Participant.OFFICIAL, officialList);		

        } catch (FileNotFoundException e) {
        	fileNotFoundRecovery();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
		return true;
	}
    public void writeToFile(String text){
    	try 
    	{
    	 FileWriter write = new FileWriter(writePath, appendToFile);
    	 PrintWriter printLine = new PrintWriter(write);
    	
    	 printLine.printf("%s" + "%n", text);
    	 printLine.close();
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    }
	private boolean createGameByInput(int gameType)
	{
		if(gameType == Game.GAME_RUNNING)
			currentGame = new Running();
		else if(gameType == Game.GAME_SWIMMING)
			currentGame = new Swimming();
		else
			currentGame = new Cycling();
		gameList.add(currentGame);
		
		//test - display gameID 
		System.out.println("\n" + "Game selected: " + currentGame.getGameID());
		return true;
	}
	private boolean displayCandidateList()
	{
		ArrayList<Athlete> candList = currentGame.getCandidate();
		if(candList != null)
		{
			System.out.print("\nPlease choose the athlete you think is going to win the game.\n\n" +
		                     "Candidate List\n" + "===================================\n");
			String candListMenu = "";
			for(int i=0 ; i<candList.size() ; i++)
			{
				Participant candInfo = candList.get(i);
				if(candInfo != null)//+ candInfo.getName() + "\n"; showing name probably is better
					candListMenu += Integer.toString(i+1) + ". " 
									+ candInfo.getName() + "\n"; 
			}
			System.out.print(candListMenu + "\nEnter an option:" );
			
			Scanner reader = new Scanner(System.in);
			String sInput = reader.next();
			int predict = 0;
			try
			{
				predict = Integer.parseInt(sInput);
			}
			catch (ArithmeticException e)
			{
				DisplayMenuAndErrorMsg.showOverflowWarning();
			}
			catch(Exception e)
			{
				DisplayMenuAndErrorMsg.showUserChoiceWarning(sInput);
			}
			
			boolean bValidate = DisplayMenuAndErrorMsg.inputValidation_Sub(predict, candList.size());
			if(!bValidate)
				return false;
			
			//save the prediction info.
			currentGame.setPredictWinner(candList.get(predict-1));
		}
		return true;
	}
	private void displayAllGameResults()
	{
		String result = "";
		for(int i=0 ; i<gameList.size() ; i++)
		{
			Game gameInfo = gameList.get(i);
			if(gameInfo != null)
				result += gameInfo.getGameResult();
		}
		System.out.println(result);
	}
	private void displayAllAthletePoints()
	{
		//need to remove all superAthletes from each list first
		swimmerList.removeAll(superAthList);
		String swimmersResult = "===== Swimmers result =====\n";
		for(int i=0 ; i<swimmerList.size() ; i++)
		{
			Participant swimmer = swimmerList.get(i);
			if(swimmer != null && swimmer instanceof Swimmer)
			{
				int point = ((Swimmer)swimmer).getPoints();
				swimmersResult += (swimmer.getName() + 
								   " -> " + Integer.toString(point) + "\n");
			}
		}
		System.out.println(swimmersResult);
		
		String cyclistsResult = "===== Cyclists result =====\n";
		cyclistList.removeAll(superAthList);
		for(int i=0 ; i<cyclistList.size() ; i++)
		{
			Participant cyclist = cyclistList.get(i);
			if(cyclist != null && cyclist instanceof Cyclist)
			{
				int point = ((Cyclist)cyclist).getPoints();
				cyclistsResult += (cyclist.getName() + 
								   " -> " + Integer.toString(point) + "\n");
			}
		}
		System.out.println(cyclistsResult);
		
		String sprintersResult = "===== Sprinters result =====\n";
		sprinterList.removeAll(superAthList);
		for(int i=0 ; i<sprinterList.size() ; i++)
		{
			Participant sprinter = sprinterList.get(i);
			if(sprinter != null && sprinter instanceof Sprinter)
			{
				int point = ((Sprinter)sprinter).getPoints();
				sprintersResult += (sprinter.getName() + 
									" -> " + Integer.toString(point) + "\n");
			}
		}
		System.out.println(sprintersResult);
		
		String superAthResult = "===== SuperAthlete result =====\n";
		for(int i=0 ; i<superAthList.size() ; i++)
		{
			Participant superAth = superAthList.get(i);
			if(superAth != null && superAth instanceof SuperAthlete)
			{
				int point = ((SuperAthlete)superAth).getPoints();
				superAthResult += (superAth.getName() + 
								   " -> " + Integer.toString(point) + "\n");
			}
		}
		System.out.println(superAthResult);
	}
	//Add by Arion
	private static final File findFile(final String rootFilePath, final String fileToBeFound) {

	    File rootFile = new File(rootFilePath);
	    File[] subFiles = rootFile.listFiles();
	    for (File file : subFiles != null ? subFiles : new File[] {}) {
	        if (file.getAbsolutePath().endsWith(fileToBeFound)) {
	            return file;
	        } else if (file.isDirectory()) {
	            File f = findFile(file.getAbsolutePath(), fileToBeFound);
	            if (f != null) {
	                return f;
	            }
	        }
	    }

	    return null; // null returned in case file is not found

	}
	private boolean fileNotFoundRecovery()
	{
		//setting dummy data here for testing	
		for(int i=0 ; i<6 ; i++)
		{
			//setting athlete
			//name format using (personType + id)
			String name = "ATH-" + Swimmer.SWIMMER 
							     + Integer.toString(i);
			int age = 20 + i;
			String id = "Oz000" +i;
			Participant swimmer = new Swimmer(id, name, age, "VIC");
			swimmerList.add(swimmer);
					
					
			name = "ATH-" + Cyclist.CYCLIST
				          + Integer.toString(i);
			Participant cyclist = new Cyclist(id, name, age, "VIC");
			cyclistList.add(cyclist);
					
			name = "ATH-" + Sprinter.SPRINTER
			              + Integer.toString(i);
			Participant sprinter = new Sprinter(id, name, age, "VIC");
			sprinterList.add(sprinter);
					
			name = "ATH-" + SuperAthlete.SUPERATHLETE 
		                  + Integer.toString(i);
			Participant superAthlete = new SuperAthlete(id, name, age, "VIC");
			superAthList.add(superAthlete);
					
			//for implement candidate list
			//adding superAth into each list
			swimmerList.add(superAthlete);
			cyclistList.add(superAthlete);
			sprinterList.add(superAthlete);
					
			//setting offical
			name = "OFF-" + Integer.toString(i);
			Participant offical = new Official(id, name, age, "VIC");
			officialList.add(offical);
		}
				
		participantList.put(Participant.SWIMMER, swimmerList);
		participantList.put(Participant.CYCLIST, cyclistList);
		participantList.put(Participant.SPRINTER, sprinterList);
		participantList.put(Participant.OFFICIAL, officialList);
		return true;
	}
}
