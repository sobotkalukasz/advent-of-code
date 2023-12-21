package pl.lsobotka.adventofcode.year_2020;

import java.util.List;

/*
 * https://adventofcode.com/2020/day/12
 * */
public class RainRisk {

    public int executeSimpleCommands(List<String> commands){
        Ship ship = new Ship();
        commands.forEach(ship::process);
        return Math.abs(ship.NS_position) + Math.abs(ship.WE_position);
    }

    public int executeWayPointCommands(List<String> commands){
        Ship ship = new Ship(1, 10);
        commands.forEach(ship::processWayPoint);
        return Math.abs(ship.NS_position) + Math.abs(ship.WE_position);
    }
}

class Ship{

    WayPoint waypoint;
    int NS_position = 0;
    int WE_position = 0;
    char[] directions = {'N','E','S','W'};
    int index = 1;

    Ship(){}
    Ship(int nsWaypoint, int weWaypoint){
        waypoint = new WayPoint(nsWaypoint, weWaypoint);
    }

    void process (String command){
        char commandType = command.charAt(0);
        int value = Integer.parseInt(command.substring(1));
        executeCommand(commandType, value);
        //System.out.println(print(command));
    }

    void processWayPoint(String command){
        if (command.charAt(0) == 'F') {
            moveForwardToWayPoint(Integer.parseInt(command.substring(1)));
        } else {
            waypoint.process(command);
        }
        //System.out.println(print(command));
    }

    private void executeCommand(char command, int value){
        switch (command) {
            case 'N' -> moveNS(value);
            case 'S' -> moveNS(-value);
            case 'E' -> moveWE(+value);
            case 'W' -> moveWE(-value);
            case 'F' -> executeCommand(directions[index], value);
            case 'R' -> rotate(value / 90);
            case 'L' -> rotate(-value / 90);
        }
    }

    private void moveNS(int value){
        NS_position += value;
    }

    private void moveWE(int value){
        WE_position += value;
    }

    private void rotate(int value){
        index += value;
        if(index > 3 || index < -3)
            index = index%4;
        if(index < 0) index += 4;
    }

    private void moveForwardToWayPoint(int value){
        moveNS(waypoint.NS_position * value);
        moveWE(waypoint.WE_position * value);
    }

    private String print(String command){
        return "Executing command: " + command + "\n" +
                (NS_position > 0 ? "N: " : "S: ") + NS_position + "\n" +
                (WE_position > 0 ? "E: " : "W: ") + WE_position + "\n" +
                "Direction: " + directions[index] + "\n" +
                (waypoint != null ? waypoint.print() : "") +
                "=======================" + "\n";
    }

    static class WayPoint{

        int NS_position;
        int WE_position;

        WayPoint(int NS_position, int WE_position){
            this.NS_position = NS_position;
            this.WE_position = WE_position;
        }

        private void process(String command){
            char commandType = command.charAt(0);
            int value = Integer.parseInt(command.substring(1));
            executeCommand(commandType, value);
        }

        private void executeCommand(char command, int value){
            switch (command) {
                case 'N' -> moveNS(value);
                case 'S' -> moveNS(-value);
                case 'E' -> moveWE(+value);
                case 'W' -> moveWE(-value);
                case 'R' -> rotate(value / 90);
                case 'L' -> rotate(-value / 90);
            }
        }

        private void moveNS(int value){
            NS_position += value;
        }

        private void moveWE(int value){
            WE_position += value;
        }

        private void rotate(int value){
            int ns = NS_position;
            int we = WE_position;

            if(value%2 == 0){
                NS_position = -ns;
                WE_position = -we;
            }
            if(value == 1 || value == -3){
                WE_position = ns;
                NS_position = -we;
            }

            if(value == -1 || value == 3){
                WE_position = -ns;
                NS_position = we;
            }
        }

        private String print(){
            return "WayPoint: " + "\n" +
                    (NS_position > 0 ? "N: " : "S: ") + NS_position + "\n" +
                    (WE_position > 0 ? "E: " : "W: ") + WE_position + "\n" +
                    WE_position + "\n";
        }
    }
}

