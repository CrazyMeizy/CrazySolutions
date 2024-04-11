import java.io.IOException;
import java.util.Arrays;
//import java.util.Scanner;
public class ReverseMinCAbc {
    public static String toAbc(int num){
        String numDigits = String.valueOf(num);
        StringBuilder numABC = new StringBuilder();
        for (int i = 0; i< numDigits.length();i++){
            if (numDigits.charAt(i)>='0' && numDigits.charAt(i)<='9'){
                numABC.append((char)(numDigits.charAt(i)+'a'-'0'));
            }else{
                numABC.append(numDigits.charAt(i));
            }
        }
        return numABC.toString();
    }
    public static void main(String[] args){
        int[][] ints2d = new int[5][];
        int[] mn = new int[0];
        try {
            Scanner sc = new Scanner(System.in);
            try {
                int i = 0;
                int j = 0;
                int mn_lastsize = 0;
                int num;
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    try {
                        Scanner sc2 = new Scanner(line);
                        if (i >= ints2d.length) {
                            ints2d = Arrays.copyOf(ints2d, ints2d.length * 2);
                        }
                        ints2d[i] = new int[10];
                        j = 0;
                        boolean needToCut = false;
                        mn_lastsize = mn.length;
                        try {
                            while (sc2.hasNext()) {
                                if (j >= ints2d[i].length) {
                                    ints2d[i] = Arrays.copyOf(ints2d[i], ints2d[i].length * 2);
                                }
                                if (j >= mn.length) {
                                    mn = Arrays.copyOf(mn, (mn.length + 1) * 2);
                                    needToCut = true;
                                }
                                num = Integer.parseInt(sc2.next());
                                if (j < mn_lastsize) {
                                    mn[j] = Math.min(mn[j], num);
                                    ints2d[i][j] = mn[j];
                                } else {
                                    mn[j] = num;
                                    ints2d[i][j] = mn[j];
                                }
                                j++;
                            }
                        }catch (IOException e){
                            System.err.println("Ошибка считывания данных: " + e.getMessage());
                        }
                        if (needToCut) {
                            mn = Arrays.copyOf(mn, j);
                        }
                        ints2d[i] = Arrays.copyOf(ints2d[i], j);
                        i++;
                    }catch (IOException e){
                        System.err.println("Ошибка конструирования экземпляра класса Scanner: "+ e.getMessage());
                    }
                }
                ints2d = Arrays.copyOf(ints2d, i);
                for (int a = 0; a < ints2d.length; a++) {
                    for (int b = 0; b < ints2d[a].length; b++) {
                        System.out.print(toAbc(ints2d[a][b]) + " ");
                    }
                    System.out.println();
                }
            }catch(IOException e){
                System.err.println("Ошибка считывания данных(nextLine): " + e.getMessage());
            }
        }catch(IOException e){
            System.err.println("Ошибка конструирования экземпляра класса Scanner: "+ e.getMessage());
        }
    }
}
