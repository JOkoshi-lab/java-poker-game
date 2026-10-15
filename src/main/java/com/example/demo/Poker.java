package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@Controller
public class Poker {
    public static void main(String[] args) {
        SpringApplication.run(Poker.class, args);
    }

    record Hand(int c0, int c1, int c2, int c3, int c4) {
    }

    private List<Hand> history;
    private List<Integer> deck;

    private void initGame() {
        deck = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            deck.add(i);
        }
        Collections.shuffle(deck);

        history = new ArrayList<>();

        int[] c = new int[5];
        for (int i = 0; i < 5; i++) {
            c[i] = deck.remove(0);
        }

        Hand hand = new Hand(c[0], c[1], c[2], c[3], c[4]);
        history.add(hand);
    }

    @RequestMapping("/poker")
    String poker(Model model) {
        if (deck == null) {
            initGame();
        }

        Hand hand = history.get(0);
        model.addAttribute("history", history);
        model.addAttribute("hand", hand);
        model.addAttribute("left", deck.size());
        model.addAttribute(
                "ranks",
                List.of("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"));
        model.addAttribute("suits", List.of("♠", "♥", "♦", "♣"));

        return "poker";
    }

    @RequestMapping("/drawCard")
    String drawCard(@RequestParam(value = "select", required = false) List<String> selected) {
        if (deck.size() < 5) {
            deck = null;
            return "redirect:/poker";
        }

        if (selected != null) {
            Hand hand = history.get(0);
            int[] newhand = { hand.c0, hand.c1, hand.c2, hand.c3, hand.c4 };

            for (String s : selected) {
                int index = Integer.parseInt(s);
                newhand[index] = deck.remove(0);
            }

            for (int i = 0; i < 5; i++) {
                for (int j = i + 1; j < 5; j++) {
                    if ((newhand[i] % 13) > (newhand[j] % 13)) {
                        int temp = newhand[i];
                        newhand[i] = newhand[j];
                        newhand[j] = temp;
                    }
                }
            }

            hand = new Hand(newhand[0], newhand[1], newhand[2], newhand[3], newhand[4]);
            history.add(0, hand);
        }

        return "redirect:/poker";
    }
}
