package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2020/day/22
 * */
public class CrabCombat {

    Map<String, Player> players;

    public CrabCombat(List<String> input){
        String name = null;
        List<Integer> cards = new ArrayList<>();
        players = new HashMap<>();

        for (String row : input) {
            if(row.contains(":")){
                if(name != null){
                    players.put(name, new Player(name, cards));
                }
                name = row.substring(0, row.length()-1);
                cards = new ArrayList<>();
            } else if(!row.isEmpty()){
                cards.add(Integer.valueOf(row));
            }
        }
        if(name != null){
            players.put(name, new Player(name, cards));
        }
    }

    public int getWinnerScore(){
        playCombat();
        return players.values().stream().mapToInt(Player::countScore).max().orElse(0);
    }

    public int getRecursiveWinnerScore(){
        playRecursiveCombat(players);
        return players.values().stream().mapToInt(Player::countScore).max().orElse(0);
    }

    private void playCombat(){
        List<List<Integer>> cards = new ArrayList<>();
        List<String> roundPlayers = new ArrayList<>(players.keySet());

        do{
            List<String> tempPlayers = roundPlayers;
            Map<String, Integer> playedCards = players.values().stream().filter(p -> tempPlayers.contains(p.getName())).collect(Collectors.toMap(Player::getName, Player::playCard));
            Integer max = playedCards.values().stream().max(Integer::compareTo).orElse(0);
            LinkedList<String> win = playedCards.entrySet().stream().filter(e -> e.getValue().compareTo(max) == 0).map(Map.Entry::getKey).collect(Collectors.toCollection(LinkedList::new));
            cards.add(playedCards.values().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()));
            if(win.size() == 1){
                cards.forEach(car -> players.get(win.getFirst()).addCards(car));
                cards = new ArrayList<>();
                roundPlayers = new ArrayList<>(players.keySet());
            } else {
                roundPlayers = win;
            }
        }while(players.values().stream().filter(p -> p.getDeck().isEmpty()).count() != players.size()-1);
    }

    private void playRecursiveCombat(Map<String, Player> localPlayers){
        playRecursiveCombat(localPlayers, 0);
    }

    private String playRecursiveCombat(Map<String, Player> localPlayers, int level){
        List<String> names = localPlayers.keySet().stream().sorted().collect(Collectors.toList());
        List<Player> playerHistory = new ArrayList<>();
        String firstPlayer = "Player 1";

        do{
            playerHistory.add(new Player(localPlayers.get(firstPlayer)));
            Map<String, Integer> playedCards = localPlayers.values().stream().collect(Collectors.toMap(Player::getName, Player::playCard));
            String winner;
            if(names.stream().map(name -> localPlayers.get(name).leftCards() >= playedCards.get(name)).filter(t -> t).count() == names.size()){
                Map<String, Player> recursivePlayers = localPlayers.values().stream()
                        .map(player -> new Player(player.getName(), player.getFirstCards(playedCards.get(player.getName()))))
                        .collect(Collectors.toMap(Player::getName, Function.identity()));
                winner = playRecursiveCombat(recursivePlayers, ++level);
            } else {
                winner = playedCards.entrySet().stream().reduce((e1, e2) -> e1.getValue().compareTo(e2.getValue()) > 0 ? e1 : e2).map(Map.Entry::getKey).orElse("");
            }
            localPlayers.get(winner).addCards(playedCards.get(winner));
            playedCards.remove(winner);
            localPlayers.get(winner).addCards(playedCards.values());
            if(level > 0 && playerHistory.stream().anyMatch(p -> p.isSameRound(localPlayers.get(firstPlayer)))){
                return firstPlayer;
            }
        }while(localPlayers.values().stream().filter(p -> p.getDeck().isEmpty()).count() != players.size()-1);

        return localPlayers.values().stream().filter(p -> !p.getDeck().isEmpty()).map(Player::getName).findFirst().orElse("");
    }

}

class Player{
    private final String name;
    private final LinkedList<Integer> deck;

    public Player(String name, List<Integer> deck){
        this.name = name;
        this.deck = new LinkedList<>(deck);
    }

    public Player(Player player){
        this.name = player.name;
        this.deck = new LinkedList<>(player.deck);
    }

    public Integer playCard(){
        return deck.removeFirst();
    }

    public void addCards(Collection<Integer> cards){
        deck.addAll(cards);
    }

    public void addCards(Integer... cards){
        deck.addAll(Arrays.asList(cards));
    }

    public int countScore(){
        int score = 0;
        for(int i = deck.size(); i > 0; i--){
            score += deck.removeFirst()*i;
        }
        return score;
    }

    public String getName(){
        return name;
    }

    public List<Integer> getDeck(){
        return deck;
    }

    public int leftCards(){
        return deck.size();
    }

    public List<Integer> getFirstCards(int size){
        return deck.subList(0, size);
    }

    public boolean isSameRound(Player o){
        if(!o.name.equals(name)) return false;
        return o.deck.equals(deck);
    }
}
