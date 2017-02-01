/*
created the class game to check it staticly
enum Cell can be changed later and player loginname can be sent instead
main method won`t be needed as methods will be called by msg handlers
this logic can help send which 3 cells made the when if needed to be sent to client
 */
package server;

/**
 *
 * @author mhesham
 */
public class Game {
    
    //enum for cell status should be kept in assets
    private enum Cell{ X, O};
    //"currentGame" will store game status
    private final Cell[][] currentGame ;
    private int incMove=0;
    public boolean Horizontal = true, Vertical = true, DiagonalOne = true, DiagonalTwo = true;

    //class constructor initiates new current game table with 3*3 dimensions
    public Game(){
        currentGame = new Cell[3][3];
        DiagonalOne = true;
    }
    
    //method to handle move request takes 3 params enum , position in game table x,y
    public boolean validateMove(Cell player,int x,int y){
        if(true){
            currentGame[x][y] = player;
            checkForWin(player,x,y);
            return true;
        }else{
            System.out.println("cell is occupied");
            return false;
        }
    }

    //method to check win horizontal, vertical and diagonal    
    public void checkForWin(Cell player,int x,int y){
        for(int i = 0; i < 3; i++){
            //horizontal check
            if(currentGame[x][i] != player){
                Horizontal = false;
            }
            //vertical check
            if(currentGame[i][y] != player){
                Vertical = false;
            }
        }
        //diagonals check
        if( x==y || x==2-y ){
            for(int i = 0; i < 3; i++){
                if(currentGame[i][i] != player)
                    DiagonalOne = false;
                if(currentGame[i][2-i] != player)
                    DiagonalTwo = false;
            }
        }else{
            DiagonalOne = false;
            DiagonalTwo = false;
        }
        if(Horizontal || Vertical || DiagonalOne || DiagonalTwo){
            System.out.println(player+" won");
        }else if (incMove==8) {
            System.out.println("Draw!!");
        }else{
            Horizontal = Vertical = DiagonalOne = DiagonalTwo = true;
            incMove++;
        }
    }
    

    public static void main(String[] args) {
        //when game starts between 2 players a game obj is created
        Game game=new Game();
        game.validateMove(Cell.X, 0, 1);
        game.validateMove(Cell.O, 0, 0);
        game.validateMove(Cell.X, 0, 2);
        game.validateMove(Cell.O, 1, 1);
        game.validateMove(Cell.X, 1, 0);
        game.validateMove(Cell.O, 1, 2);
        game.validateMove(Cell.X, 2, 0);
        game.validateMove(Cell.O, 2, 1);
        game.validateMove(Cell.X, 2, 2);
        
    }
    
}
