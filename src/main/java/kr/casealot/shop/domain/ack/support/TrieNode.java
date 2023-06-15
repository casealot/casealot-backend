package kr.casealot.shop.domain.ack.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrieNode {
    private final Map<Character, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void insert(String word) {
        TrieNode current = this;
        for (char c : word.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }
        current.isWord = true;
    }

    public List<String> autoComplete(String prefix) {
        List<String> result = new ArrayList<>();
        TrieNode node = getNode(prefix);
        if (node != null) {
            collectWords(node, prefix, result);
        }
        if(result.size() == 10){

        }
        return result;
    }

    private TrieNode getNode(String prefix) {
        TrieNode current = this;
        for (char c : prefix.toCharArray()) {
            if (!current.children.containsKey(c)
                    && !current.children.containsKey(Character.toLowerCase(c))
                    && !current.children.containsKey(Character.toUpperCase(c))) {
                return null;
            }

            if (isEnglishCharacter(c)) {
                if (current.children.containsKey(Character.toLowerCase(c))) {
                    current = current.children.get(Character.toLowerCase(c));
                } else {
                    current = current.children.get(Character.toUpperCase(c));
                }
            } else {
                current = current.children.get(c);
            }
        }
        return current;
    }

    private boolean isEnglishCharacter(char c) {
        return Character.isLetter(c) && Character.UnicodeBlock.of(c)
                == Character.UnicodeBlock.BASIC_LATIN;
    }

    private void collectWords(TrieNode node, String prefix, List<String> result) {
        if (node.isWord) {
            result.add(prefix);
        }
        for (char c : node.children.keySet()) {
            collectWords(node.children.get(c), prefix + c, result);
        }
    }


}