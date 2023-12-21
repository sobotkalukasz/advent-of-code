package pl.lsobotka.adventofcode.year_2020;

/*
 * https://adventofcode.com/2020/day/17
 * */
public class ConwayCubes {

    public final static char INACTIVE = '.';
    public final static char ACTIVE = '#';

    private char[][][] space;
    private char[][][][] hyper;
    private int w,z,y,x;

    public long calculate(char[][][] initialState, int cycles, char toCount){
        init(initialState);

        while(cycles-- != 0){
            expandSpace();
            changeState();
        }
        return countState(toCount);
    }

    public long calculate(char[][][][] initialState, int cycles, char toCount){
        init(initialState);

        while(cycles-- != 0){
            expandHyperSpace();
            changeHyperState();
        }
        return countHyperState(toCount);
    }

    private void init(char[][][] initialState){
        space = initialState;
        z = space.length;
        y = space[0].length;
        x = space[0][0].length;
    }

    private void init(char[][][][] initialState){
        hyper = initialState;
        w = hyper.length;
        z = hyper[0].length;
        y = hyper[0][0].length;
        x = hyper[0][0][0].length;
    }

    private void expandSpace(){
        int tempZ = this.z+2;
        int tempY = this.y+2;
        int tempX = this.x+2;

        char[][][] temp = new char[tempZ][tempY][tempX];

        for(int z = 0; z < tempZ; z++){
            for(int y = 0; y < tempY; y++){
                for(int x = 0; x < tempX; x++){
                    if(z == 0 || y == 0 || x == 0 || z == tempZ-1 || y == tempY-1|| x == tempX-1){
                        temp[z][y][x] = INACTIVE;
                    } else {
                        temp[z][y][x] = space[z-1][y-1][x-1];
                    }
                }
            }
        }
        init(temp);
    }

    private void expandHyperSpace(){
        int tempW = w+2;
        int tempZ = z+2;
        int tempY = y+2;
        int tempX = x+2;

        char[][][][] temp = new char[tempW][tempZ][tempY][tempX];


        for(int w = 0; w < tempW; w++){
            for(int z = 0; z < tempZ; z++){
                for(int y = 0; y < tempY; y++){
                    for(int x = 0; x < tempX; x++){
                        if(w == 0 || z == 0 || y == 0 || x == 0 || w == tempW-1 || z == tempZ-1 || y == tempY-1|| x == tempX-1){
                            temp[w][z][y][x] = INACTIVE;
                        } else {
                            temp[w][z][y][x] = hyper[w-1][z-1][y-1][x-1];
                        }
                    }
                }
            }
        }
        init(temp);
    }

    private void changeState(){
        char[][][] temp = new char[this.z][this.y][this.x];
        for(int z = 0; z < this.z; z++){
            for(int y = 0; y < this.y; y++){
                for(int x = 0; x < this.x; x++){
                    temp[z][y][x] = shouldSwitch(z, y, x) ?  getOpposite(space[z][y][x]) : space[z][y][x];
                }
            }
        }
        init(temp);
    }

    private void changeHyperState(){
        char[][][][] temp = new char[w][z][y][x];
        for(int w = 0; w < this.w; w++){
            for(int z = 0; z < this.z; z++){
                for(int y = 0; y < this.y; y++){
                    for(int x = 0; x < this.x; x++){
                        temp[w][z][y][x] = shouldSwitch(w, z, y, x) ?  getOpposite(hyper[w][z][y][x]) : hyper[w][z][y][x];
                    }
                }
            }
        }
        init(temp);
    }

    private boolean shouldSwitch(int tz, int ty, int tx){
        char state = space[tz][ty][tx];
        int count = 0;

        for(int z = -1; z <= 1; z++){
            for(int y = -1; y <= 1; y++){
                for(int x = -1; x <= 1; x++){
                    if(z == 0 && y == 0 && x == 0) continue;
                    if(validate(tz+z,ty+y,tx+x)) count++;
                }
            }
        }
        if(state == ACTIVE){
            return !(count == 2 || count == 3);
        }
        return count == 3;
    }

    private boolean shouldSwitch(int tw, int tz, int ty, int tx){
        char state = hyper[tw][tz][ty][tx];
        int count = 0;

        for(int w = -1; w <= 1; w++){
            for(int z = -1; z <= 1; z++){
                for(int y = -1; y <= 1; y++){
                    for(int x = -1; x <= 1; x++){
                        if(w == 0 && z == 0 && y == 0 && x == 0) continue;
                        if(validate(tw+w, tz+z,ty+y,tx+x)) count++;
                    }
                }
            }
        }

        if(state == ACTIVE){
            return !(count == 2 || count == 3);
        }
        return count == 3;
    }

    private boolean validate(int tz, int ty, int tx){
        if(tz >= 0 && tz < z){
            if(ty >= 0 && ty < y){
                if(tx >= 0 && tx < x){
                    return space[tz][ty][tx] == ACTIVE;
                }
            }
        }
        return false;
    }

    private boolean validate(int tw, int tz, int ty, int tx){
        if(tw >= 0 && tw < w){
            if(tz >= 0 && tz < z){
                if(ty >= 0 && ty < y){
                    if(tx >= 0 && tx < x){
                        return hyper[tw][tz][ty][tx] == ACTIVE;
                    }
                }
            }
        }
        return false;
    }

    private char getOpposite(char state){
        return state == INACTIVE ? ACTIVE : INACTIVE;
    }

    private int countState(char state){
        int count = 0;
        for(int z = 0; z < this.z; z++){
            for(int y = 0; y < this.y; y++){
                for(int x = 0; x < this.x; x++){
                   if(space[z][y][x] == state) count++;
                }
            }
        }
        return count;
    }

    private int countHyperState(char state){
        int count = 0;
        for(int w = 0; w < this.w; w++){
            for(int z = 0; z < this.z; z++){
                for(int y = 0; y < this.y; y++){
                    for(int x = 0; x < this.x; x++){
                        if(hyper[w][z][y][x] == state) count++;
                    }
                }
            }
        }
        return count;
    }

}
