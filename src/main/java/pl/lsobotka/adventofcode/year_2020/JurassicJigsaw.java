package pl.lsobotka.adventofcode.year_2020;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2020/day/20
 * */
public class JurassicJigsaw {

    private static final int SEA_MONSTER_SIZE = 15;

    PuzzleBoard board;

    public JurassicJigsaw(List<String> input){
        board = new PuzzleBoard(input);
    }

    public long getCornerCode(){
        board.matchJigsaws();
        return board.countCornerCode();
    }

    public int roughWater() {
        board.matchJigsaws();
        board.puzzleJigsaw();
        int seaMonsters = findSeaMonsters(board.puzzle);
        return countWaves(board.puzzle) - seaMonsters*SEA_MONSTER_SIZE;

    }

    private int countWaves(List<String> strings){
        return (int) strings.stream().mapToLong(str -> str.codePoints().filter(c -> c == '#').count()).sum();
    }

    private int findSeaMonsters(List<String> strings){
        List<Integer> monsters = new ArrayList<>();

        monsters.add(findSeaMonster(new ArrayList<>(strings)));
        monsters.add(findSeaMonster(reverse(strings)));

        List<String> rotate = rotate(strings);
        monsters.add(findSeaMonster(rotate));
        monsters.add(findSeaMonster(reverse(rotate)));

        rotate = rotate(rotate);
        monsters.add(findSeaMonster(rotate));
        monsters.add(findSeaMonster(reverse(rotate)));

        rotate = rotate(rotate);
        monsters.add(findSeaMonster(rotate));
        monsters.add(findSeaMonster(reverse(rotate)));

        return monsters.stream().max(Integer::compareTo).orElse(0);
    }

    private List<String> rotate(List<String> strings){
        List<String> tempList = new ArrayList<>();
        for(int i = 0; i < strings.size(); i++){
            StringBuilder temp = new StringBuilder();
            for(int j = strings.size()-1; j >= 0; j--){
                temp.append(strings.get(j).charAt(i));
            }
            tempList.add(i, temp.toString());
        }
        return tempList;
    }

    private List<String> reverse(List<String> strings){
        List<String> tempList = new ArrayList<>();
        for (String string : strings) {
            tempList.add(new StringBuilder(string).reverse().toString());
        }
        return tempList;
    }

    private int findSeaMonster(List<String> strings){
        int count = 0;

        for (int i = 0; i < strings.size() - 2; i++) {
            String rowA = strings.get(i);
            String rowB = strings.get(i + 1);
            String rowC = strings.get(i + 2);
            for (int j = 19; j < rowB.length(); j++) {
                if (rowB.charAt(j) != '#') continue;
                if (rowB.charAt(j - 1) != '#') continue;
                if (rowB.charAt(j - 2) != '#') continue;
                if (rowB.charAt(j - 7) != '#') continue;
                if (rowB.charAt(j - 8) != '#') continue;
                if (rowB.charAt(j - 13) != '#') continue;
                if (rowB.charAt(j - 14) != '#') continue;
                if (rowB.charAt(j - 19) != '#') continue;

                if (rowC.charAt(j - 3) != '#') continue;
                if (rowC.charAt(j - 6) != '#') continue;
                if (rowC.charAt(j - 9) != '#') continue;
                if (rowC.charAt(j - 12) != '#') continue;
                if (rowC.charAt(j - 15) != '#') continue;
                if (rowC.charAt(j - 18) != '#') continue;

                if (rowA.charAt(j - 1) != '#') continue;

                count++;
            }
        }
        return count;
    }

}

class PuzzleBoard {

    Map<Integer, Jigsaw> jigsaws;
    List<String> puzzle;

    PuzzleBoard(List<String> puzzles){
        createJigsaws(puzzles);
    }

    public void matchJigsaws(){
        List<Jigsaw> jigs = new ArrayList<>(jigsaws.values());
        for(int i = 0; i < jigs.size()-1; i++){
            Jigsaw a = jigs.get(i);
            for(int j = i+1; j < jigs.size(); j++){
                Jigsaw b = jigs.get(j);
                a.tryToMatch(b);
            }
        }
    }

    public long countCornerCode(){
        return jigsaws.values().stream().filter(Jigsaw::isCorner).mapToLong(jig -> jig.tileNumber).reduce(Math::multiplyExact).getAsLong();
    }

    private void createJigsaws(List<String> input){
        List<Jigsaw> jigsaws = new ArrayList<>();

        Integer number = null;
        List<String> data = new ArrayList<>();

        Iterator<String> iterator = input.iterator();

        while(iterator.hasNext()){
            String row = iterator.next();
            if(row.contains(":")){
                if(number != null){
                    jigsaws.add(new Jigsaw(number, data));
                    data = new ArrayList<>();
                }
                number = Integer.valueOf(row.split(" ")[1].replace(":", ""));
            }
            else if(!row.isEmpty()){
                data.add(row);
            }
            if(!iterator.hasNext()){
                jigsaws.add(new Jigsaw(number, data));
            }
        }
        this.jigsaws = jigsaws.stream().collect(Collectors.toMap(j -> j.tileNumber, Function.identity()));
    }

