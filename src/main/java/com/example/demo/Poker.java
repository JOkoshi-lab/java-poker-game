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

    /** 5枚の手札を保持するレコードクラス */
    record Hand(int c0, int c1, int c2, int c3, int c4) {
    }

    /** 手札の履歴（フォームの下に表示される） */
    private List<Hand> history;
    /** 山札（ページ内には表示されない） */
    private List<Integer> deck;

    /** ゲームを最初から始めるための準備を行う */
    private void initGame() {
        // 山札を初期化する
        deck = new ArrayList<>();
        for (int i = 0; i < 52; i++) { // 0～51を山札にする
            deck.add(i);
        }
        Collections.shuffle(deck); // シャッフルする
        // 履歴を消す
        history = new ArrayList<>();
        // 山札から5枚引いて最初の手札をつくり履歴に登録する
        int c[] = new int[5];
        for (int i = 0; i < 5; i++) {
            c[i] = deck.remove(0); // deckの先頭の要素を削除しc[i]に入れる
        }
        Hand hand = new Hand(c[0], c[1], c[2], c[3], c[4]);
        history.add(hand);
    }

    @RequestMapping("/poker")
    String poker(Model model) {
        if (deck == null) {
            initGame();
        }
        Hand hand = history.get(0); // historyの先頭要素をhandに入れる（削除はしない）
        model.addAttribute("history", history);
        model.addAttribute("hand", hand);
        model.addAttribute("left", deck.size()); // 山札の残り枚数をleftに入れる
        return "poker"; // ビューの名前
    }

    @RequestMapping("/drawCard")
    String drawCard(@RequestParam(value = "select", required = false) List<String> selected) {
        if (deck.size() < 5) { // 山札が5枚未満ならゲームを再開する
            deck = null;
            return "redirect:/poker";
        }
        System.out.println("selected=" + selected);
        if (selected == null) {
            // 何もしない
        } else {
            Hand hand = history.get(0); // 交換前の手札
            int newhand[] = { hand.c0, hand.c1, hand.c2, hand.c3, hand.c4 }; // 配列にする
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
            history.add(0, hand); // 先頭(0番目)に追加する．既存要素は後ろにずれる．
        }
        return "redirect:/poker";
    }
}