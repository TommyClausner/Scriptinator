
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
@SuppressWarnings("serial")
public class ScriptinatorException extends Exception {
	// Parameterless Constructor
	public ScriptinatorException() {
	}

	// Constructor that accepts a message
	public ScriptinatorException(String message) {
		super(message);
	}
}