    public void puzzleJigsaw(){
        Jigsaw firstInRow;
        Optional<Jigsaw> first = jigsaws.values().stream().filter(j -> j.nTile == null && j.wTile == null).findFirst();
        if(first.isEmpty()){
            Jigsaw jigsaw = jigsaws.values().stream().filter(Jigsaw::isCorner).findFirst().get();
            while(!(jigsaw.nTile == null && jigsaw.wTile == null)){
                jigsaw.rotate90();
            }
            firstInRow = jigsaw;
        } else{
            firstInRow = first.get();
        }

        Jigsaw current = firstInRow;
        Jigsaw previous;
        List<String> data = new LinkedList<>(firstInRow.data);
        int currentRow = 0;
        boolean newRow = false;

        while(true){
            previous = current;
            current = getNext(current);
            if(current == null){
                previous = firstInRow;
                firstInRow = getNextRow(firstInRow);
                if(firstInRow == null) break;
                current = firstInRow;
                currentRow +=current.data.size();
                newRow = true;
            }

            if(newRow){
                while(!previous.tileNumber.equals(current.nTile)){
                    current.rotate90();
                }
                alignVertical(previous, current);
                data.addAll(current.data);
                newRow = false;
            }else {
                while(!previous.tileNumber.equals(current.wTile)){
                    current.rotate90();
                }
                alignHorizontal(previous, current);

                for(int i = 0; i < current.data.size(); i++){
                    String row = data.get(i + currentRow);
                    data.remove(row);
                    data.add(i+currentRow, row.concat(current.data.get(i)));
                }

            }
        }
        puzzle = removeBorders(data, previous.data.size());
    }

    private Jigsaw getNext(Jigsaw current){
        return jigsaws.get(current.eTile);
    }

    private Jigsaw getNextRow(Jigsaw first){
        return jigsaws.get(first.sTile);
    }

    private List<String> removeBorders(List<String> board, int offset){
        for(int r = board.size()-1; r >=0; r--){
            if(r%offset == 0 || r%offset == offset-1){
                board.remove(r);
            } else {
                String row = board.get(r);
                board.remove(row);
                StringBuilder rowBuilder = new StringBuilder(row);
                for(int i = row.length()-1; i >=0; i--){
                    if(i%offset == 0 || i%offset == offset-1){
                        rowBuilder.deleteCharAt(i);
                    }
                }
                board.add(r, rowBuilder.toString());
            }
        }
        return board;
    }

    private void alignHorizontal(Jigsaw w, Jigsaw e){
        if(w.indexes.get("E").get(0).containsAll(e.indexes.get("W").get(0)))
            return;
        if(w.indexes.get("E").get(0).containsAll(e.indexes.get("W").get(1)))
            e.upSideDown();
        if(w.indexes.get("E").get(1).containsAll(e.indexes.get("W").get(0)))
            e.upSideDown();
    }

    private void alignVertical(Jigsaw n, Jigsaw s){
        if(n.indexes.get("S").get(0).containsAll(s.indexes.get("N").get(0))) return;
        if(n.indexes.get("S").get(0).containsAll(s.indexes.get("N").get(1))) s.reverse();
    }
}

class Jigsaw{

    Integer tileNumber;
    List<String> data;

    Integer nTile;
    Integer eTile;
    Integer sTile;
    Integer wTile;

    Map<String, List<List<Integer>>> indexes;

    public Jigsaw(Integer number, List<String> data){
        tileNumber = number;
        this.data = data;
        initIndexes();
    }

    public void rotate90(){
        Integer N = wTile;
        Integer E = nTile;
        Integer S = eTile;
        Integer W = sTile;

        nTile = N;
        eTile = E;
        sTile = S;
        wTile = W;

        List<String> tempData = new LinkedList<>();
        for(int i = 0; i < data.size(); i++){
            StringBuilder temp = new StringBuilder();
            for(int j = data.size()-1; j >= 0; j--){
                temp.append(data.get(j).charAt(i));
            }
            tempData.add(i, temp.toString());
        }
        data = tempData;
        initIndexes();
    }

    public void reverse(){
        List<String> tempData = new LinkedList<>();
        for(int i = 0; i < data.size(); i++){
            String row = data.get(i);
            tempData.add(i, new StringBuilder(row).reverse().toString());
        }
        Integer E = wTile;
        Integer W = eTile;

        eTile = E;
        wTile = W;
        data = tempData;
        initIndexes();
    }

    public void upSideDown(){
        List<String> tempData = new LinkedList<>();
        for(int i = 0; i < data.size(); i++){
            tempData.add(i, data.get(data.size()-1-i));
        }
        data = tempData;
        Integer N = sTile;
        Integer S = nTile;

        nTile = N;
        sTile = S;
        initIndexes();
    }

