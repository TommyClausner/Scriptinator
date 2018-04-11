import java.io.FileNotFoundException;
import java.io.IOException;

/*   _______                              _             
 *  |__   __|                            ( )            
 *     | | ___  _ __ ___  _ __ ___  _   _|/ ___         
 *     | |/ _ \| '_ ` _ \| '_ ` _ \| | | | / __|        
 *     | | (_) | | | | | | | | | | | |_| | \__ \        
 *     |_|\___/|_| |_| |_|_| |_| |_|\__, | |___/        
 *   ____            _     _         __/ |              
 *  |  _ \          | |   (_)       |___/ |             
 *  | |_) | __ _ ___| |__  _ _ __   __ _| |_ ___  _ __  
 *  |  _ < / _` / __| '_ \| | '_ \ / _` | __/ _ \| '__| 
 *  | |_) | (_| \__ \ | | | | | | | (_| | || (_) | |    
 *  |____/ \__,_|___/_| |_|_|_| |_|\__,_|\__\___/|_|    
 *   ____   ___   ___   ___    _______ __  __           
 *  |___ \ / _ \ / _ \ / _ \  |__   __|  \/  |          
 *    __) | | | | | | | | | |    | |  | \  / |          
 *   |__ <| | | | | | | | | |    | |  | |\/| |          
 *   ___) | |_| | |_| | |_| |    | |  | |  | |          
 *  |____/ \___/ \___/ \___/     |_|  |_|  |_|                  
 */

// main class to run the program
public class Scriptinator implements Runnable {

	public static void main(String args[]) throws FileNotFoundException, IOException {
		(new Thread(new Scriptinator())).start();
	}

	@Override
	public void run() {
		new Playground();
	}
}