import java.io.IOException;
import java.util.Arrays;
//import java.util.Scanner;
public class Reverse{
    public static void main(String[] args){
        try {
            Scanner sc = new Scanner(System.in);
            try {
                int[][] ints2d = new int[10][];
                int i=0;
                int j=0;
                int num;
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    try{
                        Scanner sc2 = new Scanner(line);
                        if (i >= ints2d.length) {
                            ints2d = Arrays.copyOf(ints2d, ints2d.length * 2);
                        }
                        ints2d[i] = new int[10];
                        j = 0;
                        try {
                            while (sc2.hasNext()) {
                                if (j >= ints2d[i].length) {
                                    ints2d[i] = Arrays.copyOf(ints2d[i], ints2d[i].length * 2);
                                }
                                num = Integer.parseInt(sc2.next());
                                ints2d[i][j] = num;
                                j++;
                            }
                        }catch(IOException e){
                            System.err.println("Ошибка считывания данных: " + e.getMessage());
                        }
                        ints2d[i] = Arrays.copyOf(ints2d[i], j);
                        i++;
                    }catch(IOException e){
                        System.err.println("Ошибка конструирования экземпляра класса Scanner: "+ e.getMessage());
                    }
                }
                ints2d = Arrays.copyOf(ints2d, i);
                for (int a=ints2d.length-1 ;a>=0;a--){
                    for (int b=ints2d[a].length-1 ;b>=0;b--){
                        System.out.print(ints2d[a][b]+" ");
                    }
                    System.out.println();
                }
            }catch(IOException e){
                System.err.println("Ошибка считывания данных: " + e.getMessage());
            }
        }catch(IOException e){
            System.err.println("Ошибка конструирования экземпляра класса Scanner: "+ e.getMessage());
        }
    }
}
