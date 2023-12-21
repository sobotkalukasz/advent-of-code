package pl.lsobotka.adventofcode.year_2020;

import java.util.*;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2020/day/24
 * */
public class LobbyLayout {
    Floor floor;
    LobbyLayout(List<String> directions){
        initFloor(directions);
    }

    public long flipTiles(){
        return floor.countBlackTiles();
    }

    public long applyDayRule(int days){
        while(days-- > 0){
            floor.changeTiles();
        }
        return floor.countBlackTiles();
    }

    private void initFloor(List<String> directions){
        floor = new Floor();
        directions.forEach(floor::flipTile);
    }

    private enum Color{
        WHITE, BLACK
    }
    private enum Direction{
        E("e"), SE("se"), SW("sw"), W("w"), NW("nw"), NE("ne");
        private final String value;
        Direction(String value){
            this.value = value;
        }
    }

    private static class Floor {
        Map<Point, Tile> floor;
        Tile center;

        Floor(){
            floor = new HashMap<>();
            center = new Tile(new Point(0, 0));
            floor.put(center.point, center);
        }

        public void flipTile(String direction){
            Tile current = center;
            int to = 1;
            while(!direction.isBlank()){
                String currentDir = direction.substring(0, to);
                if(Arrays.stream(Direction.values()).anyMatch(d->d.value.equals(currentDir))){
                    Direction dir = Direction.valueOf(currentDir.toUpperCase());
                    Point nextTile = getAdjacentTile(dir, current.point);
                    if(floor.containsKey(nextTile)){
                        current = floor.get(nextTile);
                    } else {
                        current = new Tile(nextTile);
                        floor.put(current.point, current);
                    }
                    direction = direction.substring(to);
                    to = 0;
                }
                to++;
            }
            current.flipSide();
        }

        public void changeTiles(){
            Set<Point> addPoints = new HashSet<>();
            Set<Point> toFlip = new HashSet<>();

            floor.forEach((p,t)-> {
                List<Point> points = Arrays.stream(Direction.values()).map(d -> getAdjacentTile(d, p)).collect(Collectors.toList());
                int blackCount = 0;
                for (Point point : points) {
                    if(floor.containsKey(point)){
                        blackCount += floor.get(point).isBlack() ? 1 : 0;
                    } else {
                        addPoints.add(point);
                    }
                }

                if(t.isBlack() && (blackCount == 0 || blackCount > 2)){
                    toFlip.add(p);
                } else if(!t.isBlack() && blackCount == 2) {
                    toFlip.add(p);
                }
            });

            addPoints.forEach(p -> floor.put(p, new Tile(p)));
            addPoints.forEach(p -> {
                List<Point> points = Arrays.stream(Direction.values()).map(d -> getAdjacentTile(d, p)).collect(Collectors.toList());
                int blackCount = 0;
                for (Point point : points) {
                    if(floor.containsKey(point)){
                        blackCount += floor.get(point).isBlack() ? 1 : 0;
                    }
                }
                if(blackCount == 2) {
                    toFlip.add(p);
                }
            });

            toFlip.forEach(p -> floor.get(p).flipSide());
        }

        private Point getAdjacentTile(Direction dir, Point point){
            return switch(dir){
                case E -> new Point(point.q+1, point.r);
                case SE -> new Point(point.q, point.r+1);
                case SW -> new Point(point.q-1, point.r+1);
                case W -> new Point(point.q -1, point.r);
                case NW -> new Point(point.q, point.r-1);
                case NE -> new Point(point.q+1, point.r-1);
            };
        }

        private long countBlackTiles(){
            return floor.values().stream().filter(Tile::isBlack).count();
        }

    }

    private static record Point(int q, int r){}

    private static class Tile{
        private final Point point;
        private Color color;

        Tile(Point point){
            this(point, Color.WHITE);
        }

        Tile(Point point, Color color){
            this.point = point;
            this.color = color;
        }

        public void flipSide(){
            color = color == Color.WHITE ? Color.BLACK : Color.WHITE;
        }

        public boolean isBlack(){
            return this.color == Color.BLACK;
        }

    }

}
