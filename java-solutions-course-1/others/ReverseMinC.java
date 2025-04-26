import java.util.Scanner;
import java.io.IOException;
import java.util.Arrays;
import static java.lang.Math.min;
public class ReverseMinC {
    public static void main(String[] args) {
        int[][] ints2d = new int[5][];
        Scanner sc = new Scanner(System.in);
        int i=0;
        int j=0;
        int num;
        int maxStrLength = -1;
        int[] lengthArray = new int[ints2d.length];
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            /*if (line.isEmpty()) {
                break;
            }*/
            //System.out.println(line + "*/*");
            Scanner sc2 = new Scanner(line);
            j=0;
            if(i>=ints2d.length){
                ints2d = Arrays.copyOf(ints2d,(ints2d.length*3)/2+1);
                lengthArray = Arrays.copyOf(lengthArray,ints2d.length);
            }
            ints2d[i] = new int[10];
            while(sc2.hasNext()){
                if (j>=ints2d[i].length){
                    ints2d[i] = Arrays.copyOf(ints2d[i],(ints2d[i].length*3)/2+1);
                }
                num = Integer.parseInt(sc2.next());
                //System.out.println(num + "*");
                ints2d[i][j] = num;
                if(j<maxStrLength) {
                    ints2d[i][j] = min(num,ints2d[i-1][j]);
                }else{
                    ints2d[i][j] = num;
                }
                j++;
            }
            if (maxStrLength<j){
                maxStrLength=j;
            }
            lengthArray[i]=j;
            int dif = maxStrLength-j;
            ints2d[i] = Arrays.copyOf(ints2d[i],maxStrLength);
            for (int h=j;h<maxStrLength;h++){
                ints2d[i][h]=ints2d[i-1][h];
            }
            i++;
        }
        ints2d = Arrays.copyOf(ints2d,i);
        lengthArray = Arrays.copyOf(lengthArray,i);
        for (int a=0 ;a<ints2d.length;a++){
            ints2d[a]=Arrays.copyOf(ints2d[a],lengthArray[a]);
        }
        for (int a=0 ;a<ints2d.length;a++){
            for (int b=0;b<ints2d[a].length;b++){
                System.out.print(ints2d[a][b]+" ");
            }
            System.out.println();
        }
    }
}