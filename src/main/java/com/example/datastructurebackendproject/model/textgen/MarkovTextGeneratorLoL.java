package com.example.datastructurebackendproject.model.textgen;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An implementation of the MTG interface that uses a list of lists.
 *
 * @author UC San Diego Intermediate Programming MOOC team
 */
public class MarkovTextGeneratorLoL implements MarkovTextGenerator {

    // The list of words with their next words
    private List<ListNode> wordList;

    // The starting "word"
    private String starter;

    // The random number generator
    private Random rnGenerator;

    public MarkovTextGeneratorLoL(Random generator) {
        wordList = new LinkedList<ListNode>();
        starter = "";
        rnGenerator = generator;
    }


    /**
     * Train the generator by adding the sourceText
     */
    @Override
    public void train(String sourceText) {
        ArrayList<String> tokens = new ArrayList<String>();
        Pattern tokSplitter = Pattern.compile("[a-zA-Z.!?]+");
        Matcher m = tokSplitter.matcher(sourceText);

        while (m.find()) {
            tokens.add(m.group());
        }

        if (!tokens.isEmpty()) {
            ArrayList<ListNode> model = new ArrayList<>();
            ListNode firstListNode = new ListNode(tokens.get(0));
            firstListNode.addNextWord(tokens.get(1));
            model.add(firstListNode);
            for (int i = 1; i < tokens.size() - 1; i++) {
                for (int j = 0; j < model.size(); j++) {
                    if (tokens.get(i).equals(model.get(j).getWord())) {
                        model.get(j).addNextWord(tokens.get(i + 1));
                        break;
                    } else if (j == model.size() - 1) {
                        ListNode listNode = new ListNode(tokens.get(i));
                        listNode.addNextWord(tokens.get(i + 1));
                        model.add(listNode);
                        break;
                    }
                }
            }
            for (int i = 0; i < model.size(); i++) {
                if (tokens.get(tokens.size() - 1).equals(model.get(i).getWord())) break;
                if (i == model.size() - 1) model.add(new ListNode(tokens.get(tokens.size() - 1)));
            }
            this.wordList.addAll(model);
        }

    }

    /**
     * Generate the number of words requested.
     */
    @Override
    public String generateText(int numWords) {
        if (this.wordList.size() == 0 || numWords == 0) return "";
        int start = rnGenerator.nextInt(this.wordList.size() - 1);
        StringBuilder sb = new StringBuilder();
        this.starter = this.wordList.get(start).getWord();
        sb.append(this.starter).append(" ");
        for (int i = 1; i < numWords; i++) {
            for (int j = 0; j < this.wordList.size(); j++) {
                if (this.wordList.get(j).getWord().equals(this.starter)) {
                    String next = this.wordList.get(j).getRandomNextWord(this.rnGenerator);
                    sb.append(next).append(" ");
                    this.starter = next;
                    break;
                }
            }
        }

        return sb.toString();
    }


    // Can be helpful for debugging
    @Override
    public String toString() {
        String toReturn = "";
        for (ListNode n : wordList) {
            toReturn += n.toString();
        }
        return toReturn;
    }

    /**
     * Retrain the generator from scratch on the source text
     */
    @Override
    public void retrain(String sourceText) {
        ArrayList<String> tokens = new ArrayList<String>();
        Pattern tokSplitter = Pattern.compile("[a-zA-Z.!?]+");
        Matcher m = tokSplitter.matcher(sourceText);

        while (m.find()) {
            tokens.add(m.group());
        }

        if (tokens.size() == 0) {
            this.wordList = new LinkedList<ListNode>();
        } else {
            ArrayList<ListNode> model = new ArrayList<>();
            ListNode firstListNode = new ListNode(tokens.get(0));
            firstListNode.addNextWord(tokens.get(1));
            model.add(firstListNode);
            for (int i = 1; i < tokens.size() - 1; i++) {
                for (int j = 0; j < model.size(); j++) {
                    if (tokens.get(i).equals(model.get(j).getWord())) {
                        model.get(j).addNextWord(tokens.get(i + 1));
                        break;
                    } else if (j == model.size() - 1) {
                        ListNode listNode = new ListNode(tokens.get(i));
                        listNode.addNextWord(tokens.get(i + 1));
                        model.add(listNode);
                        break;
                    }
                }
            }
            for (int i = 0; i < model.size(); i++) {
                if (tokens.get(tokens.size() - 1).equals(model.get(i).getWord())) break;
                if (i == model.size() - 1) model.add(new ListNode(tokens.get(tokens.size() - 1)));
            }
            this.wordList = model;
        }

    }

    // TODO: Add any private helper methods you need here.


    /**
     * This is a minimal set of tests.  Note that it can be difficult
     * to test methods/classes with randomized behavior.
     *
     * @param args
     */
    public static void main(String[] args) {
        MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));
        String input = "I love cats. I hate dogs. I I I I I I I I I I I I I I I I love cats. I I I I I I I I I I I I I I I I hate dogs. I I I I I I I I I like books. I love love. I am a text generator. I love cats. I love cats. I love cats. I love love love socks.";
        gen.retrain(input);
        String res = gen.generateText(500);
        System.out.println(res);
        System.out.println();
        gen.train("");
        res = gen.generateText(500);
        System.out.println(res);

    }

}

/**
 * Links a word to the next words in the list
 * You should use this class in your implementation.
 */
class ListNode {
    // The word that is linking to the next words
    private String word;

    // The next words that could follow it
    private List<String> nextWords;

    ListNode(String word) {
        this.word = word;
        nextWords = new LinkedList<String>();
    }

    public String getWord() {
        return word;
    }

    public void addNextWord(String nextWord) {
        nextWords.add(nextWord);
    }

    public String getRandomNextWord(Random generator) {
        if (this.nextWords.size() == 0) return "";
        if (this.nextWords.size() == 1) return this.nextWords.get(0);
        return nextWords.get(generator.nextInt(this.nextWords.size() - 1));
    }

    public String toString() {
        String toReturn = word + ": ";
        for (String s : nextWords) {
            toReturn += s + "->";
        }
        toReturn += "\n";
        return toReturn;
    }

}