    public boolean isCorner(){
        int count = 0;
        if(nTile != null) count++;
        if(sTile != null) count++;
        if(wTile != null) count++;
        if(eTile != null) count++;
        return count == 2;
    }

    private void initIndexes(){
        indexes = new HashMap<>();
        indexes.put("N", initIndex(data.get(0)));
        indexes.put("S", initIndex(data.get(data.size()-1)));
        indexes.put("W", initIndex(data.stream().map(s -> String.valueOf(s.charAt(0))).reduce(String::concat).orElse("")));
        indexes.put("E", initIndex(data.stream().map(s -> String.valueOf(s.charAt(s.length()-1))).reduce(String::concat).orElse("")));
    }

    private List<List<Integer>> initIndex(String s){
        List<List<Integer>> indexes = new LinkedList<>();
        String[] split = s.split("");
        List<Integer> index = new ArrayList<>();
        for(int i = 0; i < split.length; i++){
            if(split[i].equals("#")) index.add(i);
        }
        indexes.add(index);
        indexes.add(index.stream().map(num -> s.length()-1-num).sorted().collect(Collectors.toList()));
        return indexes;
    }

    public void tryToMatch(Jigsaw jig){
        if(nTile == null){
            if(jig.nTile == null){
                if(isMatch(indexes.get("N"), jig.indexes.get("N"))){
                    nTile = jig.tileNumber;
                    jig.nTile = tileNumber;
                    return;
                }
            }
            if(jig.sTile == null){
                if(isMatch(indexes.get("N"), jig.indexes.get("S"))){
                    nTile = jig.tileNumber;
                    jig.sTile = tileNumber;
                    return;
                }
            }
            if(jig.wTile == null){
                if(isMatch(indexes.get("N"), jig.indexes.get("W"))){
                    nTile = jig.tileNumber;
                    jig.wTile = tileNumber;
                    return;
                }
            }
            if(jig.eTile == null){
                if(isMatch(indexes.get("N"), jig.indexes.get("E"))){
                    nTile = jig.tileNumber;
                    jig.eTile = tileNumber;
                    return;
                }
            }
        }

        if(sTile == null){
            if(jig.nTile == null){
                if(isMatch(indexes.get("S"), jig.indexes.get("N"))){
                    sTile = jig.tileNumber;
                    jig.nTile = tileNumber;
                    return;
                }
            }
            if(jig.sTile == null){
                if(isMatch(indexes.get("S"), jig.indexes.get("S"))){
                    sTile = jig.tileNumber;
                    jig.sTile = tileNumber;
                    return;
                }
            }
            if(jig.wTile == null){
                if(isMatch(indexes.get("S"), jig.indexes.get("W"))){
                    sTile = jig.tileNumber;
                    jig.wTile = tileNumber;
                    return;
                }
            }
            if(jig.eTile == null){
                if(isMatch(indexes.get("S"), jig.indexes.get("E"))){
                    sTile = jig.tileNumber;
                    jig.eTile = tileNumber;
                    return;
                }
            }
        }

        if(wTile == null){
            if(jig.nTile == null){
                if(isMatch(indexes.get("W"), jig.indexes.get("N"))){
                    wTile = jig.tileNumber;
                    jig.nTile = tileNumber;
                    return;
                }
            }
            if(jig.sTile == null){
                if(isMatch(indexes.get("W"), jig.indexes.get("S"))){
                    wTile = jig.tileNumber;
                    jig.sTile = tileNumber;
                    return;
                }
            }
            if(jig.wTile == null){
                if(isMatch(indexes.get("W"), jig.indexes.get("W"))){
                    wTile = jig.tileNumber;
                    jig.wTile = tileNumber;
                    return;
                }
            }
            if(jig.eTile == null){
                if(isMatch(indexes.get("W"), jig.indexes.get("E"))){
                    wTile = jig.tileNumber;
                    jig.eTile = tileNumber;
                    return;
                }
            }
        }

        if(eTile == null){
            if(jig.nTile == null){
                if(isMatch(indexes.get("E"), jig.indexes.get("N"))){
                    eTile = jig.tileNumber;
                    jig.nTile = tileNumber;
                    return;
                }
            }
            if(jig.sTile == null){
                if(isMatch(indexes.get("E"), jig.indexes.get("S"))){
                    eTile = jig.tileNumber;
                    jig.sTile = tileNumber;
                    return;
                }
            }
            if(jig.wTile == null){
                if(isMatch(indexes.get("E"), jig.indexes.get("W"))){
                    eTile = jig.tileNumber;
                    jig.wTile = tileNumber;
                    return;
                }
            }
            if(jig.eTile == null){
                if(isMatch(indexes.get("E"), jig.indexes.get("E"))){
                    eTile = jig.tileNumber;
                    jig.eTile = tileNumber;
                    return;
                }
            }
        }
    }

    private boolean isMatch(List<List<Integer>> a, List<List<Integer>> b) {
        for (List<Integer> integers : a) {
            for (List<Integer> list : b) {
                if (integers.containsAll(list) && list.containsAll(integers))
                    return true;
            }
        }
        return false;
    }
}
