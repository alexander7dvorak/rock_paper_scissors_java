package com.company;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final String HMAC_SHA256 = "HmacSHA256";

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        boolean wrongInput = false;
        inputConstrains:
        if (args.length >= 3 && args.length % 2 == 1) {
            for (int i = 0; i < args.length; ++i) {
                for (int j = i + 1; j < args.length; ++j)
                    if (args[i].equals(args[j])) {
                        wrongInput = true;
                        break inputConstrains;
                    }
            }
            Mac sha512Hmac;
            SecureRandom sr = new SecureRandom();
            byte[] key = new byte[16];
            sr.nextBytes(key);
            StringBuilder keySb = new StringBuilder();
            for (byte b : key) {
                keySb.append(String.format("%02x", b).toUpperCase(Locale.ROOT));
            }
            int computersMove = sr.nextInt(args.length);
            sha512Hmac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(key, HMAC_SHA256);
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(Integer.toString(computersMove).getBytes(StandardCharsets.UTF_8));
            System.out.print("HMAC: ");
            StringBuilder hmacSb = new StringBuilder();
            for (byte b : macData) {
                hmacSb.append(String.format("%02x", b).toUpperCase(Locale.ROOT));
            }
            System.out.println(hmacSb);
            System.out.println("Available moves:");
            int ind = 1;
            for (String move : args) {
                System.out.println(ind + " - " + move);
                ind++;
            }
            System.out.println("0 - exit");

            Scanner in = new Scanner(System.in);
            System.out.print("Enter your move: ");
            int playersMove = in.nextInt();
            if (playersMove == 0) return;
            --playersMove;
            System.out.println("Your move: " + args[playersMove]);

            System.out.print("Computer move: ");
            System.out.println(args[computersMove]);

            if (computersMove == playersMove)
                System.out.println("Draw.");
            else if ((Math.abs(playersMove - computersMove) > args.length / 2
                    && playersMove > computersMove) || (playersMove < computersMove
                    && Math.abs(playersMove - computersMove) <= args.length / 2))
                System.out.println("You loose!");
            else System.out.println("You win!");
            System.out.println("HMAC key: " + keySb);
        } else {
            wrongInput = true;
        }
        if (wrongInput) {
            System.out.println("Wrong input parameters");
            System.out.println("Example 1: rock paper scissors");
            System.out.println("Example 2: one two three four five");
            System.out.println("Example 3: one two three four five six seven");
        }
    }
}