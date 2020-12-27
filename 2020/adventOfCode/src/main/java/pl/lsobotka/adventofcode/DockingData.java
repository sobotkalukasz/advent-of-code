package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2020/day/14
 * */
public class DockingData {

    Pattern MASK = Pattern.compile("mask = ");

    public long processMemValue(List<String> data){
        List<Instruction> instructions = createInstructions(data);
        instructions.forEach(Instruction::decodeMemoryValue);
        return calculateProgramValue(instructions);
    }

    public long processMemAddress(List<String> data){
        List<Instruction> instructions = createInstructions(data);
        instructions.forEach(Instruction::decodeMemoryAddress);
        return calculateProgramValue(instructions);
    }

    private long calculateProgramValue(List<Instruction> instructions){
        return instructions.stream().map(p -> p.instructions)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v2))
                .values().stream().mapToLong(Long::longValue).sum();
    }

    private List<Instruction> createInstructions(List<String> data){
        List<Instruction> instructions = new ArrayList<>();
        Iterator<String> iterator = data.iterator();
        String tempMask = "";
        List<String> tempData = new ArrayList<>();

        while(iterator.hasNext()){
            String next = iterator.next();
            if(MASK.matcher(next).find(0)){
                if(!tempData.isEmpty()){
                    instructions.add(new Instruction(tempMask, tempData));
                    tempData = new ArrayList<>();
                }
                tempMask = next;
            }else{
                tempData.add(next);
            }
            if(!iterator.hasNext()){
                instructions.add(new Instruction(tempMask, tempData));
            }
        }
        return instructions;
    }
}

class Instruction {
    String mask;
    Map<String, Long> instructions;

    Instruction(String mask, List<String> data){
        this.mask = initMask(mask);
        this.instructions = data.stream().map(this::initInstruction)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e2,
                        LinkedHashMap::new));
    }

    private String initMask(String mask){
        String[] split = mask.replaceAll("\\s+", "").split("=");
        return split[1];
    }

    private AbstractMap.SimpleEntry<String, Long> initInstruction(String data) {
        String replace = data.replace("mem[", "").replace("]", "").replaceAll("\\s+", "");
        String[] split = replace.split("=");
        return new AbstractMap.SimpleEntry<>(split[0], Long.valueOf(split[1]));
    }

    public void decodeMemoryValue(){
        instructions.entrySet().forEach(e -> e.setValue(decodeValue(e.getValue())));
    }

    public void decodeMemoryAddress(){
        Map<String, Long> temp = new LinkedHashMap<>();
        instructions.forEach((key, value) -> decodeAddress(key).forEach(k -> temp.put(k, value)));
        instructions = temp;
    }

    private long decodeValue(long data){
        String binary = longToBinary(data);
        String applyMask = applyMask(binary);
        return Long.parseLong(applyMask, 2);
    }

    private List<String> decodeAddress(String data){
        String binary = longToBinary(Long.parseLong(data));
        String applyMask = applyFloatingMask(binary);
        return parseFloating(applyMask);
    }

    private String longToBinary(long value){
        String binary = Long.toBinaryString(value);
        return String.format("%"+(mask.length())+"s", binary).replace(' ', '0');
    }

    private String applyMask(String binary){
        int maskLen = mask.length();
        StringBuilder builder = new StringBuilder(binary);

        while(--maskLen >= 0){
            if(mask.charAt(maskLen) != 'X'){
                builder.setCharAt(maskLen, mask.charAt(maskLen));
            }
        }
        return builder.toString();
    }

    private String applyFloatingMask(String binary){
        int maskLen = mask.length();
        StringBuilder builder = new StringBuilder(binary);

        while(--maskLen >= 0){
            if(mask.charAt(maskLen) != '0'){
                builder.setCharAt(maskLen, mask.charAt(maskLen));
            }
        }
        return builder.toString();
    }

    private List<String> parseFloating(String binaryString){
        List<String> mem = new ArrayList<>();

        Integer[] indexOf = getFloatingIndex(binaryString);

        if(indexOf.length == 0){
            mem.add(binaryString);
        } else {
            StringBuilder sb = new StringBuilder(binaryString);
            for (Integer integer : indexOf) {
                sb.setCharAt(integer, '0');
                mem.addAll(parseFloating(sb.toString()));
                sb.setCharAt(integer, '1');
                mem.addAll(parseFloating(sb.toString()));
            }
        }
        return mem;
    }

    private Integer[] getFloatingIndex(String test){
        List<Integer> indexes = new ArrayList<>();
        for(int i = 0; i < test.length(); i++){
            if(test.charAt(i) =='X') indexes.add(i);
        }
        return indexes.toArray(new Integer[0]);
    }

}